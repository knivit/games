package com.tsoft.game.xonix;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import com.tsoft.gameView.xonix.R;

public class XonixActivity extends Activity {
    private XGGameView xgGame;

    private static String ICICLE_KEY = "xonix-view";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.xonix_layout);

        xgGame = (XGGameView) findViewById(R.id.xonix);
    }

    @Override
    public void onStart() {
        super.onStart();

        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        xgGame.start(screenSize);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putBundle(ICICLE_KEY, xgGame.saveState());
    }
}
