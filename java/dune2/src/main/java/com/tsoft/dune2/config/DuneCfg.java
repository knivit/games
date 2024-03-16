package com.tsoft.dune2.config;

/**
 * This is the layout of decoded dune.cfg.
 */
public class DuneCfg {

    /* 0000(1)   */ public int graphicDrv;       /*!< Graphic mode to use. */
    /* 0001(1)   */ public int musicDrv;         /*!< Index into music drivers array. */
    /* 0002(1)   */ public int soundDrv;         /*!< Index into sound drivers array. */
    /* 0003(1)   */ public int voiceDrv;         /*!< Index into digitized sound drivers array. */
    /* 0004(1)   */ public boolean useMouse;     /*!< Use Mouse. */
    /* 0005(1)   */ public boolean useXMS;       /*!< Use Extended Memory. */
    /* 0006(1)   */ public int variable_0006;    /*!< ?? */
    /* 0007(1)   */ public int variable_0007;    /*!< ?? */
    /* 0008(1)   */ public int language;         /*!< @see Language. */
    /* 0009(1)   */ public int checksum;         /*!< Used to check validity on config data. See Config_Read(). */
}
