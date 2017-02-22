import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by petar on 2/22/2017.
 */
public class Runner {

    public static void main(String args[]) throws IOException {
        InputStream stream = System.in;

        ANTLRInputStream ais = new ANTLRInputStream(stream);
        Expr lexer = new Expr(ais);
        RecDesParser parser = new RecDesParser(lexer);

        try {
            parser.parse();
            System.out.println("Successfully parsed the input.");
        }
        catch(RecDesParserException error){
            error.printStackTrace();
        }
    }
}
