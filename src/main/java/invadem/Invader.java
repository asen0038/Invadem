package invadem;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class Invader extends Animation implements GameObjects, FireAbleObject{

    private int gotHit;
    private List<Projectile> shots;
    private PImage img;
    private PImage img1;
    private PImage img2;
    private String type;

    /*common variables used to monitor the tank movement pattern*/
    int invMovCount = 1;
    boolean invLeft = false;
    boolean invRight = true;
    boolean invDown = false;
    int invFrameCount = 0;
    int xOfst1 = 172;
    int xOfst2 = 460;

    /*Constructor of a invader overloaded with the sprite types and states*/
    public Invader(PImage img1, PImage img2, String type, float x, float y, float width, float height, float[] velocity){
        super(x, y, width, height, velocity);
        this.img = img1;
        this.img1 = img1;
        this.img2 = img2;
        this.type = type;
        this.shots = new ArrayList<Projectile>();
    }

    @Override
    public void tick(PApplet app) { //Accounts for movement
        if(invFrameCount % 2 == 0){ //Accounts for invaders movement at 1px per 2 frames

            if(xOfst2 == 475){ //once the common offset for all invaders reach this value (Rightmost invaders boundary)
                invDown = true; //The swarm will move down
                invLeft = false;
                invRight = false;
                invMovCount++; //moniters the amount by which the swarm will mve down until
            }

            if(xOfst1 == 157){ //once the common offset for all invaders reach this value (leftmost invaders boundary)
                invDown = true; //The swarm will move down
                invLeft = false;
                invRight = false;
                invMovCount++;
            }

            if(invMovCount % 8 == 0){ //The swarm moves 8 steps down
                invDown = false;
                if(xOfst2 == 475){ //Depending on which boundary it is, it will move left or right
                    invLeft = true;
                }
                if(xOfst1 == 157){
                    invRight = true;
                }
            }

            if(invRight && !invLeft && !invDown){ //increments x position and common offset
                this.x += 1;
                xOfst1++;
                xOfst2++;
            }
            if(invLeft && !invRight && !invDown){ //decrements x position and common offset
                this.x -= 1;
                xOfst1--;
                xOfst2--;
            }

            if(invRight || invLeft){ //Keeps track of the sprite state when moving left or right for different types of invaders
                if(this.type.equals("armoured")){
                    this.setImg(this.img1);
                }else if(this.type.equals("power")){
                    this.setImg(this.img1);
                }else{
                    this.setImg(this.img1);
                }
            }

            if(invDown && !invRight && !invLeft){ //Keeps track of the sprite state when moving down for different types of invaders
                if(this.type.equals("armoured")){
                    this.setImg(this.img2);
                }else if(this.type.equals("power")){
                    this.setImg(this.img2);
                }else{
                    this.setImg(this.img2);
                }
                this.y += 1;
            }
        }
        invFrameCount++; //Updates the invaders frame counter
    }

    @Override
    public void draw(PApplet app) { //renders the invaders in App draw function
        if(!isDestroyed()){ //Only renders if invader is not dead
            app.image(this.img, this.x, this.y);
            tick(app); //Updates movement while rendering
        }
    }

    public void setImg(PImage newImg){ //Changes the sprite state
        this.img = newImg;
    }

    public PImage getImg(){ //Returns the current sprite state
        return this.img;
    }

    public void fire(Projectile p){ //Fires projectile
        this.shots.add(p);
    }

    public List<Projectile> getShots(){ //returns the projectiles shot from this invader
        return this.shots;
    }

    public boolean isDestroyed(){ //Checks if the invader is destroyed or not
        if(this.type.equals("armoured")){
            if(this.gotHit == 3){ //Checks if invader has been hit thrice for armoured invader
                return true;
            }else{
                return false;
            }
        }else{
            if(this.gotHit == 1){ //Checks if invader has been hit once for power or regular invaders
                return true;
            }else{
                return false;
            }
        }
    }

    public String getType(){ //returns the type of invader
        return this.type;
    }

    public void hit(){ //Updates the number of times the invader is hit
        if(!isDestroyed()){ //Only updates if it is alive
            this.gotHit += 1;
        }
    }

    public int hitPoints(){ //returns the number of times invader has been hit
        return this.gotHit;
    }

}