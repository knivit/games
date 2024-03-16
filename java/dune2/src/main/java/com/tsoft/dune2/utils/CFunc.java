package com.tsoft.dune2.utils;

public class CFunc {

    /** Converts number chars to an int */
    public static int atoi(String str) {
        int n = 0;
        if (str != null) {
            for (int i = 0; i < str.length(); i ++) {
                char ch = str.charAt(i);
                if (ch < '0' || ch > '9') {
                    break;
                }
                n = n * 10 + (ch - '0');
            }
        }
        return n;
    }

    /** Returns a substring after (including) the giver char */
    public static String strrchr(String str, char ch) {
        int n = (str == null) ? -1 : str.lastIndexOf(ch);
        return (n == -1) ? null : str.substring(n);
    }

    /** Converts a byte to int */
    public static int uint8(byte val) {
        return (val < 0) ? 256 + val : val;
    }

    /** Unsigned byte compare */
    public static int ucmp(byte a, byte b) {
        return Integer.compare(uint8(a), uint8(b));
    }

    /** Read 2 byte from the given offset and converts them to int */
    public static int READ_LE_int(byte[] buf, int off) {
        return uint8(buf[off]) + (uint8(buf[off + 1]) << 8);
    }

    /** Read 2 byte from the given offset and converts them to int */
    public static int READ_LE_long(byte[] buf, int off) {
        return uint8(buf[off]) + (uint8(buf[off + 1]) << 8) + (uint8(buf[off + 2]) << 16) + (uint8(buf[off + 3]) << 24);
    }
}
