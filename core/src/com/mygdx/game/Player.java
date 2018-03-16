package com.mygdx.game;

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
    final boolean androidTest=false;
    ArrayList<Food> bodyParts=new ArrayList<Food>();
    Player(Vector2 position, Game game, World world, OrthographicCamera camera, boolean gyroscope){
        super(new Texture("player.png"),0.002f,0.002f,0,position,(short)1,(short)2,game);
        if(androidTest) {
            movement = new PlayerMove(gyroscope);
        }else{
            movement=new ComputerMove(gyroscope);
        }
        this.camera=camera;

    }
    void move(){

        if(game.eaten!=null){
            Food eaten=game.eaten;
            GameObject object;

            if(bodyParts.size()==0){
                object=this;
            }else{
                object=bodyParts.get(bodyParts.size()-1);
            }
            //eaten.body.getFixtureList().first().setSensor(true);
            //eaten.body.getFixtureList().first().setDensity(bodyParts.size()+1);

            RevoluteJointDef joint = new RevoluteJointDef();
            joint.bodyA = object.body;
            joint.bodyB = eaten.body;
            joint.localAnchorA.set(new Vector2( 0,-object.size.y / 2));
            joint.localAnchorB.set(new Vector2(0, eaten.size.y / 2));
            joint.collideConnected = false;
            game.world.createJoint(joint);
            bodyParts.add(eaten);
            game.foods.remove(eaten);
            game.eaten=null;
            joint.referenceAngle=0;
        }
        if(!ready){
            ready=movement.grid();
        }else{
            Vector2 add= movement.getPosition();
            if(add!=Vector2.Zero) {
                int test=bodyParts.size()+1;
                //add=new Vector2(add.x*test,add.y*test);
                body.setLinearVelocity(add);
                rotation = movement.getRotation();
            }
        }
        position=body.getPosition();
        body.setTransform(body.getPosition(),(rotation-90)* MathUtils.degreesToRadians);
        camera.position.set(position.add(size.x/2,size.y/2),0);
        camera.update();
        for (Food foods:bodyParts) {
            //foods.body.setLinearVelocity(new Vector2(0,0));
        }
    }
    void draw(SpriteBatch batch){
        for (int i = bodyParts.size()-1; i>=0; i--) {
            bodyParts.get(i).draw(batch,true);
        }
        batch.draw(sprite, position.x-size.x, position.y-size.y,size.x/2,size.y/2,size.x,size.y,1,1,rotation-90,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
    }
}