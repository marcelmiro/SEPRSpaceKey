package io.github.jordan00789.sepr;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MiniGame implements Screen {
    private final Kroy game;
    private OrthographicCamera camera;

    private Texture pink = new Texture("pink.jpg");

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
        batch.end();
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
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