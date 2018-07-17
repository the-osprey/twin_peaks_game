package byog.Core;

import java.io.Serializable;

public class Enemy extends User implements Serializable {
    public Enemy(Position p) {
        this.position = p;
    }
}