package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 8358178841294L;
    int x;
    int y;
    Position[] adj = new Position[]{new Position(this.x + 1, this.y),
            new Position(this.x - 1, this.y),
            new Position(this.x, this.y + 1),
            new Position(this.x, this.y - 1)};

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
