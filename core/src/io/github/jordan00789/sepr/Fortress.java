package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class Fortress extends Entity implements Attack {

    private ArrayList<Projectile> goos = new ArrayList<Projectile>();
    private int fortressNumber;
    private boolean ableToAttack = true;
    private float posX, posY, damage;

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
        this.posX = 0;
        this.posY = 0;
        this.fortressNumber = n;
        this.damage = 20;
        System.out.println("Fortress created incorrectly");
    }
    public Fortress(float x, float y, int health, Texture texture, float damage) {
        super(health, texture);
        this.posX = x;
        this.posY = y;
        this.damage = damage;
        this.fortressNumber = 1;
    }

    /**
     * Attempts to attack the specified entity, should be currentFiretruck.
     *
     * @param e The entity to aim at.
     * @param n The offset of the projectile from the entity in degrees
     */
    private void attack(Entity e, int n) {
        if (e != null) {
            if (ableToAttack) {
                float piConstant = (float) Math.PI / 180;
                Projectile goo = new Projectile((getX() + 384) + ((float) Math.sin(directionTo(e) * piConstant) * 10),
                        (getY() + 384 + ((float) Math.cos(directionTo(e) * piConstant) * 10)), directionTo(e) + n, MainGame.getFortProjectileSpeed(),
                        1f, new Texture("goo.png"),"goo", damage);
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
                case 5:
                    attack(MainGame.currentTruck, 0);
                    break;
                case 3:
                    for (int i = 0; i <= 360; i += 30) {
                        attack(MainGame.currentTruck, i);
                    }
                    break;
                case 4:
                    attack(MainGame.currentTruck, 0);
                    attack(MainGame.currentTruck, -30);
                    attack(MainGame.currentTruck, 30);
                    break;
            }
            ableToAttack = false;
        }
        goos.removeIf(Projectile::isDisposable);
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

    public float getPosX() { return this.posX; }
    public float getPosY() { return this.posY; }
}
