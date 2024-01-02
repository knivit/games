package com.tsoft.game.utils.console.nes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;

public class PPU {

    // http://www.romdetectives.com/Wiki/index.php?title=NES_Palette
    // 0..63
    private static final int[] PALETTE = new int[] {
        0x7C7C7C,
        0x0000FC,
        0x0000BC,
        0x4428BC,
        0x940084,
        0xA80020,
        0xA81000,
        0x881400,
        0x503000,
        0x007800,
        0x006800,
        0x005800,
        0x004058,
        0x000000,
        0x000000,
        0x000000,
        0xBCBCBC,
        0x0078F8,
        0x0058F8,
        0x6844FC,
        0xD800CC,
        0xE40058,
        0xF83800,
        0xE45C10,
        0xAC7C00,
        0x00B800,
        0x00A800,
        0x00A844,
        0x008888,
        0x000000,
        0x000000,
        0x000000,
        0xF8F8F8,
        0x3CBCFC,
        0x6888FC,
        0x9878F8,
        0xF878F8,
        0xF85898,
        0xF87858,
        0xFCA044,
        0xF8B800,
        0xB8F818,
        0x58D854,
        0x58F898,
        0x00E8D8,
        0x787878,
        0x000000,
        0x000000,
        0xFCFCFC,
        0xA4E4FC,
        0xB8B8F8,
        0xD8B8F8,
        0xF8B8F8,
        0xF8A4C0,
        0xF0D0B0,
        0xFCE0A8,
        0xF8D878,
        0xD8F878,
        0xB8F8B8,
        0xB8F8D8,
        0x00FCFC,
        0xF8D8F8,
        0x000000,
        0x000000
    };

    public Pixmap pixmap;

    public PPU() {
        pixmap = new Pixmap(16 * 8, 16 * 8, Pixmap.Format.RGB888);
    }

    public void draw(ChrTable table, int[] paletteIndexes) {
        Color color = new Color(1, 1, 1, 1);

        for (int cy = 0; cy < 16; cy ++) {
            for (int cx = 0; cx < 16; cx ++) {
                for (int ny = 0; ny < 8; ny ++) {
                    int n = cy * 256 + cx * 16 + ny;
                    byte b1 = table.buf[n];
                    byte b2 = table.buf[n + 8];

                    int x = cx * 8;
                    int y = cy * 8 + ny;
                    for (int i = 7; i >= 0; i--) {
                        int index = ((b1 >> (i - 1)) & (byte) 0x02) | ((b2 >> i) & (byte) 0x01);
                        int colorIndex = paletteIndexes[index];
                        Color.rgb888ToColor(color, PALETTE[colorIndex]);
                        pixmap.drawPixel(x ++, y, Color.rgba8888(color));
                    }
                }
            }
        }

        //PixmapIO.writePNG(Gdx.files.local("assets/pixmap.png"), pixmap);
    }
}
