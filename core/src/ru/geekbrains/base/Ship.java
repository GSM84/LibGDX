package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


public abstract class Ship extends Sprite {
    protected static final float V_LEN = 0.01f;

    protected Vector2 v;

    public Ship(TextureRegion _region) {
        super(_region);
        this.v = new Vector2();
    }

    @Override
    public abstract void touchDown(Vector2 _touch, int pointer, int button);

}
