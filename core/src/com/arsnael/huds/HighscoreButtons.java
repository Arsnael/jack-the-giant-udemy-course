package com.arsnael.huds;

import com.arsnael.helpers.GameInfo;
import com.arsnael.jackthegiant.GameMain;
import com.arsnael.scenes.MainMenu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by arsnael on 26/08/2017.
 */

public class HighscoreButtons {
    private GameMain game;
    private Stage stage;
    private Viewport gameViewport;

    private Label scoreLabel, coinLabel;

    private ImageButton backBtn;

    public HighscoreButtons(GameMain game){
        this.game = game;

        gameViewport = new FitViewport(GameInfo.WIDTH, GameInfo.HEIGHT, new OrthographicCamera());

        stage = new Stage(gameViewport, game.getBatch());

        Gdx.input.setInputProcessor(stage);

        createAndPositionUIElements();

        stage.addActor(backBtn);
        stage.addActor(scoreLabel);
        stage.addActor(coinLabel);
    }

    public Stage getStage(){
        return this.stage;
    }

    void createAndPositionUIElements(){
        backBtn = new ImageButton(new SpriteDrawable(new Sprite(new Texture("Buttons/Options Menu/Back.png"))));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Fonts/blow.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter paramater = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramater.size = 50;

        BitmapFont font = generator.generateFont(paramater);

        scoreLabel = new Label("100", new Label.LabelStyle(font, Color.WHITE));
        coinLabel = new Label("100", new Label.LabelStyle(font, Color.WHITE));

        backBtn.setPosition(17, 17, Align.bottomLeft);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenu(game));
            }
        });

        scoreLabel.setPosition(GameInfo.WIDTH/2f -40, GameInfo.HEIGHT/2f -120);
        coinLabel.setPosition(GameInfo.WIDTH/2f - 40, GameInfo.HEIGHT/2f -217);
    }
}
