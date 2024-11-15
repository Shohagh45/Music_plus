package com.example.musicplus.Controllers;

import com.example.musicplus.Model.Product;
import com.example.musicplus.Database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {
    @FXML
    private TableView<Product> productTableView;
    @FXML
    private ObservableList<Product> products;
    @FXML
    private Database database;
    @FXML

    private TextField productStockField;
@FXML
    private TextField productNameField;
@FXML
    private TextField categoryField;
@FXML
    private TextField priceField;
@FXML
    private TextField descriptionField;



    public InventoryController(Database d){
        database=d;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        products = FXCollections.observableArrayList(database.getAllProducts());
        productTableView.setItems(products);
    }
    @FXML
    private void handleImportProducts(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("products", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            importProductsFromCSV(selectedFile.toPath());
        }
    }

    private void importProductsFromCSV(Path filePath) {
        try {
            List<String> lines = Files.readAllLines(filePath);

            for (String line : lines) {
                String[] parts = line.split(";");
                if (parts.length == 5) {
                    try {
                        String name = parts[0];
                        String category = parts[1];
                        double price = Double.parseDouble(parts[2]);
                        String description = parts[3];
                        int stock = Integer.parseInt(parts[4]);

                        Product newProduct = new Product(stock, name, category, price, description);
                        products.add(newProduct);
                        database.addProduct(newProduct);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing numerics: " + line);
                    }
                } else {
                    System.err.println("Invalid format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        productTableView.refresh();
    }


    @FXML
    private void handleAddProduct(ActionEvent event) {
        try {
            // Validate and retrieve input values
            Product newProduct = validateAndCreateProduct();

            // Add the new product to the list and database
            if (newProduct != null) {
                products.add(newProduct);
                database.addProduct(newProduct);

                // Clear the input fields
                clearInputFields();
            }
        } catch (NumberFormatException e) {
            // Handle NumberFormatException (e.g., invalid numeric input)
            showAlert("Invalid Input", "Please enter valid numeric values for stock and price.");
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }
    }

    private Product validateAndCreateProduct() {
        try {
            // Validate and retrieve input values
            int stock = Integer.parseInt(productStockField.getText());
            String name = productNameField.getText();
            String category = categoryField.getText();
            double price = Double.parseDouble(priceField.getText());
            String description = descriptionField.getText();

            // Validate specific conditions (add more as needed)
            if (stock <= 0 || price <= 0) {
                showAlert("Invalid Input", "Stock and price must be greater than 0.");
                return null; // Return null if validation fails
            }

            // Create and return a new product
            return new Product(stock, name, category, price, description);
        } catch (NumberFormatException e) {
            // Handle NumberFormatException (e.g., invalid numeric input)
            showAlert("Invalid Input", "Please enter valid numeric values for stock and price.");
            return null; // Return null if validation fails
        }
    }

    private void clearInputFields() {
        productNameField.clear();
        categoryField.clear();
        productStockField.clear();
        priceField.clear();
        descriptionField.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleEditProduct(ActionEvent event) {
        try {
            Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

            if (selectedProduct != null) {
                // Validate and update selected product
                updateSelectedProduct(selectedProduct);

                // Update the product in the database
                database.updateProduct(selectedProduct);

                // Refresh the table view
                productTableView.refresh();

                // Clear input fields
                clearInputFields();
            }
        } catch (NumberFormatException e) {
            // Handle NumberFormatException (e.g., invalid numeric input)
            showAlert("Invalid Input", "Please enter valid numeric values for stock and price.");
        } catch (Exception e) {
            // Handle other exceptions
            handleException(e);
        }
    }

    private void updateSelectedProduct(Product selectedProduct) {
        // Validate and update selected product
        selectedProduct.setStock(parseTextFieldToInt(productStockField));
        selectedProduct.setName(productNameField.getText());
        selectedProduct.setCategory(categoryField.getText());
        selectedProduct.setPrice(parseTextFieldToDouble(priceField));
        selectedProduct.setDescription(descriptionField.getText());
    }

    private int parseTextFieldToInt(TextField textField) {
        return Integer.parseInt(textField.getText());
    }

    private double parseTextFieldToDouble(TextField textField) {
        return Double.parseDouble(textField.getText());
    }



    private void handleException(Exception e) {
        // Handle other exceptions
        e.printStackTrace();
        // Add additional error handling as needed
    }


@FXML
    private void deleteSelectedProduct(ActionEvent event) {
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            products.remove(selectedProduct);
            database.deleteProduct(selectedProduct.getName());
        }
    }
}
