package com.tsoft.game.games.digger;

// sound has not been ported yet

public class Sound {
    private World world;

    private int wavetype = 0, t2val = 0, t0val = 0, musvol = 0;
    private int spkrmode = 0, timerrate = 0x7d0, timercount = 0;
    private int pulsewidth = 1;
    public int volume = 0;

    private int timerclock = 0;        // sint3

    private boolean soundflag = true;
    public boolean musicflag = true;

    private boolean sndflag = false, soundpausedflag = false;

    private boolean soundlevdoneflag = false;
    private int nljpointer = 0, nljnoteduration = 0;

    private int newlevjingle[] = {0x8e8, 0x712, 0x5f2, 0x7f0, 0x6ac, 0x54c, 0x712, 0x5f2, 0x4b8, 0x474, 0x474};    // [11]

    private boolean soundfallflag = false, soundfallf = false;
    private int soundfallvalue, soundfalln = 0;

    private boolean soundbreakflag = false;
    private int soundbreakduration = 0, soundbreakvalue = 0;

    private boolean soundwobbleflag = false;
    private int soundwobblen = 0;

    private boolean soundfireflag = false;
    private int soundfirevalue, soundfiren = 0;

    private boolean soundexplodeflag = false;
    private int soundexplodevalue, soundexplodeduration;

    private boolean soundbonusflag = false;
    private int soundbonusn = 0;

    private boolean soundemflag = false;

    private boolean soundemeraldflag = false;
    private int soundemeraldduration, emerfreq, soundemeraldn;

    private boolean soundgoldflag = false, soundgoldf = false;
    private int soundgoldvalue1, soundgoldvalue2, soundgoldduration;

    private boolean soundeatmflag = false;
    private int soundeatmvalue, soundeatmduration, soundeatmn;

    private boolean soundddieflag = false;
    private int soundddien, soundddievalue;

    private boolean sound1upflag = false;
    private int sound1upduration = 0;

    private boolean musicplaying = false;
    private int musicp = 0, tuneno = 0, noteduration = 0, notevalue = 0, musicmaxvol = 0, musicattackrate = 0, musicsustainlevel = 0, musicdecayrate = 0, musicnotewidth = 0, musicreleaserate = 0, musicstage = 0, musicn = 0;

