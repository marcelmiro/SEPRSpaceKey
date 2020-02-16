package io.github.jordan00789.sepr;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class FortressTest {

    private Fortress fortress;
    private Kroy kroy;
    private Texture texture;
    private Entity testEntity;

    /*

    Commented out old tests which do not work

    @Before
    public void init() {
        fortress = new Fortress(100, new Texture("../core/assets/ctower.png"), 2);
    }

    @Test
    public void testDirectionToFloatFloat() {
        assertTrue(fortress.directionTo(fortress.getX() + 394, fortress.getY() + 384) == 90);
    }
     */

    @Before
    public void init() {
        texture = new Texture(Gdx.files.internal("../core/assets/blank.png"));
        fortress = new Fortress(20.0f, 20.0f, 150, texture, 30);
    }


    /**
     * Test that the method directionTo() can find the direction to another entity
     */
    @Test
    public void testDirectionTo() {
        testEntity = new Entity(100, texture);
        testEntity.setPosition(10.0f, 0.0f);

        fortress.setPosition(10.0f, 10.0f);

       assertTrue("The direction should be 180 degrees", fortress.directionTo(testEntity) == 180.0f);
    }

    /**
     * Test that the fortress class can find the distance to another entity
     */
    @Test
    public void testDistanceTo() {

        testEntity = new Entity(100, texture);
        testEntity.setPosition(10.0f, 0.0f);

        fortress.setPosition(10.0f, 10.0f);

        assertTrue("The distance should be 10", fortress.distanceTo(testEntity) == 10.0f);

    }

}
