package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class ETPatrol extends Fortress implements Attack{
    private float x, y, damage, movex, movey, direction, velocity;
    private int health;

    public ETPatrol (float x, float y, int health){
        super(x,y,health, new Texture("badlogic.jpg"), 10);
        this.movex = x;
        this.movey = y;
    }

    public void update(float delta){
        super.update(delta);
        //If in range of the truck the ETPatrol will stop moving
        if(distanceTo(MainGame.currentTruck) < 100f) {
            this.velocity = 0;
        } else if (this.movex == this.x && this.movey == this.y ) {
            this.movex = (float) ((this.x + Math.random() - 0.5) * 20);
            this.movey = (float) ((this.y + Math.random() - 0.5) * 20);
            Vector2 moveVector = new Vector2(movex,movey);
            this.direction = moveVector.angle() + 90;
            this.velocity = 3;
        }
        setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
                (float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));
    }
}
