package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Severi on 6.4.2018.
 */

public class Button extends Object {
    static final int CIRCLE=0,BOX=1,ELLIPSE=2,CONE=3,LINE=4,ARC=5,POLYGON=6, TEXTURE=7;
    int shape;
    int action;
    int type;
    int which;
    Text text;
    Game game;
    OrthographicCamera camera;
    Vector2 startPosition;
    Scene scene;
    public Button(String tex, String text, float rotation, float x, float y, float sX, float sY, int type, int shape, int action, int which, Color color, Game game,Scene scene) {
        super(tex, rotation, x, y, sX, sY, color);
        camera = scene.fontCamera;
        size = new Vector2(sX, sY);
        this.shape = shape;
        this.type = type;
        this.action = action;
        this.game = game;
        this.which = which;
        this.scene = scene;
        startPosition = position;
        if (text !=null) {
            this.text = new Text(text, false, 0, position.x - size.x / 2, position.y, size.x, Align.center, size.x / 380, size.y / 380, Color.BLACK, game.font, camera);
        }
    }

    public void draw(ShapeRenderer rend){
        position=new Vector2(startPosition.x+camera.position.x,startPosition.y+camera.position.y);
        rend.begin(ShapeRenderer.ShapeType.Filled);
        rend.setColor(color);
        switch (shape) {
            case CIRCLE:
                rend.circle(position.x-size.x/2, position.y-size.y/2, size.x / 2);
                break;
            case BOX:
                rend.rect(position.x-size.x/2, position.y-size.y/2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, rotation);
                break;
            case ELLIPSE:
                rend.ellipse(position.x-size.x/2, position.y-size.y/2, size.x,size.y,rotation);
                break;
            case CONE:
                rend.cone(position.x-size.x/2, position.y-size.y/2, 0,size.x/2,size.y);
                break;
            case LINE:
                rend.line(position,size);
                break;
            case ARC:
                rend.arc(position.x-size.x/2, position.y-size.y/2, size.x / 2,size.y,rotation);
                break;
            case TEXTURE:
                rend.end();
                game.batch.setColor(color);
                game.batch.begin();
                game.batch.draw(tex,position.x,position.y,size.x/2,size.y/2,size.x,size.y,1,1,rotation,0,0,tex.getWidth(),tex.getHeight(),false,false);
                game.batch.end();
        }
        rend.setColor(Color.WHITE);
        if(rend.isDrawing()) {
            rend.end();
        }
        if(text!=null) {
            text.draw(game.batch);
        }
    }

    public void pressed(Vector3 mousePosition){
        if((Gdx.input.justTouched()&&type==0)||type==1) {
            if (position.x + size.x / 2 > mousePosition.x && position.x - size.x / 2 < mousePosition.x&&
                    position.y + size.y / 2 > mousePosition.y && position.y - size.y / 2 < mousePosition.y) {
                action();
            }
        }
    }

    void action(){
        if(action==0){
            game.set(which);
        }else if(action==1) {
            scene.set(which);
        }else if(action == 2){
            game.calibrate=true;
            scene.player.ready=false;
            scene.player.movement.empty();
            scene.player.movement.center();
        }
    }
}