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
 * Main game scene.
 */
public class Scene extends ScreenAdapter {

    /**
     * List for collectables.
     */
    ArrayList<Food> foods=new ArrayList<Food>();

    /**
     * List for game objects.
     */
    ArrayList<GameObject> gameObjects=new ArrayList<GameObject>();

    /**
     * List for foods to be deleted.
     */
    ArrayList<Food> foodsToDelete=new ArrayList<Food>();

    /**
     * List for mini scenes.
     */
    ArrayList<MiniScene> miniScenes=new ArrayList<MiniScene>();

    /**
     * Player - game object.
     */
    Player player;

    /**
     * Collectables which are attached to players body.
     */
    Food eaten;

    /**
     * Game world - box2d feature.
     */
    World world;

    /**
     * Background image.
     */
    Texture background;

    /**
     * Different background images.
     */
    String[] backgrounds=new String[]{"background.png","background2.png","background3.png","calibration.png"};

    /**
     * Orthographic camera for main view.
     */
    OrthographicCamera camera=new OrthographicCamera();

    /**
     * Orthographic camera for fonts.
     */
    OrthographicCamera fontCamera=new OrthographicCamera();

    /**
     * Random - java util.
     */
    Random random = new Random();

    /**
     * Helper boolean for game running.
     */
    boolean game;

    /**
     * To render sprites.
     */
    SpriteBatch batch;

    /**
     * To render shapes.
     */
    ShapeRenderer shapeRenderer;

    /**
     * Game - class.
     */
    Game main;

    /**
     * Helper variable to keep track of current scenes.
     */
    int scene=0;

    /**
     * Integer of current color to collect in colors[].
     */
    int currentColorToCollect;

    /**
     * Integer of current shape to collect in textures[].
     */
    int currentShapeToCollect;

    /**
     * Used to insert player texture.
     */
    int playerTex;

    /**
     * Players movement.
     */
    PlayerMove move;

    /**
     * Timer used mainly to change collectable color/shape.
     */
    Timer timer;

    /**
     * Players current score.
     */
    int score=0;

    /**
     * Time - seconds.
     */
    float second=0;

    /**
     * Time - minutes.
     */
    int minute=0;

    /**
     * Text to show players score.
     */
    Text scoreText;

    /**
     * Text to show current time.
     */
    Text timeText;

    /**
     * Formats text to decimal format.
     */
    DecimalFormat df = new DecimalFormat("##");

    /**
     * Helper variable to keep track of game modes.
     */
    int gameMode=0;

    /**
     * Integer of former color to collect in colors[].
     */
    int formerColorToCollect;

    /**
     * Integer of former shape to collect in textures[].
     */
    int formerShapeToCollect;

    /**
     * Background sprite.
     */
    Sprite backgroundTextureSprite;

    /**
     * Maximum collectable spawn distance from players current point.
     */
    float maxSpawnDistance = 12f;

    /**
     * Maximum collectable spawn distance from players current point.
     */
    float maxFoodDistance = 16f;

    /**
     * Maximum concurrent background repeat width.
     */
    int maxBackgroundWidth = 4;

    /**
     * Maximum concurrent background repeat height.
     */
    int maxBackgroundHeight = 4;

