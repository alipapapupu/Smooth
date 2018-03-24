package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 16.3.2018.
 */

public class ComputerMove extends PlayerMove {
    final float speeda=1f;
    public ComputerMove(){
        maxBorder = new float[]{1, 1, -1, -1};
        zeroPoint=Vector2.Zero;
        getPoint();
    }
    @Override
    public boolean grid(){
        return true;
    }

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
