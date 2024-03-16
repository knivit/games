package com.tsoft.dune2.unit;

import com.tsoft.dune2.gobject.GObjectInfo;

/**
 * Static information per Unit type.
 */
public class UnitInfo {

    public class Flags {
        public boolean isBullet;                               /*!< If true, Unit is a bullet / missile. */
        public boolean explodeOnDeath;                         /*!< If true, Unit exploses when dying. */
        public boolean sonicProtection;                        /*!< If true, Unit receives no damage of a sonic blast. */
        public boolean canWobble;                              /*!< If true, Unit will wobble around while moving on certain tiles. */
        public boolean isTracked;                              /*!< If true, Unit is tracked-based (and leaves marks in sand). */
        public boolean isGroundUnit;                           /*!< If true, Unit is ground-based. */
        public boolean mustStayInMap;                          /*!< Unit cannot leave the map and bounces off the border (air-based units). */
        public boolean firesTwice;                             /*!< If true, Unit fires twice. */
        public boolean impactOnSand;                           /*!< If true, hitting sand (as bullet / missile) makes an impact (crater-like). */
        public boolean isNotDeviatable;                        /*!< If true, Unit can't be deviated. */
        public boolean hasAnimationSet;                        /*!< If true, the Unit has two set of sprites for animation. */
        public boolean notAccurate;                            /*!< If true, Unit is a bullet and is not very accurate at hitting the target (rockets). */
        public boolean isNormalUnit;                           /*!< If true, Unit is a normal unit (not a bullet / missile, nor a sandworm / frigate). */
    }

    public GObjectInfo o = new GObjectInfo();                   /*!< Common to UnitInfo and StructureInfo. */
    public int indexStart;                                      /*!< At Unit create, between this and indexEnd (including) a free index is picked. */
    public int indexEnd;                                        /*!< At Unit create, between indexStart and this (including) a free index is picked. */
    public Flags flags = new Flags();                           /*!< General flags of the UnitInfo. */
    public int dimension;                                       /*!< The dimension of the Unit Sprite. */
    public int movementType;                                    /*!< MovementType of Unit. */
    public int animationSpeed;                                  /*!< Speed of sprite animation of Unit. */
    public int movingSpeedFactor;                               /*!< Factor speed of movement of Unit, where 256 is full speed. */
    public int  turningSpeed;                                   /*!< Speed of orientation change of Unit. */
    public int groundSpriteID;                                  /*!< SpriteID for north direction. */
    public int turretSpriteID;                                  /*!< SpriteID of the turret for north direction. */
    public int actionAI;                                        /*!< Default action for AI units. */
    public int displayMode;                                     /*!< How to draw the Unit. */
    public int destroyedSpriteID;                               /*!< SpriteID of burning Unit for north direction. Can be zero if no such animation. */
    public int fireDelay;                                       /*!< Time between firing at Normal speed. */
    public int fireDistance;                                    /*!< Maximal distance this Unit can fire from. */
    public int damage;                                          /*!< Damage this Unit does to other Units. */
    public int explosionType;                                   /*!< Type of the explosion of Unit. */
    public int  bulletType;                                     /*!< Type of the bullets of Unit. */
    public int bulletSound;                                     /*!< Sound for the bullets. */
}
