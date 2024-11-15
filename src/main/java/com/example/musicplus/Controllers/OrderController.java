package com.example.musicplus.Controllers;


import com.example.musicplus.Database.Database;
import com.example.musicplus.Model.Order;
import com.example.musicplus.Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
public class OrderController implements Initializable {
    @FXML
    private TextField FirstName;
    @FXML
    private TextField LastName;
    @FXML
    private TextField Email;
    @FXML
    private TextField PhoneNumber;
    private ObservableList<Product> selectedProducts = FXCollections.observableArrayList();
    @FXML
    private TableView<Product> productTableView;

    private Database database;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productTableView.setItems(selectedProducts);

    }

    public OrderController(Database database) {
        this.database = database;
    }

    public void addToSelectedProducts(Product product) {
        selectedProducts.add(product);
    }

    @FXML
    private void handleAddProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/musicplus/AddProduct.fxml"));
            Parent root = loader.load();

            AddProductController controller = loader.getController();
            controller.setOrderController(this);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            handleException(e);
        }
    }

    @FXML
    private void handleCreateOrder(ActionEvent event) {
        try {
            String firstName = FirstName.getText();
            String lastName = LastName.getText();
            String email = Email.getText();
            String phoneNumber = PhoneNumber.getText();

            LocalDateTime dateTime = LocalDateTime.now();
            Order order = createOrder(dateTime, firstName, lastName);

            if (confirmOrderCreation(order)) {
                processOrder(order);
            }

        } catch (Exception e) {
            handleException(e);
        }
    }

    private Order createOrder(LocalDateTime dateTime, String firstName, String lastName) {
        return new Order(dateTime, firstName + " " + lastName, new ArrayList<>(selectedProducts));
    }

    private boolean confirmOrderCreation(Order order) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Order");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to create this order?");
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    private void processOrder(Order order) {
        database.addOrder(order);
        selectedProducts.forEach(p -> database.updateStock(p, p.getStock()));
        clearFields();
    }

    private void clearFields() {
        FirstName.clear();
        LastName.clear();
        Email.clear();
        PhoneNumber.clear();
        selectedProducts.clear();
        productTableView.getItems().clear();
    }

    @FXML
    private void handleDeleteProduct(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            selectedProducts.remove(selectedProduct);
            productTableView.getItems().remove(selectedProduct);
        }
    }

    private void handleException(Exception e) {
        e.printStackTrace();
        // Add additional error handling or logging as needed
    }
}

