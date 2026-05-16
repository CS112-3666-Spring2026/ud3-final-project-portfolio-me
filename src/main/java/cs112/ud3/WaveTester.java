package cs112.ud3;

public class WaveTester {
    public static void main(String[] args) {
        System.out.println("Testing Wave class...");
        System.out.println();

        for (int waveNumber = 1; waveNumber <= 4; waveNumber++) {
            Wave wave = new Wave(waveNumber);

            System.out.println("Wave " + wave.getWaveNumber());
            System.out.println("Number of enemies: " + wave.getNumberOfEnemies());

            while (wave.hasNextEnemy()) {
                Enemy enemy = wave.getNextEnemy();
                System.out.println("Next enemy: " + enemy.getName()
                        + " | Type: " + enemy.getEnemyType()
                        + " | Health: " + enemy.getHealth()
                        + " | Speed: " + enemy.getSpeed()
                        + " | Reward: " + enemy.getReward());
            }

            System.out.println();
        }

        System.out.println("All wave tests complete.");
    }
}