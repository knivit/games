package com.tsoft.dune2.tile;

import static com.tsoft.dune2.map.MapService.Map_IsValidPosition;
import static com.tsoft.dune2.map.MapService.Map_UnveilTile;
import static com.tsoft.dune2.tools.ToolsService.Tools_Random_256;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;

public class TileService {

    /**
     * Unpacks a 12 bits packed tile to a 32 bits tile struct.
     *
     * @param packed The uint16 containing the 12 bits packed tile information.
     * @return The unpacked tile.
     */
    public static Tile32 Tile_UnpackTile(int packed) {
        Tile32 tile = new Tile32();

        tile.x = (((packed >> 0) & 0x3F) << 8) | 0x80;
        tile.y = (((packed >> 6) & 0x3F) << 8) | 0x80;

        return tile;
    }

    /**
     * Calculates the distance between the two given tiles.
     *
     * @param from The origin.
     * @param to The destination.
     * @return The longest distance between the X or Y coordinates, plus half the shortest.
     */
    public static int Tile_GetDistance(Tile32 from, Tile32 to) {
        int distance_x = Math.abs(from.x - to.x);
        int distance_y = Math.abs(from.y - to.y);

        if (distance_x > distance_y) return distance_x + (distance_y / 2);
        return distance_y + (distance_x / 2);
    }

    /**
     * Adds two tiles together.
     *
     * @param from The origin.
     * @param diff The difference.
     * @return The new coordinates.
     */
    public static Tile32 Tile_AddTileDiff(Tile32 from, Tile32 diff) {
        Tile32 result = new Tile32();

        result.x = from.x + diff.x;
        result.y = from.y + diff.y;

        return result;
    }

    /**
     * Centers the offset of the tile.
     *
     * @param tile The tile to center.
     */
    public static Tile32 Tile_Center(Tile32 tile) {
        Tile32 result = new Tile32();

        result.x = (tile.x & 0xff00) | 0x80;
        result.y = (tile.y & 0xff00) | 0x80;

        return result;
    }

    /**
     * Calculates the distance between the two given packed tiles.
     *
     * @param packed_from The origin.
     * @param packed_to The destination.
     * @return The longest distance between the X or Y coordinates, plus half the shortest.
     */
    public static int Tile_GetDistancePacked(int packed_from, int packed_to) {
        Tile32 from = Tile_UnpackTile(packed_from);
        Tile32 to = Tile_UnpackTile(packed_to);

        return Tile_GetDistance(from, to) >> 8;
    }

    /**
     * Calculates the rounded up distance between the two given packed tiles.
     *
     * @param from The origin.
     * @param to The destination.
     * @return The longest distance between the X or Y coordinates, plus half the shortest.
     */
    public static int Tile_GetDistanceRoundedUp(Tile32 from, Tile32 to) {
        return (Tile_GetDistance(from, to) + 0x80) >> 8;
    }

    /**
     * Remove fog in the radius around the given tile.
     *
     * @param tile The tile to remove fog around.
     * @param radius The radius to remove fog around.
     */
    public static void Tile_RemoveFogInRadius(Tile32 tile, int radius) {
        int packed;
        int x, y;
        int i, j;

        /* TODO this code could be simplified */
        packed = Tile_PackTile(tile);

        if (!Map_IsValidPosition(packed)) return;

        /* setting tile from its packed position equals removing the
         * non integer part */
        x = Tile_GetPackedX(packed);
        y = Tile_GetPackedY(packed);
        Tile_MakeXY(tile, x, y);

        for (i = -radius; i <= radius; i++) {
            for (j = -radius; j <= radius; j++) {
                Tile32 t = new Tile32();

                if ((x + i) < 0 || (x + i) >= 64) continue;
                if ((y + j) < 0 || (y + j) >= 64) continue;

                packed = Tile_PackXY(x + i, y + j);
                Tile_MakeXY(t, x + i, y + j);

                if (Tile_GetDistanceRoundedUp(tile, t) > radius) continue;

                Map_UnveilTile(packed, g_playerHouseID);
            }
        }
    }

