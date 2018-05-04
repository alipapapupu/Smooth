package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Severi on 16.3.2018.
 */


class Player extends GameObject{
    float sumMass=0.1f;
    float sumDamping=10;
    Joint theJoint;
    Changer zoomChanger;
    Changer colorChanger;
    Changer shapeChanger;
    Changer exit;
    Color playerColor;
    Body otherBody;
    PlayerMove movement;
    boolean ready=false;
    OrthographicCamera camera;
    ArrayList<Food> bodyParts=new ArrayList<Food>();
    Eye[][] eyes=new Eye[2][3];
    Food indicator;
    RevoluteJointDef joint;
    boolean calibration=true;
    boolean over = false;
    Player(int mode, Vector2 position, Scene scene, World world, OrthographicCamera camera, PlayerMove move){
        super(new Texture(new String[]{"player3.png","player2.png","player4.png"}[mode]),0.002f,0.002f,scene.currentColorToCollect,position,scene);
        movement=move;

        this.scene=scene;
        this.camera=camera;
        exit=new Changer(camera.zoom,500);
        zoomChanger=new Changer(camera.zoom+1.2f,200);
        if(mode!=1) {
            colorChanger = new Changer(colors[colorNumber].cpy(), 50);
        }if(mode!=0) {
            indicator=new Food(position,scene);
            indicator.sprite=new Texture(indicator.textures[scene.currentShapeToCollect]);
            indicator.setSize(0.5f);
            indicator.color=Color.BLACK;
            shapeChanger=new Changer(200);
        }
        createBody(this.size, false);
        createBody(this.size, true);

        joint = new RevoluteJointDef();
        joint.collideConnected = false;

        for(int i = 0; i < eyes.length; i++) {
                eyes[i][0] = new Eye("eye_new.png", 0.0005f, 0.0005f, -1, position, scene, true);
                for (int o = 1; o < eyes[i].length; o++) {
                    eyes[i][o] = new Eye("line.png", 0.0015f, 0.0025f, 0, position, scene, false);

                    createJoint(eyes[i][o], eyes[i][o - 1], 0, 0.001f);
                }
                createJoint(otherBody, eyes[i][eyes[i].length - 1].body, (i*2 - 1f) / 5f, 0.5f);
        }
    }
    @Override
    void createBody(Vector2 size, boolean fixed){
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(size.x/2,size.y/2);
        createOtherBody(shape,fixed);
    }
    void createOtherBody(PolygonShape shape,boolean fixed){

            BodyDef bodyDef = new BodyDef();
            if(!fixed) {
                bodyDef.type = BodyDef.BodyType.DynamicBody;
            }else{
                bodyDef.type= BodyDef.BodyType.KinematicBody;
            }
            bodyDef.position.set(position);



            if(!fixed) {
                body = scene.world.createBody(bodyDef);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = 0.001f;
                fixtureDef.friction = 0;
                body.createFixture(fixtureDef);
                body.getFixtureList().first().setSensor(true);
                body.setAngularDamping(10);
                body.setLinearDamping(10);
            }else{
                otherBody = scene.world.createBody(bodyDef);
            }
        shape.dispose();
    }
    public void move() {

        if(!over) {
            if (scene.eaten != null) {
                Food eaten = scene.eaten;
                eaten.body.getFixtureList().first().setDensity(0.000001f);
                eaten.body.resetMassData();


                if(bodyParts.size()!=0) {
                    scene.world.destroyJoint(theJoint);
                    createJoint(eaten,bodyParts.get(bodyParts.size() - 1),0,0);
                }
                createJoint(this, eaten, 0, 0);


                bodyParts.add(eaten);
                scene.foods.remove(eaten);
                scene.eaten = null;

                scene.score++;
                zoomChanger.newTime(camera.zoom + 1f / bodyParts.size() / 10);
                for (Food part:bodyParts) {
                    part.body.getFixtureList().first().setDensity(sumMass/bodyParts.size());
                    part.body.resetMassData();
                    part.body.setLinearDamping(sumDamping/bodyParts.size());
                    part.body.setAngularDamping(sumDamping/bodyParts.size());
                }
            }

            zoomChanger.next();
            camera.zoom = zoomChanger.fromZoom;

            if (colorChanger != null) {
                colorChanger.next();
                playerColor = colorChanger.color;
            }
            if (shapeChanger != null) {
                shapeChanger.next();

                if (shapeChanger.zoom == 0) {
                    indicator.sprite = new Texture(indicator.textures[scene.currentShapeToCollect]);
                }
                indicator.indicatorSize = MathUtils.clamp(shapeChanger.zoom, 0.001f, 1);
            }


            if (!ready) {
                if (calibration) {
                    movement.center();
                    calibration = false;
                }
                ready = movement.grid();
            } else if (scene.scene == 0 || movement.getClass() == MenuMove.class) {

                if (!calibration) {
                    calibration = true;
                }
                Vector2 add = movement.getPosition();
                if (!add.isZero()) {
                    if (rotation < movement.getRotation() - 3 || rotation > movement.getRotation() + 3) {
                        float rot = (movement.getRotation() - rotation);
                        if (rot > 180) {
                            rot -= 360;
                        } else if (rot < -180) {
                            rot += 360;
                        }
                        rot /= 8;
                        rotation += rot;
                    } else {
                        rotation = movement.getRotation();
                    }
                    float testSpeed = Math.abs(add.x) + Math.abs(add.y);
                    body.setLinearVelocity(MathUtils.cos((rotation) * MathUtils.degreesToRadians) * testSpeed, MathUtils.sin((rotation) * MathUtils.degreesToRadians) * testSpeed);
                    for (Eye[] eye : eyes) {
                        eye[0].body.setLinearVelocity(body.getLinearVelocity().x, body.getLinearVelocity().y);
                    }
                }
            }
            if (rotation > 180) {
                rotation -= 360;
            } else if (rotation < -180) {
                rotation += 360;
            }
            position = body.getPosition();
            body.setTransform(position, (rotation - 90) * MathUtils.degreesToRadians);
            otherBody.setTransform(body.getPosition(), body.getAngle());
            if (indicator != null) {
                indicator.body.setTransform(position, body.getAngle());
            }
            camera.position.set(position, 0);
            camera.update();
            for (Eye[] eye : eyes) {
                eye[0].move();
            }
        }else{
            exit.next();
            camera.zoom=exit.zoom;
            if(exit.time>=exit.maxTime){
                scene.main.set(0);
            }
        }
    }
    void draw(SpriteBatch batch){
        for (int i = bodyParts.size()-1; i>=0; i--) {
            bodyParts.get(i).draw(batch);
        }
        batch.setColor(playerColor);
        batch.draw(sprite, position.x-size.x/2, position.y-size.y/2,size.x/2,size.y/2,size.x,size.y,1,1,rotation-90,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
        if(indicator!=null) {
            indicator.draw(batch);
        }
        batch.setColor(playerColor);
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
        if(object1.getClass()==Player.class){
            theJoint=scene.world.createJoint(joint);
        }else{
            scene.world.createJoint(joint);
        }
    }
    void createJoint(Body object1, Body object2, float xDif,float yDif){
        joint.bodyA = object1;
        joint.bodyB = object2;
        joint.localAnchorA.set(new Vector2(0+xDif, yDif-Math.abs(xDif)));
        joint.localAnchorB.set(new Vector2(0, 0));
        scene.world.createJoint(joint);
    }
    public void changeColor(int newColor){
        colorChanger.newTime(colors[newColor].cpy());
    }
    public void changeShape(int newShape){shapeChanger.newTime();}
}