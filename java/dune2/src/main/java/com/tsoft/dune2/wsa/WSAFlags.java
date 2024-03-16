package com.tsoft.dune2.wsa;

import static com.tsoft.dune2.utils.CFunc.isBitSet;

/**
 * The flags of a WSA Header.
 */
public class WSAFlags {

    public boolean notmalloced;                  /*!< If the WSA is in memory of the caller. */
    public boolean malloced;                     /*!< If the WSA is malloc'd by us. */
    public boolean dataOnDisk;                   /*!< Only the header is in the memory. Rest is on disk. */
    public boolean dataInMemory;                 /*!< The whole WSA is in memory. */
    public boolean displayInBuffer;              /*!< The output display is in the buffer. */
    public boolean noAnimation;                  /*!< If the WSA has animation or not. */
    public boolean hasNoFirstFrame;              /*!< The WSA is the continuation of another one. */
    public boolean hasPalette;                   /*!< Indicates if the WSA has a palette stored. */

    public WSAFlags(byte b) {
        notmalloced = isBitSet(b, 0);
        malloced = isBitSet(b, 1);
        dataOnDisk = isBitSet(b, 2);
        dataInMemory = isBitSet(b, 3);
        displayInBuffer = isBitSet(b, 4);
        noAnimation = isBitSet(b, 5);
        hasNoFirstFrame = isBitSet(b, 6);
        hasPalette = isBitSet(b, 7);
    }
}
