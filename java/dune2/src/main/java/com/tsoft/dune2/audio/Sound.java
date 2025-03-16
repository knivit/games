package com.tsoft.dune2.audio;

public class Sound {

    /** Maximal number of spoken audio fragments in one message. */
    public static final int NUM_SPEECH_PARTS = 5;

    /** Number of voices in the game. */
    public static final int NUM_VOICES = 131;

    /** Available voices.
     * Prefix :
     *  '+' : Don't include in voiceSet 0xFFFF
     *  '?' : don't preload voice in RAM
     *  '/' : Only include in voiceSet 0xFFFE
     *  '-' : Only include in voiceSet 0xFFFF
     *  '%' : Don't include in voiceSet 0xFFFF and 0xFFFE
     * %c => replaced by language 'F'(french) 'G'(german) 'Z'
     *       or house prefix char ('A'treides, 'O'rdos or Fremen,
     *           'H'arkonnen or Sardokar, 'M'ercenary)
     */
    public static VoiceData[] g_table_voices = new VoiceData[] {
        new VoiceData("+VSCREAM1.VOC",  11), /*   0 */
        new VoiceData("+EXSAND.VOC",    10), /*   1 */
        new VoiceData("+ROCKET.VOC",    11), /*   2 */
        new VoiceData("+BUTTON.VOC",    10), /*   3 */
        new VoiceData("+VSCREAM5.VOC",  11), /*   4 */
        new VoiceData("+CRUMBLE.VOC",   15), /*   5 */
        new VoiceData("+EXSMALL.VOC",    9), /*   6 */
        new VoiceData("+EXMED.VOC",     10), /*   7 */
        new VoiceData("+EXLARGE.VOC",   14), /*   8 */
        new VoiceData("+EXCANNON.VOC",  11), /*   9 */
        new VoiceData("+GUNMULTI.VOC",   9), /*  10 */
        new VoiceData("+GUN.VOC",       10), /*  11 */
        new VoiceData("+EXGAS.VOC",     10), /*  12 */
        new VoiceData("+EXDUD.VOC",     10), /*  13 */
        new VoiceData("+VSCREAM2.VOC",  11), /*  14 */
        new VoiceData("+VSCREAM3.VOC",  11), /*  15 */
        new VoiceData("+VSCREAM4.VOC",  11), /*  16 */
        new VoiceData("+%cAFFIRM.VOC",  15), /*  17 */
        new VoiceData("+%cREPORT1.VOC", 15), /*  18 */
        new VoiceData("+%cREPORT2.VOC", 15), /*  19 */
        new VoiceData("+%cREPORT3.VOC", 15), /*  20 */
        new VoiceData("+%cOVEROUT.VOC", 15), /*  21 */
        new VoiceData("+%cMOVEOUT.VOC", 15), /*  22 */
        new VoiceData("?POPPA.VOC",     15), /*  23 */
        new VoiceData("?SANDBUG.VOC",   15), /*  24 */
        new VoiceData("+STATICP.VOC",   10), /*  25 */
        new VoiceData("+WORMET3P.VOC",  16), /*  26 */
        new VoiceData("+MISLTINP.VOC",  10), /*  27 */
        new VoiceData("+SQUISH2.VOC",   12), /*  28 */
        new VoiceData("%cENEMY.VOC",    20), /*  29 */
        new VoiceData("%cHARK.VOC",     20), /*  30 */
        new VoiceData("%cATRE.VOC",     20), /*  31 */
        new VoiceData("%cORDOS.VOC",    20), /*  32 */
        new VoiceData("%cFREMEN.VOC",   20), /*  33 */
        new VoiceData("%cSARD.VOC",     20), /*  34 */
        new VoiceData("FILLER.VOC",     20), /*  35 */
        new VoiceData("%cUNIT.VOC",     20), /*  36 */
        new VoiceData("%cSTRUCT.VOC",   20), /*  37 */
        new VoiceData("%cONE.VOC",      19), /*  38 */
        new VoiceData("%cTWO.VOC",      19), /*  39 */
        new VoiceData("%cTHREE.VOC",    19), /*  40 */
        new VoiceData("%cFOUR.VOC",     19), /*  41 */
        new VoiceData("%cFIVE.VOC",     19), /*  42 */
        new VoiceData("%cCONST.VOC",    20), /*  43 */
        new VoiceData("%cRADAR.VOC",    20), /*  44 */
        new VoiceData("%cOFF.VOC",      20), /*  45 */
        new VoiceData("%cON.VOC",       20), /*  46 */
        new VoiceData("%cFRIGATE.VOC",  20), /*  47 */
        new VoiceData("?%cARRIVE.VOC",  20), /*  48 */
        new VoiceData("%cWARNING.VOC",  20), /*  49 */
        new VoiceData("%cSABOT.VOC",    20), /*  50 */
        new VoiceData("%cMISSILE.VOC",  20), /*  51 */
        new VoiceData("%cBLOOM.VOC",    20), /*  52 */
        new VoiceData("%cDESTROY.VOC",  20), /*  53 */
        new VoiceData("%cDEPLOY.VOC",   20), /*  54 */
        new VoiceData("%cAPPRCH.VOC",   20), /*  55 */
        new VoiceData("%cLOCATED.VOC",  20), /*  56 */
        new VoiceData("%cNORTH.VOC",    20), /*  57 */
        new VoiceData("%cEAST.VOC",     20), /*  58 */
        new VoiceData("%cSOUTH.VOC",    20), /*  59 */
        new VoiceData("%cWEST.VOC",     20), /*  60 */
        new VoiceData("?%cWIN.VOC",     20), /*  61 */
        new VoiceData("?%cLOSE.VOC",    20), /*  62 */
        new VoiceData("%cLAUNCH.VOC",   20), /*  63 */
        new VoiceData("%cATTACK.VOC",   20), /*  64 */
        new VoiceData("%cVEHICLE.VOC",  20), /*  65 */
        new VoiceData("%cREPAIR.VOC",   20), /*  66 */
        new VoiceData("%cHARVEST.VOC",  20), /*  67 */
        new VoiceData("%cWORMY.VOC",    20), /*  68 */
        new VoiceData("%cCAPTURE.VOC",  20), /*  69 */
        new VoiceData("%cNEXT.VOC",     20), /*  70 */
        new VoiceData("%cNEXT2.VOC",    20), /*  71 */
        new VoiceData("/BLASTER.VOC",   10), /*  72 */
        new VoiceData("/GLASS6.VOC",    10), /*  73 */
        new VoiceData("/LIZARD1.VOC",   10), /*  74 */
        new VoiceData("/FLESH.VOC",     10), /*  75 */
        new VoiceData("/CLICK.VOC",     10), /*  76 */
        new VoiceData("-3HOUSES.VOC",   12), /*  77 */
        new VoiceData("-ANDNOW.VOC",    12), /*  78 */
        new VoiceData("-ARRIVED.VOC",   12), /*  79 */
        new VoiceData("-BATTLE.VOC",    12), /*  80 */
        new VoiceData("-BEGINS.VOC",    12), /*  81 */
        new VoiceData("-BLDING.VOC",    12), /*  82 */
        new VoiceData("-CONTROL2.VOC",  12), /*  83 */
        new VoiceData("-CONTROL3.VOC",  12), /*  84 */
        new VoiceData("-CONTROL4.VOC",  12), /*  85 */
        new VoiceData("-CONTROLS.VOC",  12), /*  86 */
        new VoiceData("-DUNE.VOC",      12), /*  87 */
        new VoiceData("-DYNASTY.VOC",   12), /*  88 */
        new VoiceData("-EACHHOME.VOC",  12), /*  89 */
        new VoiceData("-EANDNO.VOC",    12), /*  90 */
        new VoiceData("-ECONTROL.VOC",  12), /*  91 */
        new VoiceData("-EHOUSE.VOC",    12), /*  92 */
        new VoiceData("-EMPIRE.VOC",    12), /*  93 */
        new VoiceData("-EPRODUCE.VOC",  12), /*  94 */
        new VoiceData("-ERULES.VOC",    12), /*  95 */
        new VoiceData("-ETERRIT.VOC",   12), /*  96 */
        new VoiceData("-EMOST.VOC",     12), /*  97 */
        new VoiceData("-ENOSET.VOC",    12), /*  98 */
        new VoiceData("-EVIL.VOC",      12), /*  99 */
        new VoiceData("-HARK.VOC",      12), /* 100 */
        new VoiceData("-HOME.VOC",      12), /* 101 */
        new VoiceData("-HOUSE2.VOC",    12), /* 102 */
        new VoiceData("-INSID.VOC",     12), /* 103 */
        new VoiceData("-KING.VOC",      12), /* 104 */
        new VoiceData("-KNOWN.VOC",     12), /* 105 */
        new VoiceData("-MELANGE.VOC",   12), /* 106 */
        new VoiceData("-NOBLE.VOC",     12), /* 107 */
        new VoiceData("?NOW.VOC",       12), /* 108 */
        new VoiceData("-OFDUNE.VOC",    12), /* 109 */
        new VoiceData("-ORD.VOC",       12), /* 110 */
        new VoiceData("-PLANET.VOC",    12), /* 111 */
        new VoiceData("-PREVAIL.VOC",   12), /* 112 */
        new VoiceData("-PROPOSED.VOC",  12), /* 113 */
        new VoiceData("-SANDLAND.VOC",  12), /* 114 */
        new VoiceData("-SPICE.VOC",     12), /* 115 */
        new VoiceData("-SPICE2.VOC",    12), /* 116 */
        new VoiceData("-VAST.VOC",      12), /* 117 */
        new VoiceData("-WHOEVER.VOC",   12), /* 118 */
        new VoiceData("?YOUR.VOC",      12), /* 119 */
        new VoiceData("?FILLER.VOC",    12), /* 120 */
        new VoiceData("-DROPEQ2P.VOC",  10), /* 121 */
        new VoiceData("/EXTINY.VOC",    10), /* 122 */
        new VoiceData("-WIND2BP.VOC",   10), /* 123 */
        new VoiceData("-BRAKES2P.VOC",  11), /* 124 */
        new VoiceData("-GUNSHOT.VOC",   10), /* 125 */
        new VoiceData("-GLASS.VOC",     11), /* 126 */
        new VoiceData("-MISSLE8.VOC",   10), /* 127 */
        new VoiceData("-CLANK.VOC",     10), /* 128 */
        new VoiceData("-BLOWUP1.VOC",   10), /* 129 */
        new VoiceData("-BLOWUP2.VOC",   11)  /* 130 */
    };

