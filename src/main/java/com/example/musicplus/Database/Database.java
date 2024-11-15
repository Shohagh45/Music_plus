package com.example.musicplus.Database;
import com.example.musicplus.Model.Order;
import com.example.musicplus.Model.Product;
import com.example.musicplus.Model.User;
import com.example.musicplus.Model.UserRole;
import com.example.musicplus.Serialize.Serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<User> users;
    private Serialization wrapper ;
    private List<Product> products ;

    public List<Product> getAllProducts() {
        return products;
    }
    private List<Order> orders;
    public List<Order> getAllOrders() {
        return orders;
    }


    public Database() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        orders= new ArrayList<>();

        users.add(new User("Solaiman", "sales1234", UserRole.SALES));
        users.add(new User("Shohagh", "manager1234", UserRole.MANAGER));

       deserialize();
    }
    public User getUserByUsernameAndPassword(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
    public UserRole getUserRole(String username){
        for (User user: users){
            if(user.getUserName().equals(username)){
                return user.getRole();
            }
        }
        return null;
    }
    public void addProduct(Product product) {
        products.add(product);
        serialize();
    }


    public void updateProduct(Product updatedProduct) {
        for (Product product : products) {
            if (product.getName().equals(updatedProduct.getName())) {

                product.setStock(updatedProduct.getStock());
                product.setName(updatedProduct.getName());
                product.setCategory(updatedProduct.getCategory());
                product.setPrice(updatedProduct.getPrice());
                product.setDescription(updatedProduct.getDescription());

                break;
            }
        }
      serialize();
    }
    public void deleteProduct(String productName) {
        products.removeIf(product -> product.getName().equals(productName));
      serialize();
    }
    public void addOrder(Order order) {
        orders.add(order);
     serialize();
    }
    public void updateStock(Product product, int decreadedStock) {

        for (Product p : products) {

            if (p.getName().equals(product.getName())) {
                int oldStock = p.getStock();
                int newStock = oldStock - decreadedStock;
                p.setStock(newStock);
                break;
            }
        }
      serialize();
    }

    public void serialize() {
        wrapper = new Serialization (users, products, orders);
        File file = new File("database.dat");
        try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file));) {
            outputStream.writeObject(wrapper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void deserialize() {
        File file = new File("database.dat");
        if(!file.exists()){
            serialize();
        }
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            wrapper = (Serialization) inputStream.readObject();
            users.clear();
            products.clear();
            orders.clear();

            products.addAll(wrapper.products);
            users.addAll(wrapper.users);
            orders.addAll(wrapper.orders);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}