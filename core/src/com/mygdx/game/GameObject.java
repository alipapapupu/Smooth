package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

/**
 * Created by Severi on 16.3.2018.
 */

abstract class GameObject {

    Game game;
    Vector2 bounds;
    Vector2 position;
    Vector2 size;
    int shape;
    Texture sprite;
    int color;
    float rotation;
    Body body;
    Filter filter;
    String[] textures=new String[]{"circle.png"};

    Color[] colors=new Color[]{Color.WHITE,Color.BLACK,Color.BLUE,Color.BROWN,Color.RED,Color.YELLOW,Color.ORANGE,Color.PINK,Color.PURPLE};

    public GameObject(Texture tex, float width, float height,int color, Vector2 position, short dontHit,short hit,  Game game){
        if(tex==null){
            int r=(int)(Math.random()*textures.length);
            sprite=new Texture(textures[r]);
            shape=r;
        }else {
            sprite = tex;
        }
        this.color=color;
        this.game=game;
        bounds=new Vector2(game.camera.viewportWidth/2,game.camera.viewportHeight/2);
        this.position=position;
        this.size = new Vector2(sprite.getWidth()*width, sprite.getHeight()*height);
        filter=new Filter();
        filter.groupIndex=dontHit;
        filter.categoryBits=hit;
        createBody(tex==null);

    }
    void createBody(boolean food){

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position);

        body = game.world.createBody(bodyDef);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(size.x / 2, size.y / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0;

        body.createFixture(fixtureDef);
        if(!food) {
            body.setFixedRotation(true);
        }
        else{
            body.getFixtureList().first().setSensor(true);
        }
        body.getFixtureList().first().setFilterData(filter);
        body.getFixtureList().first().refilter();
        shape.dispose();
    }


    public void draw(SpriteBatch batch, boolean bodyPart){
        batch.setColor(colors[color]);
        if(bodyPart){
            batch.draw(sprite, body.getPosition().x-size.x/2, body.getPosition().y-size.y/2, size.x/2, size.y/2, size.x, size.y, 1, 1, body.getAngle()*MathUtils.radiansToDegrees, 0, 0, sprite.getWidth(), sprite.getHeight(), true, false);
        }else {
            batch.draw(sprite, position.x - size.x / 2, position.y - size.y / 2, size.x / 2, size.y / 2, size.x, size.y, 1, 1, rotation, 0, 0, sprite.getWidth(), sprite.getHeight(), false, false);
        }
        batch.setColor(Color.WHITE);

    }
}