    /*int bonusjingle[]={	// [321]
      0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,
       0xd59,4, 0xbe4,4, 0xa98,4,0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,
      0x11d1,4, 0xd59,2, 0xa98,2, 0xbe4,4, 0xe24,4,0x11d1,4,0x11d1,2,0x11d1,2,
      0x11d1,4,0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2, 0xd59,4, 0xbe4,4,
       0xa98,4, 0xd59,2, 0xa98,2, 0x8e8,10,0xa00,2, 0xa98,2, 0xbe4,2, 0xd59,4,
       0xa98,4, 0xd59,4,0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,0x11d1,4,
      0x11d1,2,0x11d1,2, 0xd59,4, 0xbe4,4, 0xa98,4,0x11d1,2,0x11d1,2,0x11d1,4,
      0x11d1,2,0x11d1,2,0x11d1,4, 0xd59,2, 0xa98,2, 0xbe4,4, 0xe24,4,0x11d1,4,
      0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,0x11d1,4,0x11d1,2,0x11d1,2,
       0xd59,4, 0xbe4,4, 0xa98,4, 0xd59,2, 0xa98,2, 0x8e8,10,0xa00,2, 0xa98,2,
       0xbe4,2, 0xd59,4, 0xa98,4, 0xd59,4, 0xa98,2, 0xa98,2, 0xa98,4, 0xa98,2,
       0xa98,2, 0xa98,4, 0xa98,2, 0xa98,2, 0xa98,4, 0x7f0,4, 0xa98,4, 0x7f0,4,
       0xa98,4, 0x7f0,4, 0xa98,4, 0xbe4,4, 0xd59,4, 0xe24,4, 0xfdf,4, 0xa98,2,
       0xa98,2, 0xa98,4, 0xa98,2, 0xa98,2, 0xa98,4, 0xa98,2, 0xa98,2, 0xa98,4,
       0x7f0,4, 0xa98,4, 0x7f0,4, 0xa98,4, 0x7f0,4, 0x8e8,4, 0x970,4, 0x8e8,4,
       0x970,4, 0x8e8,4, 0xa98,2, 0xa98,2, 0xa98,4, 0xa98,2, 0xa98,2, 0xa98,4,
       0xa98,2, 0xa98,2, 0xa98,4, 0x7f0,4, 0xa98,4, 0x7f0,4, 0xa98,4, 0x7f0,4,
       0xa98,4, 0xbe4,4, 0xd59,4, 0xe24,4, 0xfdf,4, 0xa98,2, 0xa98,2, 0xa98,4,
       0xa98,2, 0xa98,2, 0xa98,4, 0xa98,2, 0xa98,2, 0xa98,4, 0x7f0,4, 0xa98,4,
       0x7f0,4, 0xa98,4, 0x7f0,4, 0x8e8,4, 0x970,4, 0x8e8,4, 0x970,4, 0x8e8,4,
      0x7d64};

    int backgjingle[]={	// [291]
       0xfdf,2,0x11d1,2, 0xfdf,2,0x1530,2,0x1ab2,2,0x1530,2,0x1fbf,4, 0xfdf,2,
      0x11d1,2, 0xfdf,2,0x1530,2,0x1ab2,2,0x1530,2,0x1fbf,4, 0xfdf,2, 0xe24,2,
       0xd59,2, 0xe24,2, 0xd59,2, 0xfdf,2, 0xe24,2, 0xfdf,2, 0xe24,2,0x11d1,2,
       0xfdf,2,0x11d1,2, 0xfdf,2,0x1400,2, 0xfdf,4, 0xfdf,2,0x11d1,2, 0xfdf,2,
      0x1530,2,0x1ab2,2,0x1530,2,0x1fbf,4, 0xfdf,2,0x11d1,2, 0xfdf,2,0x1530,2,
      0x1ab2,2,0x1530,2,0x1fbf,4, 0xfdf,2, 0xe24,2, 0xd59,2, 0xe24,2, 0xd59,2,
       0xfdf,2, 0xe24,2, 0xfdf,2, 0xe24,2,0x11d1,2, 0xfdf,2,0x11d1,2, 0xfdf,2,
       0xe24,2, 0xd59,4, 0xa98,2, 0xbe4,2, 0xa98,2, 0xd59,2,0x11d1,2, 0xd59,2,
      0x1530,4, 0xa98,2, 0xbe4,2, 0xa98,2, 0xd59,2,0x11d1,2, 0xd59,2,0x1530,4,
       0xa98,2, 0x970,2, 0x8e8,2, 0x970,2, 0x8e8,2, 0xa98,2, 0x970,2, 0xa98,2,
       0x970,2, 0xbe4,2, 0xa98,2, 0xbe4,2, 0xa98,2, 0xd59,2, 0xa98,4, 0xa98,2,
       0xbe4,2, 0xa98,2, 0xd59,2,0x11d1,2, 0xd59,2,0x1530,4, 0xa98,2, 0xbe4,2,
       0xa98,2, 0xd59,2,0x11d1,2, 0xd59,2,0x1530,4, 0xa98,2, 0x970,2, 0x8e8,2,
       0x970,2, 0x8e8,2, 0xa98,2, 0x970,2, 0xa98,2, 0x970,2, 0xbe4,2, 0xa98,2,
       0xbe4,2, 0xa98,2, 0xd59,2, 0xa98,4, 0x7f0,2, 0x8e8,2, 0xa98,2, 0xd59,2,
      0x11d1,2, 0xd59,2,0x1530,4, 0xa98,2, 0xbe4,2, 0xa98,2, 0xd59,2,0x11d1,2,
       0xd59,2,0x1530,4, 0xa98,2, 0x970,2, 0x8e8,2, 0x970,2, 0x8e8,2, 0xa98,2,
       0x970,2, 0xa98,2, 0x970,2, 0xbe4,2, 0xa98,2, 0xbe4,2, 0xd59,2, 0xbe4,2,
       0xa98,4,0x7d64};

    int dirge[]={
      0x7d00, 2,0x11d1, 6,0x11d1, 4,0x11d1, 2,0x11d1, 6, 0xefb, 4, 0xfdf, 2,
       0xfdf, 4,0x11d1, 2,0x11d1, 4,0x12e0, 2,0x11d1,12,0x7d00,16,0x7d00,16,
      0x7d00,16,0x7d00,16,0x7d00,16,0x7d00,16,0x7d00,16,0x7d00,16,0x7d00,16,
      0x7d00,16,0x7d00,16,0x7d00,16,0x7d64};
    */
    private boolean soundt0flag = false;

