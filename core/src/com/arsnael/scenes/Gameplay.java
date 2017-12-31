package com.arsnael.scenes;

import com.arsnael.clouds.Cloud;
import com.arsnael.clouds.CloudsController;
import com.arsnael.helpers.GameInfo;
import com.arsnael.huds.UIHud;
import com.arsnael.jackthegiant.GameMain;
import com.arsnael.player.Player;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by arsnael on 14/08/2017.
 */

public class Gameplay implements Screen, ContactListener {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private Viewport gameViewport;

    private OrthographicCamera box2DCamera;
    private Box2DDebugRenderer debugRenderer;

    private World world;

    private Sprite[] bgs;
    private float lastYPosition;

    private CloudsController cloudsController;

    private Player player;

    private UIHud hud;

    public Gameplay(GameMain game){
        this.game = game;

        mainCamera = new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);

        gameViewport = new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        box2DCamera = new OrthographicCamera();
        box2DCamera.setToOrtho(false, GameInfo.WIDTH/GameInfo.PPM, GameInfo.HEIGHT/GameInfo.PPM);
        box2DCamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);

        debugRenderer = new Box2DDebugRenderer();

        hud = new UIHud(game);

        world = new World(new Vector2(0, -9.8f), true);
        world.setContactListener(this);

        cloudsController = new CloudsController(world);

        player = cloudsController.positionThePlayer(player);

        createBackgrounds();
    }

    void handleInput(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            player.movePLayer(-2f);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            player.movePLayer(2f);
        } else {
            player.setWalking(false);
        }

    }

    void update(float delta){
        handleInput(delta);
        moveCamera(delta);
        checkBackgroundsOutOfBounds();
        cloudsController.setCameraY(mainCamera.position.y);
        cloudsController.createAndArrangeNewClouds();
    }

    void moveCamera(float delta){
        mainCamera.position.y -= 100*delta;
    }

    void createBackgrounds(){
        bgs = new Sprite[3];

        for (int i=0; i<bgs.length; i++){
            bgs[i] = new Sprite(new Texture("Backgrounds/Game BG.png"));
            bgs[i].setPosition(0, -(i*bgs[i].getHeight()));
        }
        lastYPosition = Math.abs(bgs[bgs.length-1].getY());
    }

    void drawBackgrounds(){
        for(Sprite bg : bgs){
            game.getBatch().draw(bg, bg.getX(), bg.getY());
        }
    }

    void checkBackgroundsOutOfBounds(){
        for(int i = 0; i < bgs.length; i++){
            if((bgs[i].getY() - bgs[i].getHeight() / 2f - 5) > mainCamera.position.y){
                float newPosition = bgs[i].getHeight() + lastYPosition;
                bgs[i].setPosition(0, -newPosition);
                lastYPosition = Math.abs(newPosition);
            }
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = game.getBatch();
        batch.begin();
        drawBackgrounds();
        cloudsController.drawClouds(batch);
        cloudsController.drawCollectables(batch);
        player.drawPlayerIdle(batch);
        player.drawPlayerAnimation(batch);
        batch.end();

        debugRenderer.render(world, box2DCamera.combined);

        batch.setProjectionMatrix(hud.getStage().getCamera().combined);
        hud.getStage().draw();

        batch.setProjectionMatrix(mainCamera.combined);
        mainCamera.update();

        player.updatePlayer();
        world.step(Gdx.graphics.getDeltaTime(), 6, 2);
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        for(int i=0; i<bgs.length; i++){
            bgs[i].getTexture().dispose();
        }
        player.getTexture().dispose();
        debugRenderer.dispose();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture body1, body2;

        if(contact.getFixtureA().getUserData() == "Player"){
            body1 = contact.getFixtureA();
            body2 = contact.getFixtureB();
        } else {
            body1 = contact.getFixtureB();
            body2 = contact.getFixtureA();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Coin"){
            //collided with the coin
            System.out.println("COIN");
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
        }

        if(body1.getUserData() == "Player" && body2.getUserData() == "Life"){
            //collided with the life
            System.out.println("LIFE");
            body2.setUserData("Remove");
            cloudsController.removeCollectables();
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
}
