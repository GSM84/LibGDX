package ru.geekbrains.sprite;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;


import ru.geekbrains.base.Ship;
import ru.geekbrains.math.Rect;

public class MainShip extends Ship {

    private Vector2 touch;
    private float dist = 0;
    private Rect worldBuonds;

    public MainShip(TextureRegion _region) {
        super(_region);
        this.touch = new Vector2();
    }

    @Override
    public void touchDown(Vector2 _touch, int pointer, int button) {
        this.touch.set(touch);
        if (_touch.x > pos.x){
            v.set(V_LEN, 0f);
        } else{
            v.set(-V_LEN, 0f);
        }
        dist = _touch.sub(pos).len();
    }

    @Override
    public void update(float delta) {
        if (dist <= 0){
            return;
        }
        if(getLeft() < worldBuonds.getLeft() && v.x < 0 ) {
            return;
        }
        if(getRight() > worldBuonds.getRight() && v.x > 0 ) {
            return;
        }

        pos.add(v);
        dist -= v.len();
    }

    @Override
    public void resize(Rect worldBuonds) {
        setHeightProportion(0.2f);
        setBottom(worldBuonds.getBottom() + 0.05f);
        this.worldBuonds = worldBuonds;
    }

    @Override
    public void keyDown(int keycode) {
        if(keycode == 29){
            pos.add(v.set(-V_LEN, 0f));
        } else if (keycode == 32){
            pos.add(v.set(V_LEN, 0f));
        }
    }

}
