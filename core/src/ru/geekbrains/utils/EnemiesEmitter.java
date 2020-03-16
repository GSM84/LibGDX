package ru.geekbrains.utils;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.math.Rect;
import ru.geekbrains.math.Rnd;
import ru.geekbrains.pool.EnemyPool;
import ru.geekbrains.screen.GameScreen;
import ru.geekbrains.sprite.Enemy;

public class EnemiesEmitter {

    private static final float ENEMY_SMALL_HEIHGT           = 0.1f;
    private static final float ENEMY_SMALL_BULLET_HEIHGT    = 0.01f;
    private static final float ENEMY_SMALL_BULLET_VY        = -0.2f;
    private static final int   ENEMY_SMALL_DAMAGE           = 1;
    private static final float ENEMY_SMALL_RELOAD_INTERVAL  = 3f;
    private static final int   ENEMY_SMALL_HP               = 1;

    private static final float ENEMY_MEDIUM_HEIHGT          = 0.15f;
    private static final float ENEMY_MEDIUM_BULLET_HEIHGT   = 0.02f;
    private static final float ENEMY_MEDIUM_BULLET_VY       = -0.2f;
    private static final int   ENEMY_MEDIUM_DAMAGE          = 5;
    private static final float ENEMY_MEDIUM_RELOAD_INTERVAL = 2f;
    private static final int   ENEMY_MEDIUM_HP              = 5;

    private static final float ENEMY_LARGE_HEIHGT           = 0.2f;
    private static final float ENEMY_LARGE_BULLET_HEIHGT    = 0.04f;
    private static final float ENEMY_LARGE_BULLET_VY        = -0.2f;
    private static final int   ENEMY_LARGE_DAMAGE           = 10;
    private static final float ENEMY_LARGE_RELOAD_INTERVAL  = 1f;
    private static final int   ENEMY_LARGE_HP               = 10;

    private Rect worldBounds;

    private float generateInterval = 2f;
    private float generateTimer;

    private final TextureRegion[] enemySmallRegions;
    private final TextureRegion[] enemyMediumRegions;
    private final TextureRegion[] enemyLargeRegions;

    private final Vector2 enemySmallV  = new Vector2(0f, -0.002f);
    private final Vector2 enemyMediumV = new Vector2(0f, -0.0015f);
    private final Vector2 enemyLargeV  = new Vector2(0f, -0.001f);

    private final Vector2 entranceSpeed  = new Vector2(0f, -0.005f);

    private TextureRegion bulletRegion;

    private final EnemyPool enemyPool;

    public EnemiesEmitter(TextureAtlas _atlas, EnemyPool _enemyPool, Rect _worldBounds) {
        this.enemyPool          = _enemyPool;
        this.enemySmallRegions  = Regions.split(_atlas.findRegion("enemy0"), 1,2,2);
        this.enemyMediumRegions = Regions.split(_atlas.findRegion("enemy1"), 1,2,2);
        this.enemyLargeRegions  = Regions.split(_atlas.findRegion("enemy2"), 1,2,2);
        this.bulletRegion       = _atlas.findRegion("bulletEnemy");
        this.worldBounds        = _worldBounds;
    }

    public void generate(float delta, int _frags){
        generateTimer += delta;
        int level     = _frags / 10 + 1;
        GameScreen.setLevel(level);
        if (generateTimer >= generateInterval){
            generateTimer = 0f;
            Enemy enemy   = enemyPool.obtain();
            float type    = (float) Math.random();
            if (type < 0.5f){
                enemy.set(enemySmallRegions
                        , enemySmallV
                        , ENEMY_SMALL_BULLET_HEIHGT
                        , ENEMY_SMALL_BULLET_VY
                        , ENEMY_SMALL_DAMAGE
                        , ENEMY_SMALL_RELOAD_INTERVAL
                        , ENEMY_SMALL_HEIHGT
                        , ENEMY_SMALL_HP * level
                        , bulletRegion
                        , entranceSpeed
                );
            } else if (type < 0.8f){
                enemy.set(enemyMediumRegions
                        , enemyMediumV
                        , ENEMY_MEDIUM_BULLET_HEIHGT
                        , ENEMY_MEDIUM_BULLET_VY
                        , ENEMY_MEDIUM_DAMAGE
                        , ENEMY_MEDIUM_RELOAD_INTERVAL
                        , ENEMY_MEDIUM_HEIHGT
                        , ENEMY_MEDIUM_HP * level
                        , bulletRegion
                        , entranceSpeed
                );
            } else {
                enemy.set(enemyLargeRegions
                        , enemyLargeV
                        , ENEMY_LARGE_BULLET_HEIHGT
                        , ENEMY_LARGE_BULLET_VY
                        , ENEMY_LARGE_DAMAGE
                        , ENEMY_LARGE_RELOAD_INTERVAL
                        , ENEMY_LARGE_HEIHGT
                        , ENEMY_LARGE_HP * level
                        , bulletRegion
                        , entranceSpeed
                );
            }

            enemy.setBottom(worldBounds.getTop());
            enemy.pos.x = Rnd.nextFloat(worldBounds.getLeft() + enemy.getHalfWidth(), worldBounds.getRight() - enemy.getHalfWidth());
        }
    }
}
