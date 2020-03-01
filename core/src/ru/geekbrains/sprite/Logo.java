package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Logo extends Sprite {

    private static final float V_LEN = 0.01f;

    private Vector2 touch;
    private Vector2 v;
    float dist = 0;

    public Logo(Texture region) {
        super(new TextureRegion(region));
        touch = new Vector2();
        v     = new Vector2();
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(0.4f);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        this.touch.set(touch);
        v.set(touch.cpy().sub(pos)).setLength(V_LEN);
        dist = touch.sub(pos).len();
    }

    @Override
    public void update(float delta) {
        if (dist > 0){
            pos.add(v);
            dist -= v.len();
        }
    }
}
