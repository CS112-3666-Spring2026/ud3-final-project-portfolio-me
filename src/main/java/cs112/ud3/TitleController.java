package cs112.ud3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class TitleController {

    @FXML
    private Button startButton;

    @FXML
    private Button howToPlayButton;

    @FXML
    private Button exitButton;

    @FXML
    public void onStartButtonClick(ActionEvent actionEvent) throws IOException {
        Parent mapView = FXMLLoader.load(CastleDefenseApp.class.getResource("map-view.fxml"));
        Scene mapViewScene = new Scene(mapView);

        Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        window.setScene(mapViewScene);
        window.show();
    }

    @FXML
    protected void onHowToPlayButtonClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText("Castle Tower Defense Instructions");
        alert.setContentText(
                """
                        Enemies move from the left side toward your castle.
                        Place towers to attack enemies before they reach the castle.
                        Defeating enemies gives you gold.
                        Use gold to buy more towers.
                        Do not let enemies reach the castle!"""
        );

        alert.showAndWait();
    }

    @FXML
    protected void onExitButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}