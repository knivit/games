package com.tsoft.game.xonix;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import com.tsoft.game.InputController;
import com.tsoft.game.TextScreen;
import com.tsoft.game.engine.GameView;
import com.tsoft.game.engine.keyboard.AlfaNumericKeyboard;

public class XGGameView extends GameView {
    public static final String GAME_STATE_TAG = "Game State";
    public static final String GAME_TIMING_TAG = "Game Timing";

    private XGScreen screen;
    private InputController inputController;

    private XGMode mode;
    private long loopCount;

    private Paint paint;

    public XGGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XGGameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void initResources(Point screenSize) {
        paint = new Paint();
        paint.setColor(Color.GREEN);
        int dx = screenSize.x / XGScreen.WIDTH;
        int dy = screenSize.y / XGScreen.HEIGHT;
        paint.setTextSize(dx < dy ? dx : dy);
        Typeface tf = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        paint.setTypeface(tf);

        screen = new XGScreen();
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
            stop();
        }

        // press 'D' to print out game's state
        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == 'D') {
            Log.i(GAME_STATE_TAG, "Loop #" + loopCount);
            Log.i(GAME_STATE_TAG, screen.getLogString());
            Log.i(GAME_STATE_TAG, inputController.toLogString());
            Log.i(GAME_STATE_TAG, mode.getLogString());
        }
        loopCount ++;
    }
    
    private char[][] screenBuf = new char[XGScreen.WIDTH][XGScreen.HEIGHT];
    private String[] screenChar = new String[127];

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        char[] buf = new char[screen.getWidth()];
        String s = new String(buf);

        for (int y = 0; y < XGScreen.HEIGHT; y ++) {
            for (int x = 0; x < XGScreen.WIDTH; x ++) {
                char ch = screen.getChar(x, y);
                if (ch < ' ') ch = ' ';
                if (screenBuf[x][y] != ch) {
                    screenBuf[x][y] = ch;
                    
                    // Draw the char
                    if (screenChar[ch] == null) {
                        screenChar[ch] = Character.toString(ch);
                    }

                    canvas.drawText(screenChar[ch], x * paint.getTextSize(), y * paint.getTextSize(), paint);
                }
            }
        }
    }
}

