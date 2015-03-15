package com.tsoft.game.xonix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class TileView extends View {
    protected static int tileWidth = 7;
    protected static int tileHeight = 14;

    protected static int mXTileCount;
    protected static int mYTileCount;

    private static int mXOffset;
    private static int mYOffset;

    private Bitmap[] mTileArray;

    private int[][] mTileGrid;

    private final Paint mPaint = new Paint();

    public TileView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TileView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void resetTiles(int tilecount) {
    	mTileArray = new Bitmap[tilecount];
    }

    public void loadTile(int key, Drawable tile) {
        Bitmap bitmap = Bitmap.createBitmap(tileWidth, tileWidth, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        tile.setBounds(0, 0, tileWidth, tileWidth);
        tile.draw(canvas);

        mTileArray[key] = bitmap;
    }

    public void clearTiles() {
        for (int x = 0; x < mXTileCount; x++) {
            for (int y = 0; y < mYTileCount; y++) {
                setTile(0, x, y);
            }
        }
    }

    public void setTile(int tileindex, int x, int y) {
        mTileGrid[x][y] = tileindex;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int x = 0; x < mXTileCount; x += 1) {
            for (int y = 0; y < mYTileCount; y += 1) {
                if (mTileGrid[x][y] > 0) {
                    canvas.drawBitmap(mTileArray[mTileGrid[x][y]],
                    		mXOffset + x * tileWidth,
                    		mYOffset + y * tileWidth,
                    		mPaint);
                }
            }
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXTileCount = (int) Math.floor(w / tileWidth);
        mYTileCount = (int) Math.floor(h / tileWidth);

        mXOffset = ((w - (tileWidth * mXTileCount)) / 2);
        mYOffset = ((h - (tileWidth * mYTileCount)) / 2);

        mTileGrid = new int[mXTileCount][mYTileCount];
        clearTiles();
    }
}