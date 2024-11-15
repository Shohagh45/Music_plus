package com.example.musicplus.Controllers;

import com.example.musicplus.Model.Product;
import com.example.musicplus.Database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    @FXML private TableView<Product> inventoryTableView;

    @FXML private TextField productQuantity;
    @FXML
    private Product selectedProduct;
    Database database;
    private OrderController orderController;
    public void setOrderController(OrderController orderController) {
        this.orderController = orderController;
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        database = new Database();
        ObservableList<Product> productList = FXCollections.observableArrayList(database.getAllProducts());
        inventoryTableView.setItems(productList);
    }

    public Product getProduct() {
        return selectedProduct;
    }

    @FXML
    private void addProductToOrder() {
        selectedProduct = inventoryTableView.getSelectionModel().getSelectedItem();

        int quantity;
        try {
            quantity = Integer.parseInt(productQuantity.getText());
        } catch (NumberFormatException e) {
            quantity = 0;
        }

        if (quantity <= 0) {

            showAlert("Invalid Quantity", "Please enter a valid quantity.");
            return;
        }

        if (selectedProduct == null) {

            showAlert("No Product Selected", "Please select a product.");
            return;
        }

        if (quantity > selectedProduct.getStock()) {

            showAlert("Insufficient Stock", "The selected quantity exceeds the available stock.");
            return;
        }


        Product productToAdd = new Product(quantity, selectedProduct.getName(), selectedProduct.getCategory(), selectedProduct.getPrice(), selectedProduct.getDescription());
        orderController.addToSelectedProducts(productToAdd);

        Stage stage = (Stage) productQuantity.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void cancelOrder() {
        Stage stage = (Stage) productQuantity.getScene().getWindow();
        stage.close();
    }

}