package invadem;

/*This interface is responsible for maintaining logics of the tank, barrier and invaders*/
public interface GameObjects {
    boolean isDestroyed();
    void hit();
    int hitPoints();
}
