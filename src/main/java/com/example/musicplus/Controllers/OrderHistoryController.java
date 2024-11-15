package com.example.musicplus.Controllers;

import com.example.musicplus.Model.Product;
import com.example.musicplus.Model.Order;
import com.example.musicplus.Database.Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrderHistoryController implements Initializable {
    @FXML
    private TableView<Order> orderTableView;
    @FXML
    private TableView<Product> productPerOrderTableView;
    private Database database;
    private ObservableList<Order> orders;
    private ObservableList<Product> orderProducts;
    private Order selectedOrder;

    public OrderHistoryController(Database database) {
        this.database = database;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        orders = FXCollections.observableArrayList(database.getAllOrders());
        orderTableView.setItems(orders);

        orderTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldOrder, newOrder) -> {
            if (newOrder != null) {
                List<Product> productList = newOrder.getProducts();
                orderProducts = FXCollections.observableArrayList(productList);
                productPerOrderTableView.setItems(orderProducts);
            }
        });
    }
}
