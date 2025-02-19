package com.tsoft.dune2.codec;

public class Format80Service {


    /**
     * Decode a memory fragment which is encoded with 'format80'.
     * @param dest The place the decoded fragment will be loaded.
     * @param source The encoded fragment.
     * @param destLength The length of the destination buffer.
     * @return The length of decoded data.
     */
    public static int Format80_Decode(byte[] dest, byte[] source, int destLength) {
        uint8 *start = dest;
        uint8 *end = dest + destLength;

        while (dest != end) {
            byte cmd;
            int size;
            int offset;

            cmd = *source++;

            if (cmd == 0x80) {
                /* Exit */
                break;

            } else if ((cmd & 0x80) == 0) {
                /* Short move, relative */
                size = (cmd >> 4) + 3;
                if (size > end - dest) size = (uint16)(end - dest);

                offset = ((cmd & 0xF) << 8) + (*source++);

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) { *dest = *(dest - offset); dest++; }

            } else if (cmd == 0xFE) {
                /* Long set */
                size = *source++;
                size += (*source++) << 8;
                if (size > end - dest) size = (uint16)(end - dest);

                memset(dest, (*source++), size);
                dest += size;

            } else if (cmd == 0xFF) {
                /* Long move, absolute */
                size = *source++;
                size += (*source++) << 8;
                if (size > end - dest) size = (uint16)(end - dest);

                offset = *source++;
                offset += (*source++) << 8;

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) *dest++ = start[offset++];

            } else if ((cmd & 0x40) != 0) {
                /* Short move, absolute */
                size = (cmd & 0x3F) + 3;
                if (size > end - dest) size = (uint16)(end - dest);

                offset = *source++;
                offset += (*source++) << 8;

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) *dest++ = start[offset++];

            } else {
                /* Short copy */
                size = cmd & 0x3F;
                if (size > end - dest) size = (uint16)(end - dest);

                /* This decoder assumes memcpy. As some platforms implement memcpy as memmove, this is much safer */
                for (; size > 0; size--) *dest++ = *source++;
            }
        }

        return (uint16)(dest - start);
    }
}
