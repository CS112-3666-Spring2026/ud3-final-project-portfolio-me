package cs112.ud3;

public class FastEnemy extends Enemy {

    public FastEnemy() {
        super("Fast Enemy", 60, 2.0, 5);
    }

    @Override
    public String getEnemyType() {
        return "Fast";
    }
}