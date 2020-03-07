package ru.geekbrains.sprite;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;

public class MainShip extends Ship {

    private boolean isPressedLeft  = false;
    private boolean isPressedRight = false;

    private       BulletPool    bulletPool;
    private       TextureRegion bulletRegion;
    private final Vector2       bulletV;
    private final Vector2       bulletPositon;

    private       float         shootingTimer;
    private       float         shootingInterval = 0.3f;

    private Vector2 touch;
    private float dist = 0;
    private Rect worldBuonds;

    public MainShip(TextureAtlas _atlas, BulletPool _bulletPool, Sound _bulletSound) {
        super(_atlas.findRegion("main_ship"), 1, 2, 2, _bulletSound);
        this.touch          = new Vector2();
        this.bulletPool     = _bulletPool;
        this.bulletRegion   = _atlas.findRegion("bulletMainShip");
        this.bulletV        = new Vector2(0, 0.5f);
        this.bulletPositon  = new Vector2();
    }

    @Override
    public void touchDown(Vector2 _touch, int pointer, int button) {
        this.touch.set(touch);
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

        pos.add(v);
        dist -= v.len();

        shootingTimer += delta;
        if (shootingTimer >= shootingInterval){
            shootingTimer = 0;
            shoot();
        } else {
            //setHeightProportion(getHeight() + 0.0001f);
        }
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

    private void shoot(){
        Bullet bullet = bulletPool.obtain();
        this.bulletPositon.set(this.pos.x, getTop() + 0.005f);
        bullet.set(this, bulletRegion, this.bulletPositon, bulletV, 0.01f, worldBuonds, 1);
        this.bulletSound.play(0.1f);
    }

}
