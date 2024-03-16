package com.tsoft.dune2.file;

import java.io.FileInputStream;

public class CFile {

    private FileInputStream fs;
    private int off;

    public byte[] fread(long len) {
        try {
            byte[] buf = new byte[(int) len];
            off += fs.read(buf, off, (int) len);
            return buf;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
