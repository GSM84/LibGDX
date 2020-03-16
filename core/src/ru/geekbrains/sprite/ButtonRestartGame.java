package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.ScaledButton;
import ru.geekbrains.math.Rect;
import ru.geekbrains.screen.GameScreen;

public class ButtonRestartGame extends ScaledButton {

    private GameScreen gameScreen;

    public ButtonRestartGame(TextureAtlas _atlas, GameScreen _gameScreen) {
        super(_atlas.findRegion("button_new_game"));
        this.gameScreen = _gameScreen;
    }

    @Override
    public void resize(Rect worldBuonds) {
        super.resize(worldBuonds);setHeightProportion(0.07f);
        setBottom(0.01f);
    }

    @Override
    public void action() {
        gameScreen.restartGame();
    }

    @Override
    public void update(float delta) {

    }
}
