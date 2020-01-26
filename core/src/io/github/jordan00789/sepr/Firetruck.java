package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;


public class Firetruck extends Entity implements Attack, Moveable {

	private int water;
	private int maxWater;
	private float acceleration = 2;
	private float direction = 0;
	private float velocity = 0;
	public ArrayList<Projectile> drops = new ArrayList<Projectile>();
	private float piConstant = (float) Math.PI / 180;

	/**
	 * Creates a Firetruck sprite using the texture provided, with the specified
	 * amounts of health and water.
	 *
	 * @param health   The amount of health the truck has
	 * @param maxWater The maximum amount of water in the truck
	 * @param texture  The texture given to the Firetruck sprite
	 */
	public Firetruck(int health, int maxWater, Texture texture) {
		super(health, texture);
		this.maxWater = water = maxWater;
	}

	/**
	 * @return The truck's current amount of water.
	 */
	public int getWater() {
		return water;
	}

	/**
	 * Sets the amount of water in the truck if the amount is valid.
	 *
	 * @param amount The amount that the water variable is set to
	 */
	private void setWater(int amount) {
		if (amount <= maxWater && amount > 0) {
			water = amount;
		}
	}

	/**
	 * Removes the specified amount of water from the truck.
	 *
	 * @param amount The amount of water removed from the truck
	 */
	public void takeWater(int amount) {
		if (amount <= water && amount > 0) {
			water -= amount;
		}
	}

	/**
	 * Refills the truck to the maximum amount of water.
	 */
	public void refill() {
		if (water < maxWater) {
			setWater(water + 1);
		}
	}

	/**
	 * Sets the trucks direction.
	 *
	 * @param direction The value in degrees the truck's direction is set to
	 */
	private void setDirection(float direction) {
		this.direction = direction;
	}

	/**
	 * Returns the truck's current direction.
	 *
	 * @return The truck's current direction
	 */
	public float getDirection() {
		return this.direction;
	}

	/**
	 * Sets the truck's velocity.
	 *
	 * @param velocity The value the velocity is set to
	 */
	private void setVelocity(float velocity) {
		this.velocity = velocity;
	}

	/**
	 * Returns the truck's current velocity.
	 *
	 * @return The truck's current velocity
	 */
	private float getVelocity() {
		return velocity;
	}

	/**
	 * Turns the truck left.
	 */
	@Override
	public void turnLeft() {
		if (getDirection() <= 0) {
			setDirection(360);
		} else {
			setDirection(getDirection() - getTurnSpeed());
		}
	}

	/**
	 * Turns the truck right.
	 */
	@Override
	public void turnRight() {
		if (getDirection() >= 360) {
			setDirection(0);
		} else {
			setDirection(getDirection() + getTurnSpeed());
		}
	}

	/**
	 * Calculates the current turning radius of the truck.
	 *
	 * @return Turning speed
	 */
	private float getTurnSpeed() {
		if (velocity > 50f) {
			return (400f / velocity);
		} else {
			return 8f;
		}
	}

	/**
	 * Moves the truck forward.
	 */
	@Override
	public void goForward() {
		if (velocity < speedLimit()) {
			setVelocity(getVelocity() + acceleration);
		}
	}

	/**
	 * Moves the truck backward.
	 */
	@Override
	public void goBackward() {
		if (velocity > -speedLimit()) {
			setVelocity(getVelocity() - acceleration);
		}
	}

	/**
	 * Updates the position and rotation of the truck and it's corresponding water
	 * droplets.
	 *
	 * @param delta The current delta time
	 */
	@Override
	public void update(float delta) {
		float deceleration = 0.5f;
		if (velocity > 0.01f) {
			velocity -= deceleration;
		} else if (velocity < -0.01f) {
			velocity += deceleration;
		} else {
			velocity = 0;
		}
		setRotation(-direction);
		float maxSpeed = speedLimit();
		// Checks the truck isn't breaking the speed limit.
		if (velocity > maxSpeed || velocity < -maxSpeed) {
			if (velocity > 0) {
				velocity = maxSpeed;
			} else if (velocity < 0) {
				velocity = -maxSpeed;
			}
		}
		drops.removeIf(Projectile::isDisposable);
		drops.forEach(drop -> drop.update(delta));

		setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
				(float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));
	}

	/**
	 * Overrides the Sprite draw method so water droplets can be drawn too
	 *
	 * @param batch The sprite batch to draw in
	 */
	@Override
	public void draw(Batch batch) {
		// drop is short for droplet
		drops.forEach(drop -> drop.draw(batch));
		super.draw(batch);
	}

	/**
	 * Calculates the truck's maximum speed and returns it.
	 *
	 * @return Speed limit
	 */
	private float speedLimit() {
		int pixcolour;
		// Checks either front or back of the truck sprite depending on whether the
		// truck is moving forwards or backwards
		if (velocity > 0) {
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX() + getOriginX() + ((float) Math.sin(direction * piConstant) * 9)),
					Gdx.graphics.getHeight()
							- Math.round(getY() + getOriginY() + ((float) Math.cos(direction * piConstant) * 9)));
		} else if (velocity > 0){
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX() + getOriginX() - ((float) Math.sin(direction * piConstant) * 9)),
					Gdx.graphics.getHeight()
							- Math.round(getY() + getOriginY() - ((float) Math.cos(direction * piConstant) * 9)));
		}	 else {
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX() + getOriginX()),
					Gdx.graphics.getHeight()
							- Math.round(getY() + getOriginY()));
		}

		// Convert 32-bit RGBA8888 integer to 3-bit hex code, using a mask.
		// Note: col is GBR instead of RGB
		String col = "#" + Integer.toHexString(pixcolour & 15790320);
		if (col.length() > 2) {
			col = col.substring(0, 7);
		}
		switch (col) {
		case ("#c070f0"):// buildings
			return 100f;
		case ("#d070f0"):// buildings 2
			return 100f;
		case ("#f0f0f0"):// road
			return 100f;
		case ("#f0c0f0"):// grass
			return 30f;
		case ("#6040f0"):// walls
			setVelocity(0f);
			return 0f;
		case ("#6050f0"):// walls 2
			setVelocity(0f);
			return 0f;
		case ("#e0f0f0"):// water
			setVelocity(0f);
			return 0f;
		case ("#c0f0f0"):// water 2
			setVelocity(0f);
			return 0f;
		case ("#8070f0"):
			if (water == maxWater) {
				setColor(Color.WHITE);
			}
			refill();
			setHealth((int) (getHealth() + 1));
			return 30f;
		case ("#0"):// off of map
			setVelocity(0f);
			return 0f;
		default:
			// System.err.println("Unknown colour");
			return 100f;
		}
	}

	/**
	 * Creates a new water droplet and launches it from the truck's position
	 */
	@Override
	public void attack() {
		if (drops.size() < 200 && water > 0) {
			takeWater(1);
			float flowRate = 40f;
			float range = 2f;
			float xdirection = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;
			float ydirection = Gdx.input.getY() - Gdx.graphics.getHeight() / 2;
			Vector2 directionVector = new Vector2 (xdirection,ydirection);
			float shootDirection = directionVector.angle() + 90;
			Projectile drop = new Projectile(
					(getX() + getOriginX() / 2) + ((float) Math.sin(shootDirection * piConstant) * 10),
					(getY() + getOriginY() / 2) + ((float) Math.cos(shootDirection * piConstant) * 10), shootDirection,
					flowRate + velocity, range, new Texture("drop.png"), "water");
			drops.add(drop);
		}
		if (water == 0) {
			setColor(Color.CYAN);
		}
	}
}