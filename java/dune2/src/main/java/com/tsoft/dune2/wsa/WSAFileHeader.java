package com.tsoft.dune2.wsa;

/**
 * The header of a WSA file as on the disk.
 */
public class WSAFileHeader {
    /* 0000(2)   */ public int frames;                     /*!< Amount of animation frames in this WSA. */
    /* 0002(2)   */ public int width;                      /*!< Width of WSA. */
    /* 0004(2)   */ public int height;                     /*!< Height of WSA. */
    /* 0006(2)   */ public int requiredBufferSize;         /*!< The size the buffer has to be at least to process this WSA. */
    /* 0008(2)   */ public int hasPalette;                 /*!< Indicates if the WSA has a palette stored. */
    /* 000A(4)   */ public int firstFrameOffset;           /*!< Offset where animation starts. */
    /* 000E(4)   */ public int secondFrameOffset;          /*!< Offset where animation ends. */
}
