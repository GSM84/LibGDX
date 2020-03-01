package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Background extends Sprite {
    public Background(Texture _region) {
        super(new TextureRegion(_region));
    }

    @Override
    public void resize(Rect worldBuonds) {
        super.resize(worldBuonds);
        setHeightProportion(1f);
        this.pos.set(worldBuonds.pos);
    }

    @Override
    public void update(float delta) {

    }
}