    private boolean int8flag = false;

    public Sound(World world) {
        this.world = world;
    }

    public void initsound() {
//  settimer2(0x20);
//  setspkrt2();
//  settimer0(0);
        wavetype = 2;
        t0val = 12000;
        musvol = 8;
        t2val = 40;
        soundt0flag = true;
        sndflag = true;
        spkrmode = 0;
        int8flag = false;
        setsoundt2();
        soundstop();
        startint8();
        timerrate = 0x4000;
//  timer0(0x4000);
    }

    public void killsound() {
        // added by me...
    }

    public void music(int tune) {
        tuneno = tune;
        musicp = 0;
        noteduration = 0;
        switch (tune) {
            case 0:
                musicmaxvol = 50;
                musicattackrate = 20;
                musicsustainlevel = 20;
                musicdecayrate = 10;
                musicreleaserate = 4;
                break;
            case 1:
                musicmaxvol = 50;
                musicattackrate = 50;
                musicsustainlevel = 8;
                musicdecayrate = 15;
                musicreleaserate = 1;
                break;
            case 2:
                musicmaxvol = 50;
                musicattackrate = 50;
                musicsustainlevel = 25;
                musicdecayrate = 5;
                musicreleaserate = 1;
        }
        musicplaying = true;
        if (tune == 2)
            soundddieoff();
    }

    public void musicoff() {
        musicplaying = false;
        musicp = 0;
    }

    private void musicupdate() {
        if (!musicplaying)
            return;
        if (noteduration != 0)
            noteduration--;
        else {
            musicstage = musicn = 0;
            switch (tuneno) {
                case 0:
//		noteduration=bonusjingle[musicp+1]*3;
                    musicnotewidth = noteduration - 3;
//		notevalue=bonusjingle[musicp];
                    musicp += 2;
//		if (bonusjingle[musicp]==0x7d64)
//		  musicp=0;
                    break;
                case 1:
//		noteduration=backgjingle[musicp+1]*6;
                    musicnotewidth = 12;
//		notevalue=backgjingle[musicp];
                    musicp += 2;
//		if (backgjingle[musicp]==0x7d64)
//		  musicp=0;
                    break;
                case 2:
//		noteduration=dirge[musicp+1]*10;
                    musicnotewidth = noteduration - 10;
//		notevalue=dirge[musicp];
                    musicp += 2;
//		if (dirge[musicp]==0x7d64)
//		  musicp=0;
                    break;
            }
        }
        musicn++;
        wavetype = 1;
        t0val = notevalue;
        if (musicn >= musicnotewidth)
            musicstage = 2;
        switch (musicstage) {
            case 0:
                if (musvol + musicattackrate >= musicmaxvol) {
                    musicstage = 1;
                    musvol = musicmaxvol;
                    break;
                }
                musvol += musicattackrate;
                break;
            case 1:
                if (musvol - musicdecayrate <= musicsustainlevel) {
                    musvol = musicsustainlevel;
                    break;
                }
                musvol -= musicdecayrate;
                break;
            case 2:
                if (musvol - musicreleaserate <= 1) {
                    musvol = 1;
                    break;
                }
                musvol -= musicreleaserate;
        }
        if (musvol == 1)
            t0val = 0x7d00;
    }

