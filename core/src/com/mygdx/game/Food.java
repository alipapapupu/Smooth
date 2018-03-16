package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 16.3.2018.
 */


public class Food extends GameObject{

    Food(Vector2 position, Game game){
        super(null,0.001f,0.001f, (int)(Math.random()*(8-2)+2),position,(short)2,(short)1,game);
    }
}