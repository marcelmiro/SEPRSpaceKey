package io.github.jordan00789.sepr;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.graphics.Texture;

@RunWith(GdxTestRunner.class)
public class FiretruckTest {

    private Firetruck truck;
    private Kroy kroy;

    @Before
    public void init() {
        truck = new Firetruck(100, 500, 100, 100, new Texture(Gdx.files.internal("../core/assets/blank.png")), 10, 10, "default");
        //kroy = new Kroy();
        //kroy.setScreen(new MainGame(kroy));
    }

    @Test
    public void testInitialisation() {
        System.out.println("reib");
        System.out.println(truck.getWater());
        org.junit.Assert.assertTrue(truck.getHealth() == 100);
        org.junit.Assert.assertTrue(truck.getWater() == 100);
    }

    @Test
    public void testRefill() {
        truck.takeWater(50);
        assertTrue(truck.getWater() == 50);
        truck.refill();
        assertTrue(truck.getWater() == 51);
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

    /**
     * We can't test the attack method as it uses things that the headless backend does not support. However we can test that the
     */

    @Test
    public void testAttack() {

        truck.setWater(truck.getMaxWater());
        assertTrue(truck.drops.isEmpty());

        truck.takeWater(1);
        assertTrue(truck.getWater() == (truck.getMaxWater() - 1));
    }

    @After
    public void clean() {
        truck.getTexture().dispose();
    }

}
