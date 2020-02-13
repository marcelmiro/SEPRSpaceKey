package io.github.jordan00789.sepr;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
import java.util.Random;

public class MiniGame implements Screen {
    private final Kroy game;
    private OrthographicCamera camera;

    //private Texture pink = new Texture("pink.jpg");
    private Sprite alien1 = new Sprite (new Texture("ufo.png"));
    private Sprite alien3 = new Sprite (new Texture("ufo.png"));
    private Sprite alien2 = new Sprite (new Texture("ufo.png"));
    private Sprite truck = new Sprite (new Texture("truck1.png"));
    private Sprite selector = new Sprite (new Texture("selector.png"));
    private static ArrayList<Sprite> pipes = new ArrayList<>();
    private static ArrayList<Sprite> correctpipes = new ArrayList<>();
    private int currentPipe = 0;

    private int level;

    MiniGame(final Kroy game) {
        Random rand = new Random();
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
         for (int i = 0; i < 9; i++){
            pipes.add(new Sprite(new Texture("pipe_broken.png")));
        }
        truck.setScale((float)0.3);
         alien1.setScale((float)0.2);

         level = rand.nextInt(2);
         if (level==0){
             truck.setPosition(Gdx.graphics.getWidth() / 2 - 408 - truck.getWidth() / 2,
                     Gdx.graphics.getHeight() / 2 - truck.getHeight() / 2);
             truck.rotate90(true);
             alien1.setPosition(Gdx.graphics.getWidth() / 2 + 408 - alien1.getWidth() / 2,
                     Gdx.graphics.getHeight() / 2 - alien1.getHeight() / 2);
             getSolution(0);
         }

        if(level==1){
            truck.setScale((float)0.3);
            truck.setPosition(Gdx.graphics.getWidth() / 2 - 408 - truck.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - truck.getHeight() / 2);
            truck.rotate90(true);
            alien1.setScale((float)0.2);
            alien1.setPosition(Gdx.graphics.getWidth() / 2 + 408 - alien1.getWidth() / 2,
                    Gdx.graphics.getHeight() / 2 - alien1.getHeight() / 2);
            getSolution(1);
        }


        for (int i = 0; i < 9; i++){
            if (correctpipes.get(i).getTexture().toString() == ("blank.png")){
                pipes.set(i, (new Sprite(new Texture("blank.png"))));
            }
        }

        while (correctpipes.get(currentPipe).getTexture().toString() == ("blank.png")){
            currentPipe += 1;
            if (currentPipe > 8) {
                currentPipe = 0;
            }
        }

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Batch batch = game.batch;
        batch.begin();

        //batch.draw(pink, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        setPosition(currentPipe, selector);
        selector.draw(batch);

        handleInput();
        drawPipes(batch);
        truck.draw(batch);
        alien1.draw(batch);
        batch.end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            dispose();
        }
        if(checkforWin()){
            dispose();
        }
    }

    private void drawPipes(Batch batch) {
        int pipenum = -1;
        for (Sprite pipe : pipes) {
            pipenum += 1;
            setPosition(pipenum, pipe);
            pipe.draw(batch);
        }
    }

    private void handleInput(){
        if (Gdx.input.isKeyJustPressed(Keys.Q)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_straight.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.W)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_curvelb.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.E)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_curvert.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.R)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_curvelt.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.T)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_curverb.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.Y)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_4_way.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.U)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_ltb.png")));
        } else if (Gdx.input.isKeyJustPressed(Keys.I)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_rtb.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.O)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_lrb.png")));
        }else if (Gdx.input.isKeyJustPressed(Keys.P)) {
            pipes.set(currentPipe,new Sprite (new Texture ("pipe_lrt.png")));
        }
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            currentPipe += 1;
            if (currentPipe > 8) {
                currentPipe = 0;
            }
            //Will not allow you to select blank tiles
            while (correctpipes.get(currentPipe).getTexture().toString().equals("blank.png")){
                currentPipe += 1;
                if (currentPipe > 8) {
                    currentPipe = 0;
                }
            }
        }
    }

    private boolean checkforWin(){
        for (int i = 0; i < 9; i++){
            //If all non-blank pipes are the same as the one in the correct pipes
            if (!(correctpipes.get(i).getTexture().toString().equals(pipes.get(i).getTexture().toString()) || correctpipes.get(i).getTexture().toString().equals("blank.png"))){
                return false;
            }
        }
        return true;
    }

    private void setPosition(int num, Sprite sprite){
        int x = num % 3;
        int y = (num - x) / 3;
        sprite.setPosition((float) (x * 204 + Gdx.graphics.getWidth() / 2.0 - 306), y * 204 + 102);
    }

    private void getSolution(int level){
        if (level == 0 || level == 1){
            correctpipes.add(new Sprite(new Texture("blank.png")));
            correctpipes.add(new Sprite(new Texture("blank.png")));
            correctpipes.add(new Sprite(new Texture("blank.png")));
            correctpipes.add(new Sprite(new Texture("pipe_straight.png")));
            correctpipes.add(new Sprite(new Texture("pipe_straight.png")));
            correctpipes.add(new Sprite(new Texture("pipe_straight.png")));
            correctpipes.add(new Sprite(new Texture("blank.png")));
            correctpipes.add(new Sprite(new Texture("blank.png")));
            correctpipes.add(new Sprite(new Texture("blank.png")));
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
        // TODO Enter pause menu
    }

    @Override
    public void resume() {
        // TODO Resume from pause menu
    }

    @Override
    public void hide() {
        // Auto-generated method stub
    }

    @Override
    public void dispose() {
        // Auto-generated method stub
    }
}