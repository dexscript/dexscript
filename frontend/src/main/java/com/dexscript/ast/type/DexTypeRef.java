package com.dexscript.ast.type;

import com.dexscript.ast.core.Expect;
import com.dexscript.ast.core.State;
import com.dexscript.ast.core.Text;
import com.dexscript.ast.token.A2Z;
import com.dexscript.ast.token.Blank;
import com.dexscript.ast.token.Zero2Nine;

public class DexTypeRef extends DexType {

    private Text matched;
    private String pkgName;
    private String typeName;

    public DexTypeRef(Text src) {
        super(src);
        new Parser();
    }

    public static DexTypeRef $(String src) {
        return new DexTypeRef(new Text(src));
    }

    @Override
    public int leftRank() {
        return 0;
    }

    @Override
    public int begin() {
        return matched.begin;
    }

    @Override
    public int end() {
        return matched.end;
    }

    @Override
    public boolean matched() {
        return matched != null;
    }

    @Override
    public void walkDown(Visitor visitor) {

    }

    public String pkgName() {
        return pkgName;
    }

    public String typeName() {
        return typeName;
    }

    private class Parser {

        int i;
        int typeRefBegin;
        int typeNameBegin;

        Parser() {
            i = src.begin;
            State.Play(this::firstChar);
        }

        @Expect("blank")
        @Expect("$")
        @Expect("a~z")
        @Expect("A~Z")
        State firstChar() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (Blank.$(b)) {
                    continue;
                }
                if (A2Z.$(b)) {
                    typeRefBegin = i;
                    typeNameBegin= i;
                    i += 1;
                    return this::remainingChars;
                }
                return null;
            }
            return null;
        }

        @Expect("a~z")
        @Expect("A~Z")
        @Expect("0~9")
        @Expect("_")
        State remainingChars() {
            for (; i < src.end; i++) {
                byte b = src.bytes[i];
                if (b == '.') {
                    typeNameBegin= i + 1;
                    continue;
                }
                if (A2Z.$(b) || Zero2Nine.$(b) || b == '_') {
                    continue;
                }
                break;
            }
            matched = new Text(src.bytes, typeRefBegin, i);
            typeName = matched.slice(typeNameBegin).toString();
            if (typeNameBegin == typeRefBegin) {
                pkgName = "";
            } else {
                pkgName = matched.slice(matched.begin, typeNameBegin - 1).toString();
            }
            return null;
        }
    }
}
