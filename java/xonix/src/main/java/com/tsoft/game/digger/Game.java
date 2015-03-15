package com.tsoft.game.digger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.IndexColorModel;
import java.awt.image.MemoryImageSource;

public class Game extends Panel implements Runnable {
    private World world;

    private Thread gamethread;

    public Game() {
        super();

        world = new World();
    }

    public void init() {
/*        subaddr = getParameter("submit");

        try {
            frametime = Integer.parseInt(getParameter("speed"));
            if (frametime > MAX_RATE)
                frametime = MAX_RATE;
            else if (frametime < MIN_RATE)
                frametime = MIN_RATE;
        } catch (Exception e) {
        }
 */
        world.pc.pixels = new int[65536];

        for (int i = 0; i < 2; i++) {
            world.pc.source[i] = new MemoryImageSource(world.pc.width, world.pc.height,
                    new IndexColorModel(8, 4,
                            world.pc.pal[i][0],
                            world.pc.pal[i][1],
                            world.pc.pal[i][2]),
                            world.pc.pixels, 0, world.pc.width);
            world.pc.source[i].setAnimated(true);
            world.pc.image[i] = createImage(world.pc.source[i]);
            world.pc.source[i].newPixels();
        }

        world.pc.currentImage = world.pc.image[0];
        world.pc.currentSource = world.pc.source[0];

        gamethread = new Thread(this);
        gamethread.start();
    }

    @Override
    public void paint(Graphics g) {
        update(g);
    }

    @Override
    public void run() {
        world.main.main();
    }

    @Override
    public void update(Graphics g) {
        g.drawImage(world.pc.currentImage, 0, 0, this);
    }

    public boolean keyDown(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                world.input.processkey(0x4b);
                break;
            case KeyEvent.VK_RIGHT:
                world.input.processkey(0x4d);
                break;
            case KeyEvent.VK_UP:
                world.input.processkey(0x48);
                break;
            case KeyEvent.VK_DOWN:
                world.input.processkey(0x50);
                break;
            case KeyEvent.VK_SPACE:
                world.input.processkey(0x3b);
                break;
            default:
/*                key &= 0x7f;
                if ((key >= 65) && (key <= 90))
                    key += (97 - 65);
                input.processkey(key);
   */               break;
        }
        return true;
    }

    public boolean keyUp(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                world.input.processkey(0xcb);
                break;
            case KeyEvent.VK_RIGHT:
                world.input.processkey(0xcd);
                break;
            case KeyEvent.VK_UP:
                world.input.processkey(0xc8);
                break;
            case KeyEvent.VK_DOWN:
                world.input.processkey(0xd0);
                break;
            case KeyEvent.VK_SPACE:
                world.input.processkey(0xbb);
                break;
            default:
/*                key &= 0x7f;
                if ((key >= 65) && (key <= 90))
                    key += (97 - 65);
                input.processkey(0x80 | key);
  */                break;
        }
        return true;
    }

    public static void main(String[] args) {
        final Game game = new Game();
        game.init();

        JFrame frame = new JFrame("Digger");
        frame.setLayout(new BorderLayout());
        frame.add(game, BorderLayout.CENTER);
        frame.setSize(320, 240);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                game.keyDown(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                game.keyUp(e);
            }
        });

        frame.setVisible(true);
    }
}
