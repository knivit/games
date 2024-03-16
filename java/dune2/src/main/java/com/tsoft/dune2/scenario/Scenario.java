package com.tsoft.dune2.scenario;

/**
 * Information about the current loaded scenario.
 */
public class Scenario {

    public int score;                                             /*!< Base score. */
    public int winFlags;                                          /*!< BASIC/WinFlags. */
    public int loseFlags;                                         /*!< BASIC/LoseFlags. */
    public int mapSeed;                                           /*!< MAP/Seed. */
    public int mapScale;                                          /*!< BASIC/MapScale. 0 is 62x62, 1 is 32x32, 2 is 21x21. */
    public int timeOut;                                           /*!< BASIC/TimeOut. */
    public char[] pictureBriefing = new char[14];                 /*!< BASIC/BriefPicture. */
    public char[] pictureWin = new char[14];                      /*!< BASIC/WinPicture. */
    public char[] pictureLose = new char[14];                     /*!< BASIC/LosePicture. */
    public int killedAllied;                                      /*!< Number of units lost by "You". */
    public int killedEnemy;                                       /*!< Number of units lost by "Enemy". */
    public int destroyedAllied;                                   /*!< Number of structures lost by "You". */
    public int destroyedEnemy;                                    /*!< Number of structures lost by "Enemy". */
    public int harvestedAllied;                                   /*!< Total amount of spice harvested by "You". */
    public int harvestedEnemy;                                    /*!< Total amount of spice harvested by "Enemy". */
    public Reinforcement[] reinforcement = new Reinforcement[16]; /*!< Reinforcement information. */
}
