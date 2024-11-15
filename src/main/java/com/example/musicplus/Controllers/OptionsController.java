package com.example.musicplus.Controllers;


import com.example.musicplus.Database.Database;
import com.example.musicplus.Model.User;
import com.example.musicplus.Model.UserRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;

public class OptionsController {
    public Button dashboard;
    public Button creatdeOrder;
    public Button inventory;
    public Button oderHistory;

    @FXML
    HBox layout;
    @FXML
    private Label username;
    @FXML
    private Label role;
    private User user;
    private Database database;
    private Scene currentScene;

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setUser(User user) {
        this.user = user;
        try {
            enableButtonsBasedOnUserRole();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enableButtonsBasedOnUserRole() {
        try {
            UserRole userRole = user.getRole();

            dashboard.setDisable(false);
            creatdeOrder.setDisable(false);
            inventory.setDisable(false);
            oderHistory.setDisable(false);

            switch (userRole) {
                case SALES:
                    inventory.setDisable(true);
                    break;
                case MANAGER:
                    creatdeOrder.setDisable(true);
                    break;
                default:
                    // Handle any other roles if necessary
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void welcomeUser(String userName, UserRole userRole) {
        username.setText("Welcome " + userName + "!");
        role.setText("Your role is: " + userRole);
    }

    public void logout(ActionEvent event) {
        loadScene("/com/example/musicplus/LoginView.fxml", new LoginController());
    }
    private void resetFields() {
        username.setText("");
        role.setText("");
    }
    public void loadScene(String name, Object controller) {
        try {
            URL resourceURL = getClass().getResource(name);
            System.out.println("Resource URL: " + resourceURL);

            FXMLLoader fxmlLoader = new FXMLLoader(resourceURL);
            fxmlLoader.setController(controller);
            Scene newScene = new Scene(fxmlLoader.load());

            if (currentScene != null) {
                layout.getChildren().remove(currentScene.getRoot());
            }

            layout.getChildren().add(newScene.getRoot());

            currentScene = newScene;
            username.setText("");
            role.setText("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void DashboardButtonClick(ActionEvent event) {
        loadScene("/com/example/musicplus/DashBoardView.fxml", new DashBoardController(database));
    }
    public void OrderButtonClick(ActionEvent event) {
        loadScene("/com/example/musicplus/CreateOrder.fxml", new OrderController(database));
    }
    public void InventoryButtonClick(ActionEvent event) {
        loadScene("/com/example/musicplus/Inventory.fxml", new InventoryController(database));
    }
    public void HistoryButtonClick(ActionEvent event) {
        loadScene("/com/example/musicplus/OderHistory.fxml", new OrderHistoryController(database));
    }
}
