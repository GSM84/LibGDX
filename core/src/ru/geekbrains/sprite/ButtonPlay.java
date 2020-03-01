package ru.geekbrains.sprite;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonPlay extends ScaledButton {

    public static final float PADDING = 0.05f;

    private final Game game;

    public ButtonPlay(TextureAtlas _atlas, Game _game) {
        super(_atlas.findRegion("btPlay"));
        this.game = _game;
    }

    @Override
    public void resize(Rect worldBuonds) {
        setHeightProportion(0.25f);
        setLeft(worldBuonds.getLeft()     + PADDING);
        setBottom(worldBuonds.getBottom() + PADDING);
    }

    @Override
    public void action() {
        game.setScreen(new GameScreen());
    }

    @Override
    public void update(float delta) {

    }
}
