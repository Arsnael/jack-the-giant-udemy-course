package com.arsnael.clouds;

import com.arsnael.collectables.Collectable;
import com.arsnael.helpers.GameInfo;
import com.arsnael.player.Player;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

/**
 * Created by arsnael on 19/08/2017.
 */

public class CloudsController {
    private World world;

    private Array<Cloud> clouds = new Array<Cloud>();
    private Array<Collectable> collectables = new Array<Collectable>();

    private final float DISTANCE_BETWEEN_CLOUDS = 250f;
    private float minX, maxX;
    private float lastCloudPositionY;

    private Random random = new Random();

    private float cameraY;

    public CloudsController(World world){
        this.world = world;
        minX = GameInfo.WIDTH / 2f - 120f;
        maxX = GameInfo.WIDTH / 2f + 120f;
        createClouds();
        positionClouds(true);
    }

    private void createClouds(){
        for(int i=0; i<2; i++){
            clouds.add(new Cloud(world, "Dark Cloud"));
        }

        for(int i=0; i<6; i++){
           clouds.add(new Cloud(world, "Cloud " + (i%3+1)));
        }

        clouds.shuffle();
    }

    public void positionClouds(boolean firstTimeArranging){
        while(clouds.get(0).getCloudName().equals("Dark Cloud")){
            clouds.shuffle();
        }

        float positionY = 0;

        if(firstTimeArranging)
            positionY = GameInfo.HEIGHT / 2f;
        else
            positionY = lastCloudPositionY;

        int controlX = 0;

        for(Cloud c : clouds){
            if(c.getX() == 0 && c.getY() == 0){
                float tempX = 0;

                if(controlX==0){
                    tempX = randomBetweenNumbers(maxX - 50, maxX);
                    controlX = 1;
                } else if (controlX == 1){
                    tempX = randomBetweenNumbers(minX, minX + 50);
                    controlX = 0;
                }

                c.setSpritePosition(tempX, positionY);
                positionY -= DISTANCE_BETWEEN_CLOUDS;
                lastCloudPositionY = positionY;

                if(!firstTimeArranging && c.getCloudName() != "Dark Cloud"){
                    int rand = random.nextInt(10);

                    if(rand > 5){
                        int randomCollectable = random.nextInt(2);
                        if(randomCollectable == 0){
                            //life
                            Collectable collectable = new Collectable(world, "Life");
                            collectable.setCollectablePosition(c.getX(), c.getY()+40);
                            collectables.add(collectable);
                        } else {
                            //coin
                            Collectable collectable = new Collectable(world, "Coin");
                            collectable.setCollectablePosition(c.getX(), c.getY()+40);
                            collectables.add(collectable);
                        }
                    }
                }
            }
        }
    }

    public void drawClouds(SpriteBatch batch){
        for(Cloud c : clouds){
            batch.draw(c, c.getX() - c.getWidth() / 2f, c.getY() - c.getHeight() / 2f);
        }
    }

    public void drawCollectables(SpriteBatch batch){
        for(Collectable c : collectables){
            batch.draw(c, c.getX(), c.getY());
        }
    }

    public void removeCollectables(){
        for(int i=0; i<collectables.size; i++){
            if(collectables.get(i).getFixture().getUserData() == "Remove"){
                collectables.get(i).changeFilter();
                collectables.get(i).getTexture().dispose();
                collectables.removeIndex(i);
            }
        }
    }


    public void createAndArrangeNewClouds(){
        for(int i = 0; i<clouds.size; i++){
            if((clouds.get(i).getY() - GameInfo.HEIGHT / 2f - 15) > cameraY){
                clouds.get(i).getTexture().dispose();
                clouds.removeIndex(i);
            }
        }

        if(clouds.size == 4){
            createClouds();
            positionClouds(false);
        }
    }

    public void setCameraY(float cameraY){
        this.cameraY = cameraY;
    }

    public Player positionThePlayer(Player player) {
        player = new Player(world, clouds.get(0).getX(), clouds.get(0).getY() + 100);
        return player;
    }

    private float randomBetweenNumbers(float min, float max){
        return random.nextFloat() * (max-min) + min;
    }
}
