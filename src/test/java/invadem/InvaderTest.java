package invadem;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import processing.core.*;


public class InvaderTest {

    /*Tests the constructor of the invader*/
    @Test
    public void testInvaderConstruction() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);
        assertNotNull(inv);
    }

    /*Tests if the invader can fire projectiles and keep track of the number of projectiles fired*/
    @Test
    public void testInvaderFireProjectile() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);

        PImage img1 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img1, inv.getX(), inv.getY(), 1, 3, projVel);

        inv.fire(proj);
        inv.fire(proj);
        inv.fire(proj);

        assertEquals(3, inv.getShots().size());
    }

    /*Tests the armoured invader can be hit twice and not die*/
    @Test
    public void testInvaderArmIsNotDead() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);
        inv.hit();
        inv.hit();
        assertFalse(inv.isDestroyed());
    }

    /*Tests that a regular invader is never dead until hit*/
    @Test
    public void testInvaderRegIsNotDead() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "regular", 320, 1, 16, 16, invVel);
        assertFalse(inv.isDestroyed());
    }

    /*Tests that a power invader is never dead until hit*/
    @Test
    public void testInvaderPowIsNotDead() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 320, 1, 16, 16, invVel);
        assertFalse(inv.isDestroyed());
    }

    /*Tests that an armoured invader will die if hit three times*/
    @Test
    public void testInvaderArmIsDead() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);
        inv.hit();
        inv.hit();
        inv.hit();
        assertTrue(inv.isDestroyed());
    }

    /*Tests that a regular invader will die if hit once*/
    @Test
    public void testInvaderRegIsDead() {
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "regular", 320, 1, 16, 16, invVel);
        inv.hit();
        assertTrue(inv.isDestroyed());
    }

    /*Tests that a power invader will die if hit once*/
    @Test
    public void testInvaderPowIsDead(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 320, 1, 16, 16, invVel);
        inv.hit();
        assertTrue(inv.isDestroyed());
    }

    /*Tests the intersection of a tank's projectile hitting the invader when in range*/
    @Test
    public void testInvaderIntersectWithProjectile() {
        App a = new App();

        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 320, 1, 16, 16, invVel);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 440, 22, 14, tankVel);

        PImage img2 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img2, tank.getX(), tank.getY(), 1, 3, projVel);

        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(inv, proj)){
                intersection = true;
                break;
            }
        }

        assertTrue(intersection);
    }

    /*Tests the intersection of a tank's projectile not hitting the invader when not in range*/
    @Test
    public void testInvaderNotIntersectPlayerProjectile() {
        App a = new App();

        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 200, 1, 16, 16, invVel);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 440, 22, 14, tankVel);

        PImage img2 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img2, tank.getX(), tank.getY(), 1, 3, projVel);

        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(inv, proj)){
                intersection = true;
                break;
            }
        }

        assertFalse(intersection);
    }

    /*Tests the armoured invader's hit counts cannot go past its maximum hit limit*/
    @Test
    public void testInvArmHitLimit(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);
        for(int i = 0; i < 10; i++){
            inv.hit();
        }

        assertEquals(3, inv.hitPoints());
    }

    /*Tests the power invader's hit counts cannot go past its maximum hit limit*/
    @Test
    public void testInvPowHitLimit(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 320, 1, 16, 16, invVel);
        for(int i = 0; i < 10; i++){
            inv.hit();
        }
        assertEquals(1, inv.hitPoints());
    }

    /*Tests the regular invader's hit counts cannot go past its maximum hit limit*/
    @Test
    public void testInvRegHitLimit(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "regular", 320, 1, 16, 16, invVel);
        for(int i = 0; i < 10; i++){
            inv.hit();
        }
        assertEquals(1, inv.hitPoints());
    }

    /*Tests the hit count being updated when the armoured invader is hit*/
    @Test
    public void testInvArmHits(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);
        inv.hit();
        inv.hit();
        assertEquals(2, inv.hitPoints());
    }

    /*Tests the sprite change method that is used to change the image of invaders when they move down*/
    @Test
    public void testInvChangeSprite(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 320, 1, 16, 16, invVel);

        PImage img2 = new PImage();
        inv.setImg(img2);

        assertEquals(img2, inv.getImg());
    }

    /*Tests the initial setup and creation of the 40 invaders*/
    @Test
    public void testGameInvaderStart(){
        App app = new App();
        PImage a = new PImage();
        PImage b = new PImage();
        PImage c = new PImage();
        PImage d = new PImage();
        PImage e = new PImage();
        PImage f = new PImage();
        List<Invader> invaders = app.setInvaders(a,b,c,d,e,f);
        assertEquals(40, invaders.size());
    }

    /*Tests the invaders right movement*/
    @Test
    public void testInvadersRight(){
        App a = new App();
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 460, 1, 16, 16, invVel);

        for(int i = 0; i < 30; i++){
            inv.tick(a);
        }

        assertTrue(inv.invRight);
    }

    /*Tests the invaders down movement*/
    @Test
    public void testInvadersDown(){
        App a = new App();
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 460, 1, 16, 16, invVel);

        for(int i = 0; i < 42; i++){
            inv.tick(a);
        }

        assertTrue(inv.invDown);
    }

    /*Tests the invaders left movement*/
    @Test
    public void testInvadersLeft(){
        App a = new App();
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 460, 1, 16, 16, invVel);

        for(int i = 0; i < 70; i++){
            inv.tick(a);
        }

        assertTrue(inv.invLeft);
    }

    /*Tests the invaders overall movement starting from top middle and going the pattern of 30 steps*/
    @Test
    public void testAllInvadersMove(){
        App app = new App();
        PImage a = new PImage();
        PImage b = new PImage();
        PImage c = new PImage();
        PImage d = new PImage();
        PImage e = new PImage();
        PImage f = new PImage();
        List<Invader> invaders = app.setInvaders(a,b,c,d,e,f);

        for(Invader n : invaders){
            for(int i = 0; i < 70; i++){
                n.tick(app);
            }
        }

        Random r = new Random();
        int x = r.nextInt(invaders.size());

        assertTrue(invaders.get(x).invLeft);
    }

    /*Tests that the invaders have a certain type (armoured/power/regular) that can be accessed*/
    @Test
    public void testInvaderType(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "armoured", 460, 1, 16, 16, invVel);
        assertEquals("armoured", inv.getType());
    }

    /*Tests collision of the tank's projectile with the armoured invader in range and update the number of invaders alive*/
    @Test
    public void testInvaderArmProjDetect(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv1 = new Invader(img, img, "armoured", 320, 340, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv1);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 340, 22, 14, tankVel);
        PImage img2 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img2, tank.getX(), tank.getY(), 1, 3, projVel);
        tank.fire(proj);
        tank.fire(proj);
        tank.fire(proj);

        App app = new App();
        app.invaderProjDetection(invs, tank.getShots());
        app.invaderProjDetection(invs, tank.getShots());
        app.invaderProjDetection(invs, tank.getShots());

        assertEquals(0, invs.size());
    }

    /*Tests collision of the tank's projectile with the power invader in range and update the number of invaders alive*/
    @Test
    public void testInvaderPowProjDetect(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv2 = new Invader(img, img, "power", 320, 340, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv2);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 340, 22, 14, tankVel);
        PImage img2 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img2, tank.getX(), tank.getY(), 1, 3, projVel);
        tank.fire(proj);

        App app = new App();
        app.invaderProjDetection(invs, tank.getShots());

        assertEquals(0, invs.size());
    }

    /*Tests collision of the tank's projectile with the regular invader in range and update the number of invaders alive*/
    @Test
    public void testInvaderRegProjDetect(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv3 = new Invader(img, img, "regular", 320, 340, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv3);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 340, 22, 14, tankVel);
        PImage img2 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img2, tank.getX(), tank.getY(), 1, 3, projVel);
        tank.fire(proj);

        App app = new App();
        app.invaderProjDetection(invs, tank.getShots());

        assertEquals(0, invs.size());
    }

    /*Tests when an invader has reached 10px above barrier and player has lost the game*/
    @Test
    public void testInvaderWin(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv3 = new Invader(img, img, "regular", 320, 395, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv3);

        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 340, 22, 14, tankVel);

        App app = new App();
        app.invaderProjDetection(invs, tank.getShots());

        assertTrue(app.gameStat());
    }

    /*Tests when all the invaders are killed and the player has won the game*/
    @Test
    public void testInvaderLose(){
        List<Invader> invs = new ArrayList<>();
        PImage img1 = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img1, 320, 340, 22, 14, tankVel);
        App app = new App();
        app.invaderProjDetection(invs, tank.getShots());

        assertTrue(app.gameStat());
    }
}


