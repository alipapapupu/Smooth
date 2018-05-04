package com.mygdx.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 16.3.2018.
 */


public class Food extends GameObject{

    float rotationSpeed;
    float time=0;
    float maxTime=20;
    Vector2 end;
    Vector2 start;
    Vector2 direcion;
    float density;
    Food(Vector2 position, Scene scene){
        super(null,0.001f,0.001f, -1,position,scene);
        createBody(size,true);
        density=body.getFixtureList().first().getDensity();
        rotationSpeed=(float)(Math.random()*10-5);
        start=newDirection();
        end=start;
        direcion=new Vector2(0,0);
    }
    public void move(){
        body.setAngularVelocity(rotationSpeed);
        time++;
        if(time == maxTime*2) {
            time=0;
            start=end;
            end=newDirection();
        }else if(time<=maxTime){
            direcion.x=MathUtils.lerp(start.x,end.x,time/maxTime);
            direcion.y=MathUtils.lerp(start.y,end.y,time/maxTime);
        }
        body.applyForceToCenter(direcion, true);
        position=body.getPosition();
    }
    Vector2 newDirection(){
        return new Vector2((float)(Math.random()*1-0.5)*density,(float)(Math.random()*1-0.5)*density);
    }
    void foodReset(){
         position=scene.newFoodPosition();
         body.setTransform(position,rotation);
         newFoodTexture();
         colorNumber=randomColor();
         color=colors[colorNumber].cpy();
    }
}