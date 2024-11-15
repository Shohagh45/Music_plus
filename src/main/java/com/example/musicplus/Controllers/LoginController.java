package com.example.musicplus.Controllers;

import com.example.musicplus.Database.Database;
import com.example.musicplus.Model.User;
import com.example.musicplus.Model.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class
LoginController {
    Database database;
    User user;

    @FXML
    private TextField UserName;

    @FXML
    private TextField Password;

    @FXML
    private Button loginButton;

    public void initialize() {
        database = new Database();
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = UserName.getText();
        String password = Password.getText();
        user = database.getUserByUsernameAndPassword(username, password);
        UserRole role = database.getUserRole(username);
        if (user != null) {
            navigateToMainWindow(username, role);
        }
    }

    private void showLoginError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void navigateToMainWindow(String username, UserRole role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/musicplus/OptionsView.fxml"));
            Parent root = loader.load();

            OptionsController optionsController = loader.getController();
            optionsController.setUser(user);
            optionsController.setDatabase(database);
            optionsController.welcomeUser(username, role);

            Stage mainStage = new Stage();
            mainStage.setTitle("Music Plus");
            mainStage.setScene(new Scene(root));
            mainStage.show();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    private void handleIOException(IOException e) {
        e.printStackTrace();
        // Add additional error handling or logging as needed
    }

}