package io.github.jordan00789.sepr;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(GdxTestRunner.class)
public class ProjectileTest {

    private Projectile projectile;

    @Before
    public void init() {
        projectile = new Projectile(.0f, .0f, .0f, .1f, .10f, new Texture(Gdx.files.internal("../core/assets/blank.png")), "goo", 1);
    }

    @Test
    public void turnLeft() {
        projectile.turnLeft();
        assertTrue(projectile.getDirection() == -1);
    }

    @Test
    public void turnRight() {
        projectile.turnRight();
        assertTrue(projectile.getDirection() == 1);
    }

    @Test
    public void goForward() {
        projectile.setVelocity(0);
        projectile.goForward();
        assertTrue(projectile.getVelocity() == 1);
    }

    @Test
    public void goBackward() {
        projectile.setVelocity(0);
        projectile.goBackward();
        assertTrue(projectile.getVelocity() == -1);
    }
}