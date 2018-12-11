package com.dexscript.runtime.std;

import com.dexscript.runtime.UInt8;

public class Encodes {

    public static UInt8[] Encode(String arg0) {
        byte[] bytes = arg0.getBytes();
        UInt8[] encoded = new UInt8[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            encoded[i] = new UInt8(b);
        }
        return encoded;
    }
}
