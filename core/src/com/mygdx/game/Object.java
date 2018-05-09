package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Defines objects.
 */
public class Object {

    /**
     * Object texture.
     */
    Texture tex;

    /**
     * Object rotation.
     */
    float rotation;

    /**
     * Vector2 coordinates for object position.
     */
    Vector2 position;

    /**
     * Vector2 for objects x and y size.
     */
    Vector2 size;

    /**
     * Object color.
     */
    Color color;

    /**
     * Vector2 coordinates for objects start position.
     */
    Vector2 startPosition;

    /**
     * Orthographic camera for objects.
     */
    OrthographicCamera camera;

    /**
     * Constructor for object.
     * @param name helper variable for texture defining.
     * @param rotation object rotation.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param sX source Xcoord.
     * @param sY source Ycoord.
     * @param color object color.
     * @param camera orthographic camera for object.
     */
    public Object(String name, float rotation, float x, float y, float sX, float sY, Color color, OrthographicCamera camera) {
        if(name!=null) {
            tex = new Texture(name);
            size = new Vector2(tex.getWidth()*sX, tex.getHeight()*sY);
        }
        this.camera=camera;
        this.rotation = rotation;
        startPosition=new Vector2(x, y);
        position=startPosition;
        this.color=color;
    }

    /**
     * Draws object.
     * @param batch to render sprite.
     */
    void draw(SpriteBatch batch) {
        updatePos();
        batch.begin();
        batch.setColor(color);
        batch.draw(tex,position.x-size.x/2,position.y-size.y/2,size.x/2,size.y/2,size.x,size.y,1,1,rotation,0,0,tex.getWidth(),tex.getHeight(),false,false);
        batch.setColor(Color.WHITE);
        batch.end();
    }

    /**
     * Updating vector2 coordinates for object position.
     */
    void updatePos(){
        position=new Vector2(startPosition.x+camera.position.x,startPosition.y+camera.position.y);
    }
}
