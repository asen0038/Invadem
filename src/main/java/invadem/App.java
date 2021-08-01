package invadem;

import java.io.*;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;
import javax.sound.sampled.*;

public class App extends PApplet {

    /*Tank fields*/
    private Tank player;
    private boolean right = false; //Used to store the arrow keys pressed states
    private boolean left = false;
    private boolean tankFired = true; //Used to store the space press state to allow one press one shot
    private boolean space;
    private boolean spaceRel;

    /*Barrier fields*/
    private List<Barrier> barriers;

    /*Invader Fields*/
    private List<Invader> invaders;
    private List<Invader> aliveProj;
    Random rnd; //Used for random selection of invaders to shoot
    int shootTime = 5; //Used for maintaning the fire rate at levels
    int shootTimer;

    /*Score fields*/
    private PFont disFont;
    private int highscore = 10000;
    private int currScore = 0;

    /*game status and counter fields*/
    private PImage over;
    private PImage nextLevel;
    private boolean gameOver;
    private int counter;


    public App() {
        //Constructor of App
    }

    public void setup() { //Setup of the game objects and logic which can be called after a game round has finished
        frameRate(60); //frame rate
        background(0); //Total black background
        disFont = createFont("PressStart2P-Regular.ttf", 20); //Font type and size
        textFont(disFont); //Font is a text
        shootTimer = 0; //Accounts for when the invader will shoot
        counter = 0; //Accounts for the time game over of next level is shown

        //Setting up the tank
        PImage tanker = loadImage("src/main/resources/tank1.png");
        float[] tankVel = {1, 0};
        player = new Tank(tanker, 320, 440, 22, 14, tankVel);

        //Setting up the barriers
        PImage barrier_left1 = loadImage("src/main/resources/barrier_left1.png");
        PImage barrier_right1 = loadImage("src/main/resources/barrier_right1.png");
        PImage barrier_solid1 = loadImage("src/main/resources/barrier_solid1.png");
        PImage barrier_top1 = loadImage("src/main/resources/barrier_top1.png");
        barriers = setBarriers(barrier_left1, barrier_top1, barrier_right1, barrier_solid1);

        //Setting up the invader swarm
        PImage armoured_invader1 = loadImage("src/main/resources/invader1_armoured.png");
        PImage armoured_invader2 = loadImage("src/main/resources/invader2_armoured.png");
        PImage power_invader1 = loadImage("src/main/resources/invader1_power.png");
        PImage power_invader2 = loadImage("src/main/resources/invader2_power.png");
        PImage invader1 = loadImage("src/main/resources/invader1.png");
        PImage invader2 = loadImage("src/main/resources/invader2.png");
        invaders = setInvaders(armoured_invader1, armoured_invader2, power_invader1, power_invader2, invader1, invader2);
        aliveProj = new ArrayList<>(invaders); //A copy of invaders so the states of invaders do not disturb the state of its projectile
        rnd = new Random();

        /*Setting up the game status*/
        this.nextLevel = loadImage("src/main/resources/nextlevel.png");
        this.over = loadImage("src/main/resources/gameover.png");
        gameOver = false;

    }

    public void settings() {
        size(640, 480); //screen size

    }

