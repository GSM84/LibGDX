package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;

public abstract class Sprite extends Rect {

    protected float angle;
    protected float scale = 1f;
    protected TextureRegion[] regions;
    protected int frame;

    public Sprite(TextureRegion region) {
        if (region == null){
            throw new RuntimeException("Не задана текстура");
        }
        regions = new TextureRegion[1];
        regions[0] = region;
    }

    public void drow(SpriteBatch batch){
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),  // начальная позиция
                halfWidth, halfHeight,   // точка вращения
                getWidth(), getHeight(), // ширина/высота
                scale, scale,            // скалирование по осям
                angle                    // угол поворота
        );
    }

    public void setHeightProportion(float height){
        setHeight(height);
        float aspect = regions[frame].getRegionWidth() / (float) regions[frame].getRegionHeight();
        setWidth(height * aspect);


    }

    public void resize(Rect worldBuonds){};

    public void touchDown(Vector2 touch, int pointer, int button){};

    public void touchUp(Vector2 touch, int pointer, int button){};

    public void keyTyped(char character){};

    public void keyDown(int keycode){};

    // add same contructions for touchUp/touchDragged

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public abstract void  update(float delta);

}
