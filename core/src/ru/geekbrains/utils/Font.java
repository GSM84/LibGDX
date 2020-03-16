package ru.geekbrains.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Font extends BitmapFont {

    public Font(String _fontFile, String _imgFile){
        super(Gdx.files.internal(_fontFile), Gdx.files.internal(_imgFile), false, false);
    }

    public void setSize(float _size){
        getData().setScale(_size / getCapHeight());
    }

    public GlyphLayout draw(Batch batch, CharSequence str, float x, float y, int haling) {
        return super.draw(batch, str, x, y, 0f, haling, false);
    }
}
