package invadem;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import processing.core.*;

public class TankTest{

    /*Tests the constructor of the tank*/
    @Test
    public void testTankConstruction() {
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        assertNotNull(tank);
    }

    /*Tests if the tank can fire projectiles and keep track of the number of projectile it has fired*/
    @Test
    public void testTankProjectile() {
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);

        PImage img1 = new PImage();
        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img1, tank.getX(), tank.getY(), 1, 3, projVel);

        tank.fire(proj);
        tank.fire(proj);
        int p = tank.getShots().size();
        assertEquals(2,p);
   }

    /*Tests that the tank can be hit twice and not die*/
    @Test
    public void testTankIsNotDead() {
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.hit();
        tank.hit();
        assertFalse(tank.isDestroyed());
    }

    /*Tests the tank being kiled by one shot from the power invader*/
    @Test
    public void testTankKilledByPower(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.instaKill();
        assertTrue(tank.isDestroyed());
    }

    /*Tests that the tank can be hit 3 times and die*/
    @Test
    public void testTankIsDead(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.hit();
        tank.hit();
        tank.hit();
        assertTrue(tank.isDestroyed());
    }

    /*Tests tanks left movement*/
    @Test
    public void testTankMoveLeft(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.moveLeft();
        tank.moveLeft();
        tank.moveLeft();
        assertEquals(317, tank.getX(), 0);
    }

    /*Tests tank right movement*/
    @Test
    public void testTankMoveRight(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.moveRight();
        tank.moveRight();
        tank.moveRight();
        assertEquals(323, tank.getX(), 0);
    }

    /*Tests tank overall movement*/
    @Test
    public void testTankMoveLeftRight(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.moveLeft();
        tank.moveLeft();
        tank.moveLeft();
        tank.moveRight();
        tank.moveRight();
        tank.moveRight();
        assertEquals(320, tank.getX(), 0);
    }

    /*Tests that the tank cannot move past its left boundry*/
    @Test
    public void testTankLeftBoundry(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        for(int i = 0; i < 300; i++){
            tank.moveLeft();
        }
        assertEquals(180, tank.getX(), 0);
    }

    /*Tests that the tank cannot move past its right boundry*/
    @Test
    public void testTankRightBoundry(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        for(int i = 0; i < 300; i++){
            tank.moveRight();
        }
        assertEquals(460, tank.getX(), 0);
    }

    /*Tests that if the tank is in range of the invader then it can hit the tank*/
    @Test
    public void testHitFromInvader(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);

        PImage img1 = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img1, img1, "armoured", 320, 1, 16, 16, invVel);

        PImage img2 = new PImage();
        float[] projVel = {0, -1};
        Projectile proj = new Projectile(img2, inv.getX(), inv.getY(), 1, 3, projVel);

        App a = new App();
        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(tank, proj)){
                intersection = true;
                break;
            }
        }

        assertTrue(intersection);
    }

    /*Tests that if the tank is not in range of the invader then it cannot hit the tank*/
    @Test
    public void testNotHitFromInvader(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);

        PImage img1 = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img1, img1, "armoured", 200, 1, 16, 16, invVel);

        PImage img2 = new PImage();
        float[] projVel = {0, -1};
        Projectile proj = new Projectile(img2, inv.getX(), inv.getY(), 1, 3, projVel);

        App a = new App();
        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(tank, proj)){
                intersection = true;
                break;
            }
        }

        assertFalse(intersection);
    }

    /*Tests that the tank can move and shoot from anywhere within its range*/
    @Test
    public void testTankHits(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.hit();
        tank.moveLeft();
        tank.hit();

        assertEquals(2, tank.hitPoints());
    }

    /*Tests that the tank cannot be hit past its maximum hit points*/
    @Test
    public void testTankHitLimit(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        tank.hit();
        tank.hit();
        tank.hit();
        tank.hit();
        assertEquals(3, tank.hitPoints(), 0);
    }

    /*Tests that the tank will not be moved by any function and only depend on key press events*/
    @Test
    public void testTankMoveAuthority(){
        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        App a = new App();
        tank.tick(a);
        assertEquals(320, tank.getX(), 0);
    }

    /*Tests the collision method of the invader's projectile with the tank*/
    @Test
    public void testTankProjDetection(){
        PImage img = new PImage();
        float[] invVel = {1, 0};
        Invader inv = new Invader(img, img, "power", 320, 440, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv);
        float[] projVel = {0, -1};
        Projectile proj = new Projectile(img, inv.getX(), inv.getY(), 2, 5, projVel);
        inv.fire(proj);

        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);

        App app = new App();
        app.tankProjDetect(invs, tank);

        assertTrue(app.gameStat());
    }

}
