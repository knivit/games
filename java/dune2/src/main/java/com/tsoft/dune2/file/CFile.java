package com.tsoft.dune2.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CFile {

    public static final int EOF = -1;

    private FileInputStream fin;
    private FileOutputStream fout;
    private int off;

    public static CFile fopen(String fileName, String mode) {
        return new CFile();
    }

    public void fclose() {

    }

    public byte[] fread(long len) {
        try {
            byte[] buf = new byte[(int) len];
            off += fin.read(buf, off, (int) len);
            return buf;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public void fseek(long pos, int mode) {

    }

    public int fwrite(byte buf, long len) {
        return (int)len;
    }

    public int putc(long val) {
        try {
            fout.write((byte)(val & 0xFF));
            return 1;
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
