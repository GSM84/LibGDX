package ru.geekbrains.sprite;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.Sprite;
import ru.geekbrains.math.Rect;

public class Bullet extends Sprite {

    private Rect          worldBounds;
    private final Vector2 v;
    private int           damage;
    private Sprite        owner;

    public Bullet() {
        regions = new TextureRegion[1];
        v = new Vector2();
    }

    public void set(
            Sprite _owner
            , TextureRegion _region
            , Vector2 _pos0
            , Vector2 _v0
            , float _height
            , Rect _worldBounds
            , int _damage
    ){
        this.owner = _owner;
        this.regions[0] = _region;
        this.pos.set(_pos0);
        this.v.set(_v0);
        setHeightProportion(_height);
        this.worldBounds = _worldBounds;
        this.damage = _damage;
    }

    public void  update(float delta){
        this.pos.mulAdd(v, delta);
        if(isOutside(worldBounds)){
            destroy();
        }
    }

    public int getDamage() {
        return damage;
    }

    public Sprite getOwner() {
        return owner;
    }
}
