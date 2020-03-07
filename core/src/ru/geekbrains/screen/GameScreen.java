package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.base.BaseScreen;
import ru.geekbrains.math.Rect;
import ru.geekbrains.pool.BulletPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.Star;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 128;

    private Star stars[] = new Star[STAR_COUNT];

    private MainShip     mainShip;

    private BulletPool bulletPool;
    private Sound      bulletSound;

    private TextureAtlas atlas;

    private Texture bg;
    private Background background;

    @Override
    public void show() {
        super.show();

        bg         = new Texture("planet4.jpg");
        background = new Background(bg);

        atlas      = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));

        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(atlas);
        }

        bulletPool  = new BulletPool();
        bulletSound = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        mainShip    = new MainShip(atlas, bulletPool, bulletSound);

    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyed();
        drow();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].resize(worldBounds);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        bulletSound.dispose();

        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        mainShip.keyTyped(character);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        mainShip.touchDown(touch, pointer, button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        return super.touchUp(touch, pointer, button);
    }

    private void update(float delta){
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].update(delta);
        }
        mainShip.update(delta);
        bulletPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyedActiveObjects();
    }

    private void drow(){
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background.drow(batch);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].drow(batch);
        }
        mainShip.drow(batch);
        bulletPool.drowAcriveObjects(batch);
        batch.end();
    }
}