    private void s0fillbuffer() {
    }

    private void s0killsound() {
        setsoundt2();
//  timer2(40);
        stopint8();
    }

    private void s0setupsound() {
        startint8();
    }

    private void setsoundmode() {
        spkrmode = wavetype;
        if (!soundt0flag && sndflag) {
            soundt0flag = true;
//	setspkrt2();
        }
    }

    public void setsoundt2() {
        if (soundt0flag) {
            spkrmode = 0;
            soundt0flag = false;
//	setspkrt2();
        }
    }

    private void sett0() {
        if (sndflag) {
//	timer2(t2val);
            if (t0val < 1000 && (wavetype == 1 || wavetype == 2))
                t0val = 1000;
//	timer0(t0val);
            timerrate = t0val;
            if (musvol < 1)
                musvol = 1;
            if (musvol > 50)
                musvol = 50;
            pulsewidth = musvol * volume;
            setsoundmode();
        }
    }

    public void sett2val(int t2v) {
//  if (sndflag)
//	timer2(t2v);
    }

    public void setupsound() {
        // added by me..
    }

    public void sound1up() {
        sound1upduration = 96;
        sound1upflag = true;
    }

    private void sound1upoff() {
        sound1upflag = false;
    }

    private void sound1upupdate() {
        if (sound1upflag) {
            if ((sound1upduration / 3) % 2 != 0)
                t2val = (sound1upduration << 2) + 600;
            sound1upduration--;
            if (sound1upduration < 1)
                sound1upflag = false;
        }
    }

    public void soundbonus() {
        soundbonusflag = true;
    }

    public void soundbonusoff() {
        soundbonusflag = false;
        soundbonusn = 0;
    }

    private void soundbonusupdate() {
        if (soundbonusflag) {
            soundbonusn++;
            if (soundbonusn > 15)
                soundbonusn = 0;
            if (soundbonusn >= 0 && soundbonusn < 6)
                t2val = 0x4ce;
            if (soundbonusn >= 8 && soundbonusn < 14)
                t2val = 0x5e9;
        }
    }

    public void soundbreak() {
        soundbreakduration = 3;
        if (soundbreakvalue < 15000)
            soundbreakvalue = 15000;
        soundbreakflag = true;
    }

    private void soundbreakoff() {
        soundbreakflag = false;
    }

    private void soundbreakupdate() {
        if (soundbreakflag)
            if (soundbreakduration != 0) {
                soundbreakduration--;
                t2val = soundbreakvalue;
            } else
                soundbreakflag = false;
    }

    public void soundddie() {
        soundddien = 0;
        soundddievalue = 20000;
        soundddieflag = true;
    }

    private void soundddieoff() {
        soundddieflag = false;
    }

    private void soundddieupdate() {
        if (soundddieflag) {
            soundddien++;
            if (soundddien == 1)
                musicoff();
            if (soundddien >= 1 && soundddien <= 10)
                soundddievalue = 20000 - soundddien * 1000;
            if (soundddien > 10)
                soundddievalue += 500;
            if (soundddievalue > 30000)
                soundddieoff();
            t2val = soundddievalue;
        }
    }

    public void soundeatm() {
        soundeatmduration = 20;
        soundeatmn = 3;
        soundeatmvalue = 2000;
        soundeatmflag = true;
    }

    private void soundeatmoff() {
        soundeatmflag = false;
    }

