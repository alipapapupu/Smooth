package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 6.4.2018.
 */

public class Object {
    Texture tex;
    float rotation;
    Vector2 position;
    Vector2 size;
    Color color;
    Vector2 startPosition;
    OrthographicCamera camera;


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

    void draw(SpriteBatch batch) {
        updatePos();
        batch.begin();
        batch.setColor(color);
        batch.draw(tex,position.x-size.x/2,position.y-size.y/2,size.x/2,size.y/2,size.x,size.y,1,1,rotation,0,0,tex.getWidth(),tex.getHeight(),false,false);
        batch.setColor(Color.WHITE);
        batch.end();
    }

    void updatePos(){
        position=new Vector2(startPosition.x+camera.position.x,startPosition.y+camera.position.y);
    }
}
