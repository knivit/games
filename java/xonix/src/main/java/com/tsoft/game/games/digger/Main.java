package com.tsoft.game.games.digger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private World world;

    private int digsprorder[] = {14, 13, 7, 6, 5, 4, 3, 2, 1, 12, 11, 10, 9, 8, 15, 0};    // [16]

    private GameStatus[] gamedat = {new GameStatus(), new GameStatus()};

    public String pldispbuf = "";

    private int curplayer = 0;
    public int nplayers = 0;
    private int penalty = 0;
    private boolean levnotdrawn = false;
    private boolean flashplayer = false;

    private boolean levfflag = false;
    private boolean biosflag = false;
    private int speedmul = 40;

    private int randv;

    private String leveldat[][] = new String[8][10];        // [8][10][15]

    public Main(World world) {
        this.world = world;
        loadLevels();
    }

    private void loadLevels() {
        for (int n = 0; n < 8; n ++) {
            FileHandle resource = Gdx.files.internal("assets/digger/levels/" + n + ".txt");

            try (BufferedReader reader = resource.reader(1024)) {
                int y = 0;
                String line;
                while ((line = reader.readLine()) != null) {
                    while (line.length() < 15) line = line + ' ';
                    leveldat[n][y] = line;
                    y ++;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void addLife(int pl) {
        gamedat[pl - 1].lives++;
        world.sound.sound1up();
    }

    private void calibrate() {
        world.sound.volume = (int) (world.pc.getkips() / 291);
        if (world.sound.volume == 0)
            world.sound.volume = 1;
    }

    private void checkLevDone() {
        if ((world.emerald.countEm() == 0 || world.monsters.monleft() == 0) && world.digger.digonscr)
            gamedat[curplayer].levdone = true;
        else
            gamedat[curplayer].levdone = false;
    }

    public void clearTopLine() {
        world.drawing.outText("                          ", 0, 0, 3);
        world.drawing.outText(" ", 308, 0, 3);
    }

    private void drawScreen() {
        world.drawing.createMbspr();
        world.drawing.drawStatics();
        world.bags.drawBags();
        world.emerald.drawEmeralds();
        world.digger.initDigger();
        world.monsters.initMonsters();
    }

    public int getCurrPlayer() {
        return curplayer;
    }

    public int getlevch(int x, int y, int l) {
        if (l == 0) l++;
        return leveldat[l - 1][y].charAt(x);
    }

    public int getLives(int pl) {
        return gamedat[pl - 1].lives;
    }

    public void incPenalty() {
        penalty++;
    }

    private void initCharacters() {
        world.drawing.initMbspr();
        world.digger.initDigger();
        world.monsters.initMonsters();
    }

    private void initLevel() {
        gamedat[curplayer].levdone = false;
        world.drawing.makeField();
        world.emerald.makeEmField();
        world.bags.initBags();
        levnotdrawn = true;
    }

    private int getLevelNo() {
        return gamedat[curplayer].level;
    }

    public int levof10() {
        if (gamedat[curplayer].level > 10)
            return 10;
        return gamedat[curplayer].level;
    }

    public int levplan() {
        int l = getLevelNo();
        if (l > 8)
            l = (l & 3) + 5; /* Level plan: 12345678, 678, (5678) 247 times, 5 forever */
        return l;
    }

    public void main() {
        randv = (int) world.pc.gethrt();
        calibrate();

        world.digger.ftime = speedmul * 2000l;
        world.pc.ginit();
        world.pc.gpal(0);
        world.input.initkeyb();
        world.input.detectjoy();
        world.scores.loadscores();
        world.sound.initsound();

        world.scores.run();        // ??
        world.scores.updateScores(world.scores.scores);

        nplayers = 1;
        while (true) {
            world.sound.soundstop();
            world.sprite.setsprorder(digsprorder);
            world.drawing.createMbspr();
            world.input.detectjoy();
            world.pc.gclear();
            world.pc.gtitle();
            world.drawing.outText("D I G G E R", 100, 0, 3);
            shownplayers();
            world.scores.showtable();

            world.digger.time = world.pc.gethrt();

            menuMode();

            gamedat[0].level = 1;
            gamedat[0].lives = 3;
            gamedat[1].lives = 0;
            if (nplayers == 2) {
                gamedat[1].level = 1;
                gamedat[1].lives = 3;
            }


            world.pc.gclear();
            curplayer = 0;
            initLevel();
            curplayer = 1;
            initLevel();
            world.scores.zeroscores();
            world.digger.bonusvisible = true;
            if (nplayers == 2)
                flashplayer = true;

            curplayer = 0;
            while ((gamedat[0].lives != 0 || gamedat[1].lives != 0) && !world.input.escape) {
                gamedat[curplayer].isDead = false;
                while (!gamedat[curplayer].isDead && gamedat[curplayer].lives != 0 && !world.input.escape) {
                    world.drawing.initMbspr();
                    play();
                }

                if (gamedat[1 - curplayer].lives != 0) {
                    curplayer = (curplayer == 0 ? 1 : 0);
                    flashplayer = levnotdrawn = true;
                }
            }
            world.input.escape = false;
        }
    }

    private void menuMode() {
        int x = 0;
        int frame = 0;
        boolean start = false;
        while (!start) {
            start = world.input.teststart();
            if (world.input.akeypressed == KeyEvent.VK_ESCAPE) {  //	esc
                switchnplayers();
                shownplayers();
                world.input.akeypressed = 0;
                world.input.keypressed = 0;
            }

            if (frame == 0)
                for (int t = 54; t < 174; t += 12)
                    world.drawing.outText("            ", 164, t, 0);
            if (frame == 50) {
                world.sprite.movedrawspr(8, 292, 63);
                x = 292;
            }
            if (frame > 50 && frame <= 77) {
                x -= 4;
                world.drawing.drawMonster(0, true, Input.Dir.LEFT, x, 63);
            }
            if (frame > 77)
                world.drawing.drawMonster(0, true, Input.Dir.RIGHT, 184, 63);
            if (frame == 83)
                world.drawing.outText("NOBBIN", 216, 64, 2);
            if (frame == 90) {
                world.sprite.movedrawspr(9, 292, 82);
                world.drawing.drawMonster(1, false, Input.Dir.LEFT, 292, 82);
                x = 292;
            }
            if (frame > 90 && frame <= 117) {
                x -= 4;
                world.drawing.drawMonster(1, false, Input.Dir.LEFT, x, 82);
            }
            if (frame > 117)
                world.drawing.drawMonster(1, false, Input.Dir.RIGHT, 184, 82);
            if (frame == 123)
                world.drawing.outText("HOBBIN", 216, 83, 2);
            if (frame == 130) {
                world.sprite.movedrawspr(0, 292, 101);
                world.drawing.drawDigger(Input.Dir.LEFT, 292, 101, true);
                x = 292;
            }
            if (frame > 130 && frame <= 157) {
                x -= 4;
                world.drawing.drawDigger(Input.Dir.LEFT, x, 101, true);
            }
            if (frame > 157)
                world.drawing.drawDigger(Input.Dir.RIGHT, 184, 101, true);
            if (frame == 163)
                world.drawing.outText("DIGGER", 216, 102, 2);
            if (frame == 178) {
                world.sprite.movedrawspr(1, 184, 120);
                world.drawing.drawGold(1, 0, 184, 120);
            }
            if (frame == 183)
                world.drawing.outText("GOLD", 216, 121, 2);
            if (frame == 198)
                world.drawing.drawEmerald(184, 141);
            if (frame == 203)
                world.drawing.outText("EMERALD", 216, 140, 2);
            if (frame == 218)
                world.drawing.drawBonus(184, 158);
            if (frame == 223)
                world.drawing.outText("BONUS", 216, 159, 2);
            world.digger.newframe();
            frame++;
            if (frame > 250)
                frame = 0;
        }
    }

    private void play() {
        if (levnotdrawn) {
            levnotdrawn = false;
            drawScreen();
            world.digger.time = world.pc.gethrt();
            if (flashplayer) {
                flashplayer = false;
                pldispbuf = "PLAYER ";
                if (curplayer == 0)
                    pldispbuf += "1";
                else
                    pldispbuf += "2";

                clearTopLine();
                for (int t = 0; t < 15; t++)
                    for (int c = 1; c <= 3; c++) {
                        world.drawing.outText(pldispbuf, 108, 0, c);
                        world.scores.writecurscore(c);
                        /* olddelay(20); */
                        world.digger.newframe();
                        if (world.input.escape)
                            return;
                    }
                world.scores.drawscores();
                world.scores.addscore(0);
            }
        } else
            initCharacters();

        world.input.keypressed = 0;
        world.drawing.outText("        ", 108, 0, 3);
        world.scores.initscores();
        world.drawing.drawLives();
        world.sound.music(1);
        world.input.readdir();
        world.digger.time = world.pc.gethrt();
        while (!gamedat[curplayer].isDead && !gamedat[curplayer].levdone && !world.input.escape) {
            penalty = 0;
            world.digger.doDigger();
            world.monsters.doMonsters();
            world.bags.doBags();
            if (penalty > 8)
                world.monsters.incMonsters(penalty - 8);
            testPause();
            checkLevDone();
        }
        world.digger.eraseDigger();
        world.sound.musicoff();

        int t = 20;
        while ((world.bags.getNMovingBags() != 0 || t != 0) && !world.input.escape) {
            if (t != 0)
                t--;
            penalty = 0;
            world.bags.doBags();
            world.digger.doDigger();
            world.monsters.doMonsters();
            if (penalty < 8)
                t = 0;
        }

        world.sound.soundstop();
        world.digger.killFire();
        world.digger.eraseBonus();
        world.bags.cleanupBags();
        world.drawing.saveField();
        world.monsters.eraseMonsters();
        world.digger.newframe();        // needed by Java version!!
        if (gamedat[curplayer].levdone)
            world.sound.soundlevdone();

        if (world.emerald.countEm() == 0) {
            nextLevel();
        }

        if (gamedat[curplayer].isDead) {
            gamedat[curplayer].lives--;
            world.drawing.drawLives();
            if (gamedat[curplayer].lives == 0 && !world.input.escape)
                world.scores.endofgame();
        }

        if (gamedat[curplayer].levdone) {
            nextLevel();
        }
    }

    private void nextLevel() {
        gamedat[curplayer].level++;
        if (gamedat[curplayer].level > 1000)
            gamedat[curplayer].level = 1000;
        initLevel();
    }

    public int randno(int n) {
        randv = randv * 0x15a4e35 + 1;
        return (randv & 0x7fffffff) % n;
    }

    public void setdead(boolean isDead) {
        gamedat[curplayer].isDead = isDead;
    }

    private void shownplayers() {
        if (nplayers == 1) {
            world.drawing.outText("ONE", 220, 25, 3);
            world.drawing.outText(" PLAYER ", 192, 39, 3);
        } else {
            world.drawing.outText("TWO", 220, 25, 3);
            world.drawing.outText(" PLAYERS", 184, 39, 3);
        }
    }

    private void switchnplayers() {
        nplayers = 3 - nplayers;
    }

    private void testPause() {
        if (world.input.akeypressed == KeyEvent.VK_SPACE) { /* Space bar */
            world.input.akeypressed = 0;
            world.sound.soundpause();
            world.sound.sett2val(40);
            world.sound.setsoundt2();

            clearTopLine();
            world.drawing.outText("PRESS ANY KEY", 80, 0, 1);
            world.digger.newframe();
            world.input.keypressed = 0;
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
                if (world.input.keypressed != 0)
                    break;
            }

            clearTopLine();
            world.scores.drawscores();
            world.scores.addscore(0);
            world.drawing.drawLives();
            world.digger.newframe();
            world.digger.time = world.pc.gethrt() - world.digger.frametime;
            world.input.keypressed = 0;
        } else
            world.sound.soundpauseoff();
    }
}
