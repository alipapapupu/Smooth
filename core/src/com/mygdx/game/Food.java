package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Food - game object.
 */
public class Food extends GameObject{

    /**
     * Foods rotation movement speed.
     */
    float rotationSpeed;

    /**
     * Time variable.
     */
    float time=0;

    /**
     * Maximum time limit.
     */
    float maxTime=20;

    /**
     * Vector2 coordinates for end position.
     */
    Vector2 end;

    /**
     * Vector2 coordinates for start position.
     */
    Vector2 start;

    /**
     * Vector2 coordinates for direction.
     */
    Vector2 direction;

    /**
     * Helper variable for object density.
     */
    float density;

    /**
     * Constructor for food game object.
     * @param position vector2 coordinates for food position.
     * @param scene main game scene.
     */
    Food(Vector2 position, Scene scene){
        super(null,0.001f,0.001f, -1,position,scene);
        createBody(size,true);
        density=body.getFixtureList().first().getDensity();
        rotationSpeed=(float)(Math.random()*10-5);
        start=newDirection();
        end=start;
        direction=new Vector2(0,0);
    }

    /**
     * Food movement.
     */
    public void move(){
        body.setAngularVelocity(rotationSpeed);
        time++;
        if(time == maxTime*2) {
            time=0;
            start=end;
            end=newDirection();
        }else if(time<=maxTime){
            direction.x=MathUtils.lerp(start.x,end.x,time/maxTime);
            direction.y=MathUtils.lerp(start.y,end.y,time/maxTime);
        }
        body.applyForceToCenter(direction, true);
        position=body.getPosition();
    }

    /**
     * Defines new food direction coordinates.
     * @return vector2 coordinates for new food direction.
     */
    Vector2 newDirection(){
        return new Vector2((float)(Math.random()*1-0.5)*density,(float)(Math.random()*1-0.5)*density);
    }

    /**
     * Resets food object.
     */
    void foodReset(){
         position=scene.newFoodPosition();
         body.setTransform(position,rotation);
         newFoodTexture();
         colorNumber=randomColor();
         color=colors[colorNumber].cpy();
    }
}