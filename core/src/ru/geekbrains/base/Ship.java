package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public abstract class Ship extends Sprite {
    protected static final float V_LEN = 0.01f;

    protected Vector2 v;
    protected Vector2 v0 = new Vector2(0.01f, 0);
    protected Sound bulletSound;

    public Ship(TextureRegion _region, int _row, int _col, int _frames, Sound _bulletSound) {
        super(_region, _row, _col, _frames);
        this.v = new Vector2();
        this.bulletSound = _bulletSound;
    }

    @Override
    public abstract void touchDown(Vector2 _touch, int pointer, int button);

}
