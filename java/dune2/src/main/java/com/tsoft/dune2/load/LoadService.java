package com.tsoft.dune2.load;

import static com.tsoft.dune2.audio.SoundService.Sound_Output_Feedback;
import static com.tsoft.dune2.file.FileService.File_ReadBlockFile;
import static com.tsoft.dune2.file.SearchDirectory.SEARCHDIR_PERSONAL_DATA_DIR;
import static com.tsoft.dune2.gfx.GfxService.g_palette1;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;
import static com.tsoft.dune2.house.HouseType.HOUSE_MERCENARY;
import static com.tsoft.dune2.opendune.GameMode.GM_RESTART;
import static com.tsoft.dune2.opendune.OpenDuneService.*;
import static com.tsoft.dune2.os.EndianService.BETOH32;
import static com.tsoft.dune2.sprites.SpritesService.Sprites_LoadTiles;

public class LoadService {

    static int Load_FindChunk(FILE fp, int chunk) {
        uint32 header;
        uint32 length;

        while (fread(&header, sizeof(uint32), 1, fp) == 1) {
            if (fread(&length, sizeof(uint32), 1, fp) != 1) return 0;
            length = BETOH32(length);
            if (BETOH32(header) != chunk) {
                fseek(fp, length + (length & 1), SEEK_CUR);
                continue;
            }
            return length;
        }
        return 0;
    }

    static boolean Load_Main(FILE fp) {
        int position;
        int length;
        int header;
        int version;

        /* All OpenDUNE / Dune2 savegames should start with 'FORM' */
        if (fread(&header, sizeof(uint32), 1, fp) != 1) return false;
        if (BETOH32(header) != CC_FORM) {
            Error("Invalid magic header in savegame. Not an OpenDUNE / Dune2 savegame.");
            return false;
        }

        /* The total length field, which is ignored */
        if (fread(&length, sizeof(uint32), 1, fp) != 1) return false;

        /* The next 'chunk' is fake, and has no length field */
        if (fread(&header, sizeof(uint32), 1, fp) != 1) return false;
        if (BETOH32(header) != CC_SCEN) return false;

        position = ftell(fp);

        /* Find the 'INFO' chunk, as it contains the savegame version */
        version = 0;
        length = Load_FindChunk(fp, CC_INFO);
        if (length == 0) return false;

        /* Read the savegame version */
        if (!fread_le_uint16(&version, fp)) return false;
        length -= 2;
        if (version == 0) return false;

        if (version != 0x0290) {
            /* Get the scenarioID / campaignID */
            if (!Info_LoadOld(fp, length)) return false;

            g_gameMode = GM_RESTART;

            /* Find the 'PLYR' chunk */
            fseek(fp, position, SEEK_SET);
            length = Load_FindChunk(fp, CC_PLYR);
            if (length == 0) return false;

            /* Find the human player */
            if (!House_LoadOld(fp, length)) return false;

            GUI_DisplayModalMessage(String_Get_ByIndex(STR_WARNING_ORIGINAL_SAVED_GAMES_ARE_INCOMPATABLE_WITH_THE_NEW_VERSION_THE_BATTLE_WILL_BE_RESTARTED), 0xFFFF);

            return true;
        }

        /* Load the 'INFO' chunk'. It has to be the first chunk loaded */
        if (!Info_Load(fp, length)) return false;

        /* Rewind, and read other chunks */
        fseek(fp, position, SEEK_SET);
        while (fread(&header, sizeof(uint32), 1, fp) == 1) {
        if (fread(&length, sizeof(uint32), 1, fp) != 1) return false;
        length = BETOH32(length);

        switch (BETOH32(header)) {
            case CC_NAME: break; /* 'NAME' chunk is of no interest to us */
            case CC_INFO: break; /* 'INFO' chunk is already read */
            case CC_MAP : if (!Map_Load      (fp, length)) return false; break;
            case CC_PLYR: if (!House_Load    (fp, length)) return false; break;
            case CC_UNIT: if (!Unit_Load     (fp, length)) return false; break;
            case CC_BLDG: if (!Structure_Load(fp, length)) return false; break;
            case CC_TEAM: if (!Team_Load     (fp, length)) return false; break;
            case CC_ODUN: if (!UnitNew_Load  (fp, length)) return false; break;

            default:
                Error("Unknown chunk in savegame: %c%c%c%c (length: %d). Skipped.\n", header, header >> 8, header >> 16, header >> 24, length);
                break;
        }

        /* Savegames are word aligned */
        position += length + 8 + (length & 1);
        fseek(fp, position, SEEK_SET);
    }

        return true;
    }

    static boolean SaveGame_LoadFile(String filename) {
        FILE *fp;
        boolean res;

        Sound_Output_Feedback(0xFFFE);

        Game_Init();

        fp = fopendatadir(SEARCHDIR_PERSONAL_DATA_DIR, filename, "rb");
        if (fp == null) {
            Error("Failed to open file '%s' for reading.\n", filename);
            return false;
        }

        Sprites_LoadTiles();

        g_validateStrictIfZero++;
        res = Load_Main(fp);
        g_validateStrictIfZero--;

        fclose(fp);

        if (!res) {
            Error("Error while loading savegame '%s'.\n", filename);
            return false;
        }

        if (g_gameMode != GM_RESTART) Game_Prepare();

        return true;
    }

    /**
     * In case the current house is Mercenary, another palette is loaded.
     */
    static void Load_Palette_Mercenaries() {
        if (g_playerHouseID == HOUSE_MERCENARY) {
            File_ReadBlockFile("IBM.PAL", g_palette1, 256 * 3);
        }
    }
}
