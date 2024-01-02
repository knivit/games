package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GdxScreen {

    // char's size in pixels
    private final int fontWidth;
    private final int fontHeight;

    // start char
    private char startChar;

    private final OrthographicCamera camera;
    private final SpriteBatch batch;

    private Sprite[] sprites;
    private Sprite[] invSprites;

    /** width, height - screen size (in chars) */
    public GdxScreen(int width, int height, int fontWidth, int fontHeight) {
        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, width * fontWidth, height * fontHeight);

        batch = new SpriteBatch();
    }

    // sprites in the pixmap
    public void create(Pixmap pixmap, char startChar) {
        this.startChar = startChar;

        Texture texture = new Texture(pixmap);

        int hy = texture.getHeight() / fontHeight;
        int hx = texture.getWidth() / fontWidth;
        sprites = new Sprite[hx * hy];

        int n = 0;
        for (int y = 0; y < texture.getHeight(); y += fontHeight) {
            for (int x = 0; x < texture.getWidth(); x += fontWidth) {
                Sprite sprite = new Sprite(texture, x, y, fontWidth, fontHeight);
                sprites[n] = sprite;
                n ++;
            }
        }
    }

    // sprites in a file
    public void create(String fontAssetsName, char startChar) {
        this.startChar = startChar;

        FileHandle assets = Gdx.files.internal(fontAssetsName);

        Texture texture = new Texture(assets);
        Texture invTexture = new Texture(inverse(new Pixmap(assets)));

        int hy = texture.getHeight() / fontHeight;
        int hx = texture.getWidth() / fontWidth;
        sprites = new Sprite[hx * hy];
        invSprites = new Sprite[hx * hy];

        int n = 0;
        for (int y = 0; y < hy * fontHeight; y += fontHeight) {
            for (int x = 0; x < hx * fontWidth; x += fontWidth) {
                Sprite sprite = new Sprite(texture, x, y, fontWidth, fontHeight);
                sprites[n] = sprite;

                Sprite invSprite = new Sprite(invTexture, x, y, fontWidth, fontHeight);
                invSprites[n] = invSprite;

                n ++;
            }
        }
    }

    public void render(TextScreen screen) {
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        Color color = new Color();
        for (int y = 0; y < screen.getHeight(); y ++) {
            for (int x = 0; x < screen.getWidth(); x ++) {
                TextSprite ts = screen.sprite(x, y);
                int n = Math.max(ts.ch - startChar, 0);
                Sprite sprite = ts.inverse ? invSprites[n] : sprites[n];

                color.set(ts.color);
                batch.setColor(color);
                batch.draw(sprite, x * fontWidth, y * fontHeight);
            }
        }

        batch.end();
    }

    private Pixmap inverse(Pixmap pixmap) {
        int black = pixmap.getPixel(0, 0);

        for (int x = 0; x < pixmap.getWidth(); x ++) {
            for (int y = 0; y < pixmap.getHeight(); y ++) {
                int rgba = pixmap.getPixel(x, y);
                int inv = (rgba == black) ? Color.rgba8888(Color.WHITE) : rgba;
                pixmap.drawPixel(x, y, inv);
            }
        }

        return pixmap;
    }

    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
    }
}
