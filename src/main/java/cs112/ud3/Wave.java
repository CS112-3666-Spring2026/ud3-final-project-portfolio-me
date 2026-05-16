package cs112.ud3;

public class Wave {
    private Enemy[] enemies;
    private int currentEnemyIndex;
    private int waveNumber;

    public Wave(int waveNumber) {
        setWaveNumber(waveNumber);
        createEnemies();
        currentEnemyIndex = 0;
    }

    public void setWaveNumber(int waveNumber) {
        if (waveNumber < 1) {
            this.waveNumber = 1;
        } else {
            this.waveNumber = waveNumber;
        }
    }

    public int getWaveNumber() {
        return waveNumber;
    }

    private void createEnemies() {
        int numberOfEnemies = 5 + ((waveNumber - 1) * 2);
        enemies = new Enemy[numberOfEnemies];

        for (int i = 0; i < enemies.length; i++) {
            if (waveNumber >= 3 && i % 5 == 0) {
                enemies[i] = new TankEnemy();
            } else if (waveNumber >= 2 && i % 3 == 0) {
                enemies[i] = new FastEnemy();
            } else {
                enemies[i] = new BasicEnemy();
            }
        }
    }

    public boolean hasNextEnemy() {
        return currentEnemyIndex < enemies.length;
    }

    public Enemy getNextEnemy() {
        if (hasNextEnemy()) {
            Enemy nextEnemy = enemies[currentEnemyIndex];
            currentEnemyIndex++;
            return nextEnemy;
        }

        return null;
    }

    public int getNumberOfEnemies() {
        return enemies.length;
    }

    public void resetWave() {
        currentEnemyIndex = 0;
    }

    @Override
    public String toString() {
        String result = "Wave " + waveNumber + " enemies:\n";

        for (int i = 0; i < enemies.length; i++) {
            result += enemies[i].toString() + "\n";
        }

        return result;
    }

    public int getEnemiesRemaining() {
        return enemies.length - currentEnemyIndex;
    }
}