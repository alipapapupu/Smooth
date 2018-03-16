package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Severi on 16.3.2018.
 */

class PlayerMove{
    Vector2 zeroPoint = Vector2.Zero;
    final float sadeX = 1f;
    final float sadeY = 1f;
    final float speed = 8;
    final float error = 0.01f;
    final int SIZE = 3;
    final float MAX = 3;
    int which = 0;
    Vector2 point;
    float[] maxRajat = new float[]{0, 0, 0, 0};
    float testi = 0;
    public float[][] allRajat = new float[SIZE + 1][4];
    boolean gyroscope;

    public PlayerMove(boolean gyroscope) {
        this.gyroscope = gyroscope;
        getPoint();
        zeroPoint = point;

        for (int i = 0; i < allRajat.length; i++) {
            for (int o = 0; o < allRajat[i].length; o++) {
                allRajat[i][o] = 0;
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
        getPoint();
        if (point.y > sadeY || point.y < -sadeY) {
            if (point.y > allRajat[which][0]) {
                Gdx.app.log("suunta", "" + 1);
                allRajat[which][0] = point.y;
                //angle(true,true);
            } else if (point.y < allRajat[which][2]) {
                Gdx.app.log("suunta", "" + 3);
                allRajat[which][2] = point.y;
                //angle(true,false);
            }
        } else {
            point.y = 0;
        }
        if (point.x > sadeX || point.x < -sadeX) {
            if (point.x > allRajat[which][1]) {
                Gdx.app.log("suunta", "" + 2);
                allRajat[which][1] = point.x;
                //angle(false,true);
            } else if (point.x < allRajat[which][3]) {
                Gdx.app.log("suunta", "" + 4);
                allRajat[which][3] = point.x;
                //angle(false,false);
            }
        } else {
            point.x = 0;
        }
        boolean cont = true;
        for (int i = 0; i < allRajat[which].length; i++) {
            if (allRajat[which][i] == 0) {
                if (testi != allRajat[which][i]) {
                }
                cont = false;
            }
        }
        if (cont && point.x == 0) {
            which++;
            if (which == allRajat.length) {
                for (int i = 1; i < allRajat.length; i++) {
                    for (int o = 0; o < maxRajat.length; o++) {
                        maxRajat[o] += allRajat[i][o];
                    }
                }
                for (int o = 0; o < maxRajat.length; o++) {
                    maxRajat[o] /= SIZE;
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
			allRajat[which][1] = new Vector2(allRajat[which][2].x / 2, allRajat[which][0].y / 2);
			allRajat[which][7] = new Vector2(allRajat[which][6].x / 2, allRajat[which][0].y / 2);
		} else {
			allRajat[which][3] = new Vector2(allRajat[which][2].x / 2, allRajat[which][4].y / 2);
			allRajat[which][5] = new Vector2(allRajat[which][6].x / 2, allRajat[which][4].y / 2);
		}
	} else {
		if (kumpi2) {
			allRajat[which][1] = new Vector2(allRajat[which][2].x / 2, allRajat[which][0].y / 2);
			allRajat[which][3] = new Vector2(allRajat[which][2].x / 2, allRajat[which][4].y / 2);
		} else {
			allRajat[which][5] = new Vector2(allRajat[which][6].x / 2, allRajat[which][4].y / 2);
			allRajat[which][7] = new Vector2(allRajat[which][6].x / 2, allRajat[which][0].y / 2);
		}
	}
}*/

    public Vector2 getPosition() {
        getPoint();
        Vector2 position = new Vector2(0, 0);
        if (point.x >= sadeX) {
            position.x = Math.min(MAX, point.x / maxRajat[1]);
        } else if (point.x <= -sadeX) {
            position.x = Math.max(-MAX, point.x / -maxRajat[3]);
        }
        if (point.y >= sadeY) {
            position.y = Math.min(MAX, point.y / maxRajat[0]);
        } else if (point.y <= -sadeY) {
            position.y = Math.max(-MAX, point.y / -maxRajat[2]);
        }
        position.x *= speed;
        position.y *= speed;
        return position;
    }

    public float getRotation() {
        float rotation= MathUtils.atan2(point.y, point.x) * MathUtils.radiansToDegrees;
        return rotation-rotation%4;
    }

    void getPoint() {
        if (gyroscope) {
            point = new Vector2(Gdx.input.getGyroscopeZ() - zeroPoint.x, Gdx.input.getGyroscopeY() - zeroPoint.y);
        } else {
            point = new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, Gdx.input.getAccelerometerZ() - zeroPoint.y);
        }
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
}