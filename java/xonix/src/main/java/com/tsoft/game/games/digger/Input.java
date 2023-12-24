package com.tsoft.game.games.digger;

public class Input {
    public static enum Dir {
        UP(2), DOWN(6), LEFT(4), RIGHT(0), NONE(-1);

        private int off;

        Dir(int off) {
            this.off = off;
        }

        public int getOff() {
            return off;
        }

        public Dir getReverse() {
            switch (this) {
                case RIGHT:
                    return LEFT;
                case LEFT:
                    return RIGHT;
                case UP:
                    return DOWN;
                case DOWN:
                    return UP;
            }
            return this;
        }
    }

    private World world;

    private boolean leftpressed = false;
    private boolean rightpressed = false;
    private boolean uppressed = false;
    private boolean downpressed = false;
    private boolean f1pressed = false;
    public boolean firepressed = false;
    private boolean minuspressed;
    private boolean pluspressed;
    private boolean f10pressed;
    public boolean escape = false;

    public int keypressed = 0;

    public int akeypressed;
    private Dir dynamicdir = Dir.NONE;
    private Dir staticdir = Dir.NONE;

    private Dir keydir = Dir.RIGHT;
    public boolean firepflag = false;

    public Input(World world) {
        this.world = world;
    }

    public void checkkeyb() {
        if (pluspressed) {
            if (world.digger.frametime > Digger.MIN_RATE)
                world.digger.frametime -= 5;
        }
        if (minuspressed) {
            if (world.digger.frametime < Digger.MAX_RATE)
                world.digger.frametime += 5;
        }
        if (f10pressed)
            escape = true;
    }

    public void detectjoy() {
        dynamicdir = Dir.NONE;
        staticdir = Dir.NONE;
    }

    public int getasciikey(int make) {
        if ((make == ' ') || ((make >= 'a') && (make <= 'z')) || ((make >= '0') && (make <= '9')))
            return make;
        return 0;
    }

    public Dir getdir() {
        return keydir;
    }

    public void initkeyb() {
    }

    private void Key_downpressed() {
        downpressed = true;
        dynamicdir = Dir.DOWN;
        staticdir = Dir.DOWN;
    }

    private void Key_downreleased() {
        downpressed = false;
        if (dynamicdir == Dir.DOWN)
            setdirec();
    }

    private void Key_f1pressed() {
        firepressed = true;
        f1pressed = true;
    }

    private void Key_f1released() {
        f1pressed = false;
    }

    private void Key_leftpressed() {
        leftpressed = true;
        dynamicdir = Dir.LEFT;
        staticdir = Dir.LEFT;
    }

    private void Key_leftreleased() {
        leftpressed = false;
        if (dynamicdir == Dir.LEFT)
            setdirec();
    }

    private void Key_rightpressed() {
        rightpressed = true;
        dynamicdir = Dir.RIGHT;
        staticdir = Dir.RIGHT;
    }

    private void Key_rightreleased() {
        rightpressed = false;
        if (dynamicdir == Dir.RIGHT)
            setdirec();
    }

    private void Key_uppressed() {
        uppressed = true;
        dynamicdir = Dir.UP;
        staticdir = Dir.UP;
    }

    private void Key_upreleased() {
        uppressed = false;
        if (dynamicdir == Dir.UP)
            setdirec();
    }

    public void processkey(int key) {
        keypressed = key;
        if (key > 0x80)
            akeypressed = key & 0x7f;
        switch (key) {
            case 0x4b:
                Key_leftpressed();
                break;
            case 0xcb:
                Key_leftreleased();
                break;
            case 0x4d:
                Key_rightpressed();
                break;
            case 0xcd:
                Key_rightreleased();
                break;
            case 0x48:
                Key_uppressed();
                break;
            case 0xc8:
                Key_upreleased();
                break;
            case 0x50:
                Key_downpressed();
                break;
            case 0xd0:
                Key_downreleased();
                break;
            case 0x3b:
                Key_f1pressed();
                break;
            case 0xbb:
                Key_f1released();
                break;
            case 0x78:
                f10pressed = true;
                break;
            case 0xf8:
                f10pressed = false;
                break;
            case 0x2b:
                pluspressed = true;
                break;
            case 0xab:
                pluspressed = false;
                break;
            case 0x2d:
                minuspressed = true;
                break;
            case 0xad:
                minuspressed = false;
                break;
        }
    }

    public void readdir() {
        keydir = staticdir;
        if (dynamicdir != Dir.NONE)
            keydir = dynamicdir;
        staticdir = Dir.NONE;

        if (f1pressed || firepressed)
            firepflag = true;
        else
            firepflag = false;

        firepressed = false;
    }

    private void setdirec() {
        dynamicdir = Dir.NONE;
        if (uppressed) dynamicdir = staticdir = Dir.UP;
        if (downpressed) dynamicdir = staticdir = Dir.DOWN;
        if (leftpressed) dynamicdir = staticdir = Dir.LEFT;
        if (rightpressed) dynamicdir = staticdir = Dir.RIGHT;
    }

    public boolean teststart() {
        boolean startf = false;
        if (keypressed != 0 && (keypressed & 0x80) == 0 && keypressed != 27) {
            startf = true;
            keypressed = 0;
        }
        if (!startf)
            return false;
        return true;
    }
}
