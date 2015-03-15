package com.tsoft.game.digger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Scores implements Runnable {
    private World world;
    public Object[][] scores;
    private String substr;

    private char highbuf[] = new char[10];
    private long scorehigh[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [12]
    private String scoreinit[] = new String[11];
    private long scoret = 0, score1 = 0, score2 = 0, nextbs1 = 0, nextbs2 = 0;
    private String hsbuf;
    private char scorebuf[] = new char[512];
    private int bonusscore = 20000;
    private boolean gotinitflag = false;

    public Scores(World world) {
        this.world = world;
    }

    public Object[][] _submit(String n, int s) {
        if (world.digger.subaddr != null) {
            int ms = 16 + (int) (System.currentTimeMillis() % (65536 - 16));
            substr = n + '+' + s + '+' + ms + '+' + ((ms + 32768) * s) % 65536;
            new Thread(this).start();
        }
        return scores;
    }

    public void updateScores(Object[][] o) {
        if (o == null)
            return;

        try {
            String[] in = new String[10];
            int[] sc = new int[10];
            for (int i = 0; i < 10; i++) {
                in[i] = (String) o[i][0];
                sc[i] = ((Integer) o[i][1]).intValue();
            }
            for (int i = 0; i < 10; i++) {
                scoreinit[i + 1] = in[i];
                scorehigh[i + 2] = sc[i];
            }
        } catch (Exception e) {
        }
        ;

    }

    public void addscore(int score) {
        if (world.main.getCurrPlayer() == 0) {
            score1 += score;
            if (score1 > 999999l)
                score1 = 0;
            writenum(score1, 0, 0, 6, 1);
            if (score1 >= nextbs1) {
                if (world.main.getLives(1) < 5) {
                    world.main.addLife(1);
                    world.drawing.drawLives();
                }
                nextbs1 += bonusscore;
            }
        } else {
            score2 += score;
            if (score2 > 999999l)
                score2 = 0;
            if (score2 < 100000l)
                writenum(score2, 236, 0, 6, 1);
            else
                writenum(score2, 248, 0, 6, 1);
            if (score2 > nextbs2) {   /* Player 2 doesn't get the life until >20,000 ! */
                if (world.main.getLives(2) < 5) {
                    world.main.addLife(2);
                    world.drawing.drawLives();
                }
                nextbs2 += bonusscore;
            }
        }
        world.main.incPenalty();
        world.main.incPenalty();
        world.main.incPenalty();
    }

    public void drawscores() {
        writenum(score1, 0, 0, 6, 3);
        if (world.main.nplayers == 2)
            if (score2 < 100000l)
                writenum(score2, 236, 0, 6, 3);
            else
                writenum(score2, 248, 0, 6, 3);
    }

    public void endofgame() {
        int i, j, z;
        addscore(0);
        if (world.main.getCurrPlayer() == 0)
            scoret = score1;
        else
            scoret = score2;
        if (scoret > scorehigh[11]) {
            world.pc.gclear();
            drawscores();
            world.main.pldispbuf = "PLAYER ";
            if (world.main.getCurrPlayer() == 0)
                world.main.pldispbuf += "1";
            else
                world.main.pldispbuf += "2";
            world.drawing.outText(world.main.pldispbuf, 108, 0, 2, true);
            world.drawing.outText(" NEW HIGH SCORE ", 64, 40, 2, true);
            getinitials();
            updateScores(_submit(scoreinit[0], (int) scoret));
            shufflehigh();
//	savescores();
        } else {
            world.main.clearTopLine();
            world.drawing.outText("GAME OVER", 104, 0, 3, true);
            updateScores(_submit("...", (int) scoret));
            world.sound.killsound();
            for (j = 0; j < 20; j++) /* Number of times screen flashes * 2 */
                for (i = 0; i < 2; i++) { //i<8;i++) {
                    world.pc.gpal(1 - (j & 1));
                    for (z = 0; z < 111; z++) ; /* A delay loop */
                    world.pc.gpal(0);
                    world.pc.ginten(1 - i & 1);
                    world.digger.newframe();
                }
            world.sound.setupsound();
            world.drawing.outText("         ", 104, 0, 3, true);
        }
    }

    private void flashywait(int n) {
        try {
            Thread.sleep(n * 2);
        } catch (Exception e) {
        }
    }

    private int getinitial(int x, int y) {
        world.input.keypressed = 0;
        world.pc.gwrite(x, y, '_', 3, true);
        for (int j = 0; j < 5; j++) {
            for (int i = 0; i < 40; i++) {
                if ((world.input.keypressed & 0x80) == 0 && world.input.keypressed != 0)
                    return world.input.keypressed;
                flashywait(15);
            }

            for (int i = 0; i < 40; i++) {
                if ((world.input.keypressed & 0x80) == 0 && world.input.keypressed != 0) {
                    world.pc.gwrite(x, y, '_', 3, true);
                    return world.input.keypressed;
                }
                flashywait(15);
            }
        }
        gotinitflag = true;
        return 0;
    }

    private void getinitials() {
        world.drawing.outText("ENTER YOUR", 100, 70, 3, true);
        world.drawing.outText(" INITIALS", 100, 90, 3, true);
        world.drawing.outText("_ _ _", 128, 130, 3, true);
        scoreinit[0] = "...";
        world.sound.killsound();
        gotinitflag = false;

        for (int i = 0; i < 3; i++) {
            int k = 0;
            while (k == 0 && !gotinitflag) {
                k = getinitial(i * 24 + 128, 130);
                if (i != 0 && k == 8)
                    i--;
                k = world.input.getasciikey(k);
            }

            if (k != 0) {
                world.pc.gwrite(i * 24 + 128, 130, k, 3, true);
                StringBuffer sb = new StringBuffer(scoreinit[0]);
                sb.setCharAt(i, (char) k);
                scoreinit[0] = sb.toString();
            }
        }

        world.input.keypressed = 0;
        for (int i = 0; i < 20; i++)
            flashywait(15);
        world.sound.setupsound();
        world.pc.gclear();
        world.pc.gpal(0);
        world.pc.ginten(0);
        world.digger.newframe();    // needed by Java version!!
    }

    public void initscores() {
        addscore(0);
    }

    public void loadscores() {
        int p = 1, i, x;
        //readscores();
        for (i = 1; i < 11; i++) {
            for (x = 0; x < 3; x++)
                scoreinit[i] = "..."; //  scorebuf[p++];	--- zmienic
            p += 2;
            for (x = 0; x < 6; x++)
                highbuf[x] = scorebuf[p++];
            scorehigh[i + 1] = 0; //atol(highbuf);
        }

        if (scorebuf[0] != 's')
            for (i = 0; i < 11; i++) {
                scorehigh[i + 1] = 0;
                scoreinit[i] = "...";
            }
    }

    private String numtostring(long n) {
        int x;
        String p = "";
        for (x = 0; x < 6; x++) {
            p = String.valueOf(n % 10) + p;
            n /= 10;
            if (n == 0) {
                x++;
                break;
            }
        }
        for (; x < 6; x++)
            p = ' ' + p;
        return p;
    }

    public void run() {
        try {
            URL u = new URL(world.digger.subaddr + '?' + substr);
            URLConnection uc = u.openConnection();
            uc.setUseCaches(false);
            uc.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            Object[][] sc = new Object[10][2];
            for (int i = 0; i < 10; i++) {
                sc[i][0] = br.readLine();
                sc[i][1] = new Integer(br.readLine());
            }
            br.close();
            scores = sc;
        } catch (Exception e) {
        }

    }

    public void scorebonus() {
        addscore(1000);
    }

    public void scoreeatm() {
        addscore(world.digger.eatmsc * 200);
        world.digger.eatmsc <<= 1;
    }

    public void scoreemerald() {
        addscore(25);
    }

    public void scoregold() {
        addscore(500);
    }

    public void scorekill() {
        addscore(250);
    }

    public void scoreoctave() {
        addscore(250);
    }

    public void showtable() {
        world.drawing.outText("HIGH SCORES", 16, 25, 3);

        int col = 2;
        for (int i = 1; i < 11; i++) {
            hsbuf = scoreinit[i] + "  " + numtostring(scorehigh[i + 1]);
            world.drawing.outText(hsbuf, 16, 31 + 13 * i, col);
            col = 1;
        }
    }

    private void shufflehigh() {
        int i, j;
        for (j = 10; j > 1; j--)
            if (scoret < scorehigh[j])
                break;
        for (i = 10; i > j; i--) {
            scorehigh[i + 1] = scorehigh[i];
            scoreinit[i] = scoreinit[i - 1];
        }
        scorehigh[j + 1] = scoret;
        scoreinit[j] = scoreinit[0];
    }

    public void writecurscore(int bp6) {
        if (world.main.getCurrPlayer() == 0)
            writenum(score1, 0, 0, 6, bp6);
        else if (score2 < 100000l)
            writenum(score2, 236, 0, 6, bp6);
        else
            writenum(score2, 248, 0, 6, bp6);
    }

    private void writenum(long n, int x, int y, int w, int c) {
        int d, xp = (w - 1) * 12 + x;
        while (w > 0) {
            d = (int) (n % 10);
            if (w > 1 || d > 0)
                world.pc.gwrite(xp, y, d + '0', c, false);    //true
            n /= 10;
            w--;
            xp -= 12;
        }
    }

    public void zeroscores() {
        score2 = 0;
        score1 = 0;
        scoret = 0;
        nextbs1 = bonusscore;
        nextbs2 = bonusscore;
    }
}
