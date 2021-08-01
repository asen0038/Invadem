package invadem;
import processing.core.PApplet;
import processing.core.PImage;

public class Barrier extends Animation implements GameObjects{

    private boolean destroyed;
    private int gotHit;
    private final int MAX_HIT = 3;
    private PImage currImg;
    private String type;

    /*Barrier component costructor*/
    public Barrier(PImage currImg, String type, float x, float y, float width, float height, float[] velocity){
        super(x, y, width, height, velocity);
        this.currImg = currImg;
        this.type = type;
        this.destroyed = false;
    }

    @Override
    public void tick(PApplet app) { //Accounts for sprite change for different number of hits
        if(this.gotHit == 1){ //First hit sprite state change for different types of components
            if(this.type.equals("left")){
                PImage barrier_left2 = app.loadImage("src/main/resources/barrier_left2.png");
                this.setImg(barrier_left2);
            }
            if(this.type.equals("top")){
                PImage barrier_top2 = app.loadImage("src/main/resources/barrier_top2.png");
                this.setImg(barrier_top2);
            }
            if(this.type.equals("right")){
                PImage barrier_right2 = app.loadImage("src/main/resources/barrier_right2.png");
                this.setImg(barrier_right2);
            }
            if(this.type.equals("solid")){
                PImage barrier_solid2 = app.loadImage("src/main/resources/barrier_solid2.png");
                this.setImg(barrier_solid2);
            }

        }if(this.gotHit == 2){ //Second hit sprite state change for different types of components
            if(this.type.equals("left")){
                PImage barrier_left3 = app.loadImage("src/main/resources/barrier_left3.png");
                this.setImg(barrier_left3);
            }
            if(this.type.equals("top")){
                PImage barrier_top3 = app.loadImage("src/main/resources/barrier_top3.png");
                this.setImg(barrier_top3);
            }
            if(this.type.equals("right")){
                PImage barrier_right3 = app.loadImage("src/main/resources/barrier_right3.png");
                this.setImg(barrier_right3);
            }
            if(this.type.equals("solid")){
                PImage barrier_solid3 = app.loadImage("src/main/resources/barrier_solid3.png");
                this.setImg(barrier_solid3);
            }
        }
    }

    @Override
    public void draw(PApplet app) { //Renders the barrier component
        if(!this.destroyed){ //Renders only if it is not destroyed
            app.image(this.currImg, this.x, this.y);
            tick(app); //Updates state of types while rendering
        }
    }

    public void instaKill(){ //Destroys the barrier component completely if intersected with a big projectile
        this.destroyed = true;
    }

    public void setImg(PImage newImg){ //sets the sprite image
        this.currImg = newImg;
    }

    public PImage getImg(){ //Returns the current sprite state
        return this.currImg;
    }

    public boolean isDestroyed(){ //Checks if the barrier is destroyed or not
        if(this.gotHit == this.MAX_HIT){ //Checks if barrier has been hit 3 times
            this.destroyed = true;
            return true;
        }else{
            return this.destroyed;
        }
    }

    public void hit(){ //Updates the number of times the barrier is hit
        if(!isDestroyed()){ //Only updates if barrier is not destroyed
            this.gotHit += 1;
        }
    }

    public int hitPoints(){ //returns the number of times the barrier component has been hit
        return this.gotHit;
    }
}