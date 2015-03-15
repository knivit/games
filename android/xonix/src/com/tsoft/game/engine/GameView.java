package com.tsoft.game.engine;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import java.util.HashMap;

public abstract class GameView extends View {
    private static final int DEFAULT_FPS = 60;

    private boolean running;
    private boolean initialized;

    private long elapsedTime;
    private long lastLoopTime;

    private HashMap<Integer, Boolean> pressedKeys = new HashMap<Integer, Boolean>();

    public abstract void initResources(Point screenSize);

    public abstract void update(long elapsedTime);

    private RefreshHandler refreshHandler;

    class RefreshHandler extends Handler {
        private GameView view;

        public RefreshHandler(GameView view) {
            this.view = view;
        }

        @Override
        public void handleMessage(Message msg) {
            view.doGameLoop();
            view.invalidate();
        }

        public void sleep(long delayMillis) {
        	this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    };

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void start(Point screenSize) {
        if (running) {
            return;
        }
        running = true;

        if (!initialized) {
            initialized = true;
            initResources(screenSize);
        }

        refreshHandler = new RefreshHandler(this);

        doGameLoop();
    }

    private void doGameLoop() {
        long now = System.currentTimeMillis();
        elapsedTime = now - lastLoopTime;

        update(elapsedTime);
        lastLoopTime = now;

        refreshHandler.sleep(10);
    }

    public void stop() {
        running = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        System.out.println("Key down: " + keyCode);
        pressedKeys.put(keyCode, true);
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        System.out.println("Key up: " + keyCode);
        pressedKeys.put(keyCode, false);
        return true;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isKeyDown(int keyCode) {
        Boolean value = pressedKeys.get(keyCode);
        return value == null ? false : value.booleanValue();
    }
}
