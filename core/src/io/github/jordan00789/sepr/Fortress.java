package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.ArrayList;

public class Fortress extends Entity implements Attack {

    private float startX, startY, damage, timer;
    private int maxHealth;
    private boolean ableToAttack = true;
    private ArrayList<Projectile> goos = new ArrayList<>();
    private String textureDirectory;
    private Texture barHealth, barBg;

    /**
     * Creates a Fortress sprite using the texture provided, with the specified
     * amount of health.
     *
     * @param x, y    The position of the fort
     * @param health  The amount of health the Fortress has
     * @param texture The texture given to the Fortress sprite
     * @param damage  The damage of the fort
     */
    Fortress(float x, float y, int health, Texture texture, float damage) {
        super(health, texture);
        this.startX = x;
        this.startY = y;
        this.damage = damage;
        this.textureDirectory = String.valueOf(texture);
        this.timer = 0;
        this.maxHealth = health;

        this.createGUI();
    }

    private void createGUI() {
        Pixmap pHealth = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pHealth.setColor(0, 1,0,1);
        pHealth.fill();

        Pixmap pBg = new Pixmap(5, 5, Pixmap.Format.RGBA8888);
        pBg.setColor(1, 0,0,1);
        pBg.fill();

        barHealth = new Texture(pHealth);
        barBg = new Texture(pBg);
    }

    /**
     * Attempts to attack the specified entity, should be currentFiretruck.
     *
     * @param e The entity to aim at.
     * @param n The offset of the projectile from the entity in degrees
     */
    public void attack(Entity e, int n) {
        if (e != null) {
            this.timer += Gdx.graphics.getDeltaTime();
            if (ableToAttack) { //Will create new goo at the fortress targeting the Trucks
                if (this.timer >= .05) {
                    addGoo(e, n);
                }
            } else if (this.textureDirectory.equals("tower.png")) {
                if (this.timer >= .15) {
                    addGoo(e, n);
                }
            } else if (this instanceof ETPatrol) {
                this.timer += Gdx.graphics.getDeltaTime();
                if (this.timer >= .15) {
                    addGoo(e, n);
                }
            }
        } else {
            System.err.println("Fortresses must target an entity");
        }
    }
    private void attack(float n) {
        if (ableToAttack || this.textureDirectory.equals("university.png")) {
            Projectile goo = new Projectile(getX(), getY(), n, MainGame.getFortProjectileSpeed(),1f, new Texture("goo.png"),"goo", damage);
            goos.add(goo);
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
            switch (this.textureDirectory) {
                case "clifford.png":
                case "tower.png":
                case "ufo.png":
                    attack(MainGame.currentTruck, 0);
                    break;
                case "minster.png":
                    attack(MainGame.currentTruck, 0);
                    attack(MainGame.currentTruck, -20);
                    attack(MainGame.currentTruck, 20);
                    break;
                case "museum.png":
                    for (int i = 0; i <= 360; i += 24) {
                        attack(i);
                    }
                    break;
                case "station.png":
                    attackRandom();
                    break;
                case "university.png":
                    attackSpiral();
                    break;
            }
            ableToAttack = false;
        }
        goos.removeIf(Projectile::isDisposable);
        goos.forEach(goo -> goo.update(delta));

        if (goos.size() < 3) {
            ableToAttack = true;
        }
    }

    private float temp = 0;
    private void attackSpiral() {
        this.timer += Gdx.graphics.getDeltaTime();

        if (this.timer >= .2) {
            for (float i = temp; i < temp + 360; i += 90) {
                attack(i);
            }
            temp += 10;
            this.timer = 0;

            if (distanceTo(MainGame.currentTruck) < 100f) {
                temp = temp >= 360 ? 0 : temp;
                attackSpiral();
            }
        }
    }

    private void attackRandom() {
        for (int i = 0; i < 12; i++) {
            double degree = Math.random() * 361;
            attack((float) degree);
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
        batch.draw(barBg, this.getX() - 20, this.getY() + 30, 40, 3);
        batch.draw(barHealth, this.getX() - 20, this.getY() + 30, (getHealth() / this.maxHealth) * 40, 3);
        super.draw(batch);
    }

    float getStartX() { return this.startX; }
    float getStartY() { return this.startY; }

    /**
     * Calculate the direction from the current entity to another entity.
     *
     * @param e The entity to calculate the direction of
     * @return The direction of the entity
     */
    float directionTo(Entity e) {
        if (this instanceof ETPatrol){
            super.directionTo(e);
        }
        return directionTo(e.getX(), e.getY());
    }

    private void addGoo(Entity e, float n){
        float piConstant = (float) Math.PI / 180;
        Projectile goo = new Projectile((getX()) + ((float) Math.sin(directionTo(e) * piConstant) * 10),
                ((getY()) + ((float) Math.cos(directionTo(e) * piConstant) * 10)), directionTo(e) + n, MainGame.getFortProjectileSpeed(),
                1f, new Texture("goo.png"),"goo", damage);
        goos.add(goo);
        this.timer = 0;
    }
}
