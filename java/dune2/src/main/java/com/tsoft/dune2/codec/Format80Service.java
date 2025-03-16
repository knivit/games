package com.tsoft.dune2.codec;

public class Format80Service {

    /**
     * Decode a memory fragment which is encoded with 'format80'.
     * @param dest The place the decoded fragment will be loaded.
     * @param source The encoded fragment.
     * @param destLength The length of the destination buffer.
     * @return The length of decoded data.
     */
    public static int Format80_Decode(byte[] dest, byte[] source, int sourceOff, int destLength) {
        int destOff = 0;

        while (destOff != destLength) {
            int size;
            int offset;

            byte cmd = source[sourceOff++];

            if (cmd == 0x80) {
                /* Exit */
                break;

            } else if ((cmd & 0x80) == 0) {
                /* Short move, relative */
                size = (cmd >> 4) + 3;
                if (size > destLength - destOff) size = destLength - destOff;

                offset = ((cmd & 0xF) << 8) + (source[sourceOff++]);

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) {
                    dest[destOff] = dest[destOff - offset];
                    destOff++;
                }

            } else if (cmd == 0xFE) {
                /* Long set */
                size = source[sourceOff++];
                size += (source[sourceOff++]) << 8;
                if (size > destLength - destOff) size = destLength - destOff;

                for (; size > 0; size--) {
                    dest[destOff++] = source[sourceOff];
                }
                sourceOff++;
            } else if (cmd == 0xFF) {
                /* Long move, absolute */
                size = source[sourceOff++];
                size += (source[sourceOff++]) << 8;
                if (size > destLength - destOff) size = destLength - destOff;

                offset = source[sourceOff++];
                offset += (source[sourceOff++]) << 8;

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) {
                    dest[destOff++] = dest[offset++];
                }
            } else if ((cmd & 0x40) != 0) {
                /* Short move, absolute */
                size = (cmd & 0x3F) + 3;
                if (size > destLength - destOff) size = destLength - destOff;

                offset = source[sourceOff++];
                offset += (source[sourceOff++]) << 8;

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) {
                    dest[destOff++] = dest[offset++];
                }
            } else {
                /* Short copy */
                size = cmd & 0x3F;
                if (size > destLength - destOff) size = destLength - destOff;

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) dest[destOff++] = source[sourceOff++];
            }
        }

        return destOff;
    }
}
