package cs112.ud3;

public class ArcherTower extends Tower {

    public ArcherTower() {
        super("Archer Tower", 15, 250.0, 50, 0.8);
    }

    @Override
    public void attack(Enemy enemy) {
        if (enemy != null && !enemy.isDefeated() && canAttack()) {
            enemy.takeDamage(getDamage());
            resetCooldown();
        }
    }
}