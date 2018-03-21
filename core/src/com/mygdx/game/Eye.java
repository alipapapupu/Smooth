package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 19.3.2018.
 */

public class Eye extends GameObject {
    public Eye(String name,float width, float height, int color, Vector2 position, Game game){
        super(new Texture(name),width,height,color,position,game);

        createBody(size,true);
    }
}
