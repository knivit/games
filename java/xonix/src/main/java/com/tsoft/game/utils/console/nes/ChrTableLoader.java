package com.tsoft.game.utils.console.nes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.InputStream;
import java.util.Arrays;

// NES CHR file format:
// - 1 sprite - 16 bytes
// - 1 CHR file is 8192 bytes length (2 tables by 256 sprites each)
//
//   .0 .1 .2 .3 .4 .5 .6 .7   .8 .9 .A .B .C .D .E .F
//   80 00 00 00 00 00 00 00 | 80 00 00 00 00 00 00 00
//
// 1 bit from .0 byte and 1 bit from .8 byte forms a number 00..11 (0-3)
// which is the number of a color from a palette
public class ChrTableLoader {

    public static ChrTable[] load(String chrAssetsName) {
        FileHandle assets = Gdx.files.internal(chrAssetsName);

        try (InputStream is = assets.read()) {
            byte[] buf = is.readAllBytes();
            if (buf.length == 0 || (buf.length % 4096) != 0) {
                throw new IllegalArgumentException("Invalid NES CHR asset: size is " + buf.length + ", must divide on 4096");
            }

            int numTables = buf.length / 4096;
            ChrTable[] tables = new ChrTable[numTables];

            for (int i = 0; i < tables.length; i ++) {
                tables[i] = new ChrTable();
                tables[i].buf = Arrays.copyOfRange(buf, i * 4096, i * 4096 + 4096);
            }

            return tables;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Error reading asset " + chrAssetsName, ex);
        }
    }
}
