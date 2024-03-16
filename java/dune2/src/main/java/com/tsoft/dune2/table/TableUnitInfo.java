package com.tsoft.dune2.table;

import com.tsoft.dune2.gobject.GObjectInfo;
import com.tsoft.dune2.gobject.GObjectInfoFlags;
import com.tsoft.dune2.unit.UnitInfo;

import static com.tsoft.dune2.explosion.ExplosionType.*;
import static com.tsoft.dune2.house.HouseFlag.*;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureFlag.FLAG_STRUCTURE_HOUSE_OF_IX;
import static com.tsoft.dune2.structure.StructureFlag.FLAG_STRUCTURE_NONE;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.DisplayMode.*;
import static com.tsoft.dune2.unit.MovementType.*;
import static com.tsoft.dune2.unit.UnitType.*;

public class TableUnitInfo {

    public final int[] g_table_actionsAI = new int[] {ACTION_HUNT, ACTION_AREA_GUARD, ACTION_AMBUSH, ACTION_GUARD};

    private static UnitInfo UI_0() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_CARRYALL;
        ui.o.name                       = "Carryall";
        ui.o.stringID_full              = STR_ALLPURPOSE_CARRYALL;
        ui.o.wsa                        = "carryall.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = true;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = false;
        ui.o.flags.scriptNoSlowdown     = true;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 100;
        ui.o.fogUncoverRadius           = 0;
        ui.o.spriteID                   = 89;
        ui.o.buildCredits               = 800;
        ui.o.buildTime                  = 64;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 16;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 20;
        ui.o.priorityTarget             = 16;
        ui.o.availableHouse             = FLAG_HOUSE_ALL;
        ui.indexStart                   = 0;
        ui.indexEnd                     = 10;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = false;
        ui.flags.mustStayInMap          = true;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = true;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 32;
        ui.movementType                 = MOVEMENT_WINGER;
        ui.animationSpeed               = 0;
        ui.movingSpeedFactor            = 200;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 283;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_STOP;
        ui.displayMode                  = DISPLAYMODE_UNIT;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 0;
        ui.fireDistance                 = 0;
        ui.damage                       = 0;
        ui.explosionType                = EXPLOSION_INVALID;
        ui.bulletType                   = UNIT_INVALID;
        ui.bulletSound                  = 42;

