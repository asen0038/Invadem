package invadem;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import processing.core.*;

public class ProjectileTest {

    /*Tests the constructor of the projectile*/
    @Test
    public void testProjectileConstruction() {
        PImage p = new PImage();
        float[] proVel = {0, 1};
        Projectile proj = new Projectile(p, 320, 440, 1, 3, proVel);
        assertNotNull(proj);
   }

    /*Tests the collision of a tank's projectile with an invader's projectile*/
    @Test
    public void testProjectileToProjectile() {
        App a = new App();

        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        PImage p = new PImage();
        float[] proVel = {0, 1};
        Projectile proj1 = new Projectile(p, tank.getX(), tank.getY(), 1, 3, proVel);

        PImage img2 = new PImage();
        float[] invVel = {1, 1};
        Invader inv = new Invader(img2, img2, "regular", 320, 440,16, 16, invVel);
        float[] proVel1 = {0, -1};
        Projectile proj2 = new Projectile(p, inv.getX(), inv.getY(), 1, 3, proVel1);

        boolean intersection = false;
        for(int i = 0; i < 440; i++){
            proj1.tick(a);
            proj2.tick(a);
            if(a.collisionImpact(proj1, proj2)){
                intersection = true;
                break;
            }
        }

        assertTrue(intersection);
    }

    /*Tests that a tank's projectile moves at 1px per frame*/
    @Test
    public void testTankProjectileVelocity(){
        App a = new App();
        PImage p = new PImage();
        float[] proVel = {0, 1};
        Projectile proj = new Projectile(p, 320, 420, 1, 3, proVel);
        proj.tick(a);
        proj.tick(a);
        proj.tick(a);
        assertEquals(417, proj.getY(), 0);
    }

    /*Tests that an invader's projectile moves at 1px per frame*/
    @Test
    public void testInvProjectileVelocity(){
        App a = new App();
        PImage p = new PImage();
        float[] proVel = {0, -1};
        Projectile proj = new Projectile(p, 320, 1, 1, 3, proVel);
        proj.tick(a);
        proj.tick(a);
        proj.tick(a);
        assertEquals(4, proj.getY(), 0);
    }

    /*Tests that the projectile continues to exist even if the invader that fired it is dead*/
    @Test
    public void testProjectileExistAfterInvArmDead(){
        App a = new App();
        PImage img = new PImage();
        float[] invVel = {1, 1};
        Invader inv = new Invader(img, img, "regular", 320, 440,16, 16, invVel);
        float[] proVel = {0, -1};
        Projectile proj = new Projectile(img, inv.getX(), inv.getY(), 1, 3, proVel);
        inv.fire(proj);
        inv.hit();
        inv.hit();
        inv.hit();

        assertEquals(1, inv.getShots().size());
    }

    /*Tests the projectile to projectile collision and updates the number of tank's projectiles once the projectile disappears*/
    @Test
    public void testProjToProjBigTank(){
        App a = new App();

        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        PImage p = new PImage();
        float[] proVel = {0, 1};
        Projectile proj1 = new Projectile(p, tank.getX(), tank.getY(), 1, 3, proVel);
        tank.fire(proj1);

        PImage img2 = new PImage();
        float[] invVel = {1, 1};
        Invader inv = new Invader(img2, img2, "regular", 320, 440,16, 16, invVel);
        float[] proVel1 = {0, -1};
        Projectile proj2 = new Projectile(p, inv.getX(), inv.getY(), 1, 3, proVel1);
        inv.fire(proj2);
        List<Invader> in = new ArrayList<>();
        in.add(inv);
        a.projToProjCollision(in, tank.getShots());

        assertEquals(0, tank.getShots().size());

    }

    /*Tests the projectile to projectile collision and updates the number of invader's projectiles once the projectile disappears*/
    @Test
    public void testProjToProjBigInv(){
        App a = new App();

        PImage img = new PImage();
        float[] tankVel = {1, 0};
        Tank tank = new Tank(img, 320, 440, 22, 14, tankVel);
        PImage p = new PImage();
        float[] proVel = {0, 1};
        Projectile proj1 = new Projectile(p, tank.getX(), tank.getY(), 1, 3, proVel);
        tank.fire(proj1);

        PImage img2 = new PImage();
        float[] invVel = {1, 1};
        Invader inv = new Invader(img2, img2, "regular", 320, 440,16, 16, invVel);
        float[] proVel1 = {0, -1};
        Projectile proj2 = new Projectile(p, inv.getX(), inv.getY(), 1, 3, proVel1);
        inv.fire(proj2);
        List<Invader> in = new ArrayList<>();
        in.add(inv);
        a.projToProjCollision(in, tank.getShots());

        assertEquals(0, inv.getShots().size());

    }

}
