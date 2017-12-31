package com.arsnael.clouds;

import com.arsnael.helpers.GameInfo;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by arsnael on 15/08/2017.
 */

public class Cloud extends Sprite {
    private World world;
    private Body body;
    private String cloudName;

    public Cloud(World world, String cloudName){
        super(new Texture("Clouds/" + cloudName + ".png"));
        this.world = world;
        this.cloudName = cloudName;
    }

    void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;

        //bodyDef.position.set((getX() + getWidth() / 2f) / GameInfo.PPM, (getY() + getHeight()/2f) / GameInfo.PPM);
        bodyDef.position.set((getX()) / GameInfo.PPM, (getY()) / GameInfo.PPM);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((getWidth()/2f)/GameInfo.PPM, (getHeight()/2f)/GameInfo.PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void setSpritePosition(float x, float y){
        setPosition(x, y);
        createBody();
    }

    public String getCloudName(){
        return this.cloudName;
    }
}