    public void draw() { 
        //Main Game Loop
        background(0); //black background rendered each time
        String highScore = Integer.toString(highscore); //scores coverted to string for display
        String score = Integer.toString(currScore);
        textSize(8); //setting text size
        fill(255); //setting the text color to white
        text("Current Score\n" + score, 30, 30); //Displaying score
        text("High Score\n" + highScore, 520, 30);

        /* GAME LOGIC */
        if(!gameOver){//When game is not over

            /* INVADER LOGIC */
            for(Invader n : invaders){//Rendering all the invaders
                n.draw(this);
            }
            invaderProjDetection(invaders, player.getShots()); //Check performed for dead/alive invaders

            /* INVADER PROJECTILE LOGIC */
            if(shootTimer % (shootTime*60) == 0){ //Formula that accounts for firing rate depending on the level
                int x = rnd.nextInt(invaders.size()); //Selects a random alive invader for shooting
                if(invaders.get(x).getType().equals("power")){ //Long projectile generation for power invader
                    PImage bullet = loadImage("src/main/resources/projectile_lg.png");
                    float[] proVel = {0, -1};
                    Projectile invProj = new Projectile(bullet, invaders.get(x).getX(), invaders.get(x).getY(), 2, 5, proVel);
                    invaders.get(x).fire(invProj);
                }else{ //Short Projectile generation for regular and armoured invaders
                    PImage bullet = loadImage("src/main/resources/projectile.png");
                    float[] proVel = {0, -1};
                    Projectile invProj = new Projectile(bullet, invaders.get(x).getX(), invaders.get(x).getY(), 1, 3, proVel);
                    invaders.get(x).fire(invProj);
                }
            }

            /* PROJECTILE TO PROJECTILE COLLISION LOGIC*/
            projToProjCollision(aliveProj, player.getShots()); //Collision check performed to see if a tank's projectile collides with an invader's projectile

            for(Invader n : aliveProj){ //Rendering the projectiles of each of the invaders that have fired a projectile
                for(Projectile p : n.getShots()){
                    p.draw(this);
                }
            }

            /* BARRIER LOGIC */
            for(Barrier a : barriers){//Renders every barrier component in its assigned place
                a.draw(this);
            }

            //Collison check performed to see if a barrier component has been hit by the tank or any invader or not
            barrierProjDetection(barriers, player.getShots(), aliveProj);

            /* TANK LOGIC */
            player.draw(this); //Renders Tank

            if(this.right){ //If right key pressed
                player.moveRight();
            }
            if(this.left){ //If left key pressed
                player.moveLeft();
            }

            if(this.spaceRel){ //If space released
                tankFired = true;
            }
            //TankFired is initially true
            if(this.space && tankFired){ //Generating the tank's projectile
                PImage bullet = loadImage("src/main/resources/projectile.png");
                float[] proVel = {0, 1};
                Projectile tankProj = new Projectile(bullet, player.getX() + 11, player.getY(), 1, 3, proVel);
                player.fire(tankProj);
                tankFired = false; //Allows only one shot per press
            }


            /* TANK PROJECTILE LOGIC */
            for(Projectile p : player.getShots()){ //Rendering all the tank's projectiles
                p.draw(this);
            }

            //Performs collision check if the tank has been hit by any invader or not
            tankProjDetect(aliveProj, player);

        }

        /* GAME OVER LOGIC */
        if(gameOver){
            if(counter <= 120){ //Display win/loss screen for two seconds
                if(invaders.size() == 0){//player win
                    image(this.nextLevel, 280, 224);
                }else{ //player loss
                    image(this.over, 280, 224);
                }
            }else if(invaders.size() == 0){
                if(shootTime != 1){ //Updating the invaders firing rate for next level
                    shootTime--;
                }
                this.setup(); //Reinitialise all game objects
            }else{
                if(currScore > highscore){ //updates highscore
                    highscore = currScore;
                }
                currScore = 0; //Reset conditions
                shootTime = 5;
                this.setup(); //Reinitialise all game objects
            }

            counter++;
        }

        shootTimer++;
    }


    public static void main(String[] args) {
        PApplet.main("invadem.App"); //Running the application

        /* SOUND LOGIC */
        File music = new File("src/main/resources/spaceinvaders.wav"); //sound file
        App a = new App(); //used to access play method of the app
        while(true){ //runs until the app is closed
            a.playMusic(music); //plays music in the original Space Invaders 1978
        }

    }

    public void playMusic(File file){//Plays music from given file

        try{
            Clip c = AudioSystem.getClip(); //Built in class in javax that gets the audio system
            c.open(AudioSystem.getAudioInputStream(file)); //opens the sound/music file
            c.start(); //Begins playing music

            Thread.sleep(c.getMicrosecondLength()/1000); //Program sleeps for the audio's milliseconds

        }catch(Exception e){

        }
    }

    @Override
    public void keyPressed(){ //overridden method that allows for key press events to be handled through booleans
        if(keyCode == 39){ //right key pressed
            this.right = true;
        }if(keyCode == 37){ //left key pressed
            this.left = true;
        }if(key == ' '){ //space pressed
            this.space = true;
            this.spaceRel = false; //Accounts for tankfired state
        }
    }

    @Override
    public void keyReleased(){ //overridden method that allows for key release events to be handled through booleans
        if(keyCode == 39){ //right key released
            this.right = false;
        }if(keyCode == 37){  //left key released
            this.left = false;
        }if(key == ' '){ //space released
            this.space = false;
            this.spaceRel = true; //Updates the tankfired state
        }
    }

    public boolean collisionImpact(Animation a, Projectile p){ //Collison detection algorithm between two game objects
        if(a.getX() < (p.getX() + p.getWidth()) &&
                (a.getX() + a.getWidth()) > p.getX() &&
                a.getY() < (p.getY() + p.getHeight()) &&
                (a.getY() + a.getHeight()) > p.getY()){
            return true;
        }else{
            return false;
        }
    }

    /*Barrier setup in its position*/
    public List<Barrier> setBarriers(PImage a, PImage b, PImage c, PImage d){
        List<Barrier> s = new ArrayList<Barrier>();
        float[] barVel = {0,0};
        float xS = 200; //Initial (Left) Barrier positioning
        float xE = 216;
        for(int i = 0; i < 3; i++){ //3 sets of barriers
            for(float y = 414; y <= 430; y+=8){ //y positions of barriers
                for(float x = xS; x <= xE; x+=16){ //x position of barriers
                    //Barriers add in terms of type and position
                    if(y == 414 && x == xS){
                        Barrier left = new Barrier(a, "left", x, y, 8, 8, barVel);
                        Barrier top = new Barrier(b, "top", x + 8, y, 8, 8, barVel);
                        s.add(left);
                        s.add(top);
                    }else if(y == 414 && x == xE){
                        Barrier right = new Barrier(c, "right", x, y, 8, 8, barVel);
                        s.add(right);
                    }else{
                        Barrier solid = new Barrier(d, "solid", x, y, 8, 8, barVel);
                        s.add(solid);
                    }
                }
            }
            xS += 119; //updating barrier positioning (middle, right)
            xE += 119;
        }
        return s;
    }

