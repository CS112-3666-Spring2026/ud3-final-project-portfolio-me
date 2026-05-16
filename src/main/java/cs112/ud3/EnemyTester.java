package cs112.ud3;

public class EnemyTester {
    public static void main(String[] args) {
        System.out.println("Testing Enemy subclasses...");
        System.out.println();

        Enemy basicEnemy = new BasicEnemy();
        Enemy fastEnemy = new FastEnemy();
        Enemy tankEnemy = new TankEnemy();

        System.out.println("Basic enemy:");
        System.out.println(basicEnemy);
        System.out.println();

        System.out.println("Fast enemy:");
        System.out.println(fastEnemy);
        System.out.println();

        System.out.println("Tank enemy:");
        System.out.println(tankEnemy);
        System.out.println();

        System.out.println("Testing polymorphism with an Enemy array:");
        Enemy[] enemies = new Enemy[3];
        enemies[0] = basicEnemy;
        enemies[1] = fastEnemy;
        enemies[2] = tankEnemy;

        for (int i = 0; i < enemies.length; i++) {
            System.out.println(enemies[i].getName()
                    + " is a " + enemies[i].getEnemyType()
                    + " enemy with " + enemies[i].getHealth()
                    + " health.");
        }

        System.out.println();

        System.out.println("Testing damage:");
        Enemy testEnemy = new BasicEnemy();

        System.out.println("Before damage:");
        System.out.println(testEnemy);

        testEnemy.takeDamage(25);
        System.out.println("After 25 damage:");
        System.out.println(testEnemy);

        testEnemy.takeDamage(100);
        System.out.println("After 100 damage:");
        System.out.println(testEnemy);

        System.out.println("Is defeated? " + testEnemy.isDefeated());
        System.out.println();

        System.out.println("Testing setters:");
        Enemy setterEnemy = new FastEnemy();

        setterEnemy.setName("Custom Fast Enemy");
        setterEnemy.setHealth(75);
        setterEnemy.setSpeed(2.5);
        setterEnemy.setReward(20);

        System.out.println(setterEnemy);
        System.out.println();

        System.out.println("Testing invalid setter values:");
        setterEnemy.setName("");
        setterEnemy.setHealth(-50);
        setterEnemy.setSpeed(-2.0);
        setterEnemy.setReward(-10);

        System.out.println(setterEnemy);
        System.out.println();

        System.out.println("All enemy tests complete.");
    }
}