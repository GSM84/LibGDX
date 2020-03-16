package ru.geekbrains.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.logging.Level;

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
import ru.geekbrains.sprite.TrackingStar;
import ru.geekbrains.utils.EnemiesEmitter;
import ru.geekbrains.utils.Font;

public class GameScreen extends BaseScreen {

    private static final int    STAR_COUNT = 128;
    private static final float  FONT_SIZE  = 0.02f;
    private static final String FRAGS      = "Frags: ";
    private static final String HP         = "HP: ";
    private static final String LEVEL      = "Level: ";
    private static final float  PADDING    = 0.01f;

    private enum State{PLAYING, GAME_OVER, PAUSE}

    private TrackingStar stars[] = new TrackingStar[STAR_COUNT];

    private MainShip          mainShip;

    private BulletPool        bulletPool;
    private ExplosionPool     explosionPool;
    private EnemyPool         enemyPool;
    private EnemiesEmitter    enemiesEmitter;

    private Sound             bulletSound;
    private Sound             enemyBullet;
    private Sound             explosionSound;

    private State             state;
    private State             prevState;

    private MessageGameOver   msgGameOver;
    private ButtonRestartGame restartGame;

    private TextureAtlas      atlas;

    private Texture           bg;
    private Background        background;

    private Font              font;

    private int               frags;
    private static int        level;
    private StringBuilder     sbFrags;
    private StringBuilder     sbHp;
    private StringBuilder     sbLevel;

    @Override
    public void show() {
        super.show();

        bg         = new Texture("planet4.jpg");
        background = new Background(bg);

        atlas      = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));



        bulletSound    = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        enemyBullet    = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        bulletPool     = new BulletPool();
        explosionPool  = new ExplosionPool(atlas, explosionSound);
        enemyPool      = new EnemyPool(bulletPool, enemyBullet, worldBounds, explosionPool);
        enemiesEmitter = new EnemiesEmitter(atlas, enemyPool, worldBounds);

        mainShip       = new MainShip(atlas, bulletPool, bulletSound, explosionPool);

        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new TrackingStar(atlas, mainShip.getV());
        }

        frags          = 0;
        level          = 1;

        sbFrags        = new StringBuilder();
        sbHp           = new StringBuilder();
        sbLevel        = new StringBuilder();

        font           = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
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
        font.dispose();

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
            enemiesEmitter.generate(delta, frags);
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
        printInfo();
        batch.end();
    }

    private void printInfo(){
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft()+ PADDING, worldBounds.getTop() - PADDING);
        font.draw(batch, sbHp.append(HP).append((mainShip.getHp() >= 0)?mainShip.getHp():0), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(level), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
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
                    if (enemy.isDestroyed()){
                        frags++;
                    }
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
                frags++;
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
        frags = 0;
        level = 1;
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();
        mainShip.restart();
    }

    public static void setLevel(int _level) {
        level = _level;
    }
}
