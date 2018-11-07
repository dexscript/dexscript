// copyrightHeader.java
package com.dexscript.parser;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.dexscript.psi.GoCompositeElementType;
import com.dexscript.psi.GoTokenType;
import com.dexscript.psi.impl.*;

public interface GoTypes {

  IElementType BLOCK = new GoCompositeElementType("BLOCK");
  IElementType FUNCTION_DECLARATION = new GoCompositeElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_TYPE = new GoCompositeElementType("FUNCTION_TYPE");
  IElementType IMPORT_DECLARATION = new GoCompositeElementType("IMPORT_DECLARATION");
  IElementType IMPORT_LIST = new GoCompositeElementType("IMPORT_LIST");
  IElementType IMPORT_SPEC = new GoCompositeElementType("IMPORT_SPEC");
  IElementType IMPORT_STRING = new GoCompositeElementType("IMPORT_STRING");
  IElementType LEFT_HAND_EXPR_LIST = new GoCompositeElementType("LEFT_HAND_EXPR_LIST");
  IElementType PACKAGE_CLAUSE = new GoCompositeElementType("PACKAGE_CLAUSE");
  IElementType PARAMETERS = new GoCompositeElementType("PARAMETERS");
  IElementType PARAMETER_DECLARATION = new GoCompositeElementType("PARAMETER_DECLARATION");
  IElementType PARAM_DEFINITION = new GoCompositeElementType("PARAM_DEFINITION");
  IElementType PAR_TYPE = new GoCompositeElementType("PAR_TYPE");
  IElementType RESULT = new GoCompositeElementType("RESULT");
  IElementType RETURN_STATEMENT = new GoCompositeElementType("RETURN_STATEMENT");
  IElementType SIGNATURE = new GoCompositeElementType("SIGNATURE");
  IElementType STATEMENT = new GoCompositeElementType("STATEMENT");
  IElementType STRING_LITERAL = new GoCompositeElementType("STRING_LITERAL");
  IElementType TYPE = new GoCompositeElementType("TYPE");
  IElementType TYPE_LIST = new GoCompositeElementType("TYPE_LIST");
  IElementType TYPE_REFERENCE_EXPRESSION = new GoCompositeElementType("TYPE_REFERENCE_EXPRESSION");

  IElementType ASSIGN = new GoTokenType("=");
  IElementType BIT_AND = new GoTokenType("&");
  IElementType BIT_AND_ASSIGN = new GoTokenType("&=");
  IElementType BIT_CLEAR = new GoTokenType("&^");
  IElementType BIT_CLEAR_ASSIGN = new GoTokenType("&^=");
  IElementType BIT_OR = new GoTokenType("|");
  IElementType BIT_OR_ASSIGN = new GoTokenType("|=");
  IElementType BIT_XOR = new GoTokenType("^");
  IElementType BIT_XOR_ASSIGN = new GoTokenType("^=");
  IElementType BREAK = new GoTokenType("break");
  IElementType CASE = new GoTokenType("case");
  IElementType CHAN = new GoTokenType("chan");
  IElementType CHAR = new GoTokenType("char");
  IElementType COLON = new GoTokenType(":");
  IElementType COMMA = new GoTokenType(",");
  IElementType COND_AND = new GoTokenType("&&");
  IElementType COND_OR = new GoTokenType("||");
  IElementType CONST = new GoTokenType("const");
  IElementType CONTINUE = new GoTokenType("continue");
  IElementType DECIMALI = new GoTokenType("decimali");
  IElementType DEFAULT = new GoTokenType("default");
  IElementType DEFER = new GoTokenType("defer");
  IElementType DOT = new GoTokenType(".");
  IElementType ELSE = new GoTokenType("else");
  IElementType EQ = new GoTokenType("==");
  IElementType EXPRESSION = new GoTokenType("Expression");
  IElementType FALLTHROUGH = new GoTokenType("fallthrough");
  IElementType FLOAT = new GoTokenType("float");
  IElementType FLOATI = new GoTokenType("floati");
  IElementType FOR = new GoTokenType("for");
  IElementType FUNC = new GoTokenType("func");
  IElementType FUNCTION = new GoTokenType("function");
  IElementType GO = new GoTokenType("go");
  IElementType GOTO = new GoTokenType("goto");
  IElementType GREATER = new GoTokenType(">");
  IElementType GREATER_OR_EQUAL = new GoTokenType(">=");
  IElementType HEX = new GoTokenType("hex");
  IElementType IDENTIFIER = new GoTokenType("identifier");
  IElementType IF = new GoTokenType("if");
  IElementType IMPORT = new GoTokenType("import");
  IElementType INT = new GoTokenType("int");
  IElementType INTERFACE = new GoTokenType("interface");
  IElementType LBRACE = new GoTokenType("{");
  IElementType LBRACK = new GoTokenType("[");
  IElementType LESS = new GoTokenType("<");
  IElementType LESS_OR_EQUAL = new GoTokenType("<=");
  IElementType LITERALTYPEEXPR = new GoTokenType("LiteralTypeExpr");
  IElementType LPAREN = new GoTokenType("(");
  IElementType MAP = new GoTokenType("map");
  IElementType MINUS = new GoTokenType("-");
  IElementType MINUS_ASSIGN = new GoTokenType("-=");
  IElementType MINUS_MINUS = new GoTokenType("--");
  IElementType MUL = new GoTokenType("*");
  IElementType MUL_ASSIGN = new GoTokenType("*=");
  IElementType NOT = new GoTokenType("!");
  IElementType NOT_EQ = new GoTokenType("!=");
  IElementType OCT = new GoTokenType("oct");
  IElementType PACKAGE = new GoTokenType("package");
  IElementType PLUS = new GoTokenType("+");
  IElementType PLUS_ASSIGN = new GoTokenType("+=");
  IElementType PLUS_PLUS = new GoTokenType("++");
  IElementType QUOTIENT = new GoTokenType("/");
  IElementType QUOTIENT_ASSIGN = new GoTokenType("/=");
  IElementType RAW_STRING = new GoTokenType("raw_string");
  IElementType RBRACE = new GoTokenType("}");
  IElementType RBRACK = new GoTokenType("]");
  IElementType REMAINDER = new GoTokenType("%");
  IElementType REMAINDER_ASSIGN = new GoTokenType("%=");
  IElementType RETURN = new GoTokenType("return");
  IElementType RPAREN = new GoTokenType(")");
  IElementType SELECT = new GoTokenType("select");
  IElementType SEMICOLON = new GoTokenType(";");
  IElementType SEMICOLON_SYNTHETIC = new GoTokenType("<NL>");
  IElementType SEND_CHANNEL = new GoTokenType("<-");
  IElementType SHIFT_LEFT = new GoTokenType("<<");
  IElementType SHIFT_LEFT_ASSIGN = new GoTokenType("<<=");
  IElementType SHIFT_RIGHT = new GoTokenType(">>");
  IElementType SHIFT_RIGHT_ASSIGN = new GoTokenType(">>=");
  IElementType STRING = new GoTokenType("string");
  IElementType STRUCT = new GoTokenType("struct");
  IElementType SWITCH = new GoTokenType("switch");
  IElementType TRIPLE_DOT = new GoTokenType("...");
  IElementType TYPE_ = new GoTokenType("type");
  IElementType VAR = new GoTokenType("var");
  IElementType VAR_ASSIGN = new GoTokenType(":=");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == BLOCK) {
        return new DexBlockImpl(node);
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
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
