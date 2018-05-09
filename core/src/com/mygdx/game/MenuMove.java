package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

/**
 * Main menu movement.
 */
public class MenuMove extends PlayerMove {

    /**
     * Constructor for main menu movement.
     * @param center game object.
     * @param game main game class.
     */
    public MenuMove(GameObject center, Game game) {
        super(center, game);
        maxBorder = new float[]{1, 1, -1, -1};
        zeroPoint=Vector2.Zero;
    }

    /**
     * Override for calibration grid.
     * @return returns true.
     */
    @Override
    public boolean grid(){
        game.calibrate=false;
        return true;
    }

    /**
     * Movement point for ai movement.
     */
    @Override
    void getPoint() {
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
        if(distance!=1000) {
            point = new Vector2(relation.x * speed, relation.y * speed);
        }
        else{
            point=new Vector2((scene.player.body.getLinearVelocity().x+(float)Math.random()*speed*2-speed)/2,(scene.player.body.getLinearVelocity().y+(float)Math.random()*speed*2-speed)/2);
        }
    }
}
