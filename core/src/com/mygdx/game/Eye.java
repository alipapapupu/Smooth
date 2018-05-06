package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 19.3.2018.
 */

public class Eye extends GameObject {
    Eye forwardEye;
    boolean theEye;
    Vector2 center;
    Vector2 originPosition;
    float speed;
    int side;
    public Eye(String name,float width, float height, int color, Vector2 position, Scene scene,boolean theEye){
        super(new Texture(name),width,height,color,position,scene);

        this.theEye=theEye;
    }
    public void move() {
        float angle = scene.player.body.getAngle();
        center = new Vector2(originPosition.x * MathUtils.cos(angle) - originPosition.y * MathUtils.sin(angle), originPosition.y * MathUtils.cos(angle) + originPosition.x * MathUtils.sin(angle)).add(scene.player.body.getPosition());

        Vector2 relation = Vector2.Zero;
        float distance = 10000;
        for (Food food : scene.foods) {
            if ((scene.gameMode == 0 && food.colorNumber == scene.currentColorToCollect) || (scene.gameMode == 1 && food.shape == scene.currentShapeToCollect) || (scene.gameMode == 2 && food.shape == scene.currentShapeToCollect && food.colorNumber == scene.currentColorToCollect)) {
                Vector2 difference = food.position.cpy().sub(center);
                float foodDistance = (float) Math.hypot(difference.x, difference.y);
                if (distance > foodDistance) {
                    distance = foodDistance;
                    relation = new Vector2(difference.x / distance * speed, difference.y / distance * speed);
                }
            }
        }
        if(relation.isZero()){
            Vector2 newRadius=new Vector2(side*2,5);
            Vector2 difference=new Vector2(newRadius.x * MathUtils.cos(angle) - newRadius.y * MathUtils.sin(angle), newRadius.y * MathUtils.cos(angle) + newRadius.x * MathUtils.sin(angle));
            float newDistance=(float)Math.hypot(difference.x,difference.y);
            relation=new Vector2(difference.x/newDistance*speed,difference.y/newDistance*speed);
        }
        Vector2 newPosition = relation.add(center);
        if(!theEye) {
            rotation = MathUtils.atan2(newPosition.y - forwardEye.position.y, newPosition.x - forwardEye.position.x) * MathUtils.radiansToDegrees - 90;
        }
        position = newPosition;
    }
    @Override
    void draw(SpriteBatch batch){
        batch.draw(sprite, position.x - size.x / 2, position.y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, rotation, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
    }
}
