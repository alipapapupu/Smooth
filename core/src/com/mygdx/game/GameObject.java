package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Severi on 16.3.2018.
 */

abstract class GameObject {

    Scene scene;
    Vector2 bounds;
    Vector2 position;
    Vector2 size;
    int shape;
    Texture sprite;
    Color color;
    float rotation;
    Body body;
    Filter filter;
    String[] textures=new String[]{"circle.png","square.png","triangle.png","diamond.png"};
    int type;
    float indicatorSize=1;

    int colorNumber;
    Color[] colors=new Color[]{Color.WHITE,Color.BLACK,Color.BLUE,Color.BROWN,Color.RED,Color.YELLOW,Color.PINK};

    public GameObject(Texture tex, float width, float height,int color, Vector2 position, Scene scene){
        if(tex==null){
            int r=(int)(Math.random()*textures.length);
            sprite=new Texture(textures[r]);
            shape=r;
            type=r+1;
        }else {
            type=0;
            sprite = tex;
        }

        if(color==-1){
            colorNumber=randomColor();
        }else {
            colorNumber = color;
        }

        this.color=colors[colorNumber].cpy();
        this.scene=scene;
        bounds=new Vector2(scene.camera.viewportWidth/2,scene.camera.viewportHeight/2);
        this.position=position;
        this.size = new Vector2(sprite.getWidth()*width , sprite.getHeight()*height);

    }
    void createBody(float radius, boolean food){
        CircleShape shape=new CircleShape();
        shape.setRadius(radius);
        createBody(shape,food);
    }
    void createBody(Vector2 size, boolean food){
        PolygonShape shape=new PolygonShape();
        shape.setAsBox(size.x/2,size.y/2);
        createBody(shape,food);
    }
    void createBody(Shape shape, boolean food){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = scene.world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.001f;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);
        if(!food) {
            body.setFixedRotation(true);
            fixtureDef.density=1000;
            fixtureDef.friction=1;
        }
        else{
            body.getFixtureList().first().setSensor(true);
            body.setAngularDamping(10);
            body.setLinearDamping(10);
        }
        shape.dispose();
    }


    void draw(SpriteBatch batch){
        Color backup=batch.getColor();
        batch.setColor(color);
        if(body==null){
            batch.draw(sprite, position.x - size.x / 2*indicatorSize, position.y - size.y / 2*indicatorSize, size.x / 2*indicatorSize, size.y / 2*indicatorSize, size.x*indicatorSize, size.y*indicatorSize, 1, 1, rotation, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
        }else {
            batch.draw(sprite, body.getPosition().x - size.x / 2, body.getPosition().y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, body.getAngle() * MathUtils.radiansToDegrees, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
        }
        batch.setColor(backup);
    }

    int randomColor(){
        return (int)(Math.random()*(colors.length-2)+2);
    }
    public abstract void move();

    void destroy(){
        if(this.getClass()==Food.class) {
            scene.foods.remove(this);
        }else if(this.getClass()==Player.class){
            for (int i = 0; i < scene.player.bodyParts.size(); i++) {
                scene.player.bodyParts.get(i).destroy();
            }
            for(int i = 0; i < scene.player.eyes.length; i++){
                for(int o = 0; o < scene.player.eyes[i].length; o++){
                    scene.player.eyes[i][o].destroy();
                }
            }
            scene.player=null;

        }
        scene.world.destroyBody(body);
    }
    public void setSize(float newSize){
        size=new Vector2(size.x*newSize,size.y*newSize);
    }
}