package com.tsoft.game.loderunner;

import com.golden.gamedev.Game;
import com.golden.gamedev.GameLoader;
import com.golden.gamedev.object.PlayField;
import com.golden.gamedev.object.background.ColorBackground;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;
import com.tsoft.game.xonix.XGScreen;

import java.awt.*;

public class LRGame extends Game {
    // { distribute = true; }
    private PlayField playField;

    private LRWorld world;
    private InputController inputController;

    private LRMode mode;
    private long loopCount;

    @Override
    public void initResources() {
        playField = new PlayField();
        playField.setBackground(new ColorBackground(Color.BLACK));

        world = new LRWorld(getImages("screen714.gif", 16, 6));
        inputController = new InputController(this);

        mode = new LRMenuMode();
    }

    @Override
    public void update(long elapsedTime) {
        if (mode.nextMode() != null) {
            mode = mode.nextMode();
        }

        mode.update(world, inputController, elapsedTime);

        if (mode.isFinishGame()) {
            finish();
        }

        // press 'D' to print out game's state
        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == 'D') {
            System.out.println("Loop #" + loopCount);
            System.out.println(world.getLogString());
            System.out.println(inputController.toLogString());
            System.out.println(mode.getLogString());
        }
        loopCount ++;
    }

    @Override
    public void render(Graphics2D graphics2D) {
        playField.render(graphics2D);

        for (int y = 0; y < world.getScreen().getHeight(); y ++) {
            for (int x = 0; x < world.getScreen().getWidth(); x ++) {
                int charIndex = world.getScreen().getChar(x, y) - 32;
                if (charIndex < 0) {
                    charIndex = 0;
                }

                graphics2D.drawImage(world.getScreen().getFontImages(charIndex),
                        x * XGScreen.FONT_WIDTH,
                        y * XGScreen.FONT_HEIGHT, null);
            }
        }
    }

    public static void main(String[] args) {
        GameLoader gameLoader = new GameLoader();
        gameLoader.setup(new LRGame(), new Dimension(LRScreen.WIDTH * LRScreen.FONT_WIDTH,
                LRScreen.HEIGHT * LRScreen.FONT_HEIGHT), false);

        gameLoader.start();
    }
}
