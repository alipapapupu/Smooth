package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Severi on 6.4.2018.
 */
class Text extends Object{

    Matrix4 mat;
    String text;
    BitmapFont font;
    float width;
    int align;
    GlyphLayout layout;
    boolean format=false;
    int more=0;
    String original;
    public Text(String text, boolean format, float rotation, float x, float y, float width, int align, float sX, float sY, Color color, BitmapFont font){
        super(null,rotation,x,y,sX,sY,color);

        this.format=format;
        size=new Vector2(sX,sY);
        this.text=text;
        original=text;
        this.font=font;
        this.width=width;
        this.align=align;
        layout=new GlyphLayout();
    }

    @Override
    public void draw(SpriteBatch batch){
        boolean drawing=false;
        if(!batch.isDrawing()) {
            drawing=true;
            batch.begin();
        }
        if(format&&!text.equals(String.format(original,more))){
            text=String.format(original,more);
            Gdx.app.log("ASD",text);
        }

        mat=batch.getTransformMatrix();
        Vector3 test=mat.getTranslation(new Vector3());
        mat.setTranslation(position.x,position.y,-10);
        mat.rotate(Vector3.Z,rotation);
        batch.setTransformMatrix(mat);

        font.setColor(color);
        font.getData().setScale(size.x,size.y);
        layout.setText(font,text,color,width,align,true);
        font.draw(batch,layout,0,layout.height/2);
        //font.draw(batch,text,0,layout.height/4,width,align,true);

        mat.rotate(Vector3.Z,-rotation);
        mat.setTranslation(test);
        batch.setTransformMatrix(mat);

        if(drawing) {
            batch.end();
        }
    }
}