    static String _music_dune00 = "dune0";
    static String _music_dune01 = "dune1";
    static String _music_dune02 = "dune2";
    static String _music_dune03 = "dune3";
    static String _music_dune04 = "dune4";
    static String _music_dune05 = "dune5";
    static String _music_dune06 = "dune6";
    static String _music_dune07 = "dune7";
    static String _music_dune08 = "dune8";
    static String _music_dune09 = "dune9";
    static String _music_dune10 = "dune10";
    static String _music_dune11 = "dune11";
    static String _music_dune12 = "dune12";
    static String _music_dune13 = "dune13";
    static String _music_dune14 = "dune14";
    static String _music_dune15 = "dune15";
    static String _music_dune16 = "dune16";
    static String _music_dune17 = "dune17";
    static String _music_dune18 = "dune18";
    static String _music_dune19 = "dune19";
    static String _music_dune20 = "dune20";

    /**
     * Available music.
     * @note The code compares pointers rather than the text itself, thus strings must be unique.
     */
    public static MusicData[] g_table_musics = new MusicData[] {
        new MusicData(null, 0), /*  0 */
        new MusicData(_music_dune01, 2), /*  1 */
        new MusicData(_music_dune01, 3), /*  2 */
        new MusicData(_music_dune01, 4), /*  3 */
        new MusicData(_music_dune01, 5), /*  4 */
        new MusicData(_music_dune17, 4), /*  5 */
        new MusicData(_music_dune08, 3), /*  6 */
        new MusicData(_music_dune08, 2), /*  7 */
        new MusicData(_music_dune01, 6), /*  8 */
        new MusicData(_music_dune02, 6), /*  9 */
        new MusicData(_music_dune03, 6), /* 10 */
        new MusicData(_music_dune04, 6), /* 11 */
        new MusicData(_music_dune05, 6), /* 12 */
        new MusicData(_music_dune06, 6), /* 13 */
        new MusicData(_music_dune09, 4), /* 14 */
        new MusicData(_music_dune09, 5), /* 15 */
        new MusicData(_music_dune18, 6), /* 16 */
        new MusicData(_music_dune10, 7), /* 17 */
        new MusicData(_music_dune11, 7), /* 18 */
        new MusicData(_music_dune12, 7), /* 19 */
        new MusicData(_music_dune13, 7), /* 20 */
        new MusicData(_music_dune14, 7), /* 21 */
        new MusicData(_music_dune15, 7), /* 22 */
        new MusicData(_music_dune01, 8), /* 23 */
        new MusicData(_music_dune07, 2), /* 24 */
        new MusicData(_music_dune07, 3), /* 25 */
        new MusicData(_music_dune07, 4), /* 26 */
        new MusicData(_music_dune00, 2), /* 27 */
        new MusicData(_music_dune07, 6), /* 28 */
        new MusicData(_music_dune16, 7), /* 29 */
        new MusicData(_music_dune19, 4), /* 30 */
        new MusicData(_music_dune19, 2), /* 31 */
        new MusicData(_music_dune19, 3), /* 32 */
        new MusicData(_music_dune20, 2), /* 33 */
        new MusicData(_music_dune16, 8), /* 34 */
        new MusicData(_music_dune00, 3), /* 35 */
        new MusicData(_music_dune00, 4), /* 36 */
        new MusicData(_music_dune00, 5), /* 37 */
    };

