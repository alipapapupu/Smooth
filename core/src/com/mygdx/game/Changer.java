package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Severi on 6.4.2018.
 */

public class Changer {
    float zoomTime;
    int maxZoomTime;
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

        zoomTime=newZoomTime;
        maxZoomTime=newZoomTime;
        mode=0;
    }
    Changer(Color color, int newZoomTime){
        fromColor=color;
        this.color=color;
        toColor=color;

        zoomTime=newZoomTime;
        maxZoomTime=newZoomTime;
        mode=1;
    }
    void newTime(float newZoom){
        toZoom=newZoom;
        zoomTime=0;
    }
    void newTime(Color newColor){
        toColor=newColor;
        zoomTime=0;
    }
    void next(){
        if(zoomTime<=maxZoomTime){
            if(mode==0) {
                fromZoom = MathUtils.lerp(fromZoom, toZoom, zoomTime / maxZoomTime);
            }else if(mode==1){
                Color backup=fromColor;
                color=fromColor.lerp(toColor,zoomTime/maxZoomTime);
                fromColor=backup;
            }
            zoomTime++;
        }
    }
}
