package com.example.musicplus.Controllers;

import com.example.musicplus.Database.Database;
import com.example.musicplus.Model.Product;
import com.example.musicplus.Model.User;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashBoardController implements Initializable {
    @FXML
    private Label NameLabel;
    @FXML
    private Label RoleLabel;
    @FXML
    private Label dateAndtime;
    @FXML
    private TableView<Product> lowStockTableView;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productStockColumn;

    private User user;
    private Database database;

    public DashBoardController(Database database) {
        this.database = database;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUserInformation();
        startRealTimeDateTimeUpdater();
        updateLowStockTable();
    }

    private void updateLowStockTable() {
        // Filter products with stock below 5
        List<Product> lowStockProducts = database.getAllProducts()
                .stream()
                .filter(product -> product.getStock() < 5)
                .collect(Collectors.toList());

        // Populate TableView with low stock products
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        lowStockTableView.getItems().setAll(lowStockProducts);

        // Hide TableView if there are no low stock products
        lowStockTableView.setVisible(!lowStockProducts.isEmpty());
    }

    private void displayUserInformation() {
        String usernameText = "";
        String roleText = "";

        if (user != null) {
            usernameText = " " + user.getUsername() + "!";
            roleText = " " + user.getRole().toString();
        }

        NameLabel.setText(usernameText);
        RoleLabel.setText(roleText);
    }

    private void startRealTimeDateTimeUpdater() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, event -> updateDateTimeLabel()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void updateDateTimeLabel() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formatDateTime = now.format(formatter);
        dateAndtime.setText("It is now: " + formatDateTime);
    }
}


