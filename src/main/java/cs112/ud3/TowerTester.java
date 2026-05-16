package cs112.ud3;

public class TowerTester {
    public static void main(String[] args) {
        Enemy enemy = new BasicEnemy();

        Tower archer = new ArcherTower();
        Tower cannon = new CannonTower();

        System.out.println(enemy);

        archer.attack(enemy);
        System.out.println("After archer attack:");
        System.out.println(enemy);

        cannon.attack(enemy);
        System.out.println("After cannon attack:");
        System.out.println(enemy);
    }
}