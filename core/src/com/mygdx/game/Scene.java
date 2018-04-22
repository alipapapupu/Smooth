package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Align;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Severi on 19.3.2018.
 */

public class Scene extends ScreenAdapter {
    ArrayList<Food> foods=new ArrayList<Food>();
    ArrayList<GameObject> gameObjects=new ArrayList<GameObject>();
    ArrayList<Food> foodsToDelete=new ArrayList<Food>();
    ArrayList<MiniScene> miniScenes=new ArrayList<MiniScene>();
    Player player;
    Food eaten;
    //ArrayList<Enemy> enemies=new ArrayList<Enemy>();
    World world;
    Texture background;
    String[] backgrounds=new String[]{"background.png","calibration.png"};
    OrthographicCamera camera=new OrthographicCamera();
    OrthographicCamera fontCamera=new OrthographicCamera();
    Random random = new Random();
    boolean game;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Game main;
    int scene=0;
    int currentColorToCollect;
    int currentShapeToCollect;
    int playerTex;
    PlayerMove move;
    Timer timer;
    int score=0;
    float second=0;
    int minute=0;
    Text scoreText;
    Text timeText;
    DecimalFormat df = new DecimalFormat("##");

    boolean gameModeColor = false;
    boolean gameModeShape = true;

    int formerColorToCollect;
    int formerShapeToCollect;

    Sprite backgroundTextureSprite;
    float maxSpawnDistance = 12f;
    float maxFoodDistance = 16f;
    int maxBackgroundWidth = 4;
    int maxBackgroundHeight = 4;


