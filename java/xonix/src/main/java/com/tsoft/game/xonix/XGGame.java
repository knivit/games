package com.tsoft.game.xonix;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.background.ColorBackground;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

import java.awt.*;

public class XGGame extends Game {
    { distribute = true; }
    private PlayField playField;

    private XGScreen screen;
    private InputController inputController;

    private XGMode mode;
    private long loopCount;

    @Override
    public void initResources() {
        playField = new PlayField();
        playField.setBackground(new ColorBackground(Color.BLACK));

        screen = new XGScreen(XGScreen.WIDTH, XGScreen.HEIGHT, getImages("screen714.gif", 16, 6));
        inputController = new InputController(this);

        mode = new XGMenuMode();
    }

    @Override
    public void update(long elapsedTime) {
        if (mode.nextMode() != null) {
            mode = mode.nextMode();
        }

        mode.update(screen, inputController, elapsedTime);

        if (mode.isFinishGame()) {
            finish();
        }

        // press 'D' to print out game's state
        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == 'D') {
            System.out.println("Loop #" + loopCount);
            System.out.println(screen.getLogString());
            System.out.println(inputController.toLogString());
            System.out.println(mode.getLogString());
        }
        loopCount ++;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        playField.render(graphics2D);

        for (int y = 0; y < screen.getHeight(); y ++) {
            for (int x = 0; x < screen.getWidth(); x ++) {
                int charIndex = screen.getChar(x, y) - 32;
                if (charIndex < 0) {
                    charIndex = 0;
                }

                graphics2D.drawImage(screen.getFontImages(charIndex), x * XGScreen.FONT_WIDTH, y * XGScreen.FONT_HEIGHT, null);
            }
        }
    }

    public static void main(String[] args) {
        GameLoader gameLoader = new GameLoader();
        gameLoader.setup(new XGGame(), new Dimension(XGScreen.WIDTH * XGScreen.FONT_WIDTH,
                XGScreen.HEIGHT * XGScreen.FONT_HEIGHT), false);

        gameLoader.start();
    }
}
