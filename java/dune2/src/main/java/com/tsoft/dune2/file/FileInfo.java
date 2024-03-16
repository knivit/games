package com.tsoft.dune2.file;

/**
 * Static information about files and their location.
 */
public class FileInfo {

    public static class Flags {
        public boolean inMemory;            /*!< File is loaded in alloc'd memory. */
        public boolean inPAKFile;           /*!< File can be in other PAK file. */
    }

	public String filename;                 /*!< Name of the file. */
    public long fileSize;                   /*!< The size of this file. */
    public byte[] buffer;                   /*!< In case the file is read in the memory, this is the location of the data. */
    public long filePosition;               /*!< Where in the file we currently are (doesn't have to start at zero when in PAK file). */
    public Flags flags;                     /*!< General flags of the FileInfo. */
}