    /**
     * Mapping soundID -> voice.
     */
    public static int[] g_table_voiceMapping = new int[] {
        0xFFFF, /*   0 */
        0xFFFF, /*   1 */
        0xFFFF, /*   2 */
        0xFFFF, /*   3 */
        0xFFFF, /*   4 */
        0xFFFF, /*   5 */
        0xFFFF, /*   6 */
        0xFFFF, /*   7 */
        0xFFFF, /*   8 */
        0xFFFF, /*   9 */
        0xFFFF, /*  10 */
        0xFFFF, /*  11 */
        0xFFFF, /*  12 */
        0xFFFF, /*  13 */
        0xFFFF, /*  14 */
        0xFFFF, /*  15 */
        0xFFFF, /*  16 */
        0xFFFF, /*  17 */
        0xFFFF, /*  18 */
        0xFFFF, /*  19 */
        13,     /*  20 */
        0xFFFF, /*  21 */
        0xFFFF, /*  22 */
        0xFFFF, /*  23 */
        121,    /*  24 */
        0xFFFF, /*  25 */
        0xFFFF, /*  26 */
        0xFFFF, /*  27 */
        0xFFFF, /*  28 */
        0xFFFF, /*  29 */
        0,      /*  30 */
        4,      /*  31 */
        14,     /*  32 */
        15,     /*  33 */
        16,     /*  34 */
        28,     /*  35 */
        0xFFFF, /*  36 */
        0xFFFF, /*  37 */
        3,      /*  38 */
        12,     /*  39 */
        1,      /*  40 */
        7,      /*  41 */
        2,      /*  42 */
        0xFFFF, /*  43 */
        5,      /*  44 */
        0xFFFF, /*  45 */
        0xFFFF, /*  46 */
        0xFFFF, /*  47 */
        0xFFFF, /*  48 */
        7,      /*  49 */
        6,      /*  50 */
        8,      /*  51 */
        0xFFFF, /*  52 */
        0xFFFF, /*  53 */
        122,    /*  54 */
        0xFFFF, /*  55 */
        9,      /*  56 */
        9,      /*  57 */
        11,     /*  58 */
        10,     /*  59 */
        43,     /*  60 */
        0xFFFF, /*  61 */
        25,     /*  62 */
        26,     /*  63 */
        27,     /*  64 */
        72,     /*  65 */
        73,     /*  66 */
        74,     /*  67 */
        75,     /*  68 */
        76,     /*  69 */
        0xFFFF, /*  70 */
        0xFFFF, /*  71 */
        0xFFFF, /*  72 */
        0xFFFF, /*  73 */
        0xFFFF, /*  74 */
        0xFFFF, /*  75 */
        0xFFFF, /*  76 */
        0xFFFF, /*  77 */
        0xFFFF, /*  78 */
        0xFFFF, /*  79 */
        0xFFFF, /*  80 */
        0xFFFF, /*  81 */
        0xFFFF, /*  82 */
        0xFFFF, /*  83 */
        0xFFFF, /*  84 */
        0xFFFF, /*  85 */
        0xFFFF, /*  86 */
        0xFFFF, /*  87 */
        0xFFFF, /*  88 */
        0xFFFF, /*  89 */
        0xFFFF, /*  90 */
        0xFFFF, /*  91 */
        0xFFFF, /*  92 */
        0xFFFF, /*  93 */
        0xFFFF, /*  94 */
        0xFFFF, /*  95 */
        0xFFFF, /*  96 */
        0xFFFF, /*  97 */
        0xFFFF, /*  98 */
        0xFFFF, /*  99 */
        0xFFFF, /* 100 */
        0xFFFF, /* 101 */
        0xFFFF, /* 102 */
        0xFFFF, /* 103 */
        0xFFFF, /* 104 */
        0xFFFF, /* 105 */
        0xFFFF, /* 106 */
        0xFFFF, /* 107 */
        123,    /* 108 */
        0xFFFF, /* 109 */
        124,    /* 110 */
        0xFFFF, /* 111 */
        125,    /* 112 */
        126,    /* 113 */
        127,    /* 114 */
        0xFFFF, /* 115 */
        0xFFFF, /* 116 */
        128,    /* 117 */
        129,    /* 118 */
        130     /* 119 */
    };

