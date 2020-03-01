package ru.geekbrains.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;

public class ButtonExit extends ScaledButton {

    public static final float PADDING = 0.05f;

    public ButtonExit(TextureAtlas _atlas) {
        super(_atlas.findRegion("btExit"));
    }

    @Override
    public void resize(Rect worldBuonds) {
        setHeightProportion(0.2f);
        setRight(worldBuonds.getRight()   - PADDING);
        setBottom(worldBuonds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        Gdx.app.exit();
    }

    @Override
    public void update(float delta) {

    }
}
