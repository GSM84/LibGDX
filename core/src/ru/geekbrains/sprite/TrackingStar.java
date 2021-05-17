package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

public class TrackingStar extends Star {

    private final Vector2 trackingV;

    private final Vector2 sumV      = new Vector2();

    public TrackingStar(TextureAtlas _atlas, Vector2 _trackingV) {
        super(_atlas);
        this.trackingV = _trackingV;
    }

    @Override
    public void update(float delta) {
        sumV.setZero().mulAdd(trackingV, 4f).rotate(180).add(v);
        pos.mulAdd(sumV, delta);
        checkAndHandleBounds();
    }
}