    public Scene(int tex, PlayerMove move,boolean game, Game main){
        this.game=game;
        this.main=main;
        this.playerTex=tex;
        this.move=move;
        batch=main.batch;
        camera.setToOrtho(false,6,4);
        camera.translate(new Vector2(-camera.viewportWidth/2,-camera.viewportHeight/2));
        camera.update();
        fontCamera.setToOrtho(false,600,400);
        fontCamera.translate(new Vector2(-camera.viewportWidth/2,-camera.viewportHeight/2));
        fontCamera.update();
        world = new World(new Vector2(0, 0), true);
        if(game){
            //recreate(0);
            move.scene=this;
        }

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                if(contact.getFixtureA().getBody()==player.body||contact.getFixtureB().getBody()==player.body) {
                    /*for (Food food:foods) {
                        if (contact.getFixtureA().getBody() == food.body || contact.getFixtureB().getBody() == food.body) {
                            eaten=food;
                            break;
                        } }
                    }*/
                    for (Food food:foods) {
                        if (contact.getFixtureA().getBody() == food.body || contact.getFixtureB().getBody() == food.body) {
                            if (gameModeColor) {
                                if (food.color == currentColorToCollect) {
                                    eaten = food;
                                } else {
                                    //if (player.bodyParts.size() > 0) {
                                    //    foodsToDelete.add(player.bodyParts.get(player.bodyParts.size() - 1));
                                    //    player.bodyParts.remove(player.bodyParts.size() - 1);
                                    //}
                                    score--;
                                    foodsToDelete.add(food);
                                    foods.remove(food);
                                }
                            } else if (gameModeShape) {
                                if (food.shape == currentShapeToCollect) {
                                    eaten = food;
                                } else {
                                    //if (player.bodyParts.size() > 0) {
                                    //    foodsToDelete.add(player.bodyParts.get(player.bodyParts.size() - 1));
                                    //    player.bodyParts.remove(player.bodyParts.size() - 1);
                                    //}
                                    score--;
                                    foodsToDelete.add(food);
                                    foods.remove(food);
                                }
                            }
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

        shapeRenderer=new ShapeRenderer();
        miniScenes.add(new MiniScene(fontCamera));

        scoreText=new Text("0",true,0,-300,190,0.1f, Align.left, 0.3f, 0.2f, Color.BLACK, main.font, fontCamera);
        timeText=new Text("0:00",true,0,0,190,0.1f, Align.center, 0.3f, 0.2f, Color.BLACK, main.font, fontCamera);
        //camera.zoom=5;
        //fontCamera.zoom=5;
    }

    public void addFood(float posX,float posY){
        foods.add(new Food(new Vector2(posX, posY), this));
    }

    @Override
    public void render(float delta) {
        second+=Gdx.graphics.getDeltaTime();
        if(second>=60){
            second-=60;
            minute++;
        }

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw(null);
    }

    public void draw(Box2DDebugRenderer renderer){
        if(game) {
            move();
        }

        batch.setProjectionMatrix(camera.combined);
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

        batch.setColor(Color.WHITE);

        batch.begin();
        if(background!=null) {
            backgroundTextureSprite.draw(batch);
        }
        for(int i = 0; i < foods.size(); i++){
            foods.get(i).draw(batch);
        }
        for(int i = 0; i < gameObjects.size(); i++){
            gameObjects.get(i).draw(batch);
        }

        if(player!=null) {
            player.draw(batch);
        }

        batch.end();

        batch.setProjectionMatrix(fontCamera.combined);
        shapeRenderer.setProjectionMatrix(fontCamera.combined);

        if(move!=null&&move.getClass()!=MenuMove.class&&scene==0) {
            scoreText.original = score + "";
            timeText.original = minute + ":" + df.format(second);
            scoreText.draw(batch);
            timeText.draw(batch);
        }
        miniScenes.get(scene).draw(batch, shapeRenderer);

        deleteBodies();
        //renderer.render(world,camera.combined);

    }

    void move(){
        player.move();
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).move();
        }

        foodDelete();

        if (foods.size() < 20) {
            addFood();
        }


        backgroundMover();
    }



    public int randomInt(int min, int max) {

        int randomNum = random.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public float randomCoord(float min, float max) {

        float randomNum = random.nextFloat() * (max - min)+  min;

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

    void deleteBodies() {
        if(score>0) {
            player.zoomChanger.newTime(camera.zoom - 1f / player.bodyParts.size() / 10);
        }
        for (int i = 0; i < foodsToDelete.size(); i++) {
            world.destroyBody(foodsToDelete.get(i).body);
        }
        foodsToDelete.clear();
    }

    public void foodDelete() {
        for (Food food:foods) {
            if (food.body.getPosition().x + maxFoodDistance < player.body.getPosition().x || food.body.getPosition().y + maxFoodDistance < player.body.getPosition().y || food.body.getPosition().x - maxFoodDistance > player.body.getPosition().x || food.body.getPosition().y - maxFoodDistance > player.body.getPosition().y) {
                foodsToDelete.add(food);
                foods.remove(food);
                break;
            }
        }
    }

    void backgroundMover(){
        if(player.position.x>backgroundTextureSprite.getX()+(backgroundTextureSprite.getWidth()/2+backgroundTextureSprite.getWidth()/4*backgroundTextureSprite.getScaleX())){
            backgroundTextureSprite.setX(backgroundTextureSprite.getX()+(backgroundTextureSprite.getWidth()/2*backgroundTextureSprite.getScaleX()));
        }else if(player.position.x<backgroundTextureSprite.getX()+(backgroundTextureSprite.getWidth()/2-backgroundTextureSprite.getWidth()/4*backgroundTextureSprite.getScaleX())){
            backgroundTextureSprite.setX(backgroundTextureSprite.getX()-(backgroundTextureSprite.getWidth()/2*backgroundTextureSprite.getScaleX()));
        }
        if(player.position.y>backgroundTextureSprite.getY()+(backgroundTextureSprite.getHeight()/2+backgroundTextureSprite.getHeight()/4*backgroundTextureSprite.getScaleY())){
            backgroundTextureSprite.setY(backgroundTextureSprite.getY()+(backgroundTextureSprite.getHeight()/2*backgroundTextureSprite.getScaleY()));
        }else if(player.position.y<backgroundTextureSprite.getY()+(backgroundTextureSprite.getHeight()/2-backgroundTextureSprite.getHeight()/4*backgroundTextureSprite.getScaleY())){
            backgroundTextureSprite.setY(backgroundTextureSprite.getY()-(backgroundTextureSprite.getHeight()/2*backgroundTextureSprite.getScaleY()));
        }
    }

    public void addGameObject(String tex, float width, float height,int color, int x, int y){
        gameObjects.add(new GameObject(new Texture(tex),width, height, color, new Vector2(x,y),this) {public void move() {}});
    }

    public void addGameObject(GameObject object){
        gameObjects.add(object);
    }

    public void addButton(int miniScene, String texName, String text, float rotation, float X, float Y, float sX, float sY,int type, int shape, int action, int which, Color col){
        miniScenes.get(miniScene).buttons.add(new Button(texName,text,rotation,X,Y,sX,sY,type,shape,action,which, col,main,this));
    }

    public void addMiniScene(){
        miniScenes.add(new MiniScene(fontCamera));
    }

    public void set(int i){
        scene=i;
    }

    void recreate(int i){
        if(game) {
            player = new Player(i, new Vector2(0, 0), this, world, camera, move);
            timer = new Timer();
            TimerTask colorChange = new TimerTask() {
                public void run() {
                    if (gameModeColor) {
                        while (currentColorToCollect == formerColorToCollect) {
                            currentColorToCollect = randomInt(2, player.colors.length - 1);
                        }

                        formerColorToCollect = currentColorToCollect;
                        player.changeColor(currentColorToCollect);
                    } else if (gameModeShape) {
                        while (currentShapeToCollect == formerShapeToCollect) {
                            currentShapeToCollect = randomInt(0, player.textures.length - 1);
                        }

                        formerShapeToCollect = currentShapeToCollect;
                    }
                }
            };

            timer.scheduleAtFixedRate(colorChange, 0, 15000);
            player.playerColor = player.colors[currentColorToCollect].cpy();
            setBackground(i);
            scene = 0;
        }
    }

    void empty(){
        if(game) {
            score=0;
            for (int i = 0; i < foods.size(); i++) {
                foods.get(i).destroy();
            }
            if(player!=null) {
                player.destroy();
            }if(timer!=null) {
                timer.cancel();
            }
        }
        camera.zoom=1;
    }
    void setBackground(int i){

        background = new Texture(backgrounds[i]);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTextureSprite = new Sprite(background, 0, 0, background.getWidth() * maxBackgroundWidth, background.getHeight() * maxBackgroundHeight);
        backgroundTextureSprite.setPosition(-backgroundTextureSprite.getWidth() / 2, -backgroundTextureSprite.getHeight() / 2);

        backgroundTextureSprite.setScale(0.01f);
    }
    void addText(int miniScene, String texName, String text, float rotation, float X, float Y, float sX, float sY,int type, int shape, int action, int which, Color col){
        miniScenes.get(miniScene).texts.add(new Text("",true,0,0,0,1, Align.center, 1, 1, Color.BLACK, main.font, camera));
    }
}
