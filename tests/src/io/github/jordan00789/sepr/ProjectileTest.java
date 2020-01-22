package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class ProjectileTest {

    private Projectile projectile;

    @Before
    public void init() {
        projectile = new Projectile(0, 0, 0, 0, 10, new Texture("../core/assets/badlogic.png"));
    }

    @org.junit.Test
    public void turnLeft() {
        projectile.turnLeft();
        assertTrue(projectile.getDirection() == -1);
    }

    @org.junit.Test
    public void turnRight() {
        projectile.turnRight();
        assertTrue(projectile.getDirection() == 1);
    }

    @org.junit.Test
    public void goForward() {
        projectile.goForward();
        assertTrue(projectile.getVelocity() == 1);
    }

    @org.junit.Test
    public void goBackward() {
        projectile.goBackward();
        assertTrue(projectile.getVelocity() == -1);
    }
}