    /*Setting up the invader swarm in their positions according to their types*/
    public List<Invader> setInvaders(PImage a, PImage b, PImage c, PImage d, PImage e, PImage f){
        List<Invader> s = new ArrayList<Invader>();
        float[] invVel = {1, 1};
        for(float y = 1; y <= 97; y+=32){ //y positions of the invaders
            for(float x = 172; x <= 460; x+=32){ //x positions of the invaders
                //invaders added according to their types and positions
                if(y == 1){
                    Invader armoured = new Invader(a, b, "armoured", x, y, 16, 16, invVel);
                    s.add(armoured);
                }else if(y == 33){
                    Invader power = new Invader(c, d, "power", x, y, 16, 16, invVel);
                    s.add(power);
                }else{
                    Invader regular = new Invader(e, f, "regular", x, y, 16, 16, invVel);
                    s.add(regular);
                }

            }
        }
        return s;
    }

    /*Projectile to Projectile collision check*/
    public void projToProjCollision(List<Invader> inv, List<Projectile> pro){
        for(Invader n : inv){ //checks for projectiles fired by every invader
            for(Projectile p : n.getShots()){
                int old = n.getShots().size();
                int next = Integer.MAX_VALUE;
                for(Projectile q : pro){ //checks projectiles fired by tank
                    next = old;
                    if(collisionImpact(p, q)){ //If collision detected then the projectile is removed
                        pro.remove(q); //projectile is removed once intersected
                        next--;
                        break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                    }
                }
                if(next < old){ //if the invaders projectile has collided then it will also be removed
                    n.getShots().remove(p); //projectile is removed once intersected
                    break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                }
            }
        }
    }

    /*Collision check for barrier being hit by tank's or any invader's projectile*/
    public void barrierProjDetection(List<Barrier> bar, List<Projectile> pro, List<Invader> inv){
        for(Barrier a : bar){
            for(Projectile p : pro){
                if(collisionImpact(a, p)){
                    a.hit();
                    pro.remove(p); //projectile is removed once intersected
                    break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                }
            }

            for(Invader n : inv){
                for(Projectile p : n.getShots()){
                    if(collisionImpact(a, p)){
                        if(p.getWidth() == 2){ //If barrier is hit by a big projectile then it is completely destroyed
                            a.instaKill();
                        }
                        a.hit();
                        n.getShots().remove(p); //projectile is removed once intersected
                        break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                    }
                }
            }

            if(a.isDestroyed()){ //If barrier is destroyed then it is removed
                bar.remove(a); //barrier is removed if destroyed
                break; //must break to avoid index out of bounds error and can be done safely as it will be called again
            }
        }
    }

    public boolean gameStat(){ //return the game's state wether it is over or not
        return this.gameOver;
    }

    /*collision check of whether the tank's projectile has hit any invader or not*/
    public void invaderProjDetection(List<Invader> invs, List<Projectile> pro){

        if(invs.size() == 0){ //if all invaders have been hit then it is game over player won
            gameOver = true;
        }

        for(Invader n : invs){

            if(n.getY() == 395){ //if invader is 10px above the barrier then the game is over and the player has lost
                gameOver = true;
            }

            for(Projectile p : pro){
                if(collisionImpact(n, p)){
                    if(n.getType().equals("power")){
                        currScore += 250; //updates score for hitting a power invader
                    }else if(n.getType().equals("regular")){
                        currScore += 100; //updates score for hitting a regular invader
                    }
                    n.hit();
                    pro.remove(p); //projectile is removed once intersected
                    break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                }
            }

            if(n.isDestroyed()){
                if(n.getType().equals("armoured")){ //updates score for hitting an armoured invader
                    currScore += 250; //score only updated after destroyed as it has to be hit 3 times to die
                }
                invs.remove(n); //invader is removed if dead
                break; //must break to avoid index out of bounds error and can be done safely as it will be called again
            }

        }
    }

    /*collision check performed to see if the tank has been hit by any invader's projectile*/
    public void tankProjDetect(List<Invader> invs, Tank tank){

        for(Invader n : invs){
            for(Projectile p : n.getShots()){
                if(collisionImpact(tank, p)){
                    if(p.getWidth() == 2){
                        tank.instaKill(); //tank is killed immediately if hit by a big projectile
                    }
                    tank.hit();
                    n.getShots().remove(p); //projectile is removed once intersected
                    break; //must break to avoid index out of bounds error and can be done safely as it will be called again
                }
            }
        }

        if(tank.isDestroyed()){ //if tank is destroyed then the game is over and player has lost
            gameOver = true;
        }
    }

}