package invadem;

import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Tank extends Animation implements GameObjects, FireAbleObject{

    private PImage img;
    private boolean destroyed;
    private int gotHit;
    private final int MAX_HIT = 3;
    private List<Projectile> shots;

    /*Constructor for the tank object*/
    public Tank(PImage img, float x, float y, float width, float height, float[] velocity){ //Constructor making a tank object
        super(x, y, width, height, velocity); //Inherited constructor
        this.img = img; //Overloaded field that sets the sprite image
        this.destroyed = false; //Overloaded field that ensures the tank is alive when newly constructed
        this.shots = new ArrayList<Projectile>(); //Overloaded field that will hold the projectiles to be shot
    }

    @Override
    public void tick(PApplet app) { //In tank class this method won't do anything as the tank movement is depended on key press events

    }

    @Override
    public void draw(PApplet app) { //This will help in drawing the sprite on screen and will be used in App draw function
        if(!this.destroyed){ //Will only draw if it is not dead
            app.image(this.img, this.x, this.y); //draws sprite at its position
            tick(app); //Accounts for movement, but in this case not required unless modification made to the game
        }
    }

    public void moveRight(){ //moves tank right
        if(this.x < 460){ //Checks for right boundary
            this.x += this.velocity[0]; //increments horizontal velocity
        }
    }

    public void moveLeft(){ //moves tank left
        if(this.x > 180){ //Checks for left boundary
            this.x -= this.velocity[0]; //decrements horizontal velocity
        }
    }

    public void fire(Projectile p){ //Fires projectile
        this.shots.add(p); //adds projectile to the list of projectile the tanks has shot
    }

    public List<Projectile> getShots(){ //returns the list of projectiles the tank has shot
        return this.shots;
    }

    public void instaKill(){ //Kills the tank immediately when hit by the big projectile
        this.destroyed = true;
    }

    public boolean isDestroyed(){ //Checks if the tank is destroyed or not
        if(this.gotHit == this.MAX_HIT){ //Checks if tank has been hit 3 times
            this.destroyed = true;
            return true;
        }else{
            return this.destroyed; //returns when not destroyed and will return the correct condition when hit by a big projectile
        }
    }

    public void hit(){ //Updates the number of times the tank is hit
        if(!isDestroyed()){ //Only updates if tank is alive
            this.gotHit += 1;
        }
    }

    public int hitPoints(){ //returns the number of times the tank has been hit
        return this.gotHit;
    }
}