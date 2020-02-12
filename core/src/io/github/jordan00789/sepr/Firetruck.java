package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class Firetruck extends Entity implements Attack, Moveable {

	private int water, maxWater;
	private float direction = 0, velocity = 0;
	public ArrayList<Projectile> drops = new ArrayList<>();
	private float piConstant = (float) Math.PI / 180;
	private float STARTX, STARTY, damage, speed;
	private String attackType;

	/**
	 * Creates a Firetruck sprite using the texture provided, with the specified
	 * amounts of health and water.
	 *
	 * @param x			x position of truck
	 * @param y			y position of truck
	 * @param health	The amount of health the truck has
	 * @param maxWater	The maximum amount of water in the truck
	 * @param texture	The texture given to the Firetruck sprite
	 * @param damage	Damage of the firetruck
	 */
	public Firetruck(float x, float y, int health, int maxWater, Texture texture, float damage, float speed, String type) {
		super(health, texture);
		this.attackType = type;
		this.speed = speed;
		this.STARTX = x;
		this.STARTY = y;
		this.maxWater = this.water = maxWater;
		this.damage = damage;
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
	void setVelocity(float velocity) {
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
		return 4f;
	}

	/**
	 * Moves the truck forward.
	 */
	@Override
	public void goForward() {
		if (velocity < speedLimit()) {
			setVelocity(getVelocity() + (speed / 5));
		}
	}

	/**
	 * Moves the truck backward.
	 */
	@Override
	public void goBackward() {
		if (velocity > -speedLimit()) {
			setVelocity(getVelocity() - (speed / 5));
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

		setOriginCenter();
		setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
				(float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity) ));
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
	public  float speedLimit() {
		int pixcolour;
		// Checks either front, back or middle of the truck sprite depending on whether the
		// truck is moving forwards, backwards or is stationary
		if (velocity > 0) {
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX() + ((float) Math.sin(direction * piConstant) * 9)),
					Gdx.graphics.getHeight()
							- Math.round(getY() + ((float) Math.cos(direction * piConstant) * 9)));
		} else if (velocity < 0){
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX() - ((float) Math.sin(direction * piConstant) * 9)),
					Gdx.graphics.getHeight()
							- Math.round(getY() - ((float) Math.cos(direction * piConstant) * 9)));
		} else {
			pixcolour = MainGame.speedMap.getPixel(
					Math.round(getX()),
					Gdx.graphics.getHeight()
							- Math.round(getY()));
		}

		// Convert 32-bit RGBA8888 integer to 3-bit hex code, using a mask.
		// Note: col is GBR instead of RGB
		String col = "#" + Integer.toHexString(pixcolour & 15790320);

		switch (col) {
			case "#f0c0f0":
				return 30f;
			case "#6040f0":
			case "#e0f0f0":
			case "#6050f0":
			case "#c0f0f0":
			case "#0":
				setVelocity(0);
				return 0f;
			case "#8070f0":
				if (!MainGame.isFireStationDestroyed) {
					if (water > 0) { setColor(Color.WHITE); }
					refill();
					setHealth((int) (getHealth() + 1));
				}
				return 29f;
			default:
				return 100f;
		}
	}






	/**
	 * Creates a new water droplet and launches it from the truck's position
	 */
	@Override
	public void attack() {
		if (this.checkAttack()) {
			if (drops.size() < 200 && water > 0) {
				takeWater(1);
				float flowRate = 100f;
				float range = 1f;

				Vector3 mousePosInWorld = MainGame.camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
				float xDirection = mousePosInWorld.x - (getX());
				float yDirection = mousePosInWorld.y - (getY());
				Vector2 directionVector = new Vector2(xDirection, -yDirection);
				float shootDirection = directionVector.angle() + 90;
				Projectile drop = new Projectile(
						(getX()) + ((float) Math.sin(shootDirection * piConstant) * 10),
						(getY()) + ((float) Math.cos(shootDirection * piConstant) * 10), shootDirection,
						flowRate, range, new Texture("drop.png"), "water", this.damage);
				drops.add(drop);
			}
			if (water == 0) {
				setColor(Color.CYAN);
			}
		}
	}

	private boolean checkAttack() {
		return (this.attackType.equals("default") || this.attackType.equals("bigBoi") && this.velocity < 5 && this.velocity > -5);
	}

	public void brake() {
		this.velocity = (float)(this.velocity * 0.8);
	}

	public float getStartX() { return this.STARTX; }
	public float getStartY() { return this.STARTY; }
	public int getMaxWater() { return this.maxWater; }
}