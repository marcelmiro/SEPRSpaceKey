package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.time.Duration;
import java.time.Instant;

public class Projectile extends Entity implements Moveable {

    private float direction;
    private float velocity;
    private float lifeTime;
    private Instant startTime;
    private boolean disposable = false;
    private final String type;
    private float damage;

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
    public Projectile(float x, float y, float direction, float velocity, float lifeTime, Texture texture, String type, float damage) {
        super(1, texture);

        this.setScale(0.05f);
        this.type = type;
        this.direction = direction;
        setRotation(180 - direction);
        this.velocity = velocity;
        this.lifeTime = lifeTime;
        startTime = Instant.now();
        this.damage = damage;
        this.setPosition(x, y);
        Gdx.app.debug("Projectile", "Projectile successfully created at (" + getX() + "," + getY() + ")");
    }
        // System.out.printf("Projectile: x=%f, y=%f, direction=%f, v=%f,
        // lifetime=%f%n", x,y,direction,velocity,lifeTime);

    @Override
    public void turnLeft() {
        direction--;
    }

    @Override
    public void turnRight() {
        direction++;
    }

    @Override
    public void goForward() { velocity++; }

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

        setOriginCenter();
        setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
                (float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));
        String col = MainGame.getPixelColour(getX(), getY());
        if (col.equals("#6040f0") || col.equals("#6050f0") || col.equals("#0") ) {
            disposable = true;
        }

        Entity e;
        for (int i = 0; i < MainGame.entities.size(); i++) {
            e = MainGame.entities.get(i);
            if (!disposable) {
                if (((distanceTo(e) < 10f && (e instanceof Firetruck || e instanceof ETPatrol)) || (distanceTo(e) < 25f && (e instanceof Fortress && !(e instanceof ETPatrol)))) && ((e instanceof Firetruck && this.type.equals("goo")) || (e instanceof Fortress && this.type.equals("water")))) {
                    if (type.equals("goo")) {
                        e.takeDamage(MainGame.getFortDamage() + this.damage);
                    } else {
                        e.takeDamage(this.damage);
                    }
                    takeDamage(1);
                    disposable = true;
                    break;
                }
            }
        }
    }

	/**
	 * Returns whether the projectile is disposable or not.

	 * @return True if the projectile is disposable)
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

    public void setVelocity(float vel) {
        this.velocity = vel;
    }


}