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
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.pool.ExplosionPool;
import ru.geekbrains.sprite.Background;
import ru.geekbrains.sprite.Bullet;
import ru.geekbrains.sprite.ButtonRestartGame;
import ru.geekbrains.sprite.Enemy;
import ru.geekbrains.sprite.MainShip;
import ru.geekbrains.sprite.MessageGameOver;
import ru.geekbrains.sprite.Star;
import ru.geekbrains.utils.EnemiesEmitter;

public class GameScreen extends BaseScreen {

    private static final int STAR_COUNT = 128;

    private enum State{PLAYING, GAME_OVER, PAUSE}

    private Star stars[] = new Star[STAR_COUNT];

    private MainShip     mainShip;

    private BulletPool     bulletPool;
    private ExplosionPool  explosionPool;
    private EnemyPool      enemyPool;
    private EnemiesEmitter enemiesEmitter;

    private Sound      bulletSound;
    private Sound      enemyBullet;
    private Sound      explosionSound;

    private State             state;
    private State             prevState;

    private MessageGameOver   msgGameOver;
    private ButtonRestartGame restartGame;

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

        bulletSound    = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        enemyBullet    = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        bulletPool     = new BulletPool();
        explosionPool  = new ExplosionPool(atlas, explosionSound);
        enemyPool      = new EnemyPool(bulletPool, enemyBullet, worldBounds, explosionPool);
        enemiesEmitter = new EnemiesEmitter(atlas, enemyPool, worldBounds);

        mainShip       = new MainShip(atlas, bulletPool, bulletSound, explosionPool);

        state          = State.PLAYING;
        msgGameOver    = new MessageGameOver(atlas);
        restartGame    = new ButtonRestartGame(atlas, this);

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
        msgGameOver.resize(worldBounds);
        restartGame.resize(worldBounds);
    }

    @Override
    public void dispose() {
        atlas.dispose();
        bg.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        bulletSound.dispose();
        enemyBullet.dispose();
        mainShip.dispose();
        explosionSound.dispose();

        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING) {
            mainShip.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (state == State.PLAYING) {
            mainShip.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (state == State.PLAYING) {
            mainShip.keyTyped(character);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING) {
            mainShip.touchDown(touch, pointer, button);
        } else if (state == State.GAME_OVER){
            restartGame.touchDown(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if (state == State.PLAYING){
            mainShip.touchDown(touch, pointer, button);
        }
        else if (state == State.GAME_OVER){
            restartGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    private void update(float delta){
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING) {
            mainShip.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(delta);
            enemiesEmitter.generate(delta);
            checkCollisions();
        }

    }

    private void freeAllDestroyed(){
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void drow(){
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        background.drow(batch);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i].drow(batch);
        }
        explosionPool.drowAcriveObjects(batch);

        if(state == State.PLAYING) {
            mainShip.drow(batch);
            bulletPool.drowAcriveObjects(batch);
            enemyPool.drowAcriveObjects(batch);
        } else if (state == State.GAME_OVER){
            msgGameOver.drow(batch);
            restartGame.drow(batch);
        }
        batch.end();

    }

    private void checkCollisions(){
        float minDist;
        // bullets
        for (Bullet bullet:bulletPool.getActiveObjects()) {
            if (bullet.getOwner() != mainShip){
                if (mainShip.isBulletCOllision(bullet)){
                    mainShip.damage(bullet.getDamage());
                    bullet.destroy();
                }
                continue;
            }

            for (Enemy enemy : enemyPool.getActiveObjects()) {
                if (enemy.isBulletCOllision(bullet)){
                    enemy.damage(bullet.getDamage());
                    bullet.destroy();
                }
            }
        }
        // ships
        for (Enemy enemy:enemyPool.getActiveObjects()) {
            minDist  = mainShip.getHalfWidth() + enemy.getHalfWidth();
            if(minDist >= mainShip.pos.dst(enemy.pos)){
                mainShip.damage(enemy.getHp());
                enemy.destroy();
            }
        }

        if (mainShip.isDestroyed()) {
            state = State.GAME_OVER;
        }
    }

    public void resume(){
        state = prevState;
    }

    public void pause(){
        prevState = state;
        state     = State.PAUSE;
    }

    public void restartGame(){
        state = State.PLAYING;
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();

        mainShip.restart();
    }
}
