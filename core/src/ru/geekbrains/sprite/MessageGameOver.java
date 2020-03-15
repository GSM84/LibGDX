package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class MessageGameOver extends Sprite {

    public MessageGameOver(TextureAtlas _atlas) {
        super(_atlas.findRegion("message_game_over"));
    }

    @Override
    public void resize(Rect worldBuonds) {
        super.resize(worldBuonds);
        setHeightProportion(0.09f);
        setTop(0.2f);
    }

    @Override
    public void update(float delta) {

    }
}