    private void soundeatmupdate() {
        if (soundeatmflag)
            if (soundeatmn != 0) {
                if (soundeatmduration != 0) {
                    if ((soundeatmduration % 4) == 1)
                        t2val = soundeatmvalue;
                    if ((soundeatmduration % 4) == 3)
                        t2val = soundeatmvalue - (soundeatmvalue >> 4);
                    soundeatmduration--;
                    soundeatmvalue -= (soundeatmvalue >> 4);
                } else {
                    soundeatmduration = 20;
                    soundeatmn--;
                    soundeatmvalue = 2000;
                }
            } else
                soundeatmflag = false;
    }

    public void soundem() {
        soundemflag = true;
    }

    public void soundemerald(int emocttime) {
        if (emocttime != 0) {
            switch (emerfreq) {
                case 0x8e8:
                    emerfreq = 0x7f0;
                    break;
                case 0x7f0:
                    emerfreq = 0x712;
                    break;
                case 0x712:
                    emerfreq = 0x6ac;
                    break;
                case 0x6ac:
                    emerfreq = 0x5f2;
                    break;
                case 0x5f2:
                    emerfreq = 0x54c;
                    break;
                case 0x54c:
                    emerfreq = 0x4b8;
                    break;
                case 0x4b8:
                    emerfreq = 0x474;
                    world.scores.scoreoctave();
                    break;
                case 0x474:
                    emerfreq = 0x8e8;
            }
        } else
            emerfreq = 0x8e8;
        soundemeraldduration = 7;
        soundemeraldn = 0;
        soundemeraldflag = true;
    }

    private void soundemeraldoff() {
        soundemeraldflag = false;
    }

    private void soundemeraldupdate() {
        if (soundemeraldflag)
            if (soundemeraldduration != 0) {
                if (soundemeraldn == 0 || soundemeraldn == 1)
                    t2val = emerfreq;
                soundemeraldn++;
                if (soundemeraldn > 7) {
                    soundemeraldn = 0;
                    soundemeraldduration--;
                }
            } else
                soundemeraldoff();
    }

    private void soundemoff() {
        soundemflag = false;
    }

    private void soundemupdate() {
        if (soundemflag) {
            t2val = 1000;
            soundemoff();
        }
    }

    public void soundexplode() {
        soundexplodevalue = 1500;
        soundexplodeduration = 10;
        soundexplodeflag = true;
        soundfireoff();
    }

    private void soundexplodeoff() {
        soundexplodeflag = false;
    }

    private void soundexplodeupdate() {
        if (soundexplodeflag)
            if (soundexplodeduration != 0) {
                soundexplodevalue = t2val = soundexplodevalue - (soundexplodevalue >> 3);
                soundexplodeduration--;
            } else
                soundexplodeflag = false;
    }

    public void soundfall() {
        soundfallvalue = 1000;
        soundfallflag = true;
    }

    public void soundfalloff() {
        soundfallflag = false;
        soundfalln = 0;
    }

    private void soundfallupdate() {
        if (soundfallflag)
            if (soundfalln < 1) {
                soundfalln++;
                if (soundfallf)
                    t2val = soundfallvalue;
            } else {
                soundfalln = 0;
                if (soundfallf) {
                    soundfallvalue += 50;
                    soundfallf = false;
                } else
                    soundfallf = true;
            }
    }

    public void soundfire() {
        soundfirevalue = 500;
        soundfireflag = true;
    }

    public void soundfireoff() {
        soundfireflag = false;
        soundfiren = 0;
    }

    private void soundfireupdate() {
        if (soundfireflag) {
            if (soundfiren == 1) {
                soundfiren = 0;
                soundfirevalue += soundfirevalue / 55;
                t2val = soundfirevalue + world.main.randno(soundfirevalue >> 3);
                if (soundfirevalue > 30000)
                    soundfireoff();
            } else
                soundfiren++;
        }
    }

    public void soundgold() {
        soundgoldvalue1 = 500;
        soundgoldvalue2 = 4000;
        soundgoldduration = 30;
        soundgoldf = false;
        soundgoldflag = true;
    }

