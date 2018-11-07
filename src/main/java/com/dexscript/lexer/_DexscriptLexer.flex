package ;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static generated.GeneratedTypes.*;

%%

%{
  public _DexscriptLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _DexscriptLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  "{"                { return LBRACE; }
  "}"                { return RBRACE; }
  "["                { return LBRACK; }
  "]"                { return RBRACK; }
  "("                { return LPAREN; }
  ")"                { return RPAREN; }
  ":"                { return COLON; }
  ";"                { return SEMICOLON; }
  ","                { return COMMA; }
  "=="               { return EQ; }
  "="                { return ASSIGN; }
  "!="               { return NOT_EQ; }
  "!"                { return NOT; }
  "++"               { return PLUS_PLUS; }
  "+="               { return PLUS_ASSIGN; }
  "+"                { return PLUS; }
  "--"               { return MINUS_MINUS; }
  "-="               { return MINUS_ASSIGN; }
  "-"                { return MINUS; }
  "||"               { return COND_OR; }
  "|="               { return BIT_OR_ASSIGN; }
  "&^="              { return BIT_CLEAR_ASSIGN; }
  "&^"               { return BIT_CLEAR; }
  "&&"               { return COND_AND; }
  "&="               { return BIT_AND_ASSIGN; }
  "&"                { return BIT_AND; }
  "|"                { return BIT_OR; }
  "<<="              { return SHIFT_LEFT_ASSIGN; }
  "<<"               { return SHIFT_LEFT; }
  "<-"               { return SEND_CHANNEL; }
  "<="               { return LESS_OR_EQUAL; }
  "<"                { return LESS; }
  "^="               { return BIT_XOR_ASSIGN; }
  "^"                { return BIT_XOR; }
  "*="               { return MUL_ASSIGN; }
  "*"                { return MUL; }
  "/="               { return QUOTIENT_ASSIGN; }
  "/"                { return QUOTIENT; }
  "%="               { return REMAINDER_ASSIGN; }
  "%"                { return REMAINDER; }
  ">>="              { return SHIFT_RIGHT_ASSIGN; }
  ">>"               { return SHIFT_RIGHT; }
  ">="               { return GREATER_OR_EQUAL; }
  ">"                { return GREATER; }
  ":="               { return VAR_ASSIGN; }
  "..."              { return TRIPLE_DOT; }
  "."                { return DOT; }
  "<NL>"             { return SEMICOLON_SYNTHETIC; }
  "type"             { return TYPE_; }
  "raw_string"       { return RAW_STRING; }


}

[^] { return BAD_CHARACTER; }
