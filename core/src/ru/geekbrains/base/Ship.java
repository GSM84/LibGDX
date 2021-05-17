package ru.geekbrains.base;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.Explosion;

public abstract class Ship extends Sprite {
    protected static final float V_LEN = 0.01f;

    protected Vector2 v;
    protected Vector2 v0 = new Vector2(0.01f, 0);

    protected Sound   bulletSound;

    protected Vector2 bulletV;
    protected Vector2 bulletPositon;

    protected BulletPool    bulletPool;
    protected ExplosionPool explosionPool;
    protected TextureRegion bulletRegion;
    protected float         bulletHeight;
    protected int           bulletDamage;

    protected final float RELOAD_ANIMATED_INTERVAL = 0.1f;
    protected  float damageAnimaterTimer           = RELOAD_ANIMATED_INTERVAL;

    protected float         shootingTimer;
    protected float         shootingInterval;
    protected int           hp;

    private boolean         isSpeedReduced = false;

    protected float dist = 0;

    protected Rect worldBuonds;

    public Ship() {
    }

    public Ship(TextureRegion _region
              , int _row
              , int _col
              , int _frames
    ) {
        super(_region, _row, _col, _frames);
        this.v            = new Vector2();
    }

    @Override
    public void touchDown(Vector2 _touch, int pointer, int button){};

    protected void shoot(){
        Bullet bullet = bulletPool.obtain();
        bullet.set(this, bulletRegion, this.bulletPositon, bulletV, bulletHeight, worldBuonds, bulletDamage);
        this.bulletSound.play(0.1f);
    }

    @Override
    public void update(float delta) {
        pos.add(v);
        dist -= v.len();

        shootingTimer += delta;
        if (shootingTimer >= shootingInterval){
            shootingTimer = 0;
            if (this.isInWorld(worldBuonds)){
                shoot();
            }
        }
        if (this.isInWorld(worldBuonds)) {
            reduceSpeed();
        }

        damageAnimaterTimer += delta;
        if(damageAnimaterTimer >= RELOAD_ANIMATED_INTERVAL){
            frame = 0;
        }
    }

    public void dispose(){
        bulletSound.dispose();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.v0.setZero();
        boom();
        this.isSpeedReduced = false;
    }

    protected void boom(){
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    private void reduceSpeed(){
        if (!this.isSpeedReduced){
            this.v.set(v0);
            this.isSpeedReduced = true;
        }
    }

    public void damage(int _damage){
        this.hp -= _damage;
        if (hp <= 0 ){
            destroy();
        }

        damageAnimaterTimer = 0f;
        frame = 1;
    }

    public int getHp() {
        return hp;
    }

    public Vector2 getV() {
        return v;
    }
}
