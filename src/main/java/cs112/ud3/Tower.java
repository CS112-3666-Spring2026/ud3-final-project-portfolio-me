package cs112.ud3;

public abstract class Tower {
    private String name;
    private int damage;
    private double range;
    private int cost;
    private double cooldownSeconds;
    private long lastAttackTime;

    public Tower(String name, int damage, double range, int cost, double cooldownSeconds) {
        setName(name);
        setDamage(damage);
        setRange(range);
        setCost(cost);
        setCooldownSeconds(cooldownSeconds);
        lastAttackTime = 0;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public double getRange() {
        return range;
    }

    public int getCost() {
        return cost;
    }

    public double getCooldownSeconds() {
        return cooldownSeconds;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setName(String name) {
        if (name == null || name.trim().equals("")) {
            this.name = "Unknown Tower";
        } else {
            this.name = name;
        }
    }

    public void setDamage(int damage) {
        if (damage < 0) {
            this.damage = 0;
        } else {
            this.damage = damage;
        }
    }

    public void setRange(double range) {
        if (range <= 0) {
            this.range = 1.0;
        } else {
            this.range = range;
        }
    }

    public void setCost(int cost) {
        if (cost < 0) {
            this.cost = 0;
        } else {
            this.cost = cost;
        }
    }

    public void setCooldownSeconds(double cooldownSeconds) {
        if (cooldownSeconds <= 0) {
            this.cooldownSeconds = 1.0;
        } else {
            this.cooldownSeconds = cooldownSeconds;
        }
    }

    public boolean canAttack() {
        long currentTime = System.currentTimeMillis();
        long cooldownMilliseconds = (long) (cooldownSeconds * 1000);

        return currentTime - lastAttackTime >= cooldownMilliseconds;
    }

    public void resetCooldown() {
        lastAttackTime = System.currentTimeMillis();
    }

    public abstract void attack(Enemy enemy);

    @Override
    public String toString() {
        return "Tower{name='" + name + "', damage=" + damage
                + ", range=" + range + ", cost=" + cost
                + ", cooldownSeconds=" + cooldownSeconds + "}";
    }
}