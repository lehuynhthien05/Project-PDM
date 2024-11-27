package com.example.parkingbookingsystems;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.event.ActionEvent;
import javafx.application.Platform;

public class ChoosingController {

    @FXML
    private Button admin;

    @FXML
    private Button close;

    @FXML
    private Button user;

    public void setClose() {
        Platform.exit();
    }

    public void choose(ActionEvent event) {
        try {
            Parent root = null;
            String fxmlPath = null;

            if (event.getSource() == admin) {
                fxmlPath = "/com/example/parkingbookingsystems/LoginAdmin.fxml";
            } else if (event.getSource() == user) {
                fxmlPath = "/com/example/parkingbookingsystems/loginUser.fxml";
            }

            if (fxmlPath != null) {
                root = FXMLLoader.load(getClass().getResource(fxmlPath));
            }

            if (root != null) {
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();

                Stage currentStage = (Stage) close.getScene().getWindow();
                currentStage.close();
            } else {
                System.err.println("Failed to load FXML file: " + fxmlPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
