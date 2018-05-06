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
    final float UPLIMIT=5;
    final float DOWNLIMIT=-5;
    float speed;
    Vector2 difference;
    boolean mover=false;
    public Eye(String name,float width, float height, int color, Vector2 position, Scene scene,boolean theEye){
        super(new Texture(name),width,height,color,position,scene);

        this.theEye=theEye;
        createBody(size,true);
    }
    public void move() {
        float angle = scene.player.body.getAngle();
        center = new Vector2(originPosition.x * MathUtils.cos(angle) - originPosition.y * MathUtils.sin(angle), originPosition.y * MathUtils.cos(angle) + originPosition.x * MathUtils.sin(angle)).add(scene.player.body.getPosition());
        float distance = 1000;
        Vector2 relation = Vector2.Zero;
        //if(theEye) {
            for (Food food : scene.foods) {
                if ((scene.gameMode == 0 && food.colorNumber == scene.currentColorToCollect) || (scene.gameMode == 1 && food.shape == scene.currentShapeToCollect) || (scene.gameMode == 2 && food.shape == scene.currentShapeToCollect && scene.currentColorToCollect == food.colorNumber)) {
                    Vector2 difference = food.position.cpy().sub(center);
                    float foodDistance = (float) Math.hypot(difference.x, difference.y);
                    if (distance > foodDistance) {
                        distance = foodDistance;
                        relation = new Vector2(difference.x / distance * speed, difference.y / distance * speed);
                    }
                }
            }
        /*}else{
            distance=0;
            Gdx.app.log("",forwardEye+"");
            Vector2 difference=forwardEye.body.getPosition().cpy().sub(center);
            relation = new Vector2(difference.x / distance * speed, difference.y / distance * speed);

        }*/
        body.setTransform(relation.add(center), scene.player.body.getAngle());
    }
    public void draw(SpriteBatch batch){
        batch.draw(sprite, body.getPosition().x - size.x / 2, body.getPosition().y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
    }
}
