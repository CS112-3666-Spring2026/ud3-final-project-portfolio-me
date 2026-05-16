package cs112.ud3;

public class BasicEnemy extends Enemy {

    public BasicEnemy() {
        super("Basic Enemy", 100, 1.0, 10);
    }

    @Override
    public String getEnemyType() {
        return "Basic";
    }
}