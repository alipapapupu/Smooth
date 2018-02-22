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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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
	@Override
	public void create () {
		background=new Texture("space.png");
		camera.setToOrtho(false,6,4);
		batch = new SpriteBatch();
		world = new World(new Vector2(0, 0), true);
		enemyMove=new Movement(-3,3);
		foodMove=new Movement(-1,1);
		foods.add(new Food(new Vector2(0,0),this));
		player=new Player(new Vector2(0,0), this,world,camera,gyroscope);
	}

	@Override
	public void render () {batch.setProjectionMatrix(camera.combined);

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(background,-3,-2,0.01f*background.getWidth(),0.01f*background.getHeight());
		batch.end();
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
abstract class GameObject {

	Game game;
	Vector2 bounds;
	Rectangle hitbox;
	Vector2 position;
	Vector2 size;
	Texture sprite;
	int color;
	float rotation;
	Color[] colors=new Color[]{Color.WHITE,Color.BLACK,Color.BLUE,Color.BROWN,Color.RED,Color.YELLOW,Color.ORANGE,Color.PINK,Color.PURPLE};

	public GameObject(Texture tex, float width, float height,int color, Vector2 position,Game game){
		sprite = tex;

		this.game=game;
		bounds=new Vector2(game.camera.viewportWidth/2,game.camera.viewportHeight/2);
		this.position=position;
		this.size = new Vector2(width, height);
		hitbox=new Rectangle(position.x,position.y,size.x,size.y);

	}

	boolean collision(Rectangle hitbox){
		if(this.hitbox.overlaps(hitbox)){
			return true;
		}
		return false;
	}
	void draw(SpriteBatch batch){
		batch.setColor(colors[color]);
		batch.draw(sprite, position.x, position.y,0.3f,0.3f,0.6f,0.6f,1,1,rotation,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
		batch.setColor(Color.WHITE);

	}
}
class Food extends GameObject{
	Food(Vector2 position, Game game){
		super(new Texture("pallo.png"),1,1, (int)Math.random()*(8-2)+2,position,game);
	}
}
class Player extends GameObject{
	PlayerMove movement;
	Body body;
	boolean ready=false;
	OrthographicCamera camera;
	Player(Vector2 position, Game game,World world, OrthographicCamera camera,boolean gyroscope){
		super(new Texture("badlogic.jpg"),1,1,0,position,game);
		movement=new PlayerMove(gyroscope);

		this.camera=camera;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(position);

		body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(sprite.getWidth()/2,sprite.getHeight()/2);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1f;

		Fixture fixture = body.createFixture(fixtureDef);

		shape.dispose();
	}
	void move(){

		if(!ready){
			ready=movement.grid();
		}else{
			Vector2 add= movement.getPosition();
			if(add!=Vector2.Zero) {
				body.setLinearVelocity(movement.getPosition());
				rotation = movement.getRotation();
			}
		}
		position=body.getPosition();
		camera.position.set(position,0);
		camera.update();

	}
	void draw(SpriteBatch batch){
		batch.draw(sprite, position.x, position.y,0.3f,0.3f,0.6f,0.6f,1,1,rotation,0,0,sprite.getWidth(),sprite.getHeight(),false,false);
	}
}

class Movement{
	int min;
	int max;
	Random r=new Random();

	Movement(int min, int max){
		this.min=min;
		this.max=max;
	}

	public Vector2 getPosition() {
		Vector2 move=new Vector2(0,0);
		move.x=r.nextFloat()*(max-min)+min;
		move.y=r.nextFloat()*(max-min)+min;
		return move;
	}

	public float getRotation(Vector2 position, Vector2 newPos) {
		return MathUtils.atan2(position.y-newPos.y, position.x-newPos.x) * MathUtils.radiansToDegrees;
	}
}


class PlayerMove{
	Vector2 zeroPoint = Vector2.Zero;
	final float sadeX = 1.5f;
	final float sadeY = 1.5f;
	final float speed = 5;
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
		empty();
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
		return MathUtils.atan2(point.y, point.x) * MathUtils.radiansToDegrees;
	}

	void getPoint() {
		if (gyroscope) {
			point = new Vector2(Gdx.input.getGyroscopeZ() - zeroPoint.x, Gdx.input.getGyroscopeY() - zeroPoint.y);
		} else {
			point = new Vector2(Gdx.input.getAccelerometerY() - zeroPoint.x, Gdx.input.getAccelerometerZ() - zeroPoint.y);
		}
	}
}