package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 19.3.2018.
 */

public class Eye extends GameObject {
    public Eye(String name,float width, float height, int color, Vector2 position, Scene scene){
        super(new Texture(name),width,height,color,position,scene);

        createBody(size,true);
    }
    public void move(){

    }
}
