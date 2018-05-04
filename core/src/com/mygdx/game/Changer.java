package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Severi on 6.4.2018.
 */

public class Changer {
    float time;
    int maxTime;
    float fromZoom;
    float zoom;
    float toZoom;

    Color fromColor;
    Color color;
    Color toColor;

    int mode;
    Changer(float zoom, int newZoomTime){
        fromZoom=zoom;
        this.zoom=zoom;
        toZoom=zoom;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=0;
    }
    Changer(Color color, int newZoomTime){
        fromColor=color;
        this.color=color;
        toColor=color;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=1;
    }
    Changer(int newZoomTime){
        fromZoom=1;
        zoom=1;
        toZoom=-1;

        time=newZoomTime;
        maxTime=newZoomTime;
        mode=2;
    }
    void newTime(float newZoom){
        toZoom=newZoom;
        time=0;
    }
    void newTime(Color newColor){
        toColor=newColor;
        time=0;
    }
    void newTime(){
        time=0;
    }
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