    /**
     * Feedback on events and user commands (English audio, viewport message, and sound).
     */
    public static Feedback[] g_feedback = new Feedback[] {
        new Feedback(new int[] {0x002B, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x33, 0x003C), /*  0 */
        new Feedback(new int[] {0x0031, 0x001D, 0x0024, 0x0037, 0xFFFF}, 0x34, 0xFFFF), /*  1 */
        new Feedback(new int[] {0x0031, 0x001D, 0x0024, 0x0037, 0x0039}, 0x34, 0xFFFF), /*  2 */
        new Feedback(new int[] {0x0031, 0x001D, 0x0024, 0x0037, 0x003A}, 0x34, 0xFFFF), /*  3 */
        new Feedback(new int[] {0x0031, 0x001D, 0x0024, 0x0037, 0x003B}, 0x34, 0xFFFF), /*  4 */
        new Feedback(new int[] {0x0031, 0x001D, 0x0024, 0x0037, 0x003C}, 0x34, 0xFFFF), /*  5 */
        new Feedback(new int[] {0x0031, 0x001E, 0x0024, 0x0037, 0xFFFF}, 0x35, 0xFFFF), /*  6 */
        new Feedback(new int[] {0x0031, 0x001F, 0x0024, 0x0037, 0xFFFF}, 0x36, 0xFFFF), /*  7 */
        new Feedback(new int[] {0x0031, 0x0020, 0x0024, 0x0037, 0xFFFF}, 0x37, 0xFFFF), /*  8 */
        new Feedback(new int[] {0x0031, 0x0021, 0x0024, 0x0037, 0xFFFF}, 0x38, 0xFFFF), /*  9 */
        new Feedback(new int[] {0x0031, 0x0022, 0x0037, 0xFFFF, 0xFFFF}, 0x39, 0xFFFF), /* 10 */
        new Feedback(new int[] {0x0031, 0x0023, 0x0024, 0x0037, 0xFFFF}, 0x3A, 0xFFFF), /* 11 */
        new Feedback(new int[] {0x0031, 0x0032, 0x0037, 0xFFFF, 0xFFFF}, 0x3B, 0xFFFF), /* 12 */
        new Feedback(new int[] {0x001D, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 13 */
        new Feedback(new int[] {0x001E, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x3C, 0xFFFF), /* 14 */
        new Feedback(new int[] {0x001F, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x3D, 0xFFFF), /* 15 */
        new Feedback(new int[] {0x0020, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x3E, 0xFFFF), /* 16 */
        new Feedback(new int[] {0x0021, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x3F, 0xFFFF), /* 17 */
        new Feedback(new int[] {0x0022, 0x0035, 0xFFFF, 0xFFFF, 0xFFFF}, 0x40, 0xFFFF), /* 18 */
        new Feedback(new int[] {0x0023, 0x0024, 0x0035, 0xFFFF, 0xFFFF}, 0x41, 0xFFFF), /* 19 */
        new Feedback(new int[] {0x0032, 0x0035, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 20 */
        new Feedback(new int[] {0x001D, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 21 */
        new Feedback(new int[] {0x001E, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x42, 0xFFFF), /* 22 */
        new Feedback(new int[] {0x001F, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x43, 0xFFFF), /* 23 */
        new Feedback(new int[] {0x0020, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x44, 0xFFFF), /* 24 */
        new Feedback(new int[] {0x0021, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x45, 0xFFFF), /* 25 */
        new Feedback(new int[] {0x0022, 0x0035, 0xFFFF, 0xFFFF, 0xFFFF}, 0x46, 0xFFFF), /* 26 */
        new Feedback(new int[] {0x0023, 0x0025, 0x0035, 0xFFFF, 0xFFFF}, 0x47, 0xFFFF), /* 27 */
        new Feedback(new int[] {0x002C, 0x002E, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 28 */
        new Feedback(new int[] {0x002C, 0x002D, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 29 */
        new Feedback(new int[] {0x001E, 0x0024, 0x0036, 0xFFFF, 0xFFFF}, 0x48, 0xFFFF), /* 30 */
        new Feedback(new int[] {0x001F, 0x0024, 0x0036, 0xFFFF, 0xFFFF}, 0x49, 0xFFFF), /* 31 */
        new Feedback(new int[] {0x0020, 0x0024, 0x0036, 0xFFFF, 0xFFFF}, 0x4A, 0xFFFF), /* 32 */
        new Feedback(new int[] {0x0021, 0x0024, 0x0036, 0xFFFF, 0xFFFF}, 0x4B, 0xFFFF), /* 33 */
        new Feedback(new int[] {0x0022, 0x0036, 0xFFFF, 0xFFFF, 0xFFFF}, 0x4C, 0xFFFF), /* 34 */
        new Feedback(new int[] {0x0023, 0x0024, 0x0036, 0xFFFF, 0xFFFF}, 0x4D, 0xFFFF), /* 35 */
        new Feedback(new int[] {0x0034, 0x0038, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 36 */
        new Feedback(new int[] {0x0031, 0x0044, 0xFFFF, 0xFFFF, 0xFFFF}, 0x4E, 0x0017), /* 37 */
        new Feedback(new int[] {0x002F, 0x0030, 0xFFFF, 0xFFFF, 0xFFFF}, 0x50, 0xFFFF), /* 38 */
        new Feedback(new int[] {0x0031, 0x0033, 0x0037, 0xFFFF, 0xFFFF}, 0x51, 0xFFFF), /* 39 */
        new Feedback(new int[] {0x003D, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 40 */
        new Feedback(new int[] {0x003E, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 41 */
        new Feedback(new int[] {0x0033, 0x003F, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 42 */
        new Feedback(new int[] {0x0026, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0x002E), /* 43 */
        new Feedback(new int[] {0x0027, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0x002E), /* 44 */
        new Feedback(new int[] {0x0028, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0x002E), /* 45 */
        new Feedback(new int[] {0x0029, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0x002E), /* 46 */
        new Feedback(new int[] {0x002A, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0x002E), /* 47 */
        new Feedback(new int[] {0x0040, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x5A, 0x0017), /* 48 */
        new Feedback(new int[] {0x001E, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9A, 0xFFFF), /* 49 */
        new Feedback(new int[] {0x001F, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9B, 0xFFFF), /* 50 */
        new Feedback(new int[] {0x0020, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9C, 0xFFFF), /* 51 */
        new Feedback(new int[] {0x0021, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9D, 0xFFFF), /* 52 */
        new Feedback(new int[] {0x0022, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9E, 0xFFFF), /* 53 */
        new Feedback(new int[] {0x0023, 0x0024, 0x003F, 0xFFFF, 0xFFFF}, 0x9F, 0xFFFF), /* 54 */
        new Feedback(new int[] {0x001E, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA2, 0xFFFF), /* 55 */
        new Feedback(new int[] {0x001F, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA3, 0xFFFF), /* 56 */
        new Feedback(new int[] {0x0020, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA4, 0xFFFF), /* 57 */
        new Feedback(new int[] {0x0021, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA5, 0xFFFF), /* 58 */
        new Feedback(new int[] {0x0022, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA6, 0xFFFF), /* 59 */
        new Feedback(new int[] {0x0023, 0x0041, 0x0042, 0xFFFF, 0xFFFF}, 0xA7, 0xFFFF), /* 60 */
        new Feedback(new int[] {0x0046, 0x0047, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 61 */
        new Feedback(new int[] {0x001E, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 62 */
        new Feedback(new int[] {0x001F, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 63 */
        new Feedback(new int[] {0x0020, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 64 */
        new Feedback(new int[] {0x0021, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 65 */
        new Feedback(new int[] {0x0022, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 66 */
        new Feedback(new int[] {0x0023, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 67 */
        new Feedback(new int[] {0x001E, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x93, 0xFFFF), /* 68 */
        new Feedback(new int[] {0x001F, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x94, 0xFFFF), /* 69 */
        new Feedback(new int[] {0x0020, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x95, 0xFFFF), /* 70 */
        new Feedback(new int[] {0x0021, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x96, 0xFFFF), /* 71 */
        new Feedback(new int[] {0x0022, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x97, 0xFFFF), /* 72 */
        new Feedback(new int[] {0x0023, 0x0043, 0x0036, 0xFFFF, 0xFFFF}, 0x98, 0xFFFF), /* 73 */
        new Feedback(new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x01, 0xFFFF), /* 74 */
        new Feedback(new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 75 */
        new Feedback(new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x01, 0xFFFF), /* 76 */
        new Feedback(new int[] {0x006F, 0x0069, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 77 */
        new Feedback(new int[] {0x0072, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 78 */
        new Feedback(new int[] {0x0065, 0x0073, 0x006A, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 79 */
        new Feedback(new int[] {0x0074, 0x0056, 0x005D, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 80 */
        new Feedback(new int[] {0x0076, 0x0053, 0x0054, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 81 */
        new Feedback(new int[] {0x0068, 0x0071, 0x0059, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 82 */
        new Feedback(new int[] {0x005C, 0x005E, 0x0061, 0x005B, 0xFFFF}, 0x00, 0xFFFF), /* 83 */
        new Feedback(new int[] {0x0062, 0x0060, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 84 */
        new Feedback(new int[] {0x005A, 0x005F, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 85 */
        new Feedback(new int[] {0x0075, 0x004F, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 86 */
        new Feedback(new int[] {0x004E, 0x004D, 0x0055, 0x006D, 0xFFFF}, 0x01, 0xFFFF), /* 87 */
        new Feedback(new int[] {0x006B, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 88 */
        new Feedback(new int[] {0x0067, 0x006E, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 89 */
        new Feedback(new int[] {0x0063, 0x0064, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 90 */
        new Feedback(new int[] {0x0066, 0x0070, 0xFFFF, 0xFFFF, 0xFFFF}, 0x00, 0xFFFF), /* 91 */
        new Feedback(new int[] {0x0077, 0x0050, 0x0051, 0xFFFF, 0xFFFF}, 0x01, 0xFFFF), /* 92 */
        new Feedback(new int[] {0x006C, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, 0x01, 0xFFFF)  /* 93 */
    };

    /** Translated audio feedback of events and user commands. */
    public static int[][] g_translatedVoice = new int[][] {
        new int[] {0x002B, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /*  0 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  1 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  2 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  3 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  4 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  5 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  6 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  7 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  8 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /*  9 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /* 10 */
        new int[] {0x0031, 0x001D, 0xFFFF, 0xFFFF, 0xFFFF}, /* 11 */
        new int[] {0x0031, 0x0032, 0xFFFF, 0xFFFF, 0xFFFF}, /* 12 */
        new int[] {0x0024, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 13 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 14 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 15 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 16 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 17 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 18 */
        new int[] {0x0037, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 19 */
        new int[] {0x0035, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 20 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 21 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 22 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 23 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 24 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 25 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 26 */
        new int[] {0x0025, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 27 */
        new int[] {0x002E, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 28 */
        new int[] {0x002D, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 29 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 30 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 31 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 32 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 33 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 34 */
        new int[] {0x0036, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 35 */
        new int[] {0x0034, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 36 */
        new int[] {0x0031, 0x0044, 0xFFFF, 0xFFFF, 0xFFFF}, /* 37 */
        new int[] {0x002F, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 38 */
        new int[] {0x0031, 0x0033, 0xFFFF, 0xFFFF, 0xFFFF}, /* 39 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 40 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 41 */
        new int[] {0x003F, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 42 */
        new int[] {0x0026, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 43 */
        new int[] {0x0027, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 44 */
        new int[] {0x0028, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 45 */
        new int[] {0x0029, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 46 */
        new int[] {0x002A, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 47 */
        new int[] {0x0040, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 48 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 49 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 50 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 51 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 52 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 53 */
        new int[] {0x0041, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 54 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 55 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 56 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 57 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 58 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 59 */
        new int[] {0x0042, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 60 */
        new int[] {0x0046, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 61 */
        new int[] {0x001E, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 62 */
        new int[] {0x001F, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 63 */
        new int[] {0x0020, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 64 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 65 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 66 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 67 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 68 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 69 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 70 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 71 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 72 */
        new int[] {0x0043, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 73 */
        new int[] {0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 74 */
        new int[] {0x0057, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 75 */
        new int[] {0x0052, 0x0058, 0xFFFF, 0xFFFF, 0xFFFF}, /* 76 */
        new int[] {0x006F, 0x0069, 0xFFFF, 0xFFFF, 0xFFFF}, /* 77 */
        new int[] {0x0072, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 78 */
        new int[] {0x0065, 0x0073, 0x006A, 0xFFFF, 0xFFFF}, /* 79 */
        new int[] {0x0074, 0x0056, 0x005D, 0xFFFF, 0xFFFF}, /* 80 */
        new int[] {0x0076, 0x0053, 0x0054, 0xFFFF, 0xFFFF}, /* 81 */
        new int[] {0x0068, 0x0071, 0x0059, 0xFFFF, 0xFFFF}, /* 82 */
        new int[] {0x005C, 0x005E, 0x0061, 0x005B, 0xFFFF}, /* 83 */
        new int[] {0x0062, 0x0060, 0xFFFF, 0xFFFF, 0xFFFF}, /* 84 */
        new int[] {0x005A, 0x005F, 0xFFFF, 0xFFFF, 0xFFFF}, /* 85 */
        new int[] {0x0075, 0x004F, 0xFFFF, 0xFFFF, 0xFFFF}, /* 86 */
        new int[] {0x004E, 0x004D, 0x0055, 0x006D, 0xFFFF}, /* 87 */
        new int[] {0x006B, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}, /* 88 */
        new int[] {0x0067, 0x006E, 0xFFFF, 0xFFFF, 0xFFFF}, /* 89 */
        new int[] {0x0063, 0x0064, 0xFFFF, 0xFFFF, 0xFFFF}, /* 90 */
        new int[] {0x0066, 0x0070, 0xFFFF, 0xFFFF, 0xFFFF}, /* 91 */
        new int[] {0x0077, 0x0050, 0x0051, 0xFFFF, 0xFFFF}, /* 92 */
        new int[] {0x006C, 0xFFFF, 0xFFFF, 0xFFFF, 0xFFFF}  /* 93 */
    };
}