    /**
     * Get a tile in the direction of a destination, randomized a bit.
     *
     * @param packed_from The origin.
     * @param packed_to The destination.
     * @return A packed tile.
     */
    public static int Tile_GetTileInDirectionOf(int packed_from, int packed_to) {
        int distance;
        int direction;

        if (packed_from == 0 || packed_to == 0) return 0;

        distance = Tile_GetDistancePacked(packed_from, packed_to);
        direction = Tile_GetDirectionPacked(packed_to, packed_from);

        if (distance <= 10) return 0;

        while (true) {
            int dir;
            Tile32 position;
            int packed;

            dir = 31 + (Tools_Random_256() & 0x3F);

            if ((Tools_Random_256() & 1) != 0) dir = -dir;

            position = Tile_UnpackTile(packed_to);
            position = Tile_MoveByDirection(position, direction + dir, Math.min(distance, 20) << 8);
            packed = Tile_PackTile(position);

            if (Map_IsValidPosition(packed)) return packed;
        }
    }

    /**
     * Get to direction to follow to go from packed_from to packed_to.
     *
     * @param packed_from The origin.
     * @param packed_to The destination.
     * @return The direction.
     */
    public static int Tile_GetDirectionPacked(int packed_from, int packed_to) {
        int[] returnValues = new int[] {
            0x20, 0x40, 0x20, 0x00, 0xE0, 0xC0, 0xE0, 0x00,
            0x60, 0x40, 0x60, 0x80, 0xA0, 0xC0, 0xA0, 0x80};

        int x1, y1, x2, y2;
        int dx, dy;
        int index;

        x1 = Tile_GetPackedX(packed_from);
        y1 = Tile_GetPackedY(packed_from);
        x2 = Tile_GetPackedX(packed_to);
        y2 = Tile_GetPackedY(packed_to);

        index = 0;

        dy = y1 - y2;
        if (dy < 0) {
            index |= 0x8;
            dy = -dy;
        }

        dx = x2 - x1;
        if (dx < 0) {
            index |= 0x4;
            dx = -dx;
        }

        if (dx >= dy) {
            if (((dx + 1) / 2) > dy) index |= 0x1;
        } else {
            index |= 0x2;
            if (((dy + 1) / 2) > dx) index |= 0x1;
        }

        return returnValues[index];
    }

    static int[] _stepX = new int[] {
        0,    3,    6,    9,   12,   15,   18,   21,   24,   27,   30,   33,   36,   39,   42,   45,
        48,   51,   54,   57,   59,   62,   65,   67,   70,   73,   75,   78,   80,   82,   85,   87,
        89,   91,   94,   96,   98,  100,  101,  103,  105,  107,  108,  110,  111,  113,  114,  116,
        117,  118,  119,  120,  121,  122,  123,  123,  124,  125,  125,  126,  126,  126,  126,  126,
        127,  126,  126,  126,  126,  126,  125,  125,  124,  123,  123,  122,  121,  120,  119,  118,
        117,  116,  114,  113,  112,  110,  108,  107,  105,  103,  102,  100,   98,   96,   94,   91,
        89,   87,   85,   82,   80,   78,   75,   73,   70,   67,   65,   62,   59,   57,   54,   51,
        48,   45,   42,   39,   36,   33,   30,   27,   24,   21,   18,   15,   12,    9,    6,    3,
        0,   -3,   -6,   -9,  -12,  -15,  -18,  -21,  -24,  -27,  -30,  -33,  -36,  -39,  -42,  -45,
        -48,  -51,  -54,  -57,  -59,  -62,  -65,  -67,  -70,  -73,  -75,  -78,  -80,  -82,  -85,  -87,
        -89,  -91,  -94,  -96,  -98, -100, -102, -103, -105, -107, -108, -110, -111, -113, -114, -116,
        -117, -118, -119, -120, -121, -122, -123, -123, -124, -125, -125, -126, -126, -126, -126, -126,
        -126, -126, -126, -126, -126, -126, -125, -125, -124, -123, -123, -122, -121, -120, -119, -118,
        -117, -116, -114, -113, -112, -110, -108, -107, -105, -103, -102, -100,  -98,  -96,  -94,  -91,
        -89,  -87,  -85,  -82,  -80,  -78,  -75,  -73,  -70,  -67,  -65,  -62,  -59,  -57,  -54,  -51,
        -48,  -45,  -42,  -39,  -36,  -33,  -30,  -27,  -24,  -21,  -18,  -15,  -12,   -9,   -6,   -3
    };

