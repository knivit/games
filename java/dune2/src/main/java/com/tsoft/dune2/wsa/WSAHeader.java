package com.tsoft.dune2.wsa;

/**
 * The header of a WSA file that is being read.
 */
public class WSAHeader {
    public int frameCurrent;                                    /*!< Current frame displaying. */
    public int frames;                                          /*!< Total frames in WSA. */
    public int width;                                           /*!< Width of WSA. */
    public int height;                                          /*!< Height of WSA. */
    public int bufferLength;                                    /*!< Length of the buffer. */
    public byte[] buffer;                                       /*!< The buffer. */
    public byte[] fileContent;                                  /*!< The content of the file. */
    public char[] filename;                                     /*!< Filename of WSA. */
    public WSAFlags flags;                                      /*!< Flags of WSA. */
    public int lengthHeader;									/*!< length of file header (8 or 10) */
}
