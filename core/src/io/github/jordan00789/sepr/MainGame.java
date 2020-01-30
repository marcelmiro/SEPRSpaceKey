package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class MainGame implements Screen {
	private final Kroy game;
	private static OrthographicCamera camera;

	// Entities
	private Firetruck camTruck;
	private static Firetruck prevTruck;
	static Firetruck currentTruck;
	private Texture map;
	private Pixmap pMap = new Pixmap(Gdx.files.internal("map_3.png"));
	static Pixmap speedMap;
	static ArrayList<Entity> entities = new ArrayList<>();
	private static ArrayList<Fortress> listFort;
	static ArrayList<Firetruck> listTruck;
	private static ArrayList<Firetruck> listTruckDead;
	private static float timer1 = 0, timer2 = 0, timer3 = 0, timer4 = 0;
	private static float fortDamage = 0, fortProjectileSpeed = 50;
	private static final double damageIncrease = 0.05, speedIncrease = 0.2;
	private boolean changeTruck = false;

	public MainGame(final Kroy game) {
		this.game = game;

		// This is a pixmap used to get the pixel RGBA values at specified coordinates.
		speedMap = new Pixmap(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), pMap.getFormat());
		speedMap.drawPixmap(pMap, 0, 0, pMap.getWidth(), pMap.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		loadTrucks();
		loadForts();
		FiretruckMenu.create();

		map = new Texture("map_3.png");
	}

	static String getPixelColour(float x, float y) {
		int pixcolour;
		pixcolour = MainGame.speedMap.getPixel(Math.round(x), Gdx.graphics.getHeight() - Math.round(y));
		String col = "#" + Integer.toHexString(pixcolour & 15790320);
		if (col.length() > 2) {
			col = col.substring(0, 7);
		}
		return col;
	}

	// Create all trucks
	private void loadTrucks() {
	    listTruck = new ArrayList<>();
	    listTruckDead = new ArrayList<>();
	    listTruck.add(new Firetruck(309, 290, 100, 100, new Texture("firetruck_red.png"), 2));
	    listTruck.add(new Firetruck(285, 260, 50, 200, new Texture("firetruck_purple.png"), 2));

	    for (Firetruck truck : listTruck) {
	        initEntity(truck, truck.getPosX(), truck.getPosY());
        }

		// camTruck is located at the centre of the screen. It is not rendered, but used
		// to switch to the full map view.
		camTruck = new Firetruck((Gdx.graphics.getWidth() / 2f) - 256, (Gdx.graphics.getHeight() / 2f) - 256, -10, 1, new Texture("badlogic.jpg"), 0);
		camTruck.setX(camTruck.getPosX());
		camTruck.setY(camTruck.getPosY());

		changeToTruck(listTruck.get(0));
	}

	// Create all fortresses
	private void loadForts() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();

		listFort = new ArrayList<>();
		listFort.add(new Fortress(.53f, .26f,150, new Texture("clifford.png"), 20));
		listFort.add(new Fortress(.22f, .54f,200, new Texture("station.png"), 20));
		listFort.add(new Fortress(.47f, .82f,300, new Texture("minster.png"), 20));
		listFort.add(new Fortress(.93f, .07f,250, new Texture("university.png"), 20));
		listFort.add(new Fortress(.25f, .86f,200, new Texture("museum.png"), 20));
		listFort.add(new Fortress(.25f, .05f,200, new Texture("tower.png"), 20));

		for (Fortress fort : listFort) {
			initEntity(fort, fort.getPosX() * width, fort.getPosY() * height);
		}

		// This entity is used to fill the end of the entity array.
		// The last entity in entities is not rendered due to a UI bug.
		Entity nullEntity = new Entity(1, new Texture("badlogic.jpg"));
		initEntity(nullEntity, 1000, 500);
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
		e.setScale(0.05f);
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
				e.setPosition((Gdx.graphics.getWidth() / 2f) - e.getOriginX(),
						(Gdx.graphics.getHeight() / 2f) - e.getOriginY());
			}
		});

		// Removes entity from list if dead. Checks if game is won or lost.
		entities.removeIf(Entity::isDestroyed);
		if (checkWin()) {
			game.setScreen(new MainWin(game));
			dispose();
		} else if (checkLoose()) {
			game.setScreen(new MainLose(game));
			dispose();
		}

		// Adds truck to listTruckDead if health = 0. Countdown timer of 1s to change to non-dead truck automatically.
		for (Firetruck truck : listTruck) {
			if (truck.getHealth() <= 0 && !listTruckDead.contains(truck)) {
				listTruckDead.add(truck);
				changeTruck = true;
			}
		}
		if (changeTruck) {
			timer1 += Gdx.graphics.getDeltaTime();

			if (timer1 >= 1) {
				changeTruck = false;
				timer1 = 0;
				changeTruckAuto();
			}
		}

		// Update fort stats and firetruck's menu
		updateFortStats(delta);
		FiretruckMenu.update(delta);

		batch.end();
	}

	// Checks if all forts are destroyed and returns true if so.
	private static boolean checkWin() {
		for (Fortress fort : listFort) {
			if (entities.contains(fort)) { return false; }
		}
		if (timer2 >= 1) {
			timer2 = 0;
			return true;
		} else {
			timer2 += Gdx.graphics.getDeltaTime();
			return false;
		}
	}

	// Checks if all trucks are destroyed and return true if so.
	private static boolean checkLoose() {
		for (Firetruck truck : listTruck) {
			if (entities.contains(truck)) { return false; }
		}
		if (timer3 >= 1) {
			timer3 = 0;
			return true;
		} else {
			timer3 += Gdx.graphics.getDeltaTime();
			return false;
		}
	}

	// Changes automatically to first non-dead truck in listTruck.
	private static void changeTruckAuto() {
		for (Firetruck truck : listTruck) {
			if (entities.contains(truck)) {
				changeToTruck(truck);
				return;
			}
		}
	}

	// Check for inputs to move the current truck.
	private void takeInputs() {
		if (Gdx.input.isKeyPressed(Keys.W)) {
			currentTruck.goForward();
		}
		if (Gdx.input.isKeyPressed(Keys.S)) {
			currentTruck.goBackward();
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			currentTruck.turnLeft();
		}
		if (Gdx.input.isKeyPressed(Keys.D)) {
			currentTruck.turnRight();
		}
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			currentTruck.attack();
		}
		switchTrucks();
	}

	// Check for inputs to switch between trucks.
	private void switchTrucks() {
		if (Gdx.input.isKeyPressed(Keys.NUM_1) && ((currentTruck.speedLimit() == 29f) || (currentTruck == camTruck && prevTruck == listTruck.get(0)) )) {
			changeToTruck(listTruck.get(0));
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_2) && ((currentTruck.speedLimit() == 29f) || (currentTruck == camTruck && prevTruck == listTruck.get(1)) )) {
			changeToTruck(listTruck.get(1));
		}
		if (Gdx.input.isKeyPressed(Keys.NUM_0)) {
			// currentTruck.setColor(Color.WHITE);
			if (currentTruck != camTruck){
				prevTruck = currentTruck;
			}
			currentTruck.setVelocity(0);
			currentTruck = camTruck;
			camera.zoom = 1f;
		}

	}

	/**
	 * Switches the camera to the specified truck.
	 *
	 * @param t The truck to switch to
	 */
	private static void changeToTruck(Firetruck t) {
		if (!listTruckDead.contains(t)) {
			currentTruck = t;
			camera.zoom = 0.25f;
		}
	}

	// Increments ET Fortress' damage each second by 0.2 and range by 0.25, by using Gdx's delta variable
	private static void updateFortStats(float delta) {
		timer4 += delta;
		if (timer4 >= 1) {
			if (fortDamage < 100) { fortDamage = Math.round((fortDamage + damageIncrease) * 10) / 10f; }
			if (fortProjectileSpeed < 100) { fortProjectileSpeed = Math.round((fortProjectileSpeed + speedIncrease) * 100) / 100f; }
			timer4 = 0;
		}
	}

	static float getFortDamage() { return fortDamage; }
	static float getFortProjectileSpeed() { return fortProjectileSpeed; }

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		speedMap.drawPixmap(pMap, 0, 0, pMap.getWidth(), pMap.getHeight(), 0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		
		// getOriginX() and getOriginY() is 512 for all fortresses
		for (Fortress fort : listFort) {
			fort.setPosition((fort.getPosX() * width) - 512, (fort.getPosY() * height) - 512);
		}
	}

	@Override
	public void show() {}
	@Override
	public void hide() {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}

}