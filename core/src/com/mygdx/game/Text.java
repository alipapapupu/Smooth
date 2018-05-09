package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sun.org.apache.xpath.internal.operations.Or;

/**
 * Text - object.
 */
class Text extends Object{

    /**
     * Matrix4 to help with text drawing.
     */
    Matrix4 mat;

    /**
     * Wanted text as a string.
     */
    String text;

    /**
     * Text font.
     */
    BitmapFont font;

    /**
     * Text width.
     */
    float width;

    /**
     * Helper integer for text alignment.
     */
    int align;

    /**
     * Contains glyph runs for the text.
     */
    GlyphLayout layout;

    /**
     * Helper boolean for text formatting.
     */
    boolean format=false;

    /**
     * Helper variable.
     */
    int more=0;

    /**
     * Text before formatting.
     */
    String original;

    /**
     * Constructor for text.
     * @param text wanted text string.
     * @param format helper boolean for text format.
     * @param rotation text rotation.
     * @param x text x coordinate.
     * @param y text y coordinate.
     * @param width text width.
     * @param align text height.
     * @param sX source Xcoord.
     * @param sY source Ycoord.
     * @param color text color.
     * @param font text font.
     * @param camera orthographic camera for text.
     */
    public Text(String text, boolean format, float rotation, float x, float y, float width, int align, float sX, float sY, Color color, BitmapFont font,OrthographicCamera camera){
        super(null,rotation,x,y,sX,sY,color,camera);

        this.format=format;
        size=new Vector2(sX,sY);
        this.text=text;
        original=text;
        this.font=font;
        this.width=width;
        this.align=align;
        layout=new GlyphLayout();
    }

    /**
     * Draws the actual text.
     * @param batch to render sprite.
     */
    @Override
    public void draw(SpriteBatch batch){
        boolean drawing=false;
        updatePos();
        if(!batch.isDrawing()) {
            drawing=true;
            batch.begin();
        }
        if(format&&!text.equals(String.format(original,more))){
            text=String.format(original,more);
        }

        batch.setColor(color);
        mat=batch.getTransformMatrix();
        Vector3 test=mat.getTranslation(new Vector3());
        mat.setTranslation(position.x,position.y,-10);
        mat.rotate(Vector3.Z,rotation);
        batch.setTransformMatrix(mat);

        font.setColor(color);
        font.getData().setScale(size.x,size.y*1.5f);
        layout.setText(font,text,color,width,align,true);
        font.draw(batch,layout,0,layout.height/2);
        //font.draw(batch,text,0,layout.height/4,width,align,true);

        mat.rotate(Vector3.Z,-rotation);
        mat.setTranslation(test);
        batch.setTransformMatrix(mat);

        batch.setColor(Color.WHITE);
        if(drawing) {
            batch.end();
        }
    }
}