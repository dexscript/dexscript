// copyrightHeader.java
package com.dexscript.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.dexscript.psi.impl.*;

public interface DexTypes {

  IElementType ADD_EXPR = new DexCompositeElementType("ADD_EXPR");
  IElementType AND_EXPR = new DexCompositeElementType("AND_EXPR");
  IElementType BLOCK = new DexCompositeElementType("BLOCK");
  IElementType CONDITIONAL_EXPR = new DexCompositeElementType("CONDITIONAL_EXPR");
  IElementType EXPRESSION = new DexCompositeElementType("EXPRESSION");
  IElementType FUNCTION_DECLARATION = new DexCompositeElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_TYPE = new DexCompositeElementType("FUNCTION_TYPE");
  IElementType IMPORT_DECLARATION = new DexCompositeElementType("IMPORT_DECLARATION");
  IElementType IMPORT_LIST = new DexCompositeElementType("IMPORT_LIST");
  IElementType IMPORT_SPEC = new DexCompositeElementType("IMPORT_SPEC");
  IElementType IMPORT_STRING = new DexCompositeElementType("IMPORT_STRING");
  IElementType LEFT_HAND_EXPR_LIST = new DexCompositeElementType("LEFT_HAND_EXPR_LIST");
  IElementType MUL_EXPR = new DexCompositeElementType("MUL_EXPR");
  IElementType OR_EXPR = new DexCompositeElementType("OR_EXPR");
  IElementType PACKAGE_CLAUSE = new DexCompositeElementType("PACKAGE_CLAUSE");
  IElementType PARAMETERS = new DexCompositeElementType("PARAMETERS");
  IElementType PARAMETER_DECLARATION = new DexCompositeElementType("PARAMETER_DECLARATION");
  IElementType PARAM_DEFINITION = new DexCompositeElementType("PARAM_DEFINITION");
  IElementType PARENTHESES_EXPR = new DexCompositeElementType("PARENTHESES_EXPR");
  IElementType PAR_TYPE = new DexCompositeElementType("PAR_TYPE");
  IElementType RESULT = new DexCompositeElementType("RESULT");
  IElementType RETURN_STATEMENT = new DexCompositeElementType("RETURN_STATEMENT");
  IElementType SIGNATURE = new DexCompositeElementType("SIGNATURE");
  IElementType STATEMENT = new DexCompositeElementType("STATEMENT");
  IElementType STRING_LITERAL = new DexCompositeElementType("STRING_LITERAL");
  IElementType TYPE = new DexCompositeElementType("TYPE");
  IElementType TYPE_LIST = new DexCompositeElementType("TYPE_LIST");
  IElementType TYPE_REFERENCE_EXPRESSION = new DexCompositeElementType("TYPE_REFERENCE_EXPRESSION");
  IElementType UNARY_EXPR = new DexCompositeElementType("UNARY_EXPR");