    static int[] _stepY = new int[] {
        127,  126,  126,  126,  126,  126,  125,  125,  124,  123,  123,  122,  121,  120,  119,  118,
        117,  116,  114,  113,  112,  110,  108,  107,  105,  103,  102,  100,   98,   96,   94,   91,
        89,   87,   85,   82,   80,   78,   75,   73,   70,   67,   65,   62,   59,   57,   54,   51,
        48,   45,   42,   39,   36,   33,   30,   27,   24,   21,   18,   15,   12,    9,    6,    3,
        0,   -3,   -6,   -9,  -12,  -15,  -18,  -21,  -24,  -27,  -30,  -33,  -36,  -39,  -42,  -45,
        -48,  -51,  -54,  -57,  -59,  -62,  -65,  -67,  -70,  -73,  -75,  -78,  -80,  -82,  -85,  -87,
        -89,  -91,  -94,  -96,  -98, -100, -102, -103, -105, -107, -108, -110, -111, -113, -114, -116,
        -117, -118, -119, -120, -121, -122, -123, -123, -124, -125, -125, -126, -126, -126, -126, -126,
        -126, -126, -126, -126, -126, -126, -125, -125, -124, -123, -123, -122, -121, -120, -119, -118,
        -117, -116, -114, -113, -112, -110, -108, -107, -105, -103, -102, -100,  -98,  -96,  -94,  -91,
        -89,  -87,  -85,  -82,  -80,  -78,  -75,  -73,  -70,  -67,  -65,  -62,  -59,  -57,  -54,  -51,
        -48,  -45,  -42,  -39,  -36,  -33,  -30,  -27,  -24,  -21,  -18,  -15,  -12,   -9,   -6,   -3,
        0,    3,    6,    9,   12,   15,   18,   21,   24,   27,   30,   33,   36,   39,   42,   45,
        48,   51,   54,   57,   59,   62,   65,   67,   70,   73,   75,   78,   80,   82,   85,   87,
        89,   91,   94,   96,   98,  100,  101,  103,  105,  107,  108,  110,  111,  113,  114,  116,
        117,  118,  119,  120,  121,  122,  123,  123,  124,  125,  125,  126,  126,  126,  126,  126
    };

    /**
     * Get the tile from given tile at given distance in given direction.
     *
     * @param tile The origin.
     * @param orientation The direction to follow.
     * @param distance The distance.
     * @return The tile.
     */
    public static Tile32 Tile_MoveByDirection(Tile32 tile, int orientation, int distance) {
        int diffX, diffY;
        int roundingOffsetX, roundingOffsetY;

        distance = Math.min(distance, 0xFF);

        if (distance == 0) return tile;

        diffX = _stepX[orientation & 0xFF];
        diffY = _stepY[orientation & 0xFF];

        /* Always round away from zero */
        roundingOffsetX = diffX < 0 ? -64 : 64;
        roundingOffsetY = diffY < 0 ? -64 : 64;

        tile.x += (diffX * distance + roundingOffsetX) / 128;
        tile.y -= (diffY * distance + roundingOffsetY) / 128;

        return tile;
    }

    /**
     * Get the tile from given tile at given maximum distance in random direction.
     *
     * @param tile The origin.
     * @param distance The distance maximum.
     * @param center Wether to center the offset of the tile.
     * @return The tile.
     */
    public static Tile32 Tile_MoveByRandom(Tile32 tile, int distance, boolean center) {
        int x;
        int y;
        Tile32 ret = new Tile32();
        int orientation;
        int newDistance;

        if (distance == 0) return tile;

        x = Tile_GetX(tile);
        y = Tile_GetY(tile);

        newDistance = Tools_Random_256();
        while (newDistance > distance) newDistance /= 2;
        distance = newDistance;

        orientation = Tools_Random_256();
        x += ((_stepX[orientation] * distance) / 128) * 16;
        y -= ((_stepY[orientation] * distance) / 128) * 16;

        if (x > 16384 || y > 16384) return tile;

        ret.x = x;
        ret.y = y;

        return center ? Tile_Center(ret) : ret;
    }

