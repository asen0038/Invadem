package invadem;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import processing.core.*;

public class BarrierTest {

    /*Tests the constructor of barrier component*/
    @Test
    public void barrierConstruction() {
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "left", 200, 414, 8, 8, barVel);
        assertNotNull(b);
    }

    /*Tests that the barrier can be hit and not destroyed*/
    @Test
    public void testBarrierNotDestroyed() {
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "left", 200, 414, 8, 8, barVel);
        b.hit();
        b.hit();
        assertFalse(b.isDestroyed());
    }

    /*Tests that a barrier cannot be hit pasts its maximum hit points*/
    @Test
    public void testBarrierHitPointsMax() {
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "left", 200, 414, 8, 8, barVel);
        for(int i = 0; i < 10; i++){
            b.hit();
        }
        assertEquals(3, b.hitPoints());
    }

    /*Tests that a barrier can be destroyed if hit three times*/
    @Test
    public void testBarrierIsDestroyed() {
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "top", 208, 414, 8, 8, barVel);
        b.hit();
        b.hit();
        b.hit();
        assertTrue(b.isDestroyed());
    }

    /*Tests that a barrier can be destroyed in one shot if hit by the power invader's projectile*/
    @Test
    public void testBarrierInstaKill() {
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "right", 216, 414, 8, 8, barVel);
        b.instaKill();
        assertTrue(b.isDestroyed());
    }

    /*Tests the barriers initial setup/location and number of the components*/
    @Test
    public void testBarriersSetup(){
        App app = new App();
        PImage a = new PImage();
        PImage b = new PImage();
        PImage c = new PImage();
        PImage d = new PImage();
        List<Barrier> bar = app.setBarriers(a,b,c,d);
        assertEquals(21, bar.size());
    }

    /*Tests the intersection of the tank's projectile and a top barrier component*/
    @Test
    public void testBarrierHitByTank(){
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "top", 208, 414, 8, 8, barVel);

        float[] tankVel = {1,0};
        Tank tank = new Tank(img, 210, 440, 22, 14, tankVel);

        float[] projVel = {0, 1};
        Projectile proj = new Projectile(img, tank.getX(), tank.getY(), 1, 3, projVel);

        App a = new App();
        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(b, proj)){
                intersection = true;
                break;
            }
        }

        assertTrue(intersection);
    }

    /*Tests the intersection of the invader's projectile and a top barrier component*/
    @Test
    public void testBarrierHitByInvader(){
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "top", 208, 414, 8, 8, barVel);

        float[] invVel = {1,1};
        Invader inv = new Invader(img, img, "armoured", 210, 5, 16, 16, invVel);

        float[] projVel = {0, -1};
        Projectile proj = new Projectile(img, inv.getX(), inv.getY(), 1, 3, projVel);

        App a = new App();
        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj.tick(a);
            if(a.collisionImpact(b, proj)){
                intersection = true;
                break;
            }
        }
        assertTrue(intersection);

    }

    /*Tests the barriers method that is used to change its sprite after a hit*/
    @Test
    public void testBarrierImg(){
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b = new Barrier(img, "top", 208, 414, 8, 8, barVel);

        PImage img1 = new PImage();
        b.setImg(img1);
        assertEquals(img1, b.getImg());
    }

    /*Tests the barrier being hit by tank's and invader's projectile and disappearing after it is hit*/
    @Test
    public void testBarrierProjDetection(){
        App app = new App();
        PImage img = new PImage();
        float[] barVel = {0,0};
        Barrier b1 = new Barrier(img, "top", 208, 414, 8, 8, barVel);
        Barrier b2 = new Barrier(img, "solid", 300, 430, 8, 8, barVel);
        b2.hit();
        b2.hit();
        List<Barrier> bars = new ArrayList<>();
        bars.add(b1);
        bars.add(b2);

        float[] invVel = {1,1};
        Invader inv = new Invader(img, img, "power", 210, 414, 16, 16, invVel);
        List<Invader> invs = new ArrayList<>();
        invs.add(inv);
        float[] projVel = {0, -1};
        Projectile proj = new Projectile(img, inv.getX(), inv.getY(), 2, 5, projVel);
        inv.fire(proj);

        float[] tankVel = {1,0};
        Tank tank = new Tank(img, 300, 430, 22, 14, tankVel);
        float[] projVel1 = {0, 1};
        Projectile proj1 = new Projectile(img, tank.getX(), tank.getY(), 1, 3, projVel1);
        tank.fire(proj1);

        app.barrierProjDetection(bars, tank.getShots(), invs);
        app.barrierProjDetection(bars, tank.getShots(), invs);

        assertEquals(0, bars.size());

    }

}