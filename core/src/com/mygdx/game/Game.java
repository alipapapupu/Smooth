package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

public class Game extends ApplicationAdapter {

	SpriteBatch batch;
	OrthographicCamera camera=new OrthographicCamera();
	Texture background;
	boolean gyroscope;
	World world;
	Movement enemyMove;
	Movement foodMove;
	Player player;
	ArrayList<Food> foods=new ArrayList<Food>();
	Box2DDebugRenderer render;
	Food eaten;

	@Override
	public void create () {
		background=new Texture("jokugimptausta.png");
		camera.setToOrtho(false,6,4);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		enemyMove=new Movement(-3,3);
		foodMove=new Movement(-1,1);
		for(int i = 1; i < 10; i++) {
			foods.add(new Food(new Vector2(i, 0), this));
		}
		player=new Player(new Vector2(0,0), this,world,camera,gyroscope);
		render=new Box2DDebugRenderer();
		world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().getBody()==player.body||contact.getFixtureB().getBody()==player.body) {
                    for (Food food:foods) {
                        if (contact.getFixtureA().getBody() == food.body || contact.getFixtureB().getBody() == food.body) {
                            eaten=food;
                            break;
                        }

                    }
                }
            }

            @Override
            public void endContact(Contact contact) {

            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });
	}

	@Override
	public void render () {batch.setProjectionMatrix(camera.combined);

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		player.move();
		batch.begin();
		batch.draw(background,-3,-2,0.01f*background.getWidth(),0.01f*background.getHeight());
		draw(batch);
		batch.end();

		render.render(world,camera.combined);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public void draw(SpriteBatch batch){
		player.draw(batch);
		for (Food f : foods) {
			f.draw(batch);
		}
	}
	void use(boolean gyroscope){
		this.gyroscope=gyroscope;
	}
}


