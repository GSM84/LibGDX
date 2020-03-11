package ru.geekbrains.pool;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.sprite.Explosion;

public class ExplosionPool extends SpritesPool<Explosion> {

    private TextureAtlas atlas;
    private Sound        exposionSound;

    public ExplosionPool(TextureAtlas atlas, Sound _explosionSound) {
        this.atlas         = atlas;
        this.exposionSound = _explosionSound;
    }

    @Override
    protected Explosion newObject() {
        return new Explosion(atlas, exposionSound);
    }
}
