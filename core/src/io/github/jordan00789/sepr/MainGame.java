package io.github.jordan00789.sepr;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.Input.Keys;

public class MainGame implements Screen {
	final Kroy game;
	OrthographicCamera camera;
	float entityScale;

	// Entities
	Firetruck camTruck;
	static Firetruck truck1;
	static Firetruck truck2;
	public static Firetruck currentTruck;
	Fortress fortress1, fortress2, fortress3;
	Entity nullEntity;
	Texture map;
	Pixmap pmap = new Pixmap(Gdx.files.internal("map.png"));
	public static Pixmap speedMap;
	public static ArrayList<Entity> entities = new ArrayList<Entity>();

	public MainGame(final Kroy game) {
		this.game = game;

		// This is a pixmap used to get the pixel RGBA values at specified coordinates.
		speedMap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), pmap.getFormat());
		speedMap.drawPixmap(pmap, 0, 0, pmap.getWidth(), pmap.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		entityScale = 0.05f;
		loadTrucks();
		loadFortresses();
		FiretruckMenu.create();

		map = new Texture("map.png");
	}

	public static String getPixelColour(float x, float y) {
		int pixcolour;
		pixcolour = MainGame.speedMap.getPixel(Math.round(x), Gdx.graphics.getHeight() - Math.round(y));
		String col = "#" + Integer.toHexString(pixcolour & 15790320);
		if (col.length() > 2) {
			col = col.substring(0, 7);
		}
		return col;
	}

	/**
	 * Separate method to load the fortresses.
	 */
	private void loadFortresses() {
		// Screen width and height.
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		// We used relative coordinates so that multiple resolutions are supported.
		fortress1 = new Fortress(200, new Texture("ctower.png"), 1);
		initEntity(fortress1, (0.53f * width), (0.26f * height));

		fortress2 = new Fortress(200, new Texture("station.png"), 2);
		initEntity(fortress2, (0.29f * width), (0.66f * height));

		fortress3 = new Fortress(200, new Texture("minster.png"), 3);
		initEntity(fortress3, (0.47f * width), (0.82f * height));

		// This entity is used to fill the end of the entity array.
		// The last entity in entities is not rendered due to a UI bug.
		nullEntity = new Entity(1, new Texture("badlogic.jpg"));
		initEntity(nullEntity, 1000, 500);
	}

	/**
	 * Separate method to load the trucks.
	 */
	private void loadTrucks() {
		truck1 = new Firetruck(100, 80, new Texture("truck1.png"));
		initEntity(truck1, 309, 290);

		truck2 = new Firetruck(50, 200, new Texture("truck2.png"));
		initEntity(truck2, 285, 260);

		// camTruck is located at the centre of the screen. It is not rendered, but used
		// to switch to the full map view.
		camTruck = new Firetruck(1, 1, new Texture("badlogic.jpg"));
		camTruck.setX((Gdx.graphics.getWidth() / 2) - 256);
		camTruck.setY((Gdx.graphics.getHeight() / 2) - 256);

		changeToTruck(truck1);
	}

	/**
	 * Initialises the entity to the right size and position, and adds it to the
	 * entity array.
	 *
	 * @param e The entity to initialise
	 * @param x The x-coordinate of the entity
	 * @param y The y-coordinate of the entity
	 */
	private void initEntity(Entity e, float x, float y) {
		e.setScale(entityScale);
		e.setOriginCenter();
		e.setPosition(x - e.getOriginX(), y - e.getOriginY());
		entities.add(e);
	}

	/**
	 * The main render method, runs 60 times a second (The frame rate of the game).
	 *
	 * @param delta The current delta time
	 */
	public void render(float delta) {

		takeInputs();

		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		// Ensures viewport edges stay within the bounds of the map.
		float cameraX = Math.max(0.125f * Gdx.graphics.getWidth(),
				Math.min(currentTruck.getX() + 256, 0.875f * Gdx.graphics.getWidth()));
		float cameraY = Math.max(0.125f * Gdx.graphics.getHeight(),
				Math.min(currentTruck.getY() + 256, 0.875f * Gdx.graphics.getHeight()));

		Batch batch = game.batch;
		camera.position.set(cameraX, cameraY, 0);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(map, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// Updates and draws each entity in the entities array.
		entities.forEach(e -> {
			e.update(delta);
			e.draw(batch);
			// Moves the entity to the screen centre when it is destroyed.
			if (e.isDestroyed()) {
				e.setPosition((Gdx.graphics.getWidth() / 2) - e.getOriginX(),
						(Gdx.graphics.getHeight() / 2) - e.getOriginY());
			}
		});
		entities.removeIf(e -> e.isDestroyed());
		// TODO This line is inefficient, may need refactoring
		if (!entities.contains(fortress1) && !entities.contains(fortress2) && !entities.contains(fortress3)) {
			game.setScreen(new MainWin(game));
			dispose();
		}
		if (!entities.contains(truck1) && !entities.contains(truck2)) {
			game.setScreen(new MainLose(game));
			dispose();
		}

		FiretruckMenu.update(delta);

		batch.end();
	}

	/**
	 * Check for inputs to move the current truck.
	 */
	private void takeInputs() {

		if (Gdx.input.isKeyPressed(Keys.UP)) {
			currentTruck.goForward();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			currentTruck.goBackward();
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			currentTruck.turnLeft();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			currentTruck.turnRight();
		}
		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			currentTruck.attack();
		}
		switchTrucks();
	}

	/**
	 * Check for inputs to switch between trucks.
	 */
	private void switchTrucks() {
		if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
			changeToTruck(truck1);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
			changeToTruck(truck2);
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_0)) {
			// currentTruck.setColor(Color.WHITE);
			currentTruck = camTruck;
			camera.zoom = 1f;
		}

	}

	/**
	 * Switches the camera to the specified truck.
	 *
	 * @param t The truck to switch to
	 */
	private void changeToTruck(Firetruck t) {
		if (currentTruck != null) {
			// currentTruck.setColor(Color.WHITE);
		}
		currentTruck = t;
		// currentTruck.setColor(Color.RED);
		camera.zoom = 0.25f;
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		speedMap.drawPixmap(pmap, 0, 0, pmap.getWidth(), pmap.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		// getOriginX() and getOriginY() is 512 for all fortresses
		fortress1.setPosition((0.53f * width) - 512, (0.26f * height) - 512);
		fortress2.setPosition((0.29f * width) - 512, (0.66f * height) - 512);
		fortress3.setPosition((0.47f * width) - 512, (0.82f * height) - 512);

	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

}