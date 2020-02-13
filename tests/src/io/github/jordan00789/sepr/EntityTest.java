package io.github.jordan00789.sepr;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import jdk.internal.jline.internal.TestAccessible;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class EntityTest {

    private Entity entity;

    /*
    @Before
    public void init() {
        entity = new Entity(100, new Texture("../core/assets/truck1.png"));
    }


    @org.junit.Test
    public void getHealth() {
        assertTrue(entity.getHealth() == 100);
    }

    @org.junit.Test
    public void setHealth() {
        entity.setHealth(50);
        assertTrue(entity.getHealth() == 50);

    }

    @org.junit.Test
    public void takeDamage() {
        entity.takeDamage(50);
        assertTrue(entity.getHealth() == 50);
    }

    @org.junit.Test
    public void isDestroyed() {
        entity.setHealth(0);
        assertTrue(entity.isDestroyed());
        entity.takeDamage(10);
        assertTrue(entity.isDestroyed());
        entity.setHealth(100);
        assertTrue(!entity.isDestroyed());
    }

     */

    /**
     * Initialise the entity
     */
    @Before
    public void init() {
        entity = new Entity(100, new Texture(Gdx.files.internal("../core/assets/blank.png")));
    }

    /**
     * Test that we can chek the health of the entity
     */
    @Test
    public void getHealth() {
        assertTrue("The health should be 100", entity.getHealth() == 100);
    }

    /**
     * Test that the entity can take damage
     */
    @Test
    public void testTakeDamage() {
        entity.takeDamage(50);
        assertTrue("The health should now be 50", entity.getHealth() == 50);
    }

    /**
     * Test that we can correctly destroy the entity
     */
    @Test
    public void testIsDestroyed() {
        entity.setHealth(0);
        assertTrue("The entity should be destroyed", entity.isDestroyed());
        entity.takeDamage(10);
        assertTrue("The entity should be destroyed", entity.isDestroyed());
        entity.setHealth(100);
        assertTrue("The entity should not be destroyed", !entity.isDestroyed());
    }


}