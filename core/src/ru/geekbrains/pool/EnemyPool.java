package ru.geekbrains.pool;

import com.badlogic.gdx.audio.Sound;

import ru.geekbrains.base.SpritesPool;
import ru.geekbrains.math.Rect;
import ru.geekbrains.sprite.Enemy;

public class EnemyPool extends SpritesPool<Enemy> {

    private BulletPool    bulletPool;
    private Sound         shootSound;
    private Rect          worldBounds;
    private ExplosionPool explosionPool;

    public EnemyPool(BulletPool _bulletPool,  Sound _shootSound, Rect _worldBounds, ExplosionPool _explosionPool) {
        this.bulletPool    = _bulletPool;
        this.shootSound    = _shootSound;
        this.worldBounds   = _worldBounds;
        this.explosionPool = _explosionPool;
    }

    @Override
    protected Enemy newObject() {
        return new Enemy(bulletPool, shootSound, worldBounds, explosionPool);
    }
}
