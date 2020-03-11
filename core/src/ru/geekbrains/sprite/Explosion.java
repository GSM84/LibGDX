package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;

public class Explosion extends Sprite {

    private float animatedInterval = 0.017f;
    private float animatedTimer;
    private Sound explosionSound;

    public Explosion(TextureAtlas _atlas, Sound _explosionSound) {
        super(_atlas.findRegion("explosion"), 9, 9, 74);
        this.explosionSound = _explosionSound;
    }

    @Override
    public void update(float delta) {
        animatedTimer += delta;
        if (animatedTimer > animatedInterval){
            animatedTimer = 0f;
            if(++frame == regions.length){
                destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        frame = 0;
    }

    public void set(float _height, Vector2 _position){
        this.pos.set(_position);
        setHeightProportion(_height);
        this.explosionSound.play(0.2f);
    }
}
