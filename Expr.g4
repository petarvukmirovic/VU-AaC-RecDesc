lexer grammar Expr;

NUM : [+|-]?[0-9]+;
VAR : [a-zA-Z][a-zA-Z0-9]*;
OPERATOR: '^' | '&' | '|';
PARENS: '(' | ')';
WS: [ \n]+ -> skip;
