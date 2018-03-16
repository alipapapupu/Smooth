package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by Severi on 16.3.2018.
 */


public class Movement{
    int min;
    int max;
    Random r=new Random();

    Movement(int min, int max){
        this.min=min;
        this.max=max;
    }

    public Vector2 getPosition() {
        Vector2 move=new Vector2(0,0);
        move.x=r.nextFloat()*(max-min)+min;
        move.y=r.nextFloat()*(max-min)+min;
        return move;
    }

    public float getRotation(Vector2 position, Vector2 newPos) {
        return MathUtils.atan2(position.y-newPos.y, position.x-newPos.x) * MathUtils.radiansToDegrees;
    }
}
