package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;

import java.time.Instant;
import java.time.Duration;

public class Projectile extends Entity implements Moveable {

    private float direction;
    private float velocity;
    private float lifeTime;
    private Instant startTime;
    private boolean disposable = false;

    /**
     * Initialises a projectile using the provided parameters
     *
     * @param x         The initial x-coordinate
     * @param y         The initial y-coordinate
     * @param direction The direction to travel in (in degrees)
     * @param velocity  The velocity to travel at
     * @param lifeTime  The life time of the projectile
     * @param texture   The projectile texture
     */
    public Projectile(float x, float y, float direction, float velocity, float lifeTime, Texture texture) {
        super(1, texture);
        this.setPosition(x, y);
        this.setScale(0.05f);
        this.direction = direction;
        setRotation(180 - direction);
        this.velocity = velocity;
        this.lifeTime = lifeTime;
        startTime = Instant.now();

        // System.out.printf("Projectile: x=%f, y=%f, direction=%f, v=%f,
        // lifetime=%f%n", x,y,direction,velocity,lifeTime);
    }

    @Override
    public void turnLeft() {
        direction--;
    }

    @Override
    public void turnRight() {
        direction++;
    }

    @Override
    public void goForward() {
        velocity++;
    }

    @Override
    public void goBackward() {
        velocity--;
    }

    /**
     * Updates the movement of the projectile.
     *
     * @param delta The current delta time.
     */
    @Override
    public void update(float delta) {

        setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
                (float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));

        String col = MainGame.getPixelColour(getX() + getOriginX(), getY() + getOriginY());
        if (col.equals("#6040f0") || col.equals("#6050f0")) {
            disposable = true;
        }
        Entity e;
        for (int i = 0; i < MainGame.entities.size(); i++) {
            e = MainGame.entities.get(i);
            if (!disposable) {
                if (distanceTo(e) < 10f) {
                    e.takeDamage(MainGame.getFortDamage());
                    takeDamage(1);
                    disposable = true;
                    break;
                }
            }
        }
    }

	/**
	 * Returns whether the projectile is disposable or not.

	 * @return True if the projectile is disposable
	 */
    boolean isDisposable() {
		return ((Duration.between(startTime, Instant.now()).getSeconds()) > lifeTime || disposable);
	}

    public float getDirection() {
        return direction;
    }

    public float getVelocity() {
        return velocity;
    }
}