package cs112.ud3;

public class TankEnemy extends Enemy {

    public TankEnemy() {
        super("Tank Enemy", 180, 0.6, 25);
    }

    @Override
    public String getEnemyType() {
        return "Tank";
    }
}