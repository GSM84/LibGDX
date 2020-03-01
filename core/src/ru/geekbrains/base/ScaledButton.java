package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class ScaledButton extends Sprite {

    private static final float PRESS_SCALE = 0.9f;

    private int pointer;
    private boolean isPressed;

    public ScaledButton(TextureRegion _region) {
        super(_region);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        if(isPressed || !isMe(touch)){
            return;
        }

        this.pointer   = pointer;
        this.scale     = PRESS_SCALE;
        this.isPressed = true;
    }

    @Override
    public void touchUp(Vector2 touch, int pointer, int button) {
        if(this.pointer != pointer || !isPressed){
            return;
        }
        if(isMe(touch)){
            action();
        }

        this.isPressed = false;
        this.scale     = 1f;
    }

    public abstract void action();
}