        return ui;
    }

    private static UnitInfo UI_1() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_THOPTER;
        ui.o.name                       = "'Thopter";
        ui.o.stringID_full              = STR_ORNITHIPTER;
        ui.o.wsa                        = "orni.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = true;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = false;
        ui.o.flags.scriptNoSlowdown     = true;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 25;
        ui.o.fogUncoverRadius           = 5;
        ui.o.spriteID                   = 97;
        ui.o.buildCredits               = 600;
        ui.o.buildTime                  = 96;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_HOUSE_OF_IX;
        ui.o.sortPriority               = 28;
        ui.o.upgradeLevelRequired       = 1;
        ui.o.actionsPlayer              = new int [] { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 75;
        ui.o.priorityTarget             = 30;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_ATREIDES;
        ui.indexStart                   = 0;
        ui.indexEnd                     = 10;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = true;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = false;
        ui.flags.mustStayInMap          = true;
        ui.flags.firesTwice             = true;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 24;
        ui.movementType                 = MOVEMENT_WINGER;
        ui.animationSpeed               = 7;
        ui.movingSpeedFactor            = 150;
        ui.turningSpeed                 = 2;
        ui.groundSpriteID               = 289;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_STOP;
        ui.displayMode                  = DISPLAYMODE_ORNITHOPTER;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 50;
        ui.fireDistance                 = 50;
        ui.damage                       = 50;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_MISSILE_TROOPER;
        ui.bulletSound                  = 42;

        return ui;
    }

    private static UnitInfo UI_2() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_INFANTRY;
        ui.o.name                       = "Infantry";
        ui.o.stringID_full              = STR_LIGHT_INFANTRY_SQUAD;
        ui.o.wsa                        = "infantry.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 50;
        ui.o.fogUncoverRadius           = 1;
        ui.o.spriteID                   = 93;
        ui.o.buildCredits               = 100;
        ui.o.buildTime                  = 32;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 4;
        ui.o.upgradeLevelRequired       = 1;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 20;
        ui.o.priorityTarget             = 20;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_ATREIDES;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = true;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_FOOT;
        ui.animationSpeed               = 15;
        ui.movingSpeedFactor            = 5;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 329;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_INFANTRY_4_FRAMES;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 45;
        ui.fireDistance                 = 2;
        ui.damage                       = 3;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 58;

        return ui;
    }

    private static UnitInfo UI_3() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_TROOPERS;
        ui.o.name                       = "Troopers";
        ui.o.stringID_full              = STR_HEAVY_TROOPER_SQUAD;
        ui.o.wsa                        = "hyinfy.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = true;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 110;
        ui.o.fogUncoverRadius           = 1;
        ui.o.spriteID                   = 103;
        ui.o.buildCredits               = 200;
        ui.o.buildTime                  = 56;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 8;
        ui.o.upgradeLevelRequired       = 1;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 50;
        ui.o.priorityTarget             = 50;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_HARKONNEN;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = true;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_FOOT;
        ui.animationSpeed               = 15;
        ui.movingSpeedFactor            = 10;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 341;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_INFANTRY_4_FRAMES;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 50;
        ui.fireDistance                 = 5;
        ui.damage                       = 5;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 59;

        return ui;
    }

    private static UnitInfo UI_4() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_SOLDIER;
        ui.o.name                       = "Soldier";
        ui.o.stringID_full              = STR_INFANTRY_SOLDIER;
        ui.o.wsa                        = "infantry.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 20;
        ui.o.fogUncoverRadius           = 1;
        ui.o.spriteID                   = 102;
        ui.o.buildCredits               = 60;
        ui.o.buildTime                  = 32;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 2;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 10;
        ui.o.priorityTarget             = 10;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_ATREIDES;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = true;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_FOOT;
        ui.animationSpeed               = 12;
        ui.movingSpeedFactor            = 8;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 311;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_INFANTRY_4_FRAMES;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 45;
        ui.fireDistance                 = 2;
        ui.damage                       = 3;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 58;

        return ui;
    }

    private static UnitInfo UI_5() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_TROOPER;
        ui.o.name                       = "Trooper";
        ui.o.stringID_full              = STR_HEAVY_TROOPER;
        ui.o.wsa                        = "hyinfy.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = true;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 45;
        ui.o.fogUncoverRadius           = 1;
        ui.o.spriteID                   = 88;
        ui.o.buildCredits               = 100;
        ui.o.buildTime                  = 56;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 6;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 20;
        ui.o.priorityTarget             = 30;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_HARKONNEN;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = true;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_FOOT;
        ui.animationSpeed               = 12;
        ui.movingSpeedFactor            = 15;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 320;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_INFANTRY_3_FRAMES;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 50;
        ui.fireDistance                 = 5;
        ui.damage                       = 5;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 59;

        return ui;
    }

    private static UnitInfo UI_6() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_SABOTEUR;
        ui.o.name                       = "Saboteur";
        ui.o.stringID_full              = STR_SABOTEUR;
        ui.o.wsa                        = "saboture.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = false;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = true;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 0;
        ui.o.hitpoints                  = 10;
        ui.o.fogUncoverRadius           = 1;
        ui.o.spriteID                   = 96;
        ui.o.buildCredits               = 120;
        ui.o.buildTime                  = 48;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 6;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_SABOTAGE, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 0;
        ui.o.priorityTarget             = 700;
        ui.o.availableHouse             = FLAG_HOUSE_ORDOS;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 21;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = false;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = false;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 8;
        ui.movementType                 = MOVEMENT_FOOT;
        ui.animationSpeed               = 7;
        ui.movingSpeedFactor            = 40;
        ui.turningSpeed                 = 3;
        ui.groundSpriteID               = 301;
        ui.turretSpriteID               = -1;
        ui.actionAI                     = ACTION_SABOTAGE;
        ui.displayMode                  = DISPLAYMODE_INFANTRY_3_FRAMES;
        ui.destroyedSpriteID            = 0;
        ui.fireDelay                    = 45;
        ui.fireDistance                 = 2;
        ui.damage                       = 2;
        ui.explosionType                = EXPLOSION_IMPACT_SMALL;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 58;

        return ui;
    }

    private static UnitInfo UI_7() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_LAUNCHER;
        ui.o.name                       = "Launcher";
        ui.o.stringID_full              = STR_ROCKET_LAUNCHER;
        ui.o.wsa                        = "rtank.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = true;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = true;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 64;
        ui.o.hitpoints                  = 100;
        ui.o.fogUncoverRadius           = 5;
        ui.o.spriteID                   = 85;
        ui.o.buildCredits               = 450;
        ui.o.buildTime                  = 72;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 26;
        ui.o.upgradeLevelRequired       = 2;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 100;
        ui.o.priorityTarget             = 150;
        ui.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ATREIDES | FLAG_HOUSE_HARKONNEN;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = true;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = true;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = true;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_TRACKED;
        ui.animationSpeed               = 0;
        ui.movingSpeedFactor            = 30;
        ui.turningSpeed                 = 1;
        ui.groundSpriteID               = 111;
        ui.turretSpriteID               = 146;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_UNIT;
        ui.destroyedSpriteID            = 162;
        ui.fireDelay                    = 120;
        ui.fireDistance                 = 9;
        ui.damage                       = 75;
        ui.explosionType                = EXPLOSION_IMPACT_EXPLODE;
        ui.bulletType                   = UNIT_MISSILE_ROCKET;
        ui.bulletSound                  = -1;

        return ui;
    }

    private static UnitInfo UI_8() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_DEVIATOR;
        ui.o.name                       = "Deviator";
        ui.o.stringID_full              = STR_DEVIATOR_LAUNCHER;
        ui.o.wsa                        = "ordrtank.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = false;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = true;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 64;
        ui.o.hitpoints                  = 120;
        ui.o.fogUncoverRadius           = 5;
        ui.o.spriteID                   = 98;
        ui.o.buildCredits               = 750;
        ui.o.buildTime                  = 80;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_HOUSE_OF_IX;
        ui.o.sortPriority               = 30;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 50;
        ui.o.priorityTarget             = 175;
        ui.o.availableHouse             = FLAG_HOUSE_ORDOS;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = true;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = true;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_TRACKED;
        ui.animationSpeed               = 0;
        ui.movingSpeedFactor            = 30;
        ui.turningSpeed                 = 1;
        ui.groundSpriteID               = 111;
        ui.turretSpriteID               = 146;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_UNIT;
        ui.destroyedSpriteID            = 162;
        ui.fireDelay                    = 180;
        ui.fireDistance                 = 7;
        ui.damage                       = 0;
        ui.explosionType                = EXPLOSION_IMPACT_EXPLODE;
        ui.bulletType                   = UNIT_MISSILE_DEVIATOR;
        ui.bulletSound                  = -1;

        return ui;
    }

    private static UnitInfo UI_9() {
        UnitInfo ui = new UnitInfo();
        ui.o                            = new GObjectInfo();
        ui.o.stringID_abbrev            = STR_TANK;
        ui.o.name                       = "Tank";
        ui.o.stringID_full              = STR_COMBAT_TANK;
        ui.o.wsa                        = "ltank.wsa";
        ui.o.flags                      = new GObjectInfoFlags();
        ui.o.flags.hasShadow            = false;
        ui.o.flags.factory              = false;
        ui.o.flags.notOnConcrete        = false;
        ui.o.flags.busyStateIsIncoming  = false;
        ui.o.flags.blurTile             = false;
        ui.o.flags.hasTurret            = true;
        ui.o.flags.conquerable          = false;
        ui.o.flags.canBePickedUp        = true;
        ui.o.flags.noMessageOnDeath     = false;
        ui.o.flags.tabSelectable        = true;
        ui.o.flags.scriptNoSlowdown     = false;
        ui.o.flags.targetAir            = false;
        ui.o.flags.priority             = true;
        ui.o.spawnChance                = 64;
        ui.o.hitpoints                  = 200;
        ui.o.fogUncoverRadius           = 3;
        ui.o.spriteID                   = 90;
        ui.o.buildCredits               = 300;
        ui.o.buildTime                  = 64;
        ui.o.availableCampaign          = 0;
        ui.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        ui.o.sortPriority               = 22;
        ui.o.upgradeLevelRequired       = 0;
        ui.o.actionsPlayer              = new int [] { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD };
        ui.o.available                  = 0;
        ui.o.hintStringID               = STR_NULL;
        ui.o.priorityBuild              = 80;
        ui.o.priorityTarget             = 100;
        ui.o.availableHouse             = FLAG_HOUSE_ALL;
        ui.indexStart                   = 22;
        ui.indexEnd                     = 101;
        ui.flags.isBullet               = false;
        ui.flags.explodeOnDeath         = true;
        ui.flags.sonicProtection        = false;
        ui.flags.canWobble              = false;
        ui.flags.isTracked              = true;
        ui.flags.isGroundUnit           = true;
        ui.flags.mustStayInMap          = false;
        ui.flags.firesTwice             = false;
        ui.flags.impactOnSand           = false;
        ui.flags.isNotDeviatable        = false;
        ui.flags.hasAnimationSet        = false;
        ui.flags.notAccurate            = false;
        ui.flags.isNormalUnit           = true;
        ui.dimension                    = 16;
        ui.movementType                 = MOVEMENT_TRACKED;
        ui.animationSpeed               = 0;
        ui.movingSpeedFactor            = 25;
        ui.turningSpeed                 = 1;
        ui.groundSpriteID               = 111;
        ui.turretSpriteID               = 116;
        ui.actionAI                     = ACTION_HUNT;
        ui.displayMode                  = DISPLAYMODE_UNIT;
        ui.destroyedSpriteID            = 162;
        ui.fireDelay                    = 80;
        ui.fireDistance                 = 4;
        ui.damage                       = 25;
        ui.explosionType                = EXPLOSION_IMPACT_MEDIUM;
        ui.bulletType                   = UNIT_BULLET;
        ui.bulletSound                  = 57;

        return ui;
    }

    public static UnitInfo[] g_table_unitInfo = new UnitInfo[]{
        UI_0(), UI_1(), UI_2(), UI_3(), UI_4(), UI_5(), UI_6(), UI_7(),
        UI_8(), UI_9()
    };


        { /* 10 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_SIEGE_TANK,
                /* name                 */ "Siege Tank",
                /* stringID_full        */ STR_HEAVY_SIEGE_TANK,
                /* wsa                  */ "htank.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ true,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 300,
                /* fogUncoverRadius     */ 4,
                /* spriteID             */ 84,
                /* buildCredits         */ 600,
                /* buildTime            */ 96,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 24,
                /* upgradeLevelRequired */ 3,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 130,
                /* priorityTarget       */ 150,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ true,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 24,
            /* movementType         */ MOVEMENT_TRACKED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 20,
            /* turningSpeed         */ 1,
            /* groundSpriteID       */ 121,
            /* turretSpriteID       */ 126,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 162,
            /* fireDelay            */ 90,
            /* fireDistance         */ 5,
            /* damage               */ 30,
            /* explosionType        */ EXPLOSION_IMPACT_MEDIUM,
            /* bulletType           */ UNIT_BULLET,
            /* bulletSound          */ 57
        },

        { /* 11 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_DEVASTATOR,
                /* name                 */ "Devastator",
                /* stringID_full        */ STR_DEVASTATOR_TANK,
                /* wsa                  */ "harktank.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 400,
                /* fogUncoverRadius     */ 4,
                /* spriteID             */ 87,
                /* buildCredits         */ 800,
                /* buildTime            */ 104,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_HOUSE_OF_IX,
                /* sortPriority         */ 32,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_DESTRUCT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 175,
                /* priorityTarget       */ 180,
                /* availableHouse       */ FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_HARKONNEN,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ true,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 24,
            /* movementType         */ MOVEMENT_TRACKED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 10,
            /* turningSpeed         */ 1,
            /* groundSpriteID       */ 131,
            /* turretSpriteID       */ 136,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 165,
            /* fireDelay            */ 100,
            /* fireDistance         */ 5,
            /* damage               */ 40,
            /* explosionType        */ EXPLOSION_IMPACT_MEDIUM,
            /* bulletType           */ UNIT_BULLET,
            /* bulletSound          */ 57
        },

        { /* 12 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_SONIC_TANK,
                /* name                 */ "Sonic Tank",
                /* stringID_full        */ STR_SONIC_WAVE_TANK,
                /* wsa                  */ "stank.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 110,
                /* fogUncoverRadius     */ 4,
                /* spriteID             */ 91,
                /* buildCredits         */ 600,
                /* buildTime            */ 104,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_HOUSE_OF_IX,
                /* sortPriority         */ 34,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 80,
                /* priorityTarget       */ 110,
                /* availableHouse       */ FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ATREIDES,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ true,
                /* canWobble            */ false,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_TRACKED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 30,
            /* turningSpeed         */ 1,
            /* groundSpriteID       */ 111,
            /* turretSpriteID       */ 141,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 162,
            /* fireDelay            */ 80,
            /* fireDistance         */ 8,
            /* damage               */ 60,
            /* explosionType        */ EXPLOSION_INVALID,
            /* bulletType           */ UNIT_SONIC_BLAST,
            /* bulletSound          */ 43
        },

        { /* 13 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_TRIKE,
                /* name                 */ "Trike",
                /* stringID_full        */ STR_LIGHT_ATTACK_TRIKE,
                /* wsa                  */ "trike.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 100,
                /* fogUncoverRadius     */ 2,
                /* spriteID             */ 92,
                /* buildCredits         */ 150,
                /* buildTime            */ 40,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 10,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 50,
                /* priorityTarget       */ 50,
                /* availableHouse       */ FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ATREIDES,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ true,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ true,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WHEELED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 45,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 243,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 50,
            /* fireDistance         */ 3,
            /* damage               */ 5,
            /* explosionType        */ EXPLOSION_IMPACT_SMALL,
            /* bulletType           */ UNIT_BULLET,
            /* bulletSound          */ 59
        },

        { /* 14 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_RAIDER_TRIKE,
                /* name                 */ "Raider Trike",
                /* stringID_full        */ STR_FAST_RAIDER_TRIKE,
                /* wsa                  */ "otrike.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 80,
                /* fogUncoverRadius     */ 2,
                /* spriteID             */ 99,
                /* buildCredits         */ 150,
                /* buildTime            */ 40,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 12,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 55,
                /* priorityTarget       */ 60,
                /* availableHouse       */ FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ true,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ true,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WHEELED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 60,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 243,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 50,
            /* fireDistance         */ 3,
            /* damage               */ 5,
            /* explosionType        */ EXPLOSION_IMPACT_SMALL,
            /* bulletType           */ UNIT_BULLET,
            /* bulletSound          */ 59
        },

        { /* 15 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_QUAD,
                /* name                 */ "Quad",
                /* stringID_full        */ STR_HEAVY_ATTACK_QUAD,
                /* wsa                  */ "quad.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 130,
                /* fogUncoverRadius     */ 2,
                /* spriteID             */ 86,
                /* buildCredits         */ 200,
                /* buildTime            */ 48,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 14,
                /* upgradeLevelRequired */ 1,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_MOVE, ACTION_RETREAT, ACTION_GUARD },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 60,
                /* priorityTarget       */ 60,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ true,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ true,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WHEELED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 40,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 238,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 50,
            /* fireDistance         */ 3,
            /* damage               */ 7,
            /* explosionType        */ EXPLOSION_IMPACT_SMALL,
            /* bulletType           */ UNIT_BULLET,
            /* bulletSound          */ 59
        },

        { /* 16 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_HARVESTER,
                /* name                 */ "Harvester",
                /* stringID_full        */ STR_SPICE_HARVESTER,
                /* wsa                  */ "harvest.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 128,
                /* hitpoints            */ 150,
                /* fogUncoverRadius     */ 2,
                /* spriteID             */ 100,
                /* buildCredits         */ 300,
                /* buildTime            */ 64,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 18,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_HARVEST, ACTION_MOVE, ACTION_RETURN, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 10,
                /* priorityTarget       */ 150,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 24,
            /* movementType         */ MOVEMENT_HARVESTER,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 20,
            /* turningSpeed         */ 1,
            /* groundSpriteID       */ 248,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_HARVEST,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 165,
            /* fireDelay            */ 0,
            /* fireDistance         */ 0,
            /* damage               */ 0,
            /* explosionType        */ EXPLOSION_INVALID,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 0
        },

        { /* 17 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_MCV,
                /* name                 */ "MCV",
                /* stringID_full        */ STR_MOBILE_CONST_VEHICLE,
                /* wsa                  */ "mcv.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ true,
                    /* noMessageOnDeath     */ false,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ false,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 64,
                /* hitpoints            */ 150,
                /* fogUncoverRadius     */ 2,
                /* spriteID             */ 101,
                /* buildCredits         */ 900,
                /* buildTime            */ 80,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 20,
                /* upgradeLevelRequired */ 1,
                /* actionsPlayer        */ { ACTION_DEPLOY, ACTION_MOVE, ACTION_RETREAT, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 10,
                /* priorityTarget       */ 150,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 22,
            /* indexEnd             */ 101,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ true,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ false,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ true
            },
            /* dimension            */ 24,
            /* movementType         */ MOVEMENT_TRACKED,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 20,
            /* turningSpeed         */ 1,
            /* groundSpriteID       */ 253,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_HUNT,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 0,
            /* damage               */ 0,
            /* explosionType        */ EXPLOSION_INVALID,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 0
        },

        { /* 18 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "Death Hand",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ "gold-bb.wsa",
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 70,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_HARKONNEN,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 32,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 250,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 278,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_ROCKET,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 15,
            /* damage               */ 100,
            /* explosionType        */ EXPLOSION_DEATH_HAND,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 42
        },

        { /* 19 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "Rocket",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 70,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ true,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ true,
                /* notAccurate          */ true,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 7,
            /* movingSpeedFactor    */ 200,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 258,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_ROCKET,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 8,
            /* damage               */ 75,
            /* explosionType        */ EXPLOSION_IMPACT_EXPLODE,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 42
        },

        { /* 20 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "ARocket",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 70,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ true,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ true,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 7,
            /* movingSpeedFactor    */ 160,
            /* turningSpeed         */ 8,
            /* groundSpriteID       */ 258,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_ROCKET,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 60,
            /* damage               */ 75,
            /* explosionType        */ EXPLOSION_IMPACT_EXPLODE,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 42
        },

        { /* 21 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "GRocket",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 70,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ true,
                /* notAccurate          */ true,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 16,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 7,
            /* movingSpeedFactor    */ 200,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 258,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_ROCKET,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 7,
            /* damage               */ 75,
            /* explosionType        */ EXPLOSION_DEVIATOR_GAS,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 42
        },

        { /* 22 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "MiniRocket",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 70,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ true,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 8,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 7,
            /* movingSpeedFactor    */ 180,
            /* turningSpeed         */ 5,
            /* groundSpriteID       */ 268,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_ROCKET,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 3,
            /* damage               */ 0,
            /* explosionType        */ EXPLOSION_MINI_ROCKET,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ 64
        },

        { /* 23 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "Bullet",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 1,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ true,
                /* explodeOnDeath       */ true,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 8,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 250,
            /* turningSpeed         */ 0,
            /* groundSpriteID       */ 174,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_SINGLE_FRAME,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 0,
            /* damage               */ 0,
            /* explosionType        */ EXPLOSION_IMPACT_SMALL,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ -1
        },

        { /* 24 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "Sonic Blast",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ true,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ false
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 1,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 12,
            /* indexEnd             */ 15,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 32,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 7,
            /* movingSpeedFactor    */ 200,
            /* turningSpeed         */ 0,
            /* groundSpriteID       */ 160,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_SINGLE_FRAME,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 10,
            /* damage               */ 25,
            /* explosionType        */ EXPLOSION_INVALID,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ -1
        },

        { /* 25 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_SANDWORM,
                /* name                 */ "Sandworm",
                /* stringID_full        */ STR_SANDWORM2,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ false,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ true,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ true,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 1000,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 105,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_FREMEN,
            },
            /* indexStart           */ 16,
            /* indexEnd             */ 17,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ true,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 24,
            /* movementType         */ MOVEMENT_SLITHER,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 35,
            /* turningSpeed         */ 3,
            /* groundSpriteID       */ 161,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 20,
            /* fireDistance         */ 0,
            /* damage               */ 300,
            /* explosionType        */ EXPLOSION_SANDWORM_SWALLOW,
            /* bulletType           */ UNIT_SANDWORM,
            /* bulletSound          */ 63
        },

        { /* 26 */
            { /* objectInfo */
                /* stringID_abbrev      */ STR_NULL,
                /* name                 */ "Frigate",
                /* stringID_full        */ STR_NULL,
                /* wsa                  */ NULL,
                { /* flags */
                    /* hasShadow            */ true,
                    /* factory              */ false,
                    /* notOnConcrete        */ false,
                    /* busyStateIsIncoming  */ false,
                    /* blurTile             */ false,
                    /* hasTurret            */ false,
                    /* conquerable          */ false,
                    /* canBePickedUp        */ false,
                    /* noMessageOnDeath     */ true,
                    /* tabSelectable        */ false,
                    /* scriptNoSlowdown     */ true,
                    /* targetAir            */ false,
                    /* priority             */ true
                },
                /* spawnChance          */ 0,
                /* hitpoints            */ 100,
                /* fogUncoverRadius     */ 0,
                /* spriteID             */ 0,
                /* buildCredits         */ 0,
                /* buildTime            */ 0,
                /* availableCampaign    */ 0,
                /* structuresRequired   */ FLAG_STRUCTURE_NONE,
                /* sortPriority         */ 0,
                /* upgradeLevelRequired */ 0,
                /* actionsPlayer        */ { ACTION_STOP, ACTION_STOP, ACTION_STOP, ACTION_STOP },
                /* available            */ 0,
                /* hintStringID         */ STR_NULL,
                /* priorityBuild        */ 0,
                /* priorityTarget       */ 0,
                /* availableHouse       */ FLAG_HOUSE_ALL,
            },
            /* indexStart           */ 11,
            /* indexEnd             */ 11,
            { /* flags */
                /* isBullet             */ false,
                /* explodeOnDeath       */ false,
                /* sonicProtection      */ false,
                /* canWobble            */ false,
                /* isTracked            */ false,
                /* isGroundUnit         */ false,
                /* mustStayInMap        */ false,
                /* firesTwice           */ false,
                /* impactOnSand         */ false,
                /* isNotDeviatable      */ true,
                /* hasAnimationSet      */ false,
                /* notAccurate          */ false,
                /* isNormalUnit         */ false
            },
            /* dimension            */ 32,
            /* movementType         */ MOVEMENT_WINGER,
            /* animationSpeed       */ 0,
            /* movingSpeedFactor    */ 130,
            /* turningSpeed         */ 2,
            /* groundSpriteID       */ 298,
            /* turretSpriteID       */ -1,
            /* actionAI             */ ACTION_INVALID,
            /* displayMode          */ DISPLAYMODE_UNIT,
            /* destroyedSpriteID    */ 0,
            /* fireDelay            */ 0,
            /* fireDistance         */ 0,
            /* damage               */ 0,
            /* explosionType        */ EXPLOSION_INVALID,
            /* bulletType           */ UNIT_INVALID,
            /* bulletSound          */ -1
        }
    };

}
