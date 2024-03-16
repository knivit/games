package com.tsoft.dune2.sprites;

import static com.tsoft.dune2.file.FileService.*;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_GetSize_ByIndex;
import static com.tsoft.dune2.gfx.GfxService.GFX_Screen_Get_ByIndex;
import static com.tsoft.dune2.gfx.Screen.SCREEN_2;
import static com.tsoft.dune2.input.MouseService.g_mouseDisabled;
import static com.tsoft.dune2.os.EndianService.HTOBE32;
import static com.tsoft.dune2.os.EndianService.READ_LE_UINT32;
import static com.tsoft.dune2.script.ScriptService.Script_LoadFromFile;
import static com.tsoft.dune2.sprites.IconMapEntries.*;
import static com.tsoft.dune2.utils.CFunc.READ_LE_int;
import static com.tsoft.dune2.utils.CFunc.uint8;

public class SpritesService {
    
    public static int[][] g_sprites = null;
    static int s_spritesCount = 0;
    int *g_iconRTBL = null;
    int *g_iconRPAL = null;
    int *g_tilesPixels = null;
    public static int[] g_iconMap = null;

    int *g_fileRgnclkCPS = null;
    void *g_fileRegionINI = null;
    int *g_regions = null;

    public static int g_veiledTileID;
    public static int g_bloomTileID;
    public static int g_landscapeTileID;
    public static int g_builtSlabTileID;
    public static int g_wallTileID;

    void *g_mouseSprite = null;
    void *g_mouseSpriteBuffer = null;

    static int s_mouseSpriteSize = 0;
    static int s_mouseSpriteBufferSize = 0;

    static boolean s_iconLoaded = false;

    /**
     * Loads the sprites.
     *
     * @param index The index of the list of sprite files to load.
     * @param sprites The array where to store CSIP for each loaded sprite.
     */
    static void Sprites_Load(String filename, String altFilename, int expectedCount) {
        int *buffer;
        int count;
        int i;
        int size;
        boolean oldFormat;	// true for Dune2 v1.0 format

        if (!File_Exists(filename)) {
            if (altFilename != null && File_Exists(altFilename)) {
                Warning("%s Does not exists, using %s instead\n", filename, altFilename);
                filename = altFilename;
            } else {
                Warning("%s Does not exists\n", filename);
                s_spritesCount += expectedCount;
                g_sprites = (int **)realloc(g_sprites, s_spritesCount * sizeof(int *));
                for (i = s_spritesCount - expectedCount; i < s_spritesCount; i++) g_sprites[i] = null;
                return;
            }
        }
        buffer = File_ReadWholeFile(filename);
        if (buffer == null) return;

        count = READ_LE_int(buffer);
        Debug("%s: %d %d\n", filename, count, expectedCount);
        oldFormat = (4 + (uint32)count * 4) != READ_LE_UINT32(buffer + 2);

        s_spritesCount += count;
        g_sprites = (int **)realloc(g_sprites, s_spritesCount * sizeof(int *));

        for (i = 0; i < count; i++) {
            int *dst = null;
            long offset = oldFormat ? (long)READ_LE_int(buffer + 2 + 2 * i) : READ_LE_UINT32(buffer + 2 + 4 * i);

            if (offset == 0) {
                Warning("Sprites %-12s %3d : Load Error\n", filename, i);
            } else {
			    int *src = buffer + offset;
                if (!oldFormat) src += 2;
                Debug("Sprites %-12s %3d : 0x%04x %2dx%2d %2d %5d %5d\n", filename, i,
                    READ_LE_int(src)/*Flags*/, READ_LE_int(src+3)/*Width*/, src[2], /* height */
                    src[5] /* height */, READ_LE_int(src+6) /* packed size */, READ_LE_int(src+8) /* decoded size */);
                if (g_unpackSHPonLoad && (src[0] & 0x2) == 0) {
                    size = READ_LE_int(src+8) + 10;
                    if (READ_LE_int(src) & 0x1) {
                        size += 16;	/* 16 bytes more for the palette */
                    }
                    dst = (int *)malloc(size);
                    if (dst == null) {
                        Error("Sprites_Load(%s) failed to allocate %u bytes\n", filename, size);
                    } else {
					    int *encoded_data = src;
                        int *decoded_data = dst;
					*decoded_data++ = *encoded_data++ | 0x2;	/* the sprite is not Format80 encoded any more */
                        memcpy(decoded_data, encoded_data, 5);
                        decoded_data += 5;
                        WRITE_LE_int(decoded_data, size);  /* new packed size */
                        decoded_data += 2;
                        encoded_data += 7;
					*decoded_data++ = *encoded_data++;    /* copy pixel size */
					*decoded_data++ = *encoded_data++;
                        if (READ_LE_int(src) & 0x1) {
                            memcpy(decoded_data, encoded_data, 16);	/* copy palette */
                            decoded_data += 16;
                            encoded_data += 16;
                        }
                        Format80_Decode(decoded_data, encoded_data, READ_LE_int(src+8));
                    }
                } else {
                    size = READ_LE_int(src + 6);	/* "packed" size */
                    dst = (int *)malloc(size);
                    if (dst == null) {
                        Error("Sprites_Load(%s) failed to allocate %u bytes\n", filename, size);
                    } else {
                        memcpy(dst, src, size);
                    }
                }
            }

            g_sprites[s_spritesCount - count + i] = dst;
        }
        if (expectedCount == 99 && count == 103) {
            // relocation of BTTN when loading SHAPES.SHP of Dune2 v1.0
            memcpy(g_sprites + 7, g_sprites + (s_spritesCount - count + 94), 4 * sizeof(int *));
            memmove(g_sprites + (s_spritesCount - count + 94), g_sprites + (s_spritesCount - count + 98), (count - 98) * sizeof(int *));
            s_spritesCount -= 4;
        } else if (expectedCount != count) {
            Warning("Sprites %-12s : %d sprites expected, found %d\n", filename, expectedCount, count);
        }

        free(buffer);
    }

