package com.tsoft.dune2.audio;

public class Driver {

    public int index;                                           /*!< Index of the loaded driver. */
    public String extension;                                    /*!< Extension used for music file names. */
    public int content;                                         /*!< Pointer to the file to play content. */
    public String filename;                                     /*!< Name of file to play. */
    public boolean contentMalloced;                             /*!< Wether content pointer is the result of a malloc. */

}
