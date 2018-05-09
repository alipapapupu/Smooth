package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Computer movement class.
 */
public class ComputerMove extends PlayerMove {
    /**
     * Computer moving speed.
     */
    final float speeda=1f;

    /**
     * Constructor for computer movement.
     * @param center game object.
     * @param game Game - class.
     */
    public ComputerMove(GameObject center, Game game){
        super(center,game);
        maxBorder = new float[]{1, 1, -1, -1};
        zeroPoint=Vector2.Zero;
        getPoint();
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
     * Movement point.
     */
    @Override
    void getPoint(){
        point=new Vector2(0,0);
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            point.x=speeda;
        }else if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            point.x=-speeda;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            point.y=speeda;
        }else if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            point.y=-speeda;
        }
    }
}