    /**
     * Constructor for main game scene.
     * @param tex indicates
     * @param move player movement.
     * @param game helper boolean for game running.
     * @param main Game - class.
     */
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
        if(game){
            //recreate(0);
            move.scene=this;
            world = new World(new Vector2(0, 0), true);
            world.setContactListener(new ContactListener() {
                /**
                 * Basic contact listener.
                 * @param contact indicates contact.
                 */
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
                                if (gameMode==0) {
                                    if (food.colorNumber == currentColorToCollect) {
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
                                else if (gameMode==1) {
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
                                }else if(gameMode==2){
                                    if (food.colorNumber == currentColorToCollect&&food.shape == currentShapeToCollect) {
                                        eaten = food;
                                    } else {
                                        //if (player.bodyParts.size() > 0) {
                                        //    foodsToDelete.add(player.bodyParts.get(player.bodyParts.size() - 1));
                                        //    player.bodyParts.remove(player.bodyParts.size() - 1);
                                        //}
                                        score--;
                                        foodsToDelete.add(food);
                                        //foods.remove(food);
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
            scoreText=new Text("0",true,0,-300,190,0.1f, Align.left, 0.3f, 0.2f, Color.BLACK, main.font, fontCamera);
            timeText=new Text("0:00",true,0,0,190,0.1f, Align.center, 0.3f, 0.2f, Color.BLACK, main.font, fontCamera);
        }

        shapeRenderer=new ShapeRenderer();
        miniScenes.add(new MiniScene(fontCamera));

        //camera.zoom=5;
        //fontCamera.zoom=5;
    }

    /**
     * Renders the game.
     * @param delta helper variable.
     */
    @Override
    public void render(float delta) {
        if(game) {
            second += Gdx.graphics.getDeltaTime();
            if (second >= 60) {
                second -= 60;
                minute++;
            }
        }

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        draw();
    }

    /**
     * Draws objects to the screen.
     */
    public void draw(){
        if(game) {
            move();
            world.step(Gdx.graphics.getDeltaTime(), 6, 2);
            deleteBodies();
        }

        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.setColor(Color.WHITE);
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
            if(second<10) {
                timeText.original = minute + ":0"+df.format(second);
            }else{
                timeText.original = minute + ":"+df.format(second);
            }
            scoreText.draw(batch);
            timeText.draw(batch);
        }
        miniScenes.get(scene).draw(batch, shapeRenderer);

        //renderer.render(world,camera.combined);

    }

    /**
     * Update the frame.
     */
    void move(){
        player.move();
        for (int i = 0; i < foods.size(); i++) {
            foods.get(i).move();
        }

        foodDelete();

        if (foods.size() < 20) {
            addFood();
        }

        indicatorAdjust();
        backgroundMover();
    }

    /**
     * Random integer between min and max value.
     * @param min minimum integer value.
     * @param max maximum integer value.
     * @return random integer.
     */
    public int randomInt(int min, int max) {

        int randomNum = random.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    /**
     * Random float between min and max value.
     * @param min minimum float value.
     * @param max maximum float value.
     * @return random float.
     */
    public float randomCoord(float min, float max) {

        float randomNum = random.nextFloat() * (max - min)+  min;

        return randomNum;

    }

    /**
     * Sorts calibration value array to descending order.
     * @param sectorWeightArray calibration values.
     * @return sorted array.
     */
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

    /**
     * Adds a collectable.
     */
    public void addFood() {
        Vector2 foodPosition=newFoodPosition();
        foods.add(new Food(foodPosition,this));
    }

    /**
     * Defines the position for collectable foods.
     * @return vector2 coordinates for new food.
     */
    public Vector2 newFoodPosition(){
        Vector2 returner=new Vector2(0,0);

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
                returner=new Vector2(rightX, topY);
            } else if (sortedSectorWeightArray[0] == topLeftSector) {
                returner=new Vector2(leftX, topY);
            } else if (sortedSectorWeightArray[0] == botRightSector) {
                returner=new Vector2(rightX, botY);
            } else if (sortedSectorWeightArray[0] == botLeftSector) {
                returner=new Vector2(leftX, botY);
            }
        } else if (randomInt > 3 && randomInt < 7) {
            if (sortedSectorWeightArray[1] == topRightSector) {
                returner=new Vector2(rightX, topY);
            } else if (sortedSectorWeightArray[1] == topLeftSector) {
                returner=new Vector2(leftX, topY);
            } else if (sortedSectorWeightArray[1] == botRightSector) {
                returner=new Vector2(rightX, botY);
            } else if (sortedSectorWeightArray[1] == botLeftSector) {
                returner=new Vector2(leftX, botY);
            }
        } else if (randomInt > 1 && randomInt < 4) {
            if (sortedSectorWeightArray[2] == topRightSector) {
                returner=new Vector2(rightX, topY);
            } else if (sortedSectorWeightArray[2] == topLeftSector) {
                returner=new Vector2(leftX, topY);
            } else if (sortedSectorWeightArray[2] == botRightSector) {
                returner=new Vector2(rightX, botY);
            } else if (sortedSectorWeightArray[2] == botLeftSector) {
                returner=new Vector2(leftX, botY);
            }
        } else {
            if (sortedSectorWeightArray[3] == topRightSector) {
                returner=new Vector2(rightX, topY);
            } else if (sortedSectorWeightArray[3] == topLeftSector) {
                returner=new Vector2(leftX, topY);
            } else if (sortedSectorWeightArray[3] == botRightSector) {
                returner=new Vector2(rightX, botY);
            } else if (sortedSectorWeightArray[3] == botLeftSector) {
                returner=new Vector2(leftX, botY);
            }
        }

        return returner;
    }

    /**
     * Method which is used to delete bodies from the world completely.
     *
     */
    void deleteBodies() {
        if(score>0) {
            player.zoomChanger.newTime(camera.zoom - 1f / player.bodyParts.size() / 10);
        }else if(score<0){
            score=0;
        }
        if(player.bodyParts.size()>=30&&main.scene!=0){
            player.over=true;
            player.exit.fromZoom=camera.zoom;
            player.exit.newTime(camera.zoom+10);
        }
        for (int i = 0; i < foodsToDelete.size(); i++) {
            //world.destroyBody(foodsToDelete.get(i).body);
            foodsToDelete.get(i).foodReset();
        }
        foodsToDelete.clear();
    }

    /**
     * Method which is used to delete collectables which are too far.
     */
    public void foodDelete() {
        for (Food food:foods) {
            if (food.body.getPosition().x + maxFoodDistance < player.body.getPosition().x || food.body.getPosition().y + maxFoodDistance < player.body.getPosition().y || food.body.getPosition().x - maxFoodDistance > player.body.getPosition().x || food.body.getPosition().y - maxFoodDistance > player.body.getPosition().y) {
                foodsToDelete.add(food);
                foods.remove(food);
                break;
            }
        }
    }

    /**
     * Moves background as player moves on the map.
     */
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

    /**
     * Moves background as player moves on the map.
     */
    public void addGameObject(String tex, float width, float height,int color, int x, int y){
        gameObjects.add(new GameObject(new Texture(tex),width, height, color, new Vector2(x,y),this) {public void move() {}});
    }

    /**
     * Adds game object.
     * @param object game object.
     */
    public void addGameObject(GameObject object){
        gameObjects.add(object);
    }

    /**
     * Adds a button.
     * @param miniScene integer of wanted mini scene.
     * @param texName texture name.
     * @param text button text.
     * @param rotation rotation.
     * @param X x coordinate.
     * @param Y y coordinate.
     * @param sX source Xcoord.
     * @param sY source Ycoord.
     * @param type type integer.
     * @param shape button shape integer, defines button shape.
     * @param action wanted action.
     * @param which helper integer.
     * @param col button color.
     */
    public void addButton(int miniScene, String texName, String text, float rotation, float X, float Y, float sX, float sY,int type, int shape, int action, int which, Color col){
        miniScenes.get(miniScene).buttons.add(new Button(texName,text,rotation,X,Y,sX,sY,type,shape,action,which, col,main,this));
    }


    /**
     * Adds text.
     * @param miniScene integer of wanted mini scene.
     * @param texName texture name.
     * @param rotation rotation.
     * @param X x coordinate.
     * @param Y y coordinate.
     * @param sX source Xcoord.
     * @param sY source Ycoord.
     * @param col text color.
     */
    void addText(int miniScene, String texName, String text, float rotation, float X, float Y, float sX,float sY, Color col){
        miniScenes.get(miniScene).texts.add(new Text(text,true,rotation,X,Y,1, Align.center, sX, sY, col, main.font, fontCamera));
    }

    /**
     * Adds an image.
     * @param miniScene integer of wanted mini scene.
     * @param texName texture name.
     * @param rotation rotation.
     * @param x x coordinate.
     * @param y y coordinate.
     * @param sX source Xcoord.
     * @param sY source Ycoord.
     * @param col color.
     */
    void addImage(int miniScene, String texName, float rotation, float x, float y, float sX, float sY,Color col){
        miniScenes.get(miniScene).images.add(new Object(texName,rotation,x,y,sX,sY,col,fontCamera));
    }

    /**
     * Adds mini scene.
     */
    public void addMiniScene(){
        miniScenes.add(new MiniScene(fontCamera));
    }

    /**
     * Sets scene accordingly.
     * @param i helper integer.
     */
    public void set(int i){
        scene=i;
    }

    /**
     * Recreates scene.
     * @param i helper integer.
     */
    void recreate(int i){
        if(game) {
            player = new Player(i, new Vector2(0, 0), this, world, camera, move);
            timer = new Timer();
            gameMode=i;
            TimerTask colorChange = new TimerTask() {
                public void run() {
                    if (gameMode!=1) {
                        while (currentColorToCollect == formerColorToCollect) {
                            currentColorToCollect = randomInt(2, player.colors.length - 1);
                        }

                        formerColorToCollect = currentColorToCollect;
                        player.changeColor(currentColorToCollect);
                    } if (gameMode!=0) {
                        while (currentShapeToCollect == formerShapeToCollect) {
                            currentShapeToCollect = randomInt(0, player.textures.length - 1);
                        }

                        formerShapeToCollect = currentShapeToCollect;
                        player.changeShape(currentShapeToCollect);
                    }
                }
            };

            timer.scheduleAtFixedRate(colorChange, 0, 30000);
            Gdx.app.log(currentColorToCollect+"",player.colors[currentColorToCollect]+"");
            player.playerColor = player.colors[currentColorToCollect].cpy();
            setBackground(i);
            scene = 0;
        }
    }

    /**
     * Resets game variables.
     */
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

    /**
     * Sets background.
     * @param i background textures integer in backgrounds[].
     */
    void setBackground(int i){

        background = new Texture(backgrounds[i]);
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        backgroundTextureSprite = new Sprite(background, 0, 0, background.getWidth() * maxBackgroundWidth, background.getHeight() * maxBackgroundHeight);
        backgroundTextureSprite.setPosition(-backgroundTextureSprite.getWidth() / 2, -backgroundTextureSprite.getHeight() / 2);

        backgroundTextureSprite.setScale(0.01f);
    }

    /**
     * Changes indicator depending on the game mode.
     */
    void indicatorAdjust() {
        if (gameMode != 0) {
            if (main.scenes[1].miniScenes.get(0).images.get(0).color != Color.WHITE) {
                main.scenes[1].miniScenes.get(0).images.get(0).color = Color.WHITE;
            }
            if (main.scenes[1].miniScenes.get(0).images.size() < 2) {
                main.scenes[1].addImage(0, "square.png", 0, -260, -160, 1, 1, Color.WHITE);
                main.scenes[1].miniScenes.get(0).images.get(1).size.set(30f, 30f);
            }
        } else if (gameMode == 0) {
            if (main.scenes[1].miniScenes.get(0).images.size() > 1) {
                main.scenes[1].miniScenes.get(0).images.remove(1);
            }
        }

        if (gameMode == 1 && player.playerColor != Color.WHITE) {
            player.playerColor = Color.WHITE;
        }
    }
}
