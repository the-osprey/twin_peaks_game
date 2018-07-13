package byog.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private static final long serialVersionUID = 8358178841294L;
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
