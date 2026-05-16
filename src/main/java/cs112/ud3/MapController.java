package cs112.ud3;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class MapController {

    @FXML
    private Button towerSpotOne;

    @FXML
    private Button towerSpotTwo;

    @FXML
    private Button towerSpotThree;

    @FXML
    private Button towerSpotFour;

    @FXML
    private Button towerSpotFive;

    @FXML
    private Button towerSpotSix;

    @FXML
    private Button startWaveButton;

    @FXML
    private Label goldLabel;

    @FXML
    private Label healthLabel;

    @FXML
    private Label waveLabel;

    @FXML
    private Label messageLabel;

    @FXML
    private Pane gamePane;

    @FXML
    private Label enemiesRemainingLabel;

    @FXML
    private Button restartButton;

    private final double towerOneX = 205;
    private final double towerOneY = 159;

    private final double towerTwoX = 375;
    private final double towerTwoY = 159;

    private final double towerThreeX = 545;
    private final double towerThreeY = 159;

    private final double towerFourX = 205;
    private final double towerFourY = 299;

    private final double towerFiveX = 375;
    private final double towerFiveY = 299;

    private final double towerSixX = 545;
    private final double towerSixY = 299;

    private Tower towerOne;
    private Tower towerTwo;
    private Tower towerThree;
    private Tower towerFour;
    private Tower towerFive;
    private Tower towerSix;

    private int waveNumber = 1;
    private Wave currentWave;
    private boolean waveRunning = false;
    private final int maxWaves = 5;
    private boolean playerWon = false;

    private ArrayList<ActiveEnemy> activeEnemies = new ArrayList<>();

    private Timeline enemyTimeline;
    private Timeline spawnTimeline;

    private double enemyStartX = 90;
    private double enemyEndX = 700;
    private double enemyY = 220;

    private ActiveEnemy towerOneTarget;
    private ActiveEnemy towerTwoTarget;
    private ActiveEnemy towerThreeTarget;
    private ActiveEnemy towerFourTarget;
    private ActiveEnemy towerFiveTarget;
    private ActiveEnemy towerSixTarget;

    private int castleHealth = 10;
    private boolean gameOver = false;
    private int gold = 100;
    private final int archerToCannonUpgradeCost = 50;

    private class ActiveEnemy {
        private Enemy enemy;
        private Circle circle;
        private Label healthLabel;
        private double x;

        public ActiveEnemy(Enemy enemy, Circle circle, Label healthLabel, double x) {
            this.enemy = enemy;
            this.circle = circle;
            this.healthLabel = healthLabel;
            this.x = x;
        }
    }

    @FXML
    public void onBackButtonClick(ActionEvent actionEvent) throws IOException {
        Parent titleScreen = FXMLLoader.load(
                Objects.requireNonNull(CastleDefenseApp.class.getResource("title-screen.fxml"))
        );

        Scene titleScene = new Scene(titleScreen);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        window.setScene(titleScene);
        window.show();
    }

    @FXML
    public void onStartWaveButtonClick(ActionEvent actionEvent) {
        if (gameOver) {
            messageLabel.setText("Game over! Press Restart to play again.");
            return;
        }

        if (playerWon) {
            messageLabel.setText("You already won! Press Restart to play again.");
            return;
        }

        if (waveRunning) {
            messageLabel.setText("A wave is already running!");
            return;
        }

        waveRunning = true;
        startWaveButton.setDisable(true);

        currentWave = new Wave(waveNumber);
        waveLabel.setText("Wave: " + waveNumber);

        messageLabel.setText("Wave " + waveNumber + " started! "
                + currentWave.getNumberOfEnemies() + " enemies incoming.");

        waveNumber++;

        updateEnemiesRemainingLabel();

        startSpawningEnemies();
        startEnemyMovement();
    }

    @FXML
    public void onTowerSpotClick(ActionEvent actionEvent) {
        if (gameOver) {
            messageLabel.setText("Game over! You cannot build or upgrade towers.");
            return;
        }

        Button clickedSpot = (Button) actionEvent.getSource();

        Tower currentTower = getTowerForButton(clickedSpot);

        if (currentTower instanceof ArcherTower) {
            upgradeArcherTower(clickedSpot);
            return;
        }

        if (currentTower instanceof CannonTower) {
            messageLabel.setText("This tower is already a Cannon Tower.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                "Archer Tower",
                "Archer Tower",
                "Cannon Tower"
        );

        dialog.setTitle("Build Tower");
        dialog.setHeaderText("Choose a tower to build");
        dialog.setContentText("Tower type:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            Tower selectedTower;

            if (result.get().equals("Archer Tower")) {
                selectedTower = new ArcherTower();
            } else {
                selectedTower = new CannonTower();
            }

            if (gold < selectedTower.getCost()) {
                messageLabel.setText("Not enough gold for " + selectedTower.getName()
                        + ". Need " + selectedTower.getCost()
                        + ", but you only have " + gold + ".");
                return;
            }

            gold = gold - selectedTower.getCost();
            updateGoldLabel();

            setTowerForButton(clickedSpot, selectedTower);

            if (selectedTower instanceof ArcherTower) {
                clickedSpot.setText("Archer");
                clickedSpot.setStyle("-fx-background-color: lightgreen;");
            } else {
                clickedSpot.setText("Cannon");
                clickedSpot.setStyle("-fx-background-color: peru;");
            }

            messageLabel.setText(selectedTower.getName() + " built! Gold left: " + gold);
        }
    }

    @FXML
    public void onRestartButtonClick(ActionEvent actionEvent) {
        if (enemyTimeline != null) {
            enemyTimeline.stop();
        }

        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }

        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            ActiveEnemy activeEnemy = activeEnemies.get(i);
            gamePane.getChildren().remove(activeEnemy.circle);
            gamePane.getChildren().remove(activeEnemy.healthLabel);
            activeEnemies.remove(i);
        }

        towerOne = null;
        towerTwo = null;
        towerThree = null;
        towerFour = null;
        towerFive = null;
        towerSix = null;

        towerOneTarget = null;
        towerTwoTarget = null;
        towerThreeTarget = null;
        towerFourTarget = null;
        towerFiveTarget = null;
        towerSixTarget = null;

        resetTowerButton(towerSpotOne);
        resetTowerButton(towerSpotTwo);
        resetTowerButton(towerSpotThree);
        resetTowerButton(towerSpotFour);
        resetTowerButton(towerSpotFive);
        resetTowerButton(towerSpotSix);

        gold = 100;
        castleHealth = 10;
        waveNumber = 1;
        currentWave = null;
        waveRunning = false;
        gameOver = false;
        playerWon = false;

        updateGoldLabel();
        updateHealthLabel();
        updateEnemiesRemainingLabel();

        waveLabel.setText("Wave: 1");
        startWaveButton.setDisable(false);
        messageLabel.setText("Game restarted. Defend the castle!");
    }

    private void resetTowerButton(Button button) {
        button.setText("Tower");
        button.setStyle("");
        button.setDisable(false);
    }

    private Tower getTowerForButton(Button button) {
        if (button == towerSpotOne) {
            return towerOne;
        } else if (button == towerSpotTwo) {
            return towerTwo;
        } else if (button == towerSpotThree) {
            return towerThree;
        } else if (button == towerSpotFour) {
            return towerFour;
        } else if (button == towerSpotFive) {
            return towerFive;
        } else if (button == towerSpotSix) {
            return towerSix;
        }

        return null;
    }

    private void setTowerForButton(Button button, Tower tower) {
        if (button == towerSpotOne) {
            towerOne = tower;
        } else if (button == towerSpotTwo) {
            towerTwo = tower;
        } else if (button == towerSpotThree) {
            towerThree = tower;
        } else if (button == towerSpotFour) {
            towerFour = tower;
        } else if (button == towerSpotFive) {
            towerFive = tower;
        } else if (button == towerSpotSix) {
            towerSix = tower;
        }
    }

    private void upgradeArcherTower(Button clickedSpot) {
        Alert confirmUpgrade = new Alert(Alert.AlertType.CONFIRMATION);
        confirmUpgrade.setTitle("Upgrade Tower");
        confirmUpgrade.setHeaderText("Upgrade Archer Tower?");
        confirmUpgrade.setContentText("Upgrade this Archer Tower to a Cannon Tower for "
                + archerToCannonUpgradeCost + " gold?");

        Optional<ButtonType> result = confirmUpgrade.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (gold < archerToCannonUpgradeCost) {
                messageLabel.setText("Not enough gold to upgrade. Need "
                        + archerToCannonUpgradeCost + ", but you only have " + gold + ".");
                return;
            }

            Tower upgradedTower = new CannonTower();

            gold = gold - archerToCannonUpgradeCost;
            updateGoldLabel();

            setTowerForButton(clickedSpot, upgradedTower);
            clickedSpot.setText("Cannon");
            clickedSpot.setStyle("-fx-background-color: peru;");

            messageLabel.setText("Archer Tower upgraded to Cannon Tower! Gold left: " + gold);
        }
    }

    private void startSpawningEnemies() {
        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }

        spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (currentWave != null && currentWave.hasNextEnemy()) {
                spawnEnemy(currentWave.getNextEnemy());
            } else {
                spawnTimeline.stop();
            }
        }));

        spawnTimeline.setCycleCount(Timeline.INDEFINITE);
        spawnTimeline.play();
    }

    private void spawnEnemy(Enemy enemy) {
        Circle enemyCircle = new Circle(enemyStartX, enemyY, 15);

        if (enemy instanceof FastEnemy) {
            enemyCircle.setFill(Color.YELLOW);
        } else if (enemy instanceof TankEnemy) {
            enemyCircle.setFill(Color.DARKGRAY);
        } else {
            enemyCircle.setFill(Color.RED);
        }

        Label healthText = new Label(String.valueOf(enemy.getHealth()));
        healthText.setLayoutX(enemyStartX - 10);
        healthText.setLayoutY(enemyY - 10);
        healthText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");

        gamePane.getChildren().add(enemyCircle);
        gamePane.getChildren().add(healthText);

        ActiveEnemy activeEnemy = new ActiveEnemy(enemy, enemyCircle, healthText, enemyStartX);
        activeEnemies.add(activeEnemy);

        messageLabel.setText("Spawned: " + enemy.getName());
        updateEnemiesRemainingLabel();
    }

    private void startEnemyMovement() {
        if (enemyTimeline != null) {
            enemyTimeline.stop();
        }

        enemyTimeline = new Timeline(new KeyFrame(Duration.millis(30), event -> {
            for (int i = activeEnemies.size() - 1; i >= 0; i--) {
                ActiveEnemy activeEnemy = activeEnemies.get(i);

                activeEnemy.x = activeEnemy.x + activeEnemy.enemy.getSpeed();

                activeEnemy.circle.setCenterX(activeEnemy.x);
                activeEnemy.healthLabel.setLayoutX(activeEnemy.x - 10);
                activeEnemy.healthLabel.setLayoutY(enemyY - 10);
                activeEnemy.healthLabel.setText(String.valueOf(activeEnemy.enemy.getHealth()));

                if (activeEnemy.x >= enemyEndX) {
                    gamePane.getChildren().remove(activeEnemy.circle);
                    gamePane.getChildren().remove(activeEnemy.healthLabel);
                    activeEnemies.remove(i);

                    enemyReachedCastle(activeEnemy.enemy);
                }
            }

            towersAttack();

            checkWaveFinished();
        }));

        enemyTimeline.setCycleCount(Timeline.INDEFINITE);
        enemyTimeline.play();
    }

    private void checkWaveFinished() {
        boolean noMoreEnemiesToSpawn = currentWave == null || !currentWave.hasNextEnemy();
        boolean noEnemiesOnScreen = activeEnemies.isEmpty();

        if (waveRunning && noMoreEnemiesToSpawn && noEnemiesOnScreen && !gameOver) {
            waveRunning = false;

            if (enemyTimeline != null) {
                enemyTimeline.stop();
            }

            if (spawnTimeline != null) {
                spawnTimeline.stop();
            }

            updateEnemiesRemainingLabel();

            int completedWave = waveNumber - 1;

            if (completedWave >= maxWaves) {
                playerWins();
            } else {
                startWaveButton.setDisable(false);
                messageLabel.setText("Wave " + completedWave + " complete!");
            }
        }
    }

    private double getDistance(double xOne, double yOne, double xTwo, double yTwo) {
        double xDifference = xTwo - xOne;
        double yDifference = yTwo - yOne;

        return Math.sqrt((xDifference * xDifference) + (yDifference * yDifference));
    }

    private void towersAttack() {
        towerOneTarget = handleTowerAttack(towerOne, towerOneX, towerOneY, towerOneTarget);
        towerTwoTarget = handleTowerAttack(towerTwo, towerTwoX, towerTwoY, towerTwoTarget);
        towerThreeTarget = handleTowerAttack(towerThree, towerThreeX, towerThreeY, towerThreeTarget);
        towerFourTarget = handleTowerAttack(towerFour, towerFourX, towerFourY, towerFourTarget);
        towerFiveTarget = handleTowerAttack(towerFive, towerFiveX, towerFiveY, towerFiveTarget);
        towerSixTarget = handleTowerAttack(towerSix, towerSixX, towerSixY, towerSixTarget);
    }

    private ActiveEnemy handleTowerAttack(Tower tower, double towerX, double towerY, ActiveEnemy currentTarget) {
        if (tower == null) {
            return null;
        }

        if (activeEnemies.isEmpty()) {
            return null;
        }

        if (!isValidTarget(currentTarget, tower, towerX, towerY)) {
            currentTarget = findNewTarget(tower, towerX, towerY);
        }

        if (currentTarget == null) {
            return null;
        }

        tower.attack(currentTarget.enemy);

        if (currentTarget.healthLabel != null) {
            currentTarget.healthLabel.setText(String.valueOf(currentTarget.enemy.getHealth()));
        }

        if (currentTarget.enemy.isDefeated()) {
            int reward = currentTarget.enemy.getReward();

            gold = gold + reward;
            updateGoldLabel();

            gamePane.getChildren().remove(currentTarget.circle);
            gamePane.getChildren().remove(currentTarget.healthLabel);
            activeEnemies.remove(currentTarget);

            messageLabel.setText(currentTarget.enemy.getName()
                    + " defeated! +" + reward
                    + " gold. Total gold: " + gold);

            updateEnemiesRemainingLabel();

            return null;
        }

        return currentTarget;
    }

    private boolean isValidTarget(ActiveEnemy target, Tower tower, double towerX, double towerY) {
        if (target == null) {
            return false;
        }

        if (tower == null) {
            return false;
        }

        if (!activeEnemies.contains(target)) {
            return false;
        }

        if (target.enemy == null || target.circle == null || target.healthLabel == null) {
            return false;
        }

        if (target.enemy.isDefeated()) {
            return false;
        }

        double distance = getDistance(towerX, towerY, target.x, enemyY);

        return distance <= tower.getRange();
    }

    private ActiveEnemy findNewTarget(Tower tower, double towerX, double towerY) {
        if (tower == null) {
            return null;
        }

        for (int i = 0; i < activeEnemies.size(); i++) {
            ActiveEnemy possibleTarget = activeEnemies.get(i);

            if (possibleTarget != null
                    && possibleTarget.enemy != null
                    && !possibleTarget.enemy.isDefeated()) {

                double distance = getDistance(towerX, towerY, possibleTarget.x, enemyY);

                if (distance <= tower.getRange()) {
                    return possibleTarget;
                }
            }
        }

        return null;
    }

    private void updateHealthLabel() {
        healthLabel.setText("Castle Health: " + castleHealth);
    }

    @FXML
    public void initialize() {
        updateHealthLabel();
        updateGoldLabel();
        updateEnemiesRemainingLabel();
    }

    private void enemyReachedCastle(Enemy enemy) {
        castleHealth--;
        updateHealthLabel();

        messageLabel.setText(enemy.getName() + " reached the castle! Castle health: " + castleHealth);

        updateEnemiesRemainingLabel();

        if (castleHealth <= 0) {
            gameOver();
        }
    }

    private void gameOver() {
        gameOver = true;
        waveRunning = false;

        if (enemyTimeline != null) {
            enemyTimeline.stop();
        }

        if (spawnTimeline != null) {
            spawnTimeline.stop();
        }

        for (int i = activeEnemies.size() - 1; i >= 0; i--) {
            ActiveEnemy activeEnemy = activeEnemies.get(i);
            gamePane.getChildren().remove(activeEnemy.circle);
            gamePane.getChildren().remove(activeEnemy.healthLabel);
            activeEnemies.remove(i);
        }

        startWaveButton.setDisable(true);
        messageLabel.setText("GAME OVER! The castle has fallen.");
    }

    private void updateGoldLabel() {
        goldLabel.setText("Gold: " + gold);
    }

    private void updateEnemiesRemainingLabel() {
        int remaining = activeEnemies.size();

        if (currentWave != null) {
            remaining = remaining + currentWave.getEnemiesRemaining();
        }

        enemiesRemainingLabel.setText("Enemies Remaining: " + remaining);
    }

    private void playerWins() {
        playerWon = true;
        waveRunning = false;

        startWaveButton.setDisable(true);
        messageLabel.setText("YOU WIN! The castle survived all " + maxWaves + " waves!");
    }
}