package io.github.jordan00789.sepr;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ETPatrol extends Fortress implements Attack{
    private float x, y, movex, movey, direction, velocity, movetimer;

    public ETPatrol (float x, float y, int health){
        super(x,y,health, new Texture("badlogic.jpg"), 10);
        this.movex = this.x;
        this.movey = this.y;
    }

    public void update(float delta){

        this.movetimer += Gdx.graphics.getDeltaTime();
        this.x = getX();
        this.y = getY();
        //If in range of the truck the ETPatrol will stop moving
        if(distanceTo(MainGame.currentTruck) < 100f) {
            this.velocity = 0;
        } else if ((java.lang.Math.abs(this.movex - this.x) <= 1) ||  (java.lang.Math.abs(this.movey - this.y) <= 1) || this.movetimer > 5 ) { //Patrol will select a new target
            this.movex = (float) (this.x + (Math.random() - 0.5) * 75);
            this.movey = (float) (this.y + (Math.random() - 0.5) * 75);
            this.direction = directionTo(movex,movey);
            this.movetimer = 0;
            this.velocity = 0;
        } else { //Patrol will move
            this.velocity = 15;
        }
        super.update(delta);
        setPosition((float) (getX() + (Math.sin(Math.toRadians(direction)) * delta * velocity)),
                (float) (getY() + (Math.cos(Math.toRadians(direction)) * delta * velocity)));
    }

    public void draw(Batch batch){
        super.draw(batch);
    }
}