    /**
     * Gets the width of the given sprite.
     *
     * @param sprite The sprite.
     * @return The width.
     */
    public static int Sprite_GetWidth(byte[] sprite) {
        if (sprite == null) return 0;

        return uint8(sprite[3]);
    }

    /**
     * Gets the height of the given sprite.
     *
     * @param sprite The sprite.
     * @return The height.
     */
    public static int Sprite_GetHeight(byte[] sprite) {
        if (sprite == null) return 0;

        return uint8(sprite[2]);
    }

    /**
     * Decodes an image.
     *
     * @param source The encoded image.
     * @param dest The place the decoded image will be.
     * @return The size of the decoded image.
     */
    static long Sprites_Decode(byte[] source, byte[] dest) {
        long size = 0;

        int sourceOff = 0;
        switch (source[0]) {
            case 0x0:
                sourceOff += 2;
                size = READ_LE_UINT32(source, sourceOff);
                sourceOff += 4;
                source += READ_LE_int(source, sourceOff);
                sourceOff += 2;
                System.arraycopy(source, sourceOff, dest, 0, (int)size);
                break;

            case 0x4:
                sourceOff += 6;
                source += READ_LE_int(source, sourceOff);
                sourceOff += 2;
                size = Format80_Decode(dest, source, 0xFFFF);
                break;

            default: break;
    }

        return size;
    }

    /**
     * Loads an ICN file.
     * NOTE : should be called "tiles"
     *
     * @param filename The name of the file to load.
     * @param screenID The index of a memory block where to store loaded sprites.
     */
    static void Tiles_LoadICNFile(String filename) {
        int fileIndex;

        long tilesDataLength;
        long tableLength;
        long paletteLength;
        byte[] info = new byte[4];

        fileIndex = ChunkFile_Open(filename);

        /* Get the length of the chunks */
        tilesDataLength = ChunkFile_Seek(fileIndex, HTOBE32(CC_SSET));
        tableLength     = ChunkFile_Seek(fileIndex, HTOBE32(CC_RTBL));
        paletteLength   = ChunkFile_Seek(fileIndex, HTOBE32(CC_RPAL));

        /* Read the header information */
        ChunkFile_Read(fileIndex, HTOBE32(CC_SINF), info, 4);
        GFX_Init_TilesInfo(info[0], info[1]);

        /* Get the SpritePixels chunk */
        free(g_tilesPixels);
        g_tilesPixels = calloc(1, tilesDataLength);
        ChunkFile_Read(fileIndex, HTOBE32(CC_SSET), g_tilesPixels, tilesDataLength);
        tilesDataLength = Sprites_Decode(g_tilesPixels, g_tilesPixels);
        /*g_tilesPixels = realloc(g_tilesPixels, tilesDataLength);*/

        /* Get the Table chunk */
        free(g_iconRTBL);
        g_iconRTBL = calloc(1, tableLength);
        ChunkFile_Read(fileIndex, HTOBE32(CC_RTBL), g_iconRTBL, tableLength);

        /* Get the Palette chunk */
        free(g_iconRPAL);
        g_iconRPAL = calloc(1, paletteLength);
        ChunkFile_Read(fileIndex, HTOBE32(CC_RPAL), g_iconRPAL, paletteLength);

        ChunkFile_Close(fileIndex);
    }

