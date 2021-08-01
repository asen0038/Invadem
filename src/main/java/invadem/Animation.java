package invadem;

import processing.core.PApplet;

/*Abstract class responsible for the different behaviours of all the game objects*/
public abstract class Animation {
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected float[] velocity;

    /*Constructor for a game object that takes its unique position, sprite dimension and movement velocity*/
    public Animation(float x, float y, float width, float height, float[] velocity){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
    }

    public abstract void tick(PApplet app); //Responsible for movement and game logic
    public abstract void draw(PApplet app); //Responsible for rendering sprites on screen

    //Getter for fields
    public float getX() { //returns the sprites x position
        return x;
    }

    public float getY() { //returns the sprites y position
        return y;
    }

    public float getWidth() { //returns the sprites width
        return width;
    }

    public float getHeight() { //returns the sprites height
        return height;
    }
}
