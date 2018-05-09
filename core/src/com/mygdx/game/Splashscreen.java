package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Before main menu splash screens.
 */
public class Splashscreen extends Scene {

    /**
     * Timer for screen changing.
     */
    Changer timer;

    /**
     * Color to change from.
     */
    Color from=new Color(1,1,1,0);

    /**
     * Color to change to.
     */
    Color to=new Color(1,1,1,1);

    /**
     * Helper variable for modes.
     */
    int mode=1;

    /**
     * Constructor for splash screens.
     * @param game main game class.
     */
    public Splashscreen(Game game){
        super(-1,null,false,game);

        timer=new Changer(from,100);

        addMiniScene();
        addImage(0,"splash.png",0,0,0,0.4f,0.4f,from);
        addImage(0,"logo.png",0,0,0,0.4f,0.4f,from);
        timer.newTime(to);
    }

    /**
     * Draws the splash screen.
     */
    @Override
    public void draw(){
        super.draw();

        if(mode<4){
            miniScenes.get(0).images.get(0).color=timer.color;
            miniScenes.get(0).images.get(1).color=Color.CLEAR;
            if(Gdx.input.justTouched()&&mode!=3){
                if(mode==1){
                    float time=timer.time;
                    timer.newTime(from);
                    timer.fromColor=to;
                    timer.time=timer.maxTime-time;
                }else {
                    timer.newTime(from);
                }
                mode=3;
            }
        }else{
            miniScenes.get(0).images.get(1).color=timer.color;
            miniScenes.get(0).images.get(0).color=Color.CLEAR;
            if(Gdx.input.justTouched()&&mode!=6){
                if(mode==4){
                    float time=timer.time;
                    timer.newTime(from);
                    timer.fromColor=to;
                    timer.time=timer.maxTime-time;
                }else {
                    timer.newTime(from);
                }
                mode=6;
            }
        }

        timer.next();

        if (timer.time>timer.maxTime) {
            mode++;
            if (mode==3||mode==6) {
                timer.newTime(from);
            } else {
                if(mode==7){
                    main.set(0);
                }
                timer.newTime(to);
            }
        }
    }
}