    /**
     * Loads the sprites for tiles.
     */
    public static void Sprites_LoadTiles() {
        if (s_iconLoaded) return;

        s_iconLoaded = true;

        Tiles_LoadICNFile("ICON.ICN");

        free(g_iconMap);
        g_iconMap = File_ReadWholeFileLE16("ICON.MAP");

        g_veiledTileID    = g_iconMap[g_iconMap[ICM_ICONGROUP_FOG_OF_WAR] + 16];
        g_bloomTileID     = g_iconMap[g_iconMap[ICM_ICONGROUP_SPICE_BLOOM]];
        g_builtSlabTileID = g_iconMap[g_iconMap[ICM_ICONGROUP_CONCRETE_SLAB] + 2];
        g_landscapeTileID = g_iconMap[g_iconMap[ICM_ICONGROUP_LANDSCAPE]];
        g_wallTileID      = g_iconMap[g_iconMap[ICM_ICONGROUP_WALLS]];

        Script_LoadFromFile("UNIT.EMC", g_scriptUnit, g_scriptFunctionsUnit, GFX_Screen_Get_ByIndex(SCREEN_2));
    }

    /**
     * Unloads the sprites for tiles.
     */
    public static void Sprites_UnloadTiles() {
        s_iconLoaded = false;
    }

    /**
     * Loads a CPS file.
     *
     * @param filename The name of the file to load.
     * @param screenID The index of a memory block where to store loaded data.
     * @return palette Loaded palette
     */
    static byte[] Sprites_LoadCPSFile(String filename, int screenID, byte[] palette) {
        int index;
        int size;
        byte[] buffer;
        int *buffer2;
        int paletteSize;

        buffer = GFX_Screen_Get_ByIndex(screenID);

        index = File_Open(filename, FILE_MODE_READ);
        if (index == FILE_INVALID) {
            Warning("Failed to open %s\n", filename);
            return 0;
        }

        size = File_Read_LE16(index);

        buffer = File_Read(index, 8);

        size -= 8;

        paletteSize = READ_LE_int(buffer, 6);

        if (palette != null && paletteSize != 0) {
            palette = File_Read(index, paletteSize);
        } else {
            File_Seek(index, paletteSize, 1);
        }

        buffer[6] = 0;	/* dont read palette next time */
        buffer[7] = 0;
        size -= paletteSize;

        buffer2 = GFX_Screen_Get_ByIndex(screenID);
        buffer2 += GFX_Screen_GetSize_ByIndex(screenID) - size - 8;

        memmove(buffer2, buffer, 8);
        File_Read(index, buffer2 + 8, size);

        File_Close(index);

        return Sprites_Decode(buffer2, buffer);
    }

    /**
     * Loads an image.
     *
     * @param filename The name of the file to load.
     * @param memory1 The index of a memory block where to store loaded data.
     * @param memory2 The index of a memory block where to store loaded data.
     * @param palette Where to store the palette, if any.
     * @return The size of the loaded image.
     */
    static int Sprites_LoadImage(String filename, int screenID, int *palette) {
        return Sprites_LoadCPSFile(filename, screenID, palette) / 8000;
    }

    public static void Sprites_SetMouseSprite(int hotSpotX, int hotSpotY, int *sprite) {
        int size;

        if (sprite == null || g_mouseDisabled != 0) return;

        while (g_mouseLock != 0) sleepIdle();

        g_mouseLock++;

        GUI_Mouse_Hide();

        size = GFX_GetSize(READ_LE_int(sprite + 3) + 16, sprite[5]);

        if (s_mouseSpriteBufferSize < size) {
            g_mouseSpriteBuffer = realloc(g_mouseSpriteBuffer, size);
            s_mouseSpriteBufferSize = size;
        }

        size = READ_LE_int(sprite + 8) + 10;
        if ((*sprite & 0x1) != 0) size += 16;

        if (s_mouseSpriteSize < size) {
            g_mouseSprite = realloc(g_mouseSprite, size);
            s_mouseSpriteSize = size;
        }

        if ((*sprite & 0x2) != 0) {
            memcpy(g_mouseSprite, sprite, READ_LE_int(sprite + 6));
        } else {
            int *dst = (int *)g_mouseSprite;
            int flags = sprite[0] | 0x2;

            dst[0] = flags;
            dst[1] = sprite[1];
            dst += 2;
            sprite += 2;

            memcpy(dst, sprite, 6);
            dst += 6;
            sprite += 6;

            size = READ_LE_int(sprite);
            dst[0] = sprite[0];
            dst[1] = sprite[1];
            dst += 2;
            sprite += 2;

            if ((flags & 0x1) != 0) {
                memcpy(dst, sprite, 16);
                dst += 16;
                sprite += 16;
            }

            Format80_Decode(dst, sprite, size);
        }

        g_mouseSpriteHotspotX = hotSpotX;
        g_mouseSpriteHotspotY = hotSpotY;

        sprite = g_mouseSprite;
        g_mouseHeight = sprite[5];
        g_mouseWidth = (READ_LE_int(sprite + 3) >> 3) + 2;

        GUI_Mouse_Show();

        g_mouseLock--;
    }

