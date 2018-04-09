package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.text.DecimalFormat;

/**
 * Created by Severi on 16.3.2018.
 */

class PlayerMove{
    Vector2 zeroPoint = Vector2.Zero;
    Scene scene;
    final float radiusX = 0.6f;
    final float radiusY = 0.5f;
    final float speed = 6;
    final float error = 0.01f;
    final int SIZE = 2;
    final float MAX = 3;
    int which = 0;
    Vector2 point;
    Vector2 direction;
    float[] maxBorder = new float[]{0, 0, 0, 0};
    float[] testBorder=new float[]{0,0,0,0};
    Vector2[] average=new Vector2[5];
    
    Game game;
    GameObject center;
    public float[][] allBorder = new float[SIZE + 1][4];
    private static DecimalFormat df2 = new DecimalFormat(".#");

    public PlayerMove(GameObject center, Game game) {
        this.game=game;
        this.center=center;
        direction=new Vector2(0,0);
        empty();
        center();
    }

    public void empty() {
        for (int i = 0; i < allBorder.length; i++) {
            for (int o = 0; o < allBorder[i].length; o++) {
                allBorder[i][o] = 0;
            }
        }
        for(int i = 0; i < average.length; i++){
            average[i]=new Vector2(0,0);
        }

    }
    void center(){
        zeroPoint = new Vector2(Gdx.input.getAccelerometerY(), (Gdx.input.getAccelerometerZ ()));
    }

    public boolean grid() {
        //empty();
        getPoint();
        //test();
        if (point.y > radiusY || point.y < -radiusY) {
            if (point.y > allBorder[which][0]) {
                allBorder[which][0] = point.y;
                //angle(true,true);
            } else if (point.y < allBorder[which][2]) {
                allBorder[which][2] = point.y;
                //angle(true,false);
            }
        } else {
            point.y = 0;
        }
        if (point.x > radiusX || point.x < -radiusX) {
            if (point.x > allBorder[which][1]) {
                allBorder[which][1] = point.x;
                //angle(false,true);
            } else if (point.x < allBorder[which][3]) {
                allBorder[which][3] = point.x;
                //angle(false,false);
            }
        } else {
            point.x = 0;
        }
        boolean cont = true;
        for (int i = 0; i < allBorder[which].length; i++) {
            if (allBorder[which][i] == 0) {
                cont = false;
            }
        }
        if (cont && point.x == 0) {
            which++;
            if (which == allBorder.length) {
                for (int i = 1; i < allBorder.length; i++) {
                    for (int o = 0; o < maxBorder.length; o++) {
                        maxBorder[o] += allBorder[i][o]/SIZE;
                    }
                }

                game.calibrate=false;
                return true;
            }
        }
        center.position=new Vector2( Float.parseFloat(df2.format(point.x/10))*2,Float.parseFloat(df2.format(point.y/10))*2);
        center.position=new Vector2( point.x/5,point.y/5);
        return false;
    }

    public Vector2 getPosition() {
        getPoint();
        Vector2 position = new Vector2(0, 0);
        if (point.x >= radiusX) {
            position.x = Math.min(MAX, point.x / maxBorder[1]);
        } else if (point.x <= -radiusX) {
            position.x = Math.max(-MAX, point.x / -maxBorder[3]);
        }
        if (point.y >= radiusY) {
            position.y = Math.min(MAX, point.y / maxBorder[0]);
        } else if (point.y <= -radiusY) {
            position.y = Math.max(-MAX, point.y / -maxBorder[2]);
        }
        position.x *= speed;
        position.y *= speed;
        return regulate(position);
    }

    public float getRotation() {
        float rotation= MathUtils.atan2(point.y, point.x) * MathUtils.radiansToDegrees;
        return rotation-rotation%4;
    }

    void getPoint() {
        for(int i = 1; i < average.length; i++){
            average[i]=average[i-1];
        }
        average[0]=new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, (Gdx.input.getAccelerometerZ ()) - zeroPoint.y);
        Vector2 ave=new Vector2(0,0);
        for(int i = 0; i < average.length; i++){
            ave.x+=average[i].x/average.length;
            ave.y+=average[i].y/average.length;
        }
        average[0]=ave;
        ave=new Vector2(0,0);
        for(int i = 0; i < average.length; i++){
            ave.x+=average[i].x/average.length;
            ave.y+=average[i].y/average.length;
        }
        point = ave;
    }

    Vector2 regulate(Vector2 position){
        double distance=Math.hypot(position.x,position.y);
        if(distance>speed) {
            Vector2 relation = new Vector2((float) (position.x/distance), (float) (position.y/distance));
            return new Vector2(relation.x*speed, relation.y*speed);
        }
        return position;
    }

    public void test(){
        if(point.x>testBorder[1]){
            testBorder[1]=point.x;
        }else if(point.x<testBorder[3]){
            testBorder[3]=point.x;
        }
        if(point.y>testBorder[0]){
            testBorder[0]=point.y;
        }else if(point.y<testBorder[2]){
            testBorder[2]=point.y;
        }
        if (Gdx.input.isTouched()) {
            Gdx.app.log("Big X",testBorder[1]+"");
            Gdx.app.log("Small X",testBorder[3]+"");
            Gdx.app.log("Big Y",testBorder[0]+"");
            Gdx.app.log("Small Y",testBorder[2]+"");
            testBorder=new float[]{0,0,0,0};
            Gdx.app.log("ZeroPoint",zeroPoint+"");
            getPoint();
            Gdx.app.log("Point",point+"");
            Gdx.app.log("Accelerometer",""+new Vector2(Gdx.input.getAccelerometerY(), (-Gdx.input.getAccelerometerZ())));
            Gdx.app.log("Calculation",""+new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, (-Gdx.input.getAccelerometerZ ()) - zeroPoint.y));
        }
    }
}