    /**
     * Get to direction to follow to go from \a from to \a to.
     *
     * @param from The origin.
     * @param to The destination.
     * @return The direction.
     */
    public static int Tile_GetDirection(Tile32 from, Tile32 to) {
        int[] orientationOffsets = new int[] {0x40, 0x80, 0x0, 0xC0};
        int[] directions = new int[] {
            0x3FFF, 0x28BC, 0x145A, 0xD8E,  0xA27, 0x81B, 0x6BD, 0x5C3,
            0x506, 0x474, 0x3FE, 0x39D,  0x34B, 0x306, 0x2CB, 0x297,
            0x26A,  0x241,  0x21D,  0x1FC,  0x1DE, 0x1C3, 0x1AB, 0x194,
            0x17F, 0x16B, 0x159, 0x148,  0x137, 0x128, 0x11A, 0x10C
        };

        int dx;
        int dy;
        int i;
        int gradient;
        int baseOrientation;
        boolean invert;
        int quadrant = 0;

        dx = to.x - from.x;
        dy = to.y - from.y;

        if (Math.abs(dx) + Math.abs(dy) > 8000) {
            dx /= 2;
            dy /= 2;
        }

        if (dy <= 0) {
            quadrant |= 0x2;
            dy = -dy;
        }

        if (dx < 0) {
            quadrant |= 0x1;
            dx = -dx;
        }

        baseOrientation = orientationOffsets[quadrant];
        invert = false;
        gradient = 0x7FFF;

        if (dx >= dy) {
            if (dy != 0) gradient = (dx << 8) / dy;
        } else {
            invert = true;
            if (dx != 0) gradient = (dy << 8) / dx;
        }

        for (i = 0; i < directions.length; i++) {
            if (directions[i] <= gradient) break;
        }

        if (!invert) i = 64 - i;

        if (quadrant == 0 || quadrant == 3) {
            return (baseOrientation + 64 - i) & 0xFF;
        }

        return (baseOrientation + i) & 0xFF;
    }

    /**
     * Move to the given orientation looking from the current position.
     * @note returns input position when going out-of-bounds.
     * @param position The position to move from.
     * @param orientation The orientation to move in.
     * @return The new position, or the old in case of out-of-bounds.
     */
    public static Tile32 Tile_MoveByOrientation(Tile32 position, int orientation) {
        int[] xOffsets = new int[] {0, 256, 256, 256, 0, -256, -256, -256};
        int[] yOffsets = new int[] {-256, -256, 0, 256, 256, 256, 0, -256};
        int x;
        int y;

        x = Tile_GetX(position);
        y = Tile_GetY(position);

        orientation = Orientation_Orientation256ToOrientation8(orientation);

        x += xOffsets[orientation];
        y += yOffsets[orientation];

        if (x > 16384 || y > 16384) return position;

        position.x = x;
        position.y = y;

        return position;
    }

    /**
     * Convert an orientation that goes from 0 .. 255 to one that goes from 0 .. 7.
     * @param orientation The 256-based orientation.
     * @return A 8-based orientation.
     */
    public static int Orientation_Orientation256ToOrientation8(int orientation) {
        return ((orientation + 16) / 32) & 0x7;
    }

    /**
     * Convert an orientation that goes from 0 .. 255 to one that goes from 0 .. 15.
     * @param orientation The 256-based orientation.
     * @return A 16-based orientation.
     */
    public static int Orientation_Orientation256ToOrientation16(int orientation) {
        return ((orientation + 8) / 16) & 0xF;
    }

    /**
     * Check whether a tile is valid.
     *
     * @param tile The tile32 to check for validity.
     * @return True if valid, false if not.
     */
    public static boolean Tile_IsValid(Tile32 tile) {
        return ((((tile).x | (tile).y) & 0xc000) == 0);
    }

