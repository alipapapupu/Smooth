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
        double distance=1000;
        Vector2 relation=Vector2.Zero;
        for (Food food:scene.foods) {
            if(food.color==scene.currentColorToCollect){
                if(distance>Math.abs(Math.hypot(position.x-food.position.x,position.y-food.position.y))) {
                    distance=Math.hypot(position.x-food.position.x,position.y-food.position.y);
                    relation = new Vector2((float) (distance / food.position.x), (float) (distance / food.position.y));
                }
            }
        }
        if(distance!=1000){
            body.setLinearVelocity(new Vector2(relation.x, relation.y));
        }
    }
}
