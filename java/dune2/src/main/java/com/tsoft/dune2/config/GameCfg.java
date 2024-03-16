package com.tsoft.dune2.config;

/**
 * This is the layout of options.cfg.
 */
public class GameCfg {

    public int music;                      /*!< 0:Off, 1:On. */
    public int sounds;                     /*!< 0:Off, 1:On. */
    public int gameSpeed;                  /*!< 0:Slowest, 1:Slow, 2:Normal, 3:Fast, 4:Fastest. */
    public int hints;                      /*!< 0:Off, 1:On. */
    public int autoScroll;                 /*!< 0:Off, 1:On. */

    public GameCfg(int music, int sounds, int gameSpeed, int hints, int autoScroll) {
        this.music = music;
        this.sounds = sounds;
        this.gameSpeed = gameSpeed;
        this.hints = hints;
        this.autoScroll = autoScroll;
    }
}
