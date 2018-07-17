package byog.Core;

import java.io.Serializable;
import java.util.ArrayList;

public class Position implements Serializable {
    private static final long serialVersionUID = 8358178841294L;
    int x;
    int y;
//    ArrayList<Position> adj = new ArrayList<>(); // new Position[]{new Position(this.x + 1, this.y),
//            new Position(this.x - 1, this.y),
//            new Position(this.x, this.y + 1),
//            new Position(this.x, this.y - 1)};

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
//        if(this.x + 1 < WorldGenerator.WIDTH) {
////            System.out.println(this.x+1);
//            adj.add(new Position(this.x + 1, this.y));
//        }
//        if(this.x - 1 > 0) {
////            System.out.println(this.x+1);
//            adj.add(new Position(this.x - 1, this.y));
//        }
//        if(this.y + 1 < WorldGenerator.HEIGHT) {
//            adj.add(new Position(this.x, this.y + 1));
//        }
//        if(this.y - 1 > 0) {
//            adj.add(new Position(this.x, this.y - 1));
//        }
    }
}
