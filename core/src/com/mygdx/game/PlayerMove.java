package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 16.3.2018.
 */

class PlayerMove{
    Vector2 zeroPoint = Vector2.Zero;
    final float radiusX = 0.2f;
    final float radiusY = 0.2f;
    final float speed = 8;
    final float error = 0.01f;
    final int SIZE = 3;
    final float MAX = 3;
    int which = 0;
    Vector2 point;
    Vector2 direction;
    float[] maxBorder = new float[]{0, 0, 0, 0};
    float[] testBorder=new float[]{0,0,0,0};
    float testi = 0;
    public float[][] allBorder = new float[SIZE + 1][4];

    public PlayerMove() {
        getPoint();
        zeroPoint = point;
        direction=new Vector2(0,0);

        for (int i = 0; i < allBorder.length; i++) {
            for (int o = 0; o < allBorder[i].length; o++) {
                allBorder[i][o] = 0;
            }
        }
    }

    public void empty() {
        if (Gdx.input.isTouched()) {
            zeroPoint = Vector2.Zero;
            getPoint();
            zeroPoint = point;
        }

    }

    public boolean grid() {
        //empty();
        getPoint();
        //test();
        if (point.y > radiusY || point.y < -radiusY) {
            if (point.y > allBorder[which][0]) {
                Gdx.app.log("suunta", "" + 1);
                allBorder[which][0] = point.y;
                //angle(true,true);
            } else if (point.y < allBorder[which][2]) {
                Gdx.app.log("suunta", "" + 3);
                allBorder[which][2] = point.y;
                //angle(true,false);
            }
        } else {
            point.y = 0;
        }
        if (point.x > radiusX || point.x < -radiusX) {
            if (point.x > allBorder[which][1]) {
                Gdx.app.log("suunta", "" + 2);
                allBorder[which][1] = point.x;
                //angle(false,true);
            } else if (point.x < allBorder[which][3]) {
                Gdx.app.log("suunta", "" + 4);
                allBorder[which][3] = point.x;
                //angle(false,false);
            }
        } else {
            point.x = 0;
        }
        boolean cont = true;
        for (int i = 0; i < allBorder[which].length; i++) {
            if (allBorder[which][i] == 0) {
                if (testi != allBorder[which][i]) {
                }
                cont = false;
            }
        }
        if (cont && point.x == 0) {
            which++;
            if (which == allBorder.length) {
                for (int i = 1; i < allBorder.length; i++) {
                    for (int o = 0; o < maxBorder.length; o++) {
                        maxBorder[o] += allBorder[i][o];
                    }
                }
                for (int o = 0; o < maxBorder.length; o++) {
                    maxBorder[o] /= SIZE;
                }

                Gdx.app.log("suunta", "valmis");
                return true;
            }
        }

        return false;
    }/*
public void angle(boolean kumpi1, boolean kumpi2) {
	if (kumpi1) {
		if (kumpi2) {
			allBorder[which][1] = new Vector2(allBorder[which][2].x / 2, allBorder[which][0].y / 2);
			allBorder[which][7] = new Vector2(allBorder[which][6].x / 2, allBorder[which][0].y / 2);
		} else {
			allBorder[which][3] = new Vector2(allBorder[which][2].x / 2, allBorder[which][4].y / 2);
			allBorder[which][5] = new Vector2(allBorder[which][6].x / 2, allBorder[which][4].y / 2);
		}
	} else {
		if (kumpi2) {
			allBorder[which][1] = new Vector2(allBorder[which][2].x / 2, allBorder[which][0].y / 2);
			allBorder[which][3] = new Vector2(allBorder[which][2].x / 2, allBorder[which][4].y / 2);
		} else {
			allBorder[which][5] = new Vector2(allBorder[which][6].x / 2, allBorder[which][4].y / 2);
			allBorder[which][7] = new Vector2(allBorder[which][6].x / 2, allBorder[which][0].y / 2);
		}
	}
}*/

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
        return position;
    }
    /*public Vector2 getPosition(){
        getPoint();
        if (point.x >= radiusX) {
            direction.x += Math.min(MAX, point.x / maxBorder[1]);
        } else if (point.x <= -radiusX) {
            direction.x += Math.max(-MAX, point.x / -maxBorder[3]);
        }
        if (point.y >= radiusY) {
            direction.y += Math.min(MAX, point.y / maxBorder[0]);
        } else if (point.y <= -radiusY) {
            direction.y += Math.max(-MAX, point.y / -maxBorder[2]);
        }
        direction=new Vector2(MathUtils.clamp(direction.x,-radiusX,radiusX)*speed,MathUtils.clamp(direction.y,-radiusY,radiusY)*speed);
        return direction;
    }*/

    public float getRotation() {
        float rotation= MathUtils.atan2(point.y, point.x) * MathUtils.radiansToDegrees;
        return rotation-rotation%4;
    }

    void getPoint() {
        point = new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, (-Gdx.input.getAccelerometerX ()) - zeroPoint.y);
    }
    public float getRotation(Vector2 position, Vector2 newPos, float size, float rotation) {
        float rot=MathUtils.degreesToRadians*(rotation+180);
        Vector2 connectPoint=new Vector2(size*MathUtils.cos(rot) +newPos.x,size*MathUtils.sin(rot) +newPos.y);
        return MathUtils.atan2(position.y-connectPoint.y, position.x+size-connectPoint.x);
    }
    Vector2 getPosition(Vector2 newPos, float size, float rotation){
        float rot=MathUtils.degreesToRadians*(rotation+180);
        return new Vector2(size*MathUtils.cos(rot) +newPos.x-size,size*MathUtils.sin(rot) +newPos.y);
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
            Gdx.app.log("Accelerometer",""+new Vector2(Gdx.input.getAccelerometerY(), (-Gdx.input.getAccelerometerX ())));
            Gdx.app.log("Calculation",""+new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, (-Gdx.input.getAccelerometerX ()) - zeroPoint.y));
        }
    }
}