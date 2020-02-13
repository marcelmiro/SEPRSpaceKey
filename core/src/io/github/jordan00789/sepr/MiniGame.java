package io.github.jordan00789.sepr;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class MiniGame implements Screen {
    private final Kroy game;
    private OrthographicCamera camera;

    private Texture pink = new Texture("pink.jpg");
    private Texture alien1 = new Texture("badlogic.jpg");
    private Texture alien3 = new Texture("badlogic.jpg");
    private Texture alien2 = new Texture("badlogic.jpg");

    private Texture truck = new Texture("truck1.png");
    private Texture pipe1 = new Texture("pipe_straight.png");
    private Texture pipe2 = new Texture("pipe_straight.png");
    private Texture pipe3 = new Texture("pipe_straight.png");
    private Texture pipe4 = new Texture("pipe_straight.png");
    private Texture pipe5 = new Texture("pipe_straight.png");
    private Texture pipe6 = new Texture("pipe_straight.png");
    private Texture pipe7 = new Texture("pipe_straight.png");
    private Texture waypipe4 = new Texture("pipe_4_way.png");
    static ArrayList<Texture> correct = new ArrayList<>();



    MiniGame(final Kroy game) {
        this.game = game;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        batch.draw(pink, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        // draw pipes
        batch.draw(truck, ((Gdx.graphics.getWidth() / 2) - (pipe1.getWidth() / 2)), 0, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        batch.draw(alien1, ((Gdx.graphics.getWidth() / 2) - (pipe1.getWidth() / 2)), ((Gdx.graphics.getHeight()) - (pipe1.getHeight() / 2)), Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        //batch.draw(alien2, ((Gdx.graphics.getWidth()/4)-(pipe1.getWidth()/2)), ((Gdx.graphics.getHeight())-(pipe1.getHeight()/2)), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
        //batch.draw(alien3, (((Gdx.graphics.getWidth()/4)*3)-(pipe1.getWidth()/2)), ((Gdx.graphics.getHeight())-(pipe1.getHeight()/2)), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);

        keyspressed1();
        keyspressed2();
        keyspressed3();
        keyspressed4();
        keyspressed5();
        if (drawpipe1 == true) {
            batch.draw(pipe1, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe2 == true) {
            batch.draw(pipe2, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight(), Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe3 == true) {
            //batch.draw(pipe2, ((Gdx.graphics.getWidth()/2)-(pipe2.getWidth()/2)), pipe2.getHeight()+pipe2.getHeight()/2, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
            batch.draw(pipe3, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() + pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);

        }
        if (drawpipe4 == true) {
            batch.draw(pipe4, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() * 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe5 == true) {
            batch.draw(pipe5, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), (pipe2.getHeight() * 2) + pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (correct.contains(pipe1) && correct.contains(pipe2) && correct.contains(pipe3) && correct.contains(pipe4) && correct.contains(pipe5)) {
            drawpipe1 = false;
            drawpipe2 = false;
            drawpipe3 = false;
            drawpipe4 = false;
            drawpipe5 = false;
            correct.remove(drawpipe1 && drawpipe2 && drawpipe3 && drawpipe4 && drawpipe5);


        }


        // draw pipes
        batch.draw(truck, ((Gdx.graphics.getWidth() / 2) - (pipe1.getWidth() / 2)), 0, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        batch.draw(alien1, ((Gdx.graphics.getWidth() / 2) - (pipe1.getWidth() / 2)), ((Gdx.graphics.getHeight()) - (pipe1.getHeight() / 2)), Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        batch.draw(alien2, ((Gdx.graphics.getWidth()/4)-(pipe1.getWidth()/2)), ((Gdx.graphics.getHeight())-(pipe1.getHeight()/2)), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
        batch.draw(alien3, (((Gdx.graphics.getWidth()/4)*3)-(pipe1.getWidth()/2)), ((Gdx.graphics.getHeight())-(pipe1.getHeight()/2)), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);

        keyspressed1();
        keyspressed2();
        keyspressed3();
        keyspressed4();
        keyspressed5();
        if (drawpipe1 == true) {
            batch.draw(pipe1, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe2 == true) {
            batch.draw(pipe2, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight(), Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe3 == true) {
            //batch.draw(pipe2, ((Gdx.graphics.getWidth()/2)-(pipe2.getWidth()/2)), pipe2.getHeight()+pipe2.getHeight()/2, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
            batch.draw(pipe3, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() + pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);

        }
        if (drawpipe4 == true) {
            batch.draw(pipe4, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), pipe2.getHeight() * 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (drawpipe5 == true) {
            batch.draw(pipe5, ((Gdx.graphics.getWidth() / 2) - (pipe2.getWidth() / 2)), (pipe2.getHeight() * 2) + pipe2.getHeight() / 2, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10);
        }
        if (correct.contains(pipe1) && correct.contains(pipe2) && correct.contains(pipe3) && correct.contains(pipe4) && correct.contains(pipe5)) {
            drawpipe1 = false;
            drawpipe2 = false;
            drawpipe3 = false;
            drawpipe4 = false;
            drawpipe5 = false;
            correct.remove(drawpipe1 && drawpipe2 && drawpipe3 && drawpipe4 && drawpipe5);
        }




    //batch.draw(pipe2, ((Gdx.graphics.getWidth()/2)-(pipe2.getWidth()/2)), pipe2.getHeight(), Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
    //batch.draw(pipecurve, ((Gdx.graphics.getWidth()/2)-(toppipe.getWidth()/2)), 0, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);
    //batch.draw(pipelongcurve, ((Gdx.graphics.getWidth()/2)-(toppipe.getWidth()/2)), 0, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10);

        batch.end();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if(Gdx.input.isKeyPressed(Keys.ESCAPE))

    {
        game.setScreen(new MainGame(game));

    }

}






    public boolean drawpipe1 = false;
    public void keyspressed1(){
        if (Gdx.input.isKeyPressed(Keys.L)) {
            drawpipe1=true;
            correct.add(pipe1);


        }
    }
    private boolean drawpipe2 = false;
    public void keyspressed2(){
        if (Gdx.input.isKeyPressed(Keys.K)) {
            drawpipe2=true;
            correct.add(pipe2);

        }
    }
    private boolean drawpipe3 = false;
    public void keyspressed3(){
        if (Gdx.input.isKeyPressed(Keys.J)) {
            drawpipe3=true;
            correct.add(pipe3);

        }
    }
    private boolean drawpipe4 = false;
    public void keyspressed4(){
        if (Gdx.input.isKeyPressed(Keys.H)) {
            drawpipe4=true;
            correct.add(pipe4);

        }
    }
    private boolean drawpipe5 = false;
    public void keyspressed5(){
        if (Gdx.input.isKeyPressed(Keys.G)) {
            drawpipe5=true;
            correct.add(pipe5);

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