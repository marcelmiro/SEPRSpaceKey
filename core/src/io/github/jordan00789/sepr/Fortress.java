package io.github.jordan00789.sepr;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Fortress extends Entity implements Attack {

    public ArrayList<Projectile> goos = new ArrayList<Projectile>();
    private float piConstant = (float) Math.PI / 180;
    int fortressNumber;
    boolean ableToAttack = true;

    /**
     * Creates a Fortress sprite using the texture provided, with the specified
     * amount of health.
     *
     * @param health  The amount of health the Fortress has
     * @param texture The texture given to the Fortress sprite
     * @param n       The number that the fortress is
     */
    public Fortress(int health, Texture texture, int n) {
        super(health, texture);
        this.fortressNumber = n;
    }

    /**
     * Attempts to attack the specified entity, should be currentFiretruck.
     *
     * @param e The entity to aim at.
     */
    public void attack(Entity e, int n) {
        if (e != null) {
            if (ableToAttack) {
                Projectile goo = new Projectile((getX() + 384) + ((float) Math.sin(directionTo(e) * piConstant) * 10),
                        (getY() + 384 + ((float) Math.cos(directionTo(e) * piConstant) * 10)), directionTo(e) + n, MainGame.getFortProjectileSpeed(),
                        1f, new Texture("goo.png"));
                goos.add(goo);

            }
        } else {
            System.err.println("Fortresses must target an entity");
        }
    }

    /**
     * A method to catch any attack method calls that aren't aimed at an entity.
     */
    @Override
    public void attack() {
        attack(null, 0);
    }

    /**
     * Attacks currentTruck if it's in range, and updates the goo projectiles.
     *
     * @param delta The current delta time.
     */
    @Override
    public void update(float delta) {
        if (distanceTo(MainGame.currentTruck) < 100f) {
            // choose different attack method for each fortress
            switch (fortressNumber) {
                case 1:
                    attack(MainGame.currentTruck, 0);
                    attack(MainGame.currentTruck, 30);
                    attack(MainGame.currentTruck, -30);
                    break;
                case 2:
                    attack(MainGame.currentTruck, 0);
                    break;
                case 3:
                    for (int i = 0; i <= 360; i += 45) {
                        attack(MainGame.currentTruck, i);
                    }
                    break;
            }
            ableToAttack = false;
        }
        goos.removeIf(goo -> goo.isDisposable());
        goos.forEach(goo -> goo.update(delta));

        if (goos.size() < 1) {
            ableToAttack = true;
        }
    }

    /**
     * Overrides the Sprite draw method so goo projectiles can be drawn too.
     *
     * @param batch The sprite batch to draw in
     */
    @Override
    public void draw(Batch batch) {
        goos.forEach(goo -> goo.draw(batch));
        super.draw(batch);
    }

    /**
     * Calculate the direction from the fortress to a point.
     *
     * @param x The x-coordinate of the point
     * @param y The y-coordinate of the point
     * @return The direction of the entity
     */
    @Override
    public float directionTo(float x, float y) {
        return (float) ((180 / Math.PI) * Math.atan2(x - (getX() + 384), y - (getY() + 384)));
    }

}
