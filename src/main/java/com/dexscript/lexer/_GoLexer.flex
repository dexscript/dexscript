package com.dexscript.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static com.dexscript.parser.GoTypes.*;

%%

%{
  public _GoLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _GoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+


%%
<YYINITIAL> {
  {WHITE_SPACE}        { return WHITE_SPACE; }

  "{"                  { return LBRACE; }
  "}"                  { return RBRACE; }
  "["                  { return LBRACK; }
  "]"                  { return RBRACK; }
  "("                  { return LPAREN; }
  ")"                  { return RPAREN; }
  ":"                  { return COLON; }
  ";"                  { return SEMICOLON; }
  ","                  { return COMMA; }
  "=="                 { return EQ; }
  "="                  { return ASSIGN; }
  "!="                 { return NOT_EQ; }
  "!"                  { return NOT; }
  "++"                 { return PLUS_PLUS; }
  "+="                 { return PLUS_ASSIGN; }
  "+"                  { return PLUS; }
  "--"                 { return MINUS_MINUS; }
  "-="                 { return MINUS_ASSIGN; }
  "-"                  { return MINUS; }
  "||"                 { return COND_OR; }
  "|="                 { return BIT_OR_ASSIGN; }
  "&^="                { return BIT_CLEAR_ASSIGN; }
  "&^"                 { return BIT_CLEAR; }
  "&&"                 { return COND_AND; }
  "&="                 { return BIT_AND_ASSIGN; }
  "&"                  { return BIT_AND; }
  "|"                  { return BIT_OR; }
  "<<="                { return SHIFT_LEFT_ASSIGN; }
  "<<"                 { return SHIFT_LEFT; }
  "<-"                 { return SEND_CHANNEL; }
  "<="                 { return LESS_OR_EQUAL; }
  "<"                  { return LESS; }
  "^="                 { return BIT_XOR_ASSIGN; }
  "^"                  { return BIT_XOR; }
  "*="                 { return MUL_ASSIGN; }
  "*"                  { return MUL; }
  "/="                 { return QUOTIENT_ASSIGN; }
  "/"                  { return QUOTIENT; }
  "%="                 { return REMAINDER_ASSIGN; }
  "%"                  { return REMAINDER; }
  ">>="                { return SHIFT_RIGHT_ASSIGN; }
  ">>"                 { return SHIFT_RIGHT; }
  ">="                 { return GREATER_OR_EQUAL; }
  ">"                  { return GREATER; }
  ":="                 { return VAR_ASSIGN; }
  "..."                { return TRIPLE_DOT; }
  "."                  { return DOT; }
  "<NL>"               { return SEMICOLON_SYNTHETIC; }
  "type"               { return TYPE_; }
  "raw_string"         { return RAW_STRING; }
  "package"            { return PACKAGE; }
  "identifier"         { return IDENTIFIER; }
  "import"             { return IMPORT; }
  "string"             { return STRING; }
  "function"           { return FUNCTION; }
  "func"               { return FUNC; }
  "break"              { return BREAK; }
  "case"               { return CASE; }
  "char"               { return CHAR; }
  "const"              { return CONST; }
  "continue"           { return CONTINUE; }
  "decimali"           { return DECIMALI; }
  "default"            { return DEFAULT; }
  "defer"              { return DEFER; }
  "else"               { return ELSE; }
  "fallthrough"        { return FALLTHROUGH; }
  "float"              { return FLOAT; }
  "floati"             { return FLOATI; }
  "for"                { return FOR; }
  "go"                 { return GO; }
  "goto"               { return GOTO; }
  "hex"                { return HEX; }
  "if"                 { return IF; }
  "int"                { return INT; }
  "interface"          { return INTERFACE; }
  "map"                { return MAP; }
  "oct"                { return OCT; }
  "return"             { return RETURN; }
  "select"             { return SELECT; }
  "struct"             { return STRUCT; }
  "switch"             { return SWITCH; }
  "var"                { return VAR; }
  "Expression"         { return EXPRESSION; }
  "LiteralTypeExpr"    { return LITERALTYPEEXPR; }
  "chan"               { return CHAN; }


}

[^] { return BAD_CHARACTER; }
