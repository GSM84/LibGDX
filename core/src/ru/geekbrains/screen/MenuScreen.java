package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;

public class MenuScreen extends BaseScreen {

    private Texture img;
    private Texture background;
    private Vector2 touch;
    private Vector2 v;
    private Vector2 pos;
    private float   dist;

    @Override
    public void show() {
        super.show();
        img        = new Texture("badlogic.jpg");
        background = new Texture("planet.jpg");

        touch = new Vector2();
        v     = new Vector2();
        pos   = new Vector2();
        dist  = 0;
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //repeat while distance not finished
        if (dist > 0){
            pos.add(v);
            dist -= v.len();
        }

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.draw(img, pos.x, pos.y);
        batch.end();
    }

    @Override
    public void dispose() {
        img.dispose();
        super.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        //get click coords
        touch.set(screenX, Gdx.graphics.getHeight() - screenY);

        //calculate distance
        dist = touch.sub(pos).len();
        //define velocity
        v = touch.scl(0.01f);

        return false;
    }

}
