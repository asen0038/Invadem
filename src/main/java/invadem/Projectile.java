package invadem;
import processing.core.PApplet;
import processing.core.PImage;

public class Projectile extends Animation{
    private PImage img;

    /*Projectile constructor*/
    public Projectile(PImage img, float x, float y, float width, float height, float[] velocity){
        super(x, y, width, height, velocity);
        this.img = img;
    }

    @Override
    public void tick(PApplet app) { //Updates the velocity depending on which object fired
        this.y -= velocity[1];
        //tank projectile velocity is 1 so its position decrements and goes up
        //invaders projectile velocity is -1 so its position increments and goes down
    }

    @Override
    public void draw(PApplet app) { //Renders the projectile on screen
        app.image(this.img, this.x, this.y);
        tick(app); //updates movement while rendering
    }

}