    private void soundgoldoff() {
        soundgoldflag = false;
    }

    private void soundgoldupdate() {
        if (soundgoldflag) {
            if (soundgoldduration != 0)
                soundgoldduration--;
            else
                soundgoldflag = false;
            if (soundgoldf) {
                soundgoldf = false;
                t2val = soundgoldvalue1;
            } else {
                soundgoldf = true;
                t2val = soundgoldvalue2;
            }
            soundgoldvalue1 += (soundgoldvalue1 >> 4);
            soundgoldvalue2 -= (soundgoldvalue2 >> 4);
        }
    }

    private void soundint() {
        timerclock++;
        if (soundflag && !sndflag)
            sndflag = musicflag = true;
        if (!soundflag && sndflag) {
            sndflag = false;
//	timer2(40);
            setsoundt2();
        }
        if (sndflag && !soundpausedflag) {
            t0val = 0x7d00;
            t2val = 40;
            if (musicflag)
                musicupdate();
            soundemeraldupdate();
            soundwobbleupdate();
            soundddieupdate();
            soundbreakupdate();
            soundgoldupdate();
            soundemupdate();
            soundexplodeupdate();
            soundfireupdate();
            soundeatmupdate();
            soundfallupdate();
            sound1upupdate();
            soundbonusupdate();
            if (t0val == 0x7d00 || t2val != 40)
                setsoundt2();
            else {
                setsoundmode();
                sett0();
            }
            sett2val(t2val);
        }
    }

    public void soundlevdone() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
/*  int timer=0;
  soundstop();
  nljpointer=0;
  nljnoteduration=20;
  soundlevdoneflag=soundpausedflag=true;
  while (soundlevdoneflag) {
	if (timerclock==timer)
	  continue;
	soundlevdoneupdate();
	timer=timerclock;
  } */
    }

    private void soundlevdoneoff() {
        soundlevdoneflag = soundpausedflag = false;
    }

    private void soundlevdoneupdate() {
        if (sndflag) {
            if (nljpointer < 11)
                t2val = newlevjingle[nljpointer];
            t0val = t2val + 35;
            musvol = 50;
            setsoundmode();
            sett0();
            sett2val(t2val);
            if (nljnoteduration > 0)
                nljnoteduration--;
            else {
                nljnoteduration = 20;
                nljpointer++;
                if (nljpointer > 10)
                    soundlevdoneoff();
            }
        } else {
//	olddelay(100);
            soundlevdoneflag = false;
        }
    }

    private void soundoff() {
        // phony
    }

    public void soundpause() {
        soundpausedflag = true;
    }

    public void soundpauseoff() {
        soundpausedflag = false;
    }

    public void soundstop() {
        soundfalloff();
        soundwobbleoff();
        soundfireoff();
        musicoff();
        soundbonusoff();
        soundexplodeoff();
        soundbreakoff();
        soundemoff();
        soundemeraldoff();
        soundgoldoff();
        soundeatmoff();
        soundddieoff();
        sound1upoff();
    }

    public void soundwobble() {
        soundwobbleflag = true;
    }

    public void soundwobbleoff() {
        soundwobbleflag = false;
        soundwobblen = 0;
    }

    private void soundwobbleupdate() {
        if (soundwobbleflag) {
            soundwobblen++;
            if (soundwobblen > 63)
                soundwobblen = 0;
            switch (soundwobblen) {
                case 0:
                    t2val = 0x7d0;
                    break;
                case 16:
                case 48:
                    t2val = 0x9c4;
                    break;
                case 32:
                    t2val = 0xbb8;
                    break;
            }
        }
    }

    private void startint8() {
        if (!int8flag) {
//	initint8();
            timerrate = 0x4000;
//	timer0(0x4000);
            int8flag = true;
        }
    }

    private void stopint8() {
//  timer0(0);
        if (int8flag) {
//	restoreint8();
            int8flag = false;
        }
        sett2val(40);
//  setspkrt2();
    }
}
