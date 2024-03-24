package com.tsoft.dune2.wsa;

import static com.tsoft.dune2.utils.CFunc.READ_LE_int;

/**
 * The header of a WSA file that is being read.
 */
public class WSAHeader {

    public int frameCurrent;            /*!< Current frame displaying. */
    public int frames;                  /*!< Total frames in WSA. */
    public int width;                   /*!< Width of WSA. */
    public int height;                  /*!< Height of WSA. */
    public int bufferLength;            /*!< Length of the buffer. */
    public byte[] buffer;               /*!< The buffer. */
    public byte[] fileContent;          /*!< The content of the file. */
    public String filename;             /*!< Filename of WSA. */
    public WSAFlags flags;              /*!< Flags of WSA. */
    public int lengthHeader;			/*!< length of file header (8 or 10) */

    public static WSAHeader from(byte[] wsa) {
        if (wsa == null) {
            return null;
        }

        WSAHeader header = new WSAHeader();
        header.frameCurrent = READ_LE_int(wsa, 0);
        header.frames = READ_LE_int(wsa, 2);
        header.width = READ_LE_int(wsa, 4);
        header.height = READ_LE_int(wsa, 6);
        header.bufferLength = READ_LE_int(wsa, 8);
        // ? header.fileContent = READ_LE_int(wsa, 10);
        // ? header.fileContent = READ_LE_int(wsa, 12);
        System.arraycopy(wsa, 14, header.filename.getBytes(), 0, 13);
        header.flags = new WSAFlags(wsa[26]);
        header.lengthHeader = READ_LE_int(wsa, 28);
        return header;
    }

    public static int sizeof() {
        return 30;
    }
}
