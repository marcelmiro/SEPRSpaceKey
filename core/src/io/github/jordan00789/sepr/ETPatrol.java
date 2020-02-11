package io.github.jordan00789.sepr;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

public class ETPatrol extends Fortress implements Attack{
    private float x, y, movex, movey, direction, velocity;

    public ETPatrol (float x, float y, int health){
        super(x,y,health, new Texture("badlogic.jpg"), 10);

        this.movex = this.x;
        this.movey = this.y;
    }

    public void update(float delta){

        //If in range of the truck the ETPatrol will stop moving
        if(distanceTo(MainGame.currentTruck) < 100f) {
            this.velocity = 0;
        } else if (this.movex - this.x <= 1 || this.movey - this.y <= 1 ) { //Patrol will select a new target
            this.movex = (float) (this.x + (Math.random() - 0.5) * 5);
            this.movey = (float) (this.y + (Math.random() - 0.5) * 5);
            this.direction = directionTo(movex,movey);
        } else { //Patrol will move
            this.velocity = 15;
            this.x = this.getX();
            this.y = this.getY();
        }
        super.update(delta);

        setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
                (float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));


    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}
