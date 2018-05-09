package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

/**
 * Mini scenes for game transitions.
 */
public class MiniScene {

    /**
     * List for all buttons in the scene.
     */
    ArrayList<Button> buttons=new ArrayList<Button>();

    /**
     * List for all texts in the scene.
     */
    ArrayList<Text> texts=new ArrayList<Text>();

    /**
     * List for all images in the scene.
     */
    ArrayList<Object> images=new ArrayList<Object>();

    /**
     * Orthographic camera for scene.
     */
    OrthographicCamera camera;

    /**
     * Constructor for mini scene.
     * @param camera orthographic scene camera.
     */
    MiniScene (OrthographicCamera camera){
        this.camera=camera;
    }

    /**
     * Draws the scene.
     * @param batch to render sprites.
     * @param shapeRenderer to render shapes.
     */
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
