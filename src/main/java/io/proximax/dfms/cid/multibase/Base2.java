package io.proximax.dfms.cid.multibase;

import org.apache.commons.lang3.StringUtils;

public class Base2 {

    public static byte[] decode(String binary) {
        byte[] res = new byte[binary.length()/8];
        for (int i=0; i < res.length; i++) {
            res[i] = (byte) Integer.parseInt(binary.substring(8 * i, 8 * i + 8), 2);
        }
        return res;
    }

    public static String encode(byte[] data) {
        StringBuilder s = new StringBuilder();
        for (byte b : data) {
            s.append(StringUtils.leftPad(Integer.toBinaryString(b & 0xFF), 8, "0"));
        }
        return s.toString();
    }
}
