package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 19.3.2018.
 */

public class Eye extends GameObject {
    boolean theEye;
    final float UPLIMIT=5;
    final float DOWNLIMIT=-5;
    final float SPEED=2;
    Vector2 difference;
    boolean mover=false;
    public Eye(String name,float width, float height, int color, Vector2 position, Scene scene,boolean theEye){
        super(new Texture(name),width,height,color,position,scene);

        this.theEye=theEye;
        createBody(size,true);
    }
    public void move(){
        if(theEye) {
            float distance = 1000;
            Vector2 relation = Vector2.Zero;
            for (Food food : scene.foods) {
                if ((scene.gameMode==0&&food.colorNumber == scene.currentColorToCollect)||(scene.gameMode==1&&food.shape==scene.currentShapeToCollect)||(scene.gameMode==2&&food.shape==scene.currentShapeToCollect&&scene.currentColorToCollect==food.colorNumber)) {
                    float foodDistance=(float)Math.hypot(food.position.x-scene.player.position.x, food.position.y-scene.player.position.y);
                    if (distance > foodDistance) {
                        distance = foodDistance;
                        relation = new Vector2((food.position.x-scene.player.position.x) / distance, (food.position.y-scene.player.position.y) / distance);
                    }
                }
            }
            if (distance != 1000) {
                body.setLinearVelocity(new Vector2(relation.x*SPEED, relation.y*SPEED).add(scene.player.body.getLinearVelocity().x/2,scene.player.body.getLinearVelocity().y/2));
            }
        }
    }
    public void draw(SpriteBatch batch){
        batch.draw(sprite, body.getPosition().x - size.x / 2, body.getPosition().y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
    }
}
