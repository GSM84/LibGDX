package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class Enemy extends Ship {


    public Enemy(BulletPool _bulletPool, Sound _shootSound, Rect _worldBounds, ExplosionPool _explosionPool) {
        this.bulletPool    = _bulletPool;
        this.bulletSound   = _shootSound;
        this.v             = new Vector2();
        this.v0            = new Vector2();
        this.bulletV       = new Vector2();
        this.bulletPositon = new Vector2();
        this.worldBuonds   = _worldBounds;
        this.explosionPool = _explosionPool;
    }

    @Override
    public void update(float delta) {
        bulletPositon.set(this.pos.x, getBottom() + 0.01f);
        super.update(delta);

        if (getBottom() < worldBuonds.getBottom()){
            destroy();
        }
    }

    public void set(TextureRegion[] _regions,
                    Vector2         _v0,
                    float           _bulletHeight,
                    float           _bulletVY,
                    int             _damage,
                    float           _shootingInterval,
                    float           _height,
                    int             _hp,
                    TextureRegion   _region,
                    Vector2         _entranceSpeed
    ){
        this.regions          = _regions;
        this.bulletHeight     = _bulletHeight;
        this.bulletDamage     = _damage;
        this.shootingInterval = _shootingInterval;
        this.shootingTimer    = _shootingInterval;
        this.hp               = _hp;
        this.bulletRegion     = _region;
        setHeightProportion(_height);
        this.v0.set(_v0);
        this.bulletV.set(0, _bulletVY);
        this.v.set(_entranceSpeed);
    }

    @Override
    protected void shoot() {
        super.shoot();
    }

    public boolean isBulletCOllision(Rect _bullet){
        return !(_bullet.getRight() < getLeft()
                || _bullet.getLeft() > getRight()
                || _bullet.getBottom() > getTop()
                || _bullet.getTop() < pos.y);

    }

}
