import org.antlr.v4.runtime.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by petar on 2/22/2017.
 */

class RecDesParserException extends RuntimeException{
    public RecDesParserException(String msg){
        super(msg);
    }
}

public class RecDesParser {

    private final List<Token> _tokens;
    private int _curr = 0;

    public RecDesParser(Expr lexer){
        _tokens = new ArrayList<Token>(lexer.getAllTokens());
    }

    public void  parse(){
        expr(); // get the ball rolling.
        if (!_atTheEnd()){
            String err = "Excessive tokens: ";
            for(Token t: _tokens.subList(_curr, _tokens.size())){
                err += t.getText();
            }

            throw new RecDesParserException(err);
        }
    }

    private void expr(){
        if (_lookaheadMatchesType(Expr.NUM, Expr.VAR) ||
                _lookaheadMatchesText("(")) {
            expr1();
            exprP();
        }
        else {
            String msg = "Error parsing expr: Expected NUM, VAR or (";
            throw new RecDesParserException(msg);
        }
    }

    private void exprP(){
        if (_lookaheadMatchesText("^")){
            _advance();
            expr1();
            exprP(); // tail recursion!
        }
        else if (_atTheEnd() || _lookaheadMatchesText(")")) {
            return; // lambda rule
        }
        else {
            String msg = "Error parsing exprP: Expected EOF, ^ or )";
            throw new RecDesParserException(msg);
        }
    }

    private boolean _lookaheadMatchesText(String... text){
        return !_atTheEnd() &&
                Arrays.stream(text)
                      .anyMatch(t -> _tokens.get(_curr).getText().equals(t));
    }

    private boolean _lookaheadMatchesType(int... type){
        return !_atTheEnd() &&
                Arrays.stream(type)
                      .anyMatch(t -> _tokens.get(_curr).getType() == t);
    }

    private boolean _atTheEnd(){
        return _curr == _tokens.size();
    }

    private void _advance(){
        _curr++;
    }

    private void expr1(){
        if (_lookaheadMatchesType(Expr.NUM, Expr.VAR) ||
                _lookaheadMatchesText("(")) {
            expr2();
            expr1P();
        }
        else {
            String msg = "Error parsing expr1: Expected NUM, VAR or (";
            throw new RecDesParserException(msg);
        }
    }

    private void expr1P(){
        if (_lookaheadMatchesText("|")){
            _advance();
            expr2();
            expr1P(); // tail recursion!
        }
        else if (_atTheEnd() || _lookaheadMatchesText(")", "^")) {
            return;
        }
        else {
            String msg = "Error parsing expr1P: Expected |, ) or ^";
            throw new RecDesParserException(msg);
        }

        return;
    }

    private void expr2(){
        if (_lookaheadMatchesType(Expr.NUM, Expr.VAR) ||
                _lookaheadMatchesText("(")) {
            expr3();
            expr2P();
        }
        else {
            String msg = "Error parsing expr3: Expected NUM, VAR or (";
            throw new RecDesParserException(msg);
        }
    }

    private void expr2P(){
        if (_lookaheadMatchesText("&")){
            _advance();
            expr3();
            expr2P(); // tail recursion!
        }
        else if (_atTheEnd() || _lookaheadMatchesText(")", "^", "|")) {
            return;
        }
        else {
            String msg = "Error parsing expr2P: Expected &, ), ^, | or EOF";
            throw new RecDesParserException(msg);
        }
    }

    private void expr3(){
        if (_lookaheadMatchesType(Expr.NUM, Expr.VAR)){
            _advance();
        }
        else if (_lookaheadMatchesText("(")){
            _advance();
            expr();
            if(_lookaheadMatchesText(")")){
                _advance();
            }
            else {
                throw new RecDesParserException("Error parsing expr3: Expected )");
            }
        }
        else {
            String msg = "Error parsing expr3: Expected NUM, VAR or (";
            throw new RecDesParserException(msg);
        }
    }
}