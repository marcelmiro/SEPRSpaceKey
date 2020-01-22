package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class EntityTest {

    private Entity entity;

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
}