package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;

/**
 * Created by Severi on 16.3.2018.
 */


class Player extends GameObject{
    Changer zoomChanger;
    Changer colorChanger;
    Color playerColor;
    PlayerMove movement;
    boolean ready=false;
    OrthographicCamera camera;
    ArrayList<Food> bodyParts=new ArrayList<Food>();
    Eye[][] eyes=new Eye[3][3];
    String[] playerTextures=new String[]{"player.png"};
    RevoluteJointDef joint;
    Player(int tex, Vector2 position, Scene scene, World world, OrthographicCamera camera, PlayerMove move){
        super(new Texture("player2.png"),0.002f,0.002f,scene.currentColorToCollect,position,scene);
        movement=move;

        this.scene=scene;
        this.camera=camera;
        zoomChanger=new Changer(camera.zoom,200);
        colorChanger=new Changer(colors[color],200);

        createBody(this.size,false);

        joint = new RevoluteJointDef();
        joint.collideConnected = false;

        for(int i = 0; i < eyes.length; i++){
            eyes[i][0]=new Eye("eye_new.png",0.0005f,0.0005f,-1,position,scene,true);
            for(int o = 1; o < eyes[i].length; o++){
                eyes[i][o]=new Eye("line.png",0.0015f,0.0025f,0,position,scene,false);

                createJoint(eyes[i][o],eyes[i][o-1],0,0.001f);
            }
            createJoint(this,eyes[i][eyes[i].length-1],(i-1)/5f,0.8f);
        }
    }
    public void move() {

        if (scene.eaten != null) {
            Food eaten = scene.eaten;
            GameObject object;

            if (bodyParts.size() == 0) {
                object = this;
            } else {
                object = bodyParts.get(bodyParts.size() - 1);
            }

            createJoint(object,eaten,0,0);

            bodyParts.add(eaten);
            scene.foods.remove(eaten);
            scene.eaten = null;

            scene.score++;
            zoomChanger.newTime(camera.zoom+1f/bodyParts.size()/10);
        }
        for(Eye[] eye:eyes){
            eye[0].move();
            float angle=body.getAngle()+180*MathUtils.degreesToRadians;
            for(int i = eye.length-1; i>=0; i--) {
                angle=eye[i].rotate(angle);
            }
        }

        zoomChanger.next();
        colorChanger.next();

        camera.zoom=zoomChanger.fromZoom;
        playerColor=colorChanger.color;

        if (!ready) {
            ready = movement.grid();
        } else if(scene.scene==0||movement.getClass()==MenuMove.class) {

            Vector2 add = movement.getPosition();
            if (!add.isZero()) {
                if(rotation<movement.getRotation()-3||rotation>movement.getRotation()+3) {
                    float rot = (movement.getRotation() - rotation);
                    if(rot>180){
                        rot-=360;
                    }else if(rot<-180){
                        rot+=360;
                    }
                    rot/=8;
                    rotation += rot;
                }else{
                    rotation=movement.getRotation();
                }
                float testSpeed=Math.abs(add.x)+Math.abs(add.y);
                body.setLinearVelocity(MathUtils.cos((rotation) * MathUtils.degreesToRadians)*testSpeed,MathUtils.sin((rotation) * MathUtils.degreesToRadians)*testSpeed);
                for (Eye[] eye:eyes) {
                    eye[0].body.setLinearVelocity(body.getLinearVelocity().x,body.getLinearVelocity().y);
                }
            }
        }
        if(rotation>180){
            rotation-=360;
        }else if(rotation<-180){
            rotation+=360;
        }
        position=body.getPosition();
        body.setTransform(position, (rotation - 90) * MathUtils.degreesToRadians);
        camera.position.set(position,0);
        camera.update();

    }
    void draw(SpriteBatch batch){
        for (int i = bodyParts.size()-1; i>=0; i--) {
            bodyParts.get(i).draw(batch);
        }
        batch.setColor(playerColor);
        batch.draw(sprite, position.x-size.x/2, position.y-size.y/2,size.x/2,size.y/2,size.x,size.y,1,1,rotation-90,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
        for(int i = 0; i<eyes.length; i++){
            for(int o = eyes[i].length-1; o >=0; o--){
                eyes[i][o].draw(batch);
            }
        }
        batch.setColor(Color.WHITE);
    }
    void createJoint(GameObject object1, GameObject object2, float xDif,float yDif){
        joint.bodyA = object1.body;
        joint.bodyB = object2.body;
        joint.localAnchorA.set(new Vector2(0+xDif, -object1.size.y / 2+yDif-Math.abs(xDif)));
        joint.localAnchorB.set(new Vector2(0, object2.size.y / 2));
        scene.world.createJoint(joint);
    }
    public void changeColor(int newColor){
        colorChanger.newTime(colors[newColor]);
    }
}