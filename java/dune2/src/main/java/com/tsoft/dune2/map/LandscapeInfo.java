package com.tsoft.dune2.map;

/**
 * Information about LandscapeType.
 */
public class LandscapeInfo {

    public int[] movementSpeed = new int[6];         /*!< Per MovementType the speed a Unit has on this LandscapeType. */
    public boolean letUnitWobble;                    /*!< True if a Unit on this LandscapeType should wobble around while moving on it. */
    public boolean isValidForStructure;              /*!< True if a Structure with notOnConcrete false can be build on this LandscapeType. */
    public boolean isSand;                           /*!< True if the LandscapeType is a sand tile (sand, dune, spice, thickspice, bloom). */
    public boolean isValidForStructure2;             /*!< True if a Structure with notOnConcrete true can be build on this LandscapeType. */
    public boolean canBecomeSpice;                   /*!< True if the LandscapeType can become a spice tile. */
    public int craterType;                           /*!< Type of crater on tile; 0 for none, 1 for sand, 2 for concrete. */
    public int radarColour;                          /*!< Colour used on radar for this LandscapeType. */
    public int spriteID;                             /*!< Sprite used on map for this LandscapeType. */

    public LandscapeInfo(int[] movementSpeed, boolean letUnitWobble, boolean isValidForStructure, boolean isSand, boolean isValidForStructure2, boolean canBecomeSpice, int craterType, int radarColour, int spriteID) {
        this.movementSpeed = movementSpeed;
        this.letUnitWobble = letUnitWobble;
        this.isValidForStructure = isValidForStructure;
        this.isSand = isSand;
        this.isValidForStructure2 = isValidForStructure2;
        this.canBecomeSpice = canBecomeSpice;
        this.craterType = craterType;
        this.radarColour = radarColour;
        this.spriteID = spriteID;
    }
}
