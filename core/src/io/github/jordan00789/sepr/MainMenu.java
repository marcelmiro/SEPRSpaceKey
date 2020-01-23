package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MainMenu implements Screen {

    private final Kroy game;
    private OrthographicCamera camera;

    private Texture menuImage;

    MainMenu(final Kroy game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuImage = new Texture("splashscreen.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Batch batch = game.batch;
        batch.begin();
        batch.draw(menuImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            game.setScreen(new MainGame(game));
            dispose();
        }
    }

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
	}

    @Override
    public void hide() {
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
    public void dispose() {
    }
}
