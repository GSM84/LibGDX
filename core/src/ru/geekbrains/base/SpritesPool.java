package ru.geekbrains.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

public abstract class SpritesPool<T extends Sprite> {

    private final List<T> activeObjects = new ArrayList<>();
    private final List<T> freeObjects   = new ArrayList<>();

    protected abstract T newObject();

    public T obtain(){
        T object;
        if (freeObjects.isEmpty()){
            object = newObject();
        }else{
            object = freeObjects.remove(freeObjects.size() - 1);
        }

        activeObjects.add(object);

        return object;
    }

    public void updateActiveSprites(float delta){
        for(Sprite sprite : activeObjects){
            if(!sprite.isDestroyed){
                sprite.update(delta);
            }
        }
    }

    public void drowAcriveObjects(SpriteBatch _b){
        for(Sprite sprite : activeObjects){
            if(!sprite.isDestroyed){
                sprite.drow(_b);
            }
        }
    }

    private void free(T _object){
        if(activeObjects.remove(_object)){
            freeObjects.add(_object);
        }
    }

    public void freeAllDestroyedActiveObjects(){
        for(int i =0; i < activeObjects.size(); i++){
            T sprite = activeObjects.get(i);
            if(sprite.isDestroyed){
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    public void dispose(){
        activeObjects.clear();
        freeObjects.clear();
    }

    public void freeAllActiveObjects(){
        freeObjects.addAll(activeObjects);
        activeObjects.clear();
    }

}
