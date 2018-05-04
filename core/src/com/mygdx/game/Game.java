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
import com.badlogic.gdx.physics.box2d.Shape;
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

    final int SPLASHSCENE=5;
    final int SETTINGSSCENE=4;
    final int CALIBRATIONSCENE=3;
    SpriteBatch batch;
    boolean accelerometer;
    Movement enemyMove;
    Movement foodMove;
    Box2DDebugRenderer render;
    Scene[] scenes=new Scene[6];
    int scene;
    boolean calibrate=false;
    BitmapFont font;

    @Override
    public void create () {
        accelerometer=false;
        batch = new SpriteBatch();
        font=new BitmapFont(Gdx.files.internal("fonts//font.txt"));
        PlayerMove movement;
        scenes[CALIBRATIONSCENE]=new Scene(-1,null,false,this);
        GameObject center=new GameObject(new Texture("chooser.png"),0.001f,0.001f,0,new Vector2(0,0),scenes[CALIBRATIONSCENE]) {public void move() {}};
        if(accelerometer) {
            movement = new PlayerMove(center,this);
        }else{
            movement=new ComputerMove(center,this);
        }
        scenes[0]=new Scene(0,new MenuMove(center,this),true,this);
        scenes[0].addMiniScene();
        scenes[0].addMiniScene();
        scenes[0].addButton(0,null,"START",0,0.2f,-100f,150,50,0, Button.BOX,1,1,Color.GREEN);
        scenes[0].addImage(0,"title.png",0,0,130,1,1,Color.WHITE);
        scenes[0].addButton(1,null,"New Game",0,0.2f,100f,150,50,0, Button.BOX,1,2,Color.GREEN);
        scenes[0].addButton(2, null, "Game 1",0,-200,0,150,50,0,Button.BOX,0,1,Color.GREEN);
        scenes[0].addButton(2, null, "Game 2",0,0,0,150,50,0,Button.BOX,0,2,Color.GREEN);
        scenes[0].addButton(2, null, "Game 3",0,200,0,150,50,0,Button.BOX,0,3,Color.GREEN);

        scenes[1]=new Scene(0,movement,true,this);
        scenes[1].addMiniScene();
        scenes[1].addButton(0,"pause.png",null,0,260,160,30,30,0,Button.TEXTURE,1,1,Color.WHITE);

        scenes[1].addButton(1,null,"CONTINUE",0,0.2f,100f,150,50,0, Button.BOX,1,0,Color.GREEN);
        scenes[1].addButton(1,null,"CALIBRATE",0,0.2f,0f,150,50,0, Button.BOX,2,0,Color.GREEN);
        scenes[1].addButton(1,null,"EXIT",0,0.2f,-100f,150,50,0, Button.BOX,0,0,Color.GREEN);
        enemyMove=new Movement(-3,3);
        foodMove=new Movement(-1,1);

        scenes[CALIBRATIONSCENE].addGameObject("calibration.png",0.003f,0.003f,0,0,0);
        scenes[CALIBRATIONSCENE].addGameObject(center);

        scenes[SPLASHSCENE]=new Splashscreen(this);

        render=new Box2DDebugRenderer();
        set(SPLASHSCENE);
    }

	@Override
	public void render () {
        super.render();
        if(calibrate){
            scenes[CALIBRATIONSCENE].draw();
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
        scenes[scene].empty();
	    switch(i) {
            case 1:
            case 2:
            case 3:
                scene=1;
                i-=1;
                calibrate=true;
                break;
	        default:
                scene = i;
        }
        setScreen(scenes[scene]);
        scenes[scene].recreate(i);
    }
}


