package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Entity extends Sprite{

	private int health;
	private int maxHealth;

	/**
	 * Creates a new entity with the specified texture and health.
	 *
	 * @param health  The amount of health the Fortress has
	 * @param texture The texture given to the entity sprite
	 */
	public Entity(int health, Texture texture) {
		super(texture);
		this.health = health;
		maxHealth = health;
	}

	/**
	 * Returns the current health of the entity.
	 *
	 * @return The current health of the entity
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * Sets the health of the entity, if the value passed is less than maxHealth
	 *
	 * @param health The value to set health to
	 */
	protected void setHealth(int health) {
		if (health <= maxHealth) {
			this.health = health;
		}
	}

	/**
	 * Removes the specified amount of health from the entity.
	 *
	 * @param damage The amount of health to remove
	 */
	public void takeDamage(int damage) {
		health -= damage;
	}
	void takeDamage(float damage) { health -= damage; }

	/**
	 * Returns true if the entity has a health value less than or equal to 0.
	 *
	 * @return A boolean of whether the entity is destroyed or not
	 */
	public boolean isDestroyed() {
		return (getHealth() <= 0);
	}

	/**
	 * Updates the entity. If not overridden, does nothing.
	 *
	 * @param delta The current delta time
	 */
	public void update(float delta) {
	}

	/**
	 * Calculate the direction from the current entity to another entity.
	 *
	 * @param e The entity to calculate the direction of
	 * @return The direction of the entity
	 */
	float directionTo(Entity e) {
		return directionTo(e.getX(), e.getY());
	}

	/**
	 * Calculate the direction from the entity to a point.
	 *
	 * @param x The x-coordinate of the point
	 * @param y The y-coordinate of the point
	 * @return The direction of the entity
	 */
	public float directionTo(float x, float y) {
		return (float) ((180 / Math.PI) * Math.atan2(x - getX(), y - getY()));
	}

	/**
	 * Calculate the distance from the current entity to another entity.
	 *
	 * @param e The entity to calculate the distance to
	 * @return The distance to the entity
	 */
	float distanceTo(Entity e) {
	    return distanceTo(e.getX(), e.getY());
	}

	/**
	 * Calculate the distance from the entity to a point.
	 *
	 * @param farx The x-coordinate of the point
	 * @param fary The y-coordinate of the point
	 * @return The distance to the point
	 */
	private float distanceTo(float farx, float fary) {
		Vector2 vector = new Vector2 (getX() - farx, getY() - fary);
		return vector.len();
	}

	//Ensures all co-ordinates are relative to the centre of the entity rather than the corner of the unscaled sprite
	@Override
	public void setPosition(float x, float y){ super.setPosition(x - getWidth() / 2,y - getHeight() / 2);}
	@Override
	public float getX() {return super.getX() + getWidth() / 2;}
	@Override
	public float getY() {return super.getY() + getHeight() / 2;}
}