    static void InitRegions() {
        int *regions = g_regions;
        int i;
        char textBuffer[81];

        Ini_GetString("INFO", "TOTAL REGIONS", null, textBuffer, lengthof(textBuffer) - 1, g_fileRegionINI);

        sscanf(textBuffer, "%hu", &regions[0]);

        for (i = 0; i < regions[0]; i++) regions[i + 1] = 0xFFFF;
    }

    static void Sprites_CPS_LoadRegionClick() {
        int *buf;
        int i;
        String filename;

        buf = GFX_Screen_Get_ByIndex(SCREEN_2);

        g_fileRgnclkCPS = buf;
        Sprites_LoadCPSFile("RGNCLK.CPS", SCREEN_2, null);
        for (i = 0; i < 120; i++) memcpy(buf + (i * 304), buf + 7688 + (i * 320), 304);
        buf += 120 * 304;

        g_fileRegionINI = buf;
        filename = String.format("REGION%c.INI", g_table_houseInfo[g_playerHouseID].name[0]);
        buf += File_ReadFile(filename, buf);

        g_regions = (int *)buf;

        InitRegions();
    }

    /**
     * Check if a spriteID is part of the veiling sprites.
     * @param spriteID The sprite to check for.
     * @return True if and only if the spriteID is part of the veiling sprites.
     */
    public static boolean Tile_IsUnveiled(int tileID) {
        if (tileID > g_veiledTileID) return true;
        if (tileID < g_veiledTileID - 15) return true;

        return false;
    }

    static void Sprites_Init() {
        Sprites_Load("MOUSE.SHP", null, 7);              /*   0 -   6 */
        Sprites_Load(String_GenerateFilename("BTTN"), null, 5); /*   7 -  11 */
        Sprites_Load("SHAPES.SHP", null, 99);            /*  12 - 110 */
        Sprites_Load("UNITS2.SHP", null, 40);            /* 111 - 150 */
        Sprites_Load("UNITS1.SHP", null, 87);            /* 151 - 237 */
        Sprites_Load("UNITS.SHP", null, 117);            /* 238 - 354 */
        Sprites_Load(String_GenerateFilename("CHOAM"), "CHOAMSHP.SHP", 18); /* 355 - 372 */
        Sprites_Load(String_GenerateFilename("MENTAT"), "MENTAT.SHP", 14);  /* 373 - 386 */
        Sprites_Load("MENSHPH.SHP", null, 15);           /* 387 - 401 */
        Sprites_Load("MENSHPA.SHP", null, 15);           /* 402 - 416 */
        Sprites_Load("MENSHPO.SHP", null, 15);                    /* 417 - 431 */
        Sprites_Load("MENSHPM.SHP", null, 15);                    /* 432 - 446 (Placeholder - Fremen) */
        Sprites_Load("MENSHPM.SHP", null, 15);                    /* 447 - 461 (Placeholder - Sardaukar) */
        Sprites_Load("MENSHPM.SHP", null, 15);                    /* 462 - 476 */
        Sprites_Load("PIECES.SHP", null, 28);                     /* 477 - 504 */
        Sprites_Load("ARROWS.SHP", null, 9);                      /* 505 - 513 */
        Sprites_Load("CREDIT1.SHP", null, 1);                     /* 514 */
        Sprites_Load("CREDIT2.SHP", null, 1);                     /* 515 */
        Sprites_Load("CREDIT3.SHP", null, 1);                     /* 516 */
        Sprites_Load("CREDIT4.SHP", null, 1);                     /* 517 */
        Sprites_Load("CREDIT5.SHP", null, 1);                     /* 518 */
        Sprites_Load("CREDIT6.SHP", null, 1);                     /* 519 */
        Sprites_Load("CREDIT7.SHP", null, 1);                     /* 520 */
        Sprites_Load("CREDIT8.SHP", null, 1);                     /* 521 */
        Sprites_Load("CREDIT9.SHP", null, 1);                     /* 522 */
        Sprites_Load("CREDIT10.SHP", null, 1);                    /* 523 */
        Sprites_Load("CREDIT11.SHP", null, 1);                    /* 524 */
    }

    static void Sprites_Uninit() {
        int i;

        for (i = 0; i < s_spritesCount; i++) free(g_sprites[i]);
        free(g_sprites); g_sprites = null;

        free(g_mouseSpriteBuffer); g_mouseSpriteBuffer = null;
        free(g_mouseSprite); g_mouseSprite = null;

        free(g_tilesPixels); g_tilesPixels = null;
        free(g_iconRTBL); g_iconRTBL = null;
        free(g_iconRPAL); g_iconRPAL = null;

        free(g_iconMap); g_iconMap = null;
    }
}
