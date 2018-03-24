package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Severi on 19.3.2018.
 */

public class Scene {
    ArrayList<Food> foods=new ArrayList<Food>();
    Player player;
    Food eaten;
    //ArrayList<Enemy> enemies=new ArrayList<Enemy>();
    World world;
    Texture background;
    OrthographicCamera camera=new OrthographicCamera();


    Sprite backgroundTextureSprite;
    float maxSpawnDistance = 25f;
    float maxFoodDistance = 30f;
    int maxBackgroundWidth = 4;
    int maxBackgroundHeight = 4;


    public Scene(int tex, boolean accelerometer){
        camera.setToOrtho(false,6,4);
        world = new World(new Vector2(0, 0), true);
        if(tex!=-1){
            player=new Player(tex, new Vector2(0,0), this,world,camera,accelerometer);
        }
        background=new Texture("background.png");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTextureSprite = new Sprite(background, 0,0,background.getWidth()*maxBackgroundWidth, background.getHeight()*maxBackgroundHeight);
        backgroundTextureSprite.setPosition(-background.getWidth()*maxBackgroundWidth/2, -background.getHeight()*maxBackgroundHeight/2);

        backgroundTextureSprite.setScale(0.01f);

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

    public void addFood(float posX,float posY){
        foods.add(new Food(new Vector2(posX, posY), this));
    }

    public void draw(SpriteBatch batch, Box2DDebugRenderer renderer){
        move();
        batch.setProjectionMatrix(camera.combined);

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (foods.size() < 20) {
            addFood();
        }

        foodDelete();

        batch.begin();
        backgroundTextureSprite.draw(batch);
        for(int i = 0; i < foods.size(); i++){
            foods.get(i).draw(batch);
        }

        if(player!=null) {
            player.draw(batch);
        }


        batch.end();

        renderer.render(world,camera.combined);
    }
    void move(){
        if(player!=null) {
            player.move();
        }
        for(int i = 0; i < foods.size(); i++){
            foods.get(i).move();
        }
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
        float topRightSector = player.movement.maxBorder[1] + player.movement.maxBorder[0];
        float topLeftSector = Math.abs(player.movement.maxBorder[3]) + player.movement.maxBorder[0];
        float botRightSector = player.movement.maxBorder[1] + Math.abs(player.movement.maxBorder[2]);
        float botLeftSector = Math.abs(player.movement.maxBorder[3]) + Math.abs(player.movement.maxBorder[2]);

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
}
