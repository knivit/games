package com.tsoft.dune2.os;

import static com.tsoft.dune2.utils.CFunc.uint8;

public class EndianService {

    public static int endian_bswap16(int x) {
        return ((x & 0xFF00) >> 8 | (x & 0x00FF) << 8);
    }

    public static long endian_bswap32(long x) {
        return ((x & 0xFF000000) >> 24 | (x & 0x00FF0000) >> 8 | (x & 0x0000FF00) << 8 | (x & 0x000000FF) << 24);
    }

    public static long HTOBE32(long x) {
        return endian_bswap32(x);
    }

    public static long BETOH32(long x) {
        return endian_bswap32(x);
    }

    public static int HTOBE16(int x) {
        return endian_bswap16(x);
    }

    public static int BETOH16(int x) {
        return endian_bswap16(x);
    }

    public static long HTOLE32(long x) {
        return (x);
    }

    public static long LETOH32(long x) {
        return (x);
    }

    public static int HTOLE16(int x) {
        return (x);
    }

    public static int LETOH16(int x) {
        return (x);
    }

    public static int READ_LE_UINT16(byte[] p, int off) {
        return uint8(p[off + 0]) | ((uint8(p[off + 1]) << 8));
    }

    public static long READ_LE_UINT32(byte[] p, int off) {
        return (uint8(p[off + 0]) | ((long) uint8(p[off + 1]) << 8) | ((long) uint8(p[off + 2]) << 16) | ((long) uint8(p[off + 3) << 24));
    }

    public static int WRITE_LE_UINT16(int p, int value) {
        return ((p)[0] = ((value) & 0xFF), (p)[1] = (((value) >> 8) & 0xFF));
    }

    public static long READ_BE_UINT32(long p) {
        return (((long)(p)[0] << 24) | ((uint32)(p)[1] << 16) | ((uint32)(p)[2] << 8) | (uint32)(p)[3]);
    }
}