    /**
     * Returns the tile as an uint32 value.
     *
     * @param tile The tile32 to retrieve the data from.
     * @return The uint32 representation of the tile32.
     */
    /*extern uint32 Tile_GetXY(tile32 tile);*/
    public static long Tile_GetXY(Tile32 tile) { return ((long)((tile).x) | ((long)((tile).y) << 16)); }

    /**
     * Returns the X-position of the tile.
     *
     * @param tile The tile32 to get the X-position from.
     * @return The X-position of the tile.
     */
    /*extern uint16 Tile_GetX(tile32 tile);*/
    public static int Tile_GetX(Tile32 tile) { return ((tile).x); }

    /**
     * Returns the Y-position of the tile.
     *
     * @param tile The tile32 to get the Y-position from.
     * @return The Y-position of the tile.
     */
    /*extern uint16 Tile_GetY(tile32 tile);*/
    public static int Tile_GetY(Tile32 tile) { return ((tile).y); }

    /**
     * Unpacks a 12 bits packed tile and retrieves the X-position.
     *
     * @param packed The uint16 containing the 12 bits packed tile.
     * @return The unpacked X-position.
     */
    /*extern uint8 Tile_GetPackedX(uint16 packed);*/
    public static int Tile_GetPackedX(int packed) { return ((packed) & 0x3F); }

    /**
     * Unpacks a 12 bits packed tile and retrieves the Y-position.
     *
     * @param packed The uint16 containing the 12 bits packed tile.
     * @return The unpacked Y-position.
     */
    /*extern uint8 Tile_GetPackedY(uint16 packed);*/
    public static int Tile_GetPackedY(int packed) { return (((packed) >> 6) & 0x3F); }

    /**
     * Packs an x and y coordinate into a 12 bits packed tile.
     *
     * @param x The X-coordinate from.
     * @param x The Y-coordinate from.
     * @return The coordinates packed into 12 bits.
     */
    /*extern uint16 Tile_PackXY(uint16 x, uint16 y);*/
    public static int Tile_PackXY(int x, int y) { return (((y) << 6) | (x)); }

    /**
     * Packs a 32 bits tile struct into a 12 bits packed tile.
     *
     * @param tile The tile32 to get it's Y-position from.
     * @return The tile packed into 12 bits.
     */
    /*extern uint16 Tile_PackTile(tile32 tile);*/
    public static int Tile_PackTile(Tile32 tile) { return ((Tile_GetPosY(tile) << 6) | Tile_GetPosX(tile)); }

    /**
     * Returns the X-position of the tile.
     *
     * @param tile The tile32 to get the X-position from.
     * @return The X-position of the tile.
     */
    /*extern uint8 Tile_GetPosX(tile32 tile);*/
    public static int Tile_GetPosX(Tile32 tile) { return (((tile).x >> 8) & 0x3f); }

    /**
     * Returns the Y-position of the tile.
     *
     * @param tile The tile32 to get the Y-position from.
     * @return The Y-position of the tile.
     */
    /*extern uint8 Tile_GetPosY(tile32 tile);*/
    public static int Tile_GetPosY(Tile32 tile) { return (((tile).y >> 8) & 0x3f); }


    /**
     * Check if a packed tile is out of map. Useful after additional or substraction.
     * @param packed The packed tile to check.
     * @return True if and only if the tile is out of map.
     */
    /*extern bool Tile_IsOutOfMap(uint16 packed);*/
    public static boolean Tile_IsOutOfMap(int packed) {
        return (((packed) & 0xF000) != 0);
    }

    /**
     * Make a tile32 from an X- and Y-position.
     *
     * @param x The X-position.
     * @param y The Y-position.
     * @return A tile32 at the top-left corner of the X- and Y-position.
     */
    /*extern tile32 Tile_MakeXY(uint16 x, uint16 y);*/
    public static Tile32 Tile_MakeXY(Tile32 tile, int x, int y) {
        (tile).x = (x) << 8;
        (tile).y = (y) << 8;
        return tile;
    }
}
