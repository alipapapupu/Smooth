package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    Sprite backgroundTextureSprite;
    float maxSpawnDistance = 25f;
    float maxFoodDistance = 30f;
    int maxBackgroundWidth = 1000;
    int maxBackgroundHeight = 1000;


    @Override
	public void create () {
		background=new Texture("jokugimptausta.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTextureSprite = new Sprite(background, 0, 0, maxBackgroundWidth, maxBackgroundHeight);

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

    public int randomInt(int min, int max) {

        Random random = new Random();

        int randomNum = random.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public float randomCoord(float min, float max) {

        Random random = new Random();

        float randomNum = min + random.nextFloat() * (max - min);

        return randomNum;
    }

    public static float[] sortArray(float[] sectorWeightArray) {
        float[] sortedSectorWeightArray = new float[sectorWeightArray.length];
        float t;

        for (int j = 0; j < sectorWeightArray.length - 1; j++) {

            for (int i = 0; i < sectorWeightArray.length - 1; i++) {
                if (sectorWeightArray[i] >= sectorWeightArray[i + 1]) {
                    t = sectorWeightArray[i];
                    sectorWeightArray[i] = sectorWeightArray[i + 1];
                    sectorWeightArray[i + 1] = t;
                    sortedSectorWeightArray = sectorWeightArray;

                }
            }
        }
        return sortedSectorWeightArray;
    }

    public void addFood() {
        float topRightSector = player.movement.maxRajat[1] + player.movement.maxRajat[0];
        float topLeftSector = Math.abs(player.movement.maxRajat[3]) + player.movement.maxRajat[0];
        float botRightSector = player.movement.maxRajat[1] + Math.abs(player.movement.maxRajat[2]);
        float botLeftSector = Math.abs(player.movement.maxRajat[3]) + Math.abs(player.movement.maxRajat[2]);

        float botY = randomCoord(player.body.getPosition().y - maxSpawnDistance, player.body.getPosition().y);
        float topY = randomCoord(player.body.getPosition().y, player.body.getPosition().y + maxSpawnDistance);
        float leftX = randomCoord(player.body.getPosition().x - maxSpawnDistance, player.body.getPosition().x);
        float rightX = randomCoord(player.body.getPosition().x, player.body.getPosition().x + maxSpawnDistance);

        float sectorWeightArray[] = new float[]{topRightSector, topLeftSector, botRightSector, botLeftSector};

        float sortedSectorWeightArray[] = sortArray(sectorWeightArray);

        int randomInt = randomInt(1, 10);

        if (randomInt > 6) {
            if (sortedSectorWeightArray[0] == topRightSector) {
                foods.add(new Food(new Vector2(rightX, topY),this));
            } else if (sortedSectorWeightArray[0] == topLeftSector) {
                foods.add(new Food(new Vector2(leftX, topY),this));
            } else if (sortedSectorWeightArray[0] == botRightSector) {
                foods.add(new Food(new Vector2(rightX, botY),this));
            } else if (sortedSectorWeightArray[0] == botLeftSector) {
                foods.add(new Food(new Vector2(leftX, botY),this));
            }
        } else if (randomInt > 3 && randomInt < 7) {
            if (sortedSectorWeightArray[1] == topRightSector) {
                foods.add(new Food(new Vector2(rightX, topY),this));
            } else if (sortedSectorWeightArray[1] == topLeftSector) {
                foods.add(new Food(new Vector2(leftX, topY),this));
            } else if (sortedSectorWeightArray[1] == botRightSector) {
                foods.add(new Food(new Vector2(rightX, botY),this));
            } else if (sortedSectorWeightArray[1] == botLeftSector) {
                foods.add(new Food(new Vector2(leftX, botY),this));
            }
        } else if (randomInt > 1 && randomInt < 4) {
            if (sortedSectorWeightArray[2] == topRightSector) {
                foods.add(new Food(new Vector2(rightX, topY),this));
            } else if (sortedSectorWeightArray[2] == topLeftSector) {
                foods.add(new Food(new Vector2(leftX, topY),this));
            } else if (sortedSectorWeightArray[2] == botRightSector) {
                foods.add(new Food(new Vector2(rightX, botY),this));
            } else if (sortedSectorWeightArray[2] == botLeftSector) {
                foods.add(new Food(new Vector2(leftX, botY),this));
            }
        } else {
            if (sortedSectorWeightArray[3] == topRightSector) {
                foods.add(new Food(new Vector2(rightX, topY),this));
            } else if (sortedSectorWeightArray[3] == topLeftSector) {
                foods.add(new Food(new Vector2(leftX, topY),this));
            } else if (sortedSectorWeightArray[3] == botRightSector) {
                foods.add(new Food(new Vector2(rightX, botY),this));
            } else if (sortedSectorWeightArray[3] == botLeftSector) {
                foods.add(new Food(new Vector2(leftX, botY),this));
            }
        }
    }

    public void foodDelete() {
        for (Food food:foods) {
            if (food.body.getPosition().x + maxFoodDistance < player.body.getPosition().x || food.body.getPosition().y + maxFoodDistance < player.body.getPosition().y) {
                foods.remove(food);
                break;
            }
        }
    }

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);

		world.step(Gdx.graphics.getDeltaTime(), 6, 2);
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (foods.size() < 20) {
            addFood();
        }

        foodDelete();

		player.move();
		batch.begin();
        batch.draw(backgroundTextureSprite,-maxBackgroundWidth / 2,-maxBackgroundHeight / 2);
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


