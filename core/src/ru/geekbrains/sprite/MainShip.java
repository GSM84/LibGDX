package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.pool.ExplosionPool;

public class MainShip extends Ship {

    private boolean isPressedLeft  = false;
    private boolean isPressedRight = false;

    private static final int HP = 10;

    private Vector2 touch;

    public MainShip(TextureAtlas _atlas, BulletPool _bulletPool, Sound _bulletSound, ExplosionPool _explosionPool) {
        super(_atlas.findRegion("main_ship"), 1, 2, 2);
        this.touch            = new Vector2();
        this.bulletV          = new Vector2(0, 0.5f);
        this.bulletPositon    = new Vector2();
        this.shootingInterval = 0.3f;
        this.bulletHeight     = 0.01f;
        this.bulletDamage     = 1;
        this.hp               = HP;
        this.bulletSound      = _bulletSound;
        this.bulletPool       = _bulletPool;
        this.bulletRegion     = _atlas.findRegion("bulletMainShip");
        this.explosionPool    = _explosionPool;
    }

    @Override
    public void touchDown(Vector2 _touch, int pointer, int button) {
        System.out.println("main "+v0);
        this.touch.set(_touch);
        if (_touch.x > pos.x){
            v.set(v0);
        } else{
            v.set(v0).rotate(180);
        }
        dist = _touch.sub(pos).len();
    }

    @Override
    public void update(float delta) {
        if (dist <= 0){
            v.setZero();
        }
        if(getLeft() < worldBuonds.getLeft()) {
            setLeft(worldBuonds.getLeft());
            v.setZero();
        }
        if(getRight() > worldBuonds.getRight()) {
            setRight(worldBuonds.getRight());
            v.setZero();
        }

        bulletPositon.set(this.pos.x, getTop() + 0.005f);

        super.update(delta);
    }

    @Override
    public void resize(Rect worldBuonds) {
        setHeightProportion(0.15f);
        setBottom(worldBuonds.getBottom() + 0.05f);
        this.worldBuonds = worldBuonds;
    }

    @Override
    public void keyDown(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                v.set(v0).rotate(180);
                isPressedLeft = true;
            break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                v.set(v0);
                isPressedRight = true;
            break;
            case Input.Keys.SPACE:
                shoot();
            default:
        }

        dist = 1;
    }

    @Override
    public void keyUp(int keycode) {
        switch (keycode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                isPressedLeft = false;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                isPressedRight = false;
                break;
            default:
        }
        if(!isPressedLeft && !isPressedRight){
            v.setZero();
        }
    }

    @Override
    protected void shoot() {
        super.shoot();
    }

    public boolean isBulletCOllision(Rect _bullet){
        return !(_bullet.getRight() < getLeft()
                || _bullet.getLeft() > getRight()
                || _bullet.getBottom() > pos.y
                || _bullet.getTop() < getBottom());

    }

    public void restart(){
        this.isPressedLeft  = false;
        this.isPressedRight = false;
        this.hp             = HP;
        this.v0.set(0.01f, 0);
        this.dist = 0;
        this.pos.x          = worldBuonds.pos.x;
        flushDestroy();
    }
}
