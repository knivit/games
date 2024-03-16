package com.tsoft.dune2.house;

/**
 * Static information per House type.
 */
public class HouseInfo {

	public String name;                                       /* Pointer to name of house. */
    public int toughness;                                     /* How though the House is. Gives probability of deviation and chance of retreating. */
    public int degradingChance;                               /* On Unit create, this is the chance a Unit will be set to 'degrading'. */
    public int degradingAmount;                               /* Amount of damage dealt to degrading Structures. */
    public int minimapColor;                                  /* The color used on the minimap. */
    public int specialCountDown;                              /* Time between activation of Special Weapon. */
    public int starportDeliveryTime;                          /* Time it takes for a starport delivery. */
    public int prefixChar;                                    /* Char used as prefix for some filenames. */
    public int specialWeapon;                                 /* Which Special Weapon this House has. @see HouseWeapon. */
    public int musicWin;                                      /* Music played when you won a mission. */
    public int musicLose;                                     /* Music played when you lose a mission. */
    public int musicBriefing;                                 /* Music played during initial briefing of mission. */
	public String voiceFilename;                              /* Pointer to filename with the voices of the house. */

    public HouseInfo(String name, int toughness, int degradingChance, int degradingAmount, int minimapColor, int specialCountDown, int starportDeliveryTime, int prefixChar, int specialWeapon, int musicWin, int musicLose, int musicBriefing, String voiceFilename) {
        this.name = name;
        this.toughness = toughness;
        this.degradingChance = degradingChance;
        this.degradingAmount = degradingAmount;
        this.minimapColor = minimapColor;
        this.specialCountDown = specialCountDown;
        this.starportDeliveryTime = starportDeliveryTime;
        this.prefixChar = prefixChar;
        this.specialWeapon = specialWeapon;
        this.musicWin = musicWin;
        this.musicLose = musicLose;
        this.musicBriefing = musicBriefing;
        this.voiceFilename = voiceFilename;
    }
}