  IElementType ADDOP = new DexTokenType("AddOp");
  IElementType ASSIGN = new DexTokenType("=");
  IElementType BIT_AND = new DexTokenType("&");
  IElementType BIT_AND_ASSIGN = new DexTokenType("&=");
  IElementType BIT_CLEAR = new DexTokenType("&^");
  IElementType BIT_CLEAR_ASSIGN = new DexTokenType("&^=");
  IElementType BIT_OR = new DexTokenType("|");
  IElementType BIT_OR_ASSIGN = new DexTokenType("|=");
  IElementType BIT_XOR = new DexTokenType("^");
  IElementType BIT_XOR_ASSIGN = new DexTokenType("^=");
  IElementType BREAK = new DexTokenType("break");
  IElementType CASE = new DexTokenType("case");
  IElementType CHAN = new DexTokenType("chan");
  IElementType CHAR = new DexTokenType("char");
  IElementType COLON = new DexTokenType(":");
  IElementType COMMA = new DexTokenType(",");
  IElementType COND_AND = new DexTokenType("&&");
  IElementType COND_OR = new DexTokenType("||");
  IElementType CONST = new DexTokenType("const");
  IElementType CONTINUE = new DexTokenType("continue");
  IElementType DECIMALI = new DexTokenType("decimali");
  IElementType DEFAULT = new DexTokenType("default");
  IElementType DEFER = new DexTokenType("defer");
  IElementType DOT = new DexTokenType(".");
  IElementType ELSE = new DexTokenType("else");
  IElementType EQ = new DexTokenType("==");
  IElementType FALLTHROUGH = new DexTokenType("fallthrough");
  IElementType FLOAT = new DexTokenType("float");
  IElementType FLOATI = new DexTokenType("floati");
  IElementType FOR = new DexTokenType("for");
  IElementType FUNC = new DexTokenType("func");
  IElementType FUNCTION = new DexTokenType("function");
  IElementType GO = new DexTokenType("go");
  IElementType GOTO = new DexTokenType("goto");
  IElementType GREATER = new DexTokenType(">");
  IElementType GREATER_OR_EQUAL = new DexTokenType(">=");
  IElementType HEX = new DexTokenType("hex");
  IElementType IDENTIFIER = new DexTokenType("identifier");
  IElementType IF = new DexTokenType("if");
  IElementType IMPORT = new DexTokenType("import");
  IElementType INT = new DexTokenType("int");
  IElementType INTERFACE = new DexTokenType("interface");
  IElementType LBRACE = new DexTokenType("{");
  IElementType LBRACK = new DexTokenType("[");
  IElementType LESS = new DexTokenType("<");
  IElementType LESS_OR_EQUAL = new DexTokenType("<=");
  IElementType LITERALTYPEEXPR = new DexTokenType("LiteralTypeExpr");
  IElementType LPAREN = new DexTokenType("(");
  IElementType MAP = new DexTokenType("map");
  IElementType MINUS = new DexTokenType("-");
  IElementType MINUS_ASSIGN = new DexTokenType("-=");
  IElementType MINUS_MINUS = new DexTokenType("--");
  IElementType MUL = new DexTokenType("*");
  IElementType MULOP = new DexTokenType("MulOp");
  IElementType MUL_ASSIGN = new DexTokenType("*=");
  IElementType NOT = new DexTokenType("!");
  IElementType NOT_EQ = new DexTokenType("!=");
  IElementType OCT = new DexTokenType("oct");
  IElementType PACKAGE = new DexTokenType("package");
  IElementType PLUS = new DexTokenType("+");
  IElementType PLUS_ASSIGN = new DexTokenType("+=");
  IElementType PLUS_PLUS = new DexTokenType("++");
  IElementType QUOTIENT = new DexTokenType("/");
  IElementType QUOTIENT_ASSIGN = new DexTokenType("/=");
  IElementType RAW_STRING = new DexTokenType("raw_string");
  IElementType RBRACE = new DexTokenType("}");
  IElementType RBRACK = new DexTokenType("]");
  IElementType RELOP = new DexTokenType("RelOp");
  IElementType REMAINDER = new DexTokenType("%");
  IElementType REMAINDER_ASSIGN = new DexTokenType("%=");
  IElementType RETURN = new DexTokenType("return");
  IElementType RPAREN = new DexTokenType(")");
  IElementType SELECT = new DexTokenType("select");
  IElementType SEMICOLON = new DexTokenType(";");
  IElementType SEMICOLON_SYNTHETIC = new DexTokenType("<NL>");
  IElementType SEND_CHANNEL = new DexTokenType("<-");
  IElementType SHIFT_LEFT = new DexTokenType("<<");
  IElementType SHIFT_LEFT_ASSIGN = new DexTokenType("<<=");
  IElementType SHIFT_RIGHT = new DexTokenType(">>");
  IElementType SHIFT_RIGHT_ASSIGN = new DexTokenType(">>=");
  IElementType STRING = new DexTokenType("string");
  IElementType STRUCT = new DexTokenType("struct");
  IElementType SWITCH = new DexTokenType("switch");
  IElementType TRIPLE_DOT = new DexTokenType("...");
  IElementType TYPE_ = new DexTokenType("type");
  IElementType UNARYOP = new DexTokenType("UnaryOp");
  IElementType VAR = new DexTokenType("var");
  IElementType VAR_ASSIGN = new DexTokenType(":=");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADD_EXPR) {
        return new DexAddExprImpl(node);
      }
      else if (type == AND_EXPR) {
        return new DexAndExprImpl(node);
      }
      else if (type == BLOCK) {
        return new DexBlockImpl(node);
      }
      else if (type == CONDITIONAL_EXPR) {
        return new DexConditionalExprImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new DexFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_TYPE) {
        return new DexFunctionTypeImpl(node);
      }
      else if (type == IMPORT_DECLARATION) {
        return new DexImportDeclarationImpl(node);
      }
      else if (type == IMPORT_LIST) {
        return new DexImportListImpl(node);
      }
      else if (type == IMPORT_SPEC) {
        return new DexImportSpecImpl(node);
      }
      else if (type == IMPORT_STRING) {
        return new DexImportStringImpl(node);
      }
      else if (type == LEFT_HAND_EXPR_LIST) {
        return new DexLeftHandExprListImpl(node);
      }
      else if (type == MUL_EXPR) {
        return new DexMulExprImpl(node);
      }
      else if (type == OR_EXPR) {
        return new DexOrExprImpl(node);
      }
      else if (type == PACKAGE_CLAUSE) {
        return new DexPackageClauseImpl(node);
      }
      else if (type == PARAMETERS) {
        return new DexParametersImpl(node);
      }
      else if (type == PARAMETER_DECLARATION) {
        return new DexParameterDeclarationImpl(node);
      }
      else if (type == PARAM_DEFINITION) {
        return new DexParamDefinitionImpl(node);
      }
      else if (type == PARENTHESES_EXPR) {
        return new DexParenthesesExprImpl(node);
      }
      else if (type == PAR_TYPE) {
        return new DexParTypeImpl(node);
      }
      else if (type == RESULT) {
        return new DexResultImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new DexReturnStatementImpl(node);
      }
      else if (type == SIGNATURE) {
        return new DexSignatureImpl(node);
      }
      else if (type == STATEMENT) {
        return new DexStatementImpl(node);
      }
      else if (type == STRING_LITERAL) {
        return new DexStringLiteralImpl(node);
      }
      else if (type == TYPE) {
        return new DexTypeImpl(node);
      }
      else if (type == TYPE_LIST) {
        return new DexTypeListImpl(node);
      }
      else if (type == TYPE_REFERENCE_EXPRESSION) {
        return new DexTypeReferenceExpressionImpl(node);
      }
      else if (type == UNARY_EXPR) {
        return new DexUnaryExprImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
