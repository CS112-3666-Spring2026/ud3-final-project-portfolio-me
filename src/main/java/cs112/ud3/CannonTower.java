package cs112.ud3;

public class CannonTower extends Tower {

    public CannonTower() {
        super("Cannon Tower", 40, 180.0, 75, 1.8);
    }

    @Override
    public void attack(Enemy enemy) {
        if (enemy != null && !enemy.isDefeated() && canAttack()) {
            enemy.takeDamage(getDamage());
            resetCooldown();
        }
    }
}