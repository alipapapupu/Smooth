package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.util.ArrayList;

/**
 * Created by Severi on 16.3.2018.
 */


class Player extends GameObject{
    PlayerMove movement;
    boolean ready=false;
    OrthographicCamera camera;
    ArrayList<Food> bodyParts=new ArrayList<Food>();
    Eye[][] eyes=new Eye[3][3];
    String[] playerTextures=new String[]{"player.png"};
    Player(int tex, Vector2 position, Scene scene, World world, OrthographicCamera camera, boolean accelerometer){
        super(new Texture("player.png"),0.002f,0.002f,1,position,scene);
        if(accelerometer) {
            movement = new PlayerMove();
        }else{
            movement=new ComputerMove();
        }
        this.camera=camera;
        createBody(this.size,false);
        for(int i = 0; i < eyes.length; i++){
            eyes[i][0]=new Eye("eye.png",0.002f,0.002f,-1,position,scene);
            for(int o = 1; o < eyes[i].length; o++){
                eyes[i][o]=new Eye("line.png",0.0015f,0.002f,0,position,scene);

                createJoint(eyes[i][o],eyes[i][o-1],0,0);
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
        }
        if (!ready) {
            ready = movement.grid();
        } else {
            Vector2 add = movement.getPosition();
            if (!add.isZero()) {
                //int test = bodyParts.size() + 1;
                //add=new Vector2(add.x*test,add.y*test);
                //position.add(add);
                //body.applyForceToCenter(add,true);
                body.setLinearVelocity(add);
                /*for(int i = 1; i < bodyParts.size(); i++){
                    bodyParts.get(i).body.applyForceToCenter(bodyParts.get(i-1).position.cpy().sub(bodyParts.get(i).position),true);
                }*/
                Gdx.app.log("movement",movement.getRotation()+"");
                if(rotation<movement.getRotation()-3||rotation>movement.getRotation()+3) {
                    float rot = (movement.getRotation() - rotation);
                    if(rot>180){
                        rot-=360;
                    }else if(rot<-180){
                        rot+=360;
                    }
                    rot/=10;
                    rotation += rot;
                }else{
                    rotation=movement.getRotation();
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
        for (Food foods : bodyParts) {
            //foods.body.setLinearVelocity(new Vector2(0,0));
        }
    }
    void draw(SpriteBatch batch){
        for (int i = bodyParts.size()-1; i>=0; i--) {
            bodyParts.get(i).draw(batch);
        }
        batch.draw(sprite, position.x-size.x/2, position.y-size.y/2,size.x/2,size.y/2,size.x,size.y,1,1,rotation-90,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
        for(int i = 0; i<eyes.length; i++){
            for(int o = eyes[i].length-1; o >=0; o--){
                eyes[i][o].draw(batch);
            }
        }
    }
    void createJoint(GameObject object1, GameObject object2, float xDif,float yDif){
        RevoluteJointDef joint = new RevoluteJointDef();
        joint.bodyA = object1.body;
        joint.bodyB = object2.body;
        joint.localAnchorA.set(new Vector2(0+xDif, -object1.size.y / 2+yDif-Math.abs(xDif)));
        joint.localAnchorB.set(new Vector2(0, object2.size.y / 2));
        joint.collideConnected = false;
        scene.world.createJoint(joint);
    }
}