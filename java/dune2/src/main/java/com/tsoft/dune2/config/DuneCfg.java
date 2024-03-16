package com.tsoft.dune2.config;

/**
 * This is the layout of decoded dune.cfg.
 */
public class DuneCfg {
    /* 0000(1)   */ PACK uint8  graphicDrv;                 /*!< Graphic mode to use. */
    /* 0001(1)   */ PACK uint8  musicDrv;                   /*!< Index into music drivers array. */
    /* 0002(1)   */ PACK uint8  soundDrv;                   /*!< Index into sound drivers array. */
    /* 0003(1)   */ PACK uint8  voiceDrv;                   /*!< Index into digitized sound drivers array. */
    /* 0004(1)   */ PACK bool   useMouse;                   /*!< Use Mouse. */
    /* 0005(1)   */ PACK bool   useXMS;                     /*!< Use Extended Memory. */
    /* 0006(1)   */ PACK uint8  variable_0006;              /*!< ?? */
    /* 0007(1)   */ PACK uint8  variable_0007;              /*!< ?? */
    /* 0008(1)   */ PACK uint8  language;                   /*!< @see Language. */
    /* 0009(1)   */ PACK uint8  checksum;                   /*!< Used to check validity on config data. See Config_Read(). */
}
