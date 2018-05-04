package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Created by Severi on 9.4.2018.
 */

public class MiniScene {
    ArrayList<Button> buttons=new ArrayList<Button>();
    ArrayList<Text> texts=new ArrayList<Text>();
    ArrayList<Object> images=new ArrayList<Object>();
    OrthographicCamera camera;

    MiniScene (OrthographicCamera camera){
        this.camera=camera;
    }
    void draw(SpriteBatch batch, ShapeRenderer shapeRenderer){

        for (Button button:buttons) {
            button.draw(shapeRenderer);
            button.pressed(camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)));
        }
        for(Text text : texts){
            text.draw(batch);
        }
        for(Object object:images){
            object.draw(batch);
        }
    }
}
