package io.github.jordan00789.sepr;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;

@RunWith(GdxTestRunner.class)
public class FiretruckTest {

    private Firetruck truck;
    private Kroy kroy;

    @Before
    public void init() {
        truck = new Firetruck(100, 500, 100, 100, new Texture("../core/assets/truck1.png"), 10, 10, "default");
        kroy = new Kroy();
        kroy.setScreen(new MainGame(kroy));
    }

    @Test
    public void testInitialisation() {
        org.junit.Assert.assertTrue(truck.getHealth() == 100);
        org.junit.Assert.assertTrue(truck.getWater() == 500);
    }

    @Test
    public void testRefill() {
        truck.takeWater(50);
        assertTrue(truck.getWater() == 450);
        truck.refill();
        assertTrue(truck.getWater() == 451);
    }

    @Test
    public void testTurning() {
        assertTrue(truck.getDirection() == 0);
        truck.turnRight();
        assertTrue(truck.getDirection() > 0);
        truck.turnLeft();
        assertTrue(truck.getDirection() == 0);
    }

    @Test
    public void testUpdate() {
        /** assertTrue(truck.getVelocity() == 0);
         truck.goForward();
         float v = truck.getVelocity();
         assertTrue(v > 0);
         truck.update(1);
         assertTrue(truck.getVelocity() < v);
         **/
    }

    @Test
    public void testAttack() {
        assertTrue(truck.drops.isEmpty());
        truck.attack();
        assertTrue(!truck.drops.isEmpty());
    }

    @After
    public void clean() {
        truck.getTexture().dispose();
    }

}
