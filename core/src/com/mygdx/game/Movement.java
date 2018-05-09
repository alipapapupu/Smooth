package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Basic movement.
 */
public class Movement {

    /**
     * Minimum movement value.
     */
    int min;

    /**
     * Maximum movement value.
     */
    int max;

    /**
     * Random - java util.
     */
    Random r=new Random();

    /**
     * Constructor for movement.
     * @param min minimum movement value.
     * @param max maximum movement value.
     */
    Movement(int min, int max){
        this.min=min;
        this.max=max;
    }

    /**
     * Vector2 coordinates for position to move.
     * @return position to move.
     */
    public Vector2 getPosition() {
        Vector2 move=new Vector2(0,0);
        move.x=r.nextFloat()*(max-min)+min;
        move.y=r.nextFloat()*(max-min)+min;
        return move;
    }

    /**
     * Movement rotation.
     * @param position vector2 coordinates of current position.
     * @param newPos vector2 coordinates for new position.
     * @return rotation degrees.
     */
    public float getRotation(Vector2 position, Vector2 newPos) {
        return MathUtils.atan2(position.y-newPos.y, position.x-newPos.x) * MathUtils.radiansToDegrees;
    }
}
