package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Changer class for time, zoom, color and game mode.
 */
public class Changer {

    /**
     * Variable for time.
     */
    float time;

    /**
     * Maximum time limit.
     */
    int maxTime;

    /**
     * Former zoom amount.
     */
    float fromZoom;

    /**
     * Zoom amount.
     */
    float zoom;

    /**
     * New zoom amount.
     */
    float toZoom;

    /**
     * Former color.
     */
    Color fromColor;

    /**
     * Current color.
     */
    Color color;

    /**
     * New color.
     */
    Color toColor;

    /**
     * Helper variable.
     */
    int mode;

    /**
     * Changer for time and zoom.
     * @param zoom current zoom value.
     * @param newZoomTime zoom time value to be.
     */
    Changer(float zoom, int newZoomTime){
        fromZoom=zoom;
        this.zoom=zoom;
        toZoom=zoom;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=0;
    }

    /**
     * Changer for color and zoom time.
     * @param color color.
     * @param newZoomTime zoom time value to be.
     */
    Changer(Color color, int newZoomTime){
        fromColor=color;
        this.color=color;
        toColor=color;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=1;
    }

    /**
     * Changer for zoom time.
     * @param newZoomTime zoom time to be.
     */
    Changer(int newZoomTime){
        fromZoom=1;
        zoom=1;
        toZoom=-1;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=2;
    }

    /**
     * Resets time and sets new zoom amount.
     * @param newZoom new zoom amount.
     */
    void newTime(float newZoom){
        toZoom=newZoom;
        time=0;
    }

    /**
     * Resets time amount and changes color.
     * @param newColor new color.
     */
    void newTime(Color newColor){
        toColor=newColor;
        time=0;
    }

    /**
     * Resets time amount.
     */
    void newTime(){
        time=0;
    }

    /**
     * Updates changer values.
     */
    void next(){
        if(time<=maxTime){
            if(mode==0) {
                fromZoom = MathUtils.lerp(fromZoom, toZoom, time / maxTime);

            }else if(mode==1){
                Color backup=fromColor.cpy();
                color=fromColor.lerp(toColor,time/maxTime);
                if(time<maxTime) {
                    fromColor=backup;
                }
            }else if(mode==2){
                zoom=Math.abs(MathUtils.lerp(fromZoom,toZoom,time/maxTime));
            }
            time++;
        }
    }
}
