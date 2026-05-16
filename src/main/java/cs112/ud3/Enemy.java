package cs112.ud3;

public abstract class Enemy {
    private String name;
    private int health;
    private double speed;
    private int reward;

    public Enemy(String name, int health, double speed, int reward) {
        setName(name);
        setHealth(health);
        setSpeed(speed);
        setReward(reward);
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public double getSpeed() {
        return speed;
    }

    public int getReward() {
        return reward;
    }

    public void setName(String name) {
        if (name == null || name.trim().equals("")) {
            this.name = "Unknown Enemy";
        } else {
            this.name = name;
        }
    }

    public void setHealth(int health) {
        if (health < 0) {
            this.health = 0;
        } else {
            this.health = health;
        }
    }

    public void setSpeed(double speed) {
        if (speed <= 0) {
            this.speed = 1.0;
        } else {
            this.speed = speed;
        }
    }

    public void setReward(int reward) {
        if (reward < 0) {
            this.reward = 0;
        } else {
            this.reward = reward;
        }
    }

    public void takeDamage(int amount) {
        if (amount > 0) {
            setHealth(health - amount);
        }
    }

    public boolean isDefeated() {
        return health == 0;
    }

    public abstract String getEnemyType();

    @Override
    public String toString() {
        return "Enemy{name='" + name + "', health=" + health
                + ", speed=" + speed + ", reward=" + reward
                + ", type='" + getEnemyType() + "'}";
    }
}