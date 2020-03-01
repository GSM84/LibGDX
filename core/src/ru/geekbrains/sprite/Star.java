package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;

public class Star extends Sprite {

    public static final float STAR_HEIGHT = 0.005f;

    private final Vector2 v;
    private Rect  worldBounds;

    private float animatedTimer;
    private float animatedInterval = 1f;

    public Star(TextureAtlas _atlas) {
        super(_atlas.findRegion("star"));
        v = new Vector2().set(Rnd.nextFloat(-0.005f, 0.005f), Rnd.nextFloat(-0.2f, -0.001f));
        animatedTimer = Rnd.nextFloat(0, 1f);
    }

    @Override
    public void resize(Rect worldBuonds) {
        setHeightProportion(STAR_HEIGHT);
        float posX = Rnd.nextFloat(worldBuonds.getLeft(), worldBuonds.getRight());
        float posY = Rnd.nextFloat(worldBuonds.getBottom(), worldBuonds.getTop());
        pos.set(posX, posY);
        this.worldBounds = worldBuonds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(v, delta);
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if(getLeft() > worldBounds.getRight()){
            setRight(worldBounds.getLeft());
        }
        if(getTop() < worldBounds.getBottom()){
            setBottom(worldBounds.getTop());
        }

        animatedTimer += delta;
        if (animatedTimer >= animatedInterval){
            animatedTimer = 0;
            setHeightProportion(STAR_HEIGHT);
        } else {
            setHeightProportion(getHeight() + 0.0001f);
        }
    }
}
