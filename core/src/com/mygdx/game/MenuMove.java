package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 8.4.2018.
 */

public class MenuMove extends PlayerMove {
    public MenuMove(GameObject center, Game game) {
        super(center, game);
        maxBorder = new float[]{1, 1, -1, -1};
        zeroPoint=Vector2.Zero;
    }

    @Override
    public boolean grid(){
        game.calibrate=false;
        return true;
    }

    @Override
    void getPoint() {
        float distance = 1000;
        Vector2 relation = Vector2.Zero;
        for (Food food : scene.foods) {
            if (food.color == scene.currentColorToCollect) {
                float foodDistance=(float)Math.hypot(food.position.x-scene.player.position.x, food.position.y-scene.player.position.y);
                if (distance > foodDistance) {
                    distance = foodDistance;
                    relation = new Vector2((food.position.x-scene.player.position.x) / distance, (food.position.y-scene.player.position.y) / distance);
                }
            }
        }
        if(distance!=1000) {
            point = new Vector2(relation.x * speed, relation.y * speed);
        }
        else{
            point=new Vector2((scene.player.body.getLinearVelocity().x+(float)Math.random()*speed*2-speed)/2,(scene.player.body.getLinearVelocity().y+(float)Math.random()*speed*2-speed)/2);
        }
    }
}
