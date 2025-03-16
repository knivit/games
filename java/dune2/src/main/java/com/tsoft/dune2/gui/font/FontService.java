package com.tsoft.dune2.gui.font;

import static com.tsoft.dune2.file.FileService.File_Exists;
import static com.tsoft.dune2.file.FileService.File_ReadWholeFile;
import static com.tsoft.dune2.os.EndianService.READ_LE_UINT16;

public class FontService {

    static Font g_fontIntro = null;
    static Font g_fontNew6p = null;
    static Font g_fontNew8p = null;

    static int g_fontCharOffset = -1;

    static Font g_fontCurrent = null;

    /**
     * Get the width of a char in pixels.
     *
     * @param c The char to get the width of.
     * @return The width of the char in pixels.
     */
    public static int Font_GetCharWidth(char c) {
        return g_fontCurrent.chars[c].width + g_fontCharOffset;
    }

    /**
     * Get the width of the string in pixels.
     *
     * @param string The string to get the width of.
     * @return The width of the string in pixels.
     */
    public static int Font_GetStringWidth(String string) {
        int width = 0;

        if (string == null) return 0;

        int i = 0;
        while (string.charAt(i) != '\0') {
            width += Font_GetCharWidth(string.charAt(i++));
        }

        return width;
    }

    /**
     * Load a font file.
     *
     * @param filename The name of the font file.
     * @return The pointer of the allocated memory where the file has been read.
     */
    static Font Font_LoadFile(String filename) {
        byte[] buf;
        Font f;
        int i;
        int start;
        int dataStart;
        int widthList;
        int lineList;

        if (File_Exists(filename) == 0) return null;

        buf = File_ReadWholeFile(filename);

        if (buf[2] != 0x00 || buf[3] != 0x05) {
            free(buf);
            return null;
        }

        f = new Font();
        start = READ_LE_UINT16(buf, 4);
        dataStart = READ_LE_UINT16(buf, 6);
        widthList = READ_LE_UINT16(buf, 8);
        lineList = READ_LE_UINT16(buf, 12);
        f.height = buf[start + 4];
        f.maxWidth = buf[start + 5];
        f.count = READ_LE_UINT16(buf, 10) - widthList;
        f.chars = new FontChar[f.count];

        for (i = 0; i < f.count; i++) {
            FontChar fc = f.chars[i];
            int dataOffset;
            int x;
            int y;

            fc.width = buf[widthList + i];
            fc.unusedLines = buf[lineList + i * 2];
            fc.usedLines = buf[lineList + i * 2 + 1];

            dataOffset = READ_LE_UINT16(buf, dataStart + i * 2);
            if (dataOffset == 0) continue;

            fc.data = new byte[fc.usedLines * fc.width];

            for (y = 0; y < fc.usedLines; y++) {
                for (x = 0; x < fc.width; x++) {
                    int data = buf[dataOffset + y * ((fc.width + 1) / 2) + x / 2];
                    if (x % 2 != 0) data >>= 4;
                    fc.data[y * fc.width + x] = (byte)(data & 0xF);
                }
            }
        }

        free(buf);

        return f;
    }

    /**
     * Select a font.
     *
     * @param font The pointer of the font to use.
     */
    static void Font_Select(Font f) {
        if (f == null) return;

        g_fontCurrent = f;
    }

    public static boolean Font_Init() {
        g_fontIntro = Font_LoadFile("INTRO.FNT");
        if ((g_config.language == LANGUAGE_GERMAN) && File_Exists("new6pg.fnt")) {
            g_fontNew6p = Font_LoadFile("new6pg.fnt");
        } else {
            g_fontNew6p = Font_LoadFile("new6p.fnt");
        }
        g_fontNew8p = Font_LoadFile("new8p.fnt");

        return g_fontNew8p != null;
    }

    static void Font_Unload(Font f) {
        int i;

        for (i = 0; i < f.count; i++) free(f.chars[i].data);
        free(f.chars);
        free(f);
    }

    public static void Font_Uninit() {
        Font_Unload(g_fontIntro); g_fontIntro = null;
        Font_Unload(g_fontNew6p); g_fontNew6p = null;
        Font_Unload(g_fontNew8p); g_fontNew8p = null;
    }
}
