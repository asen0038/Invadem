package invadem;

import java.util.List;

/*This interface can only be implemented by tank and invaders as they are the only ones that can shoot*/
public interface FireAbleObject {

    void fire(Projectile p);
    List<Projectile> getShots();

}
