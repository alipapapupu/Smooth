package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJoint;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

public class Game extends com.badlogic.gdx.Game {

    SpriteBatch batch;
    boolean accelerometer;
    Movement enemyMove;
    Movement foodMove;
    Box2DDebugRenderer render;
    Scene[] scenes=new Scene[2];
    int scene;
    boolean calibrate=true;
    BitmapFont font;

    @Override
    public void create () {
        Gdx.app.log("ASD","ISD");
        batch = new SpriteBatch();

        accelerometer=true;
        PlayerMove movement;
        scenes[1]=new Scene(-1,null,false,this);
        GameObject center=new GameObject(new Texture("chooser.png"),0.001f,0.001f,0,new Vector2(0,0),scenes[1]) {public void move() {}};
        if(accelerometer) {
            movement = new PlayerMove(center,this);
        }else{
            movement=new ComputerMove(center,this);
        }
        scenes[0]=new Scene(0,movement,true,this);
        enemyMove=new Movement(-3,3);
        foodMove=new Movement(-1,1);

        scenes[1].addGameObject("calibration.png",0.003f,0.003f,0,0,0);
        scenes[1].addGameObject(center);
        render=new Box2DDebugRenderer();
        set(0);
    }

	@Override
	public void render () {
        super.render();
        if(calibrate){
            scenes[1].draw(render);
        }
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
	void use(boolean accelerometer){
		this.accelerometer=accelerometer;
	}

    public void set(int i){
        scene=i;

        setScreen(scenes[scene]);
    }
}


