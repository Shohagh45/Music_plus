package com.example.musicplus.Serialize;



import com.example.musicplus.Model.Order;
import com.example.musicplus.Model.Product;
import com.example.musicplus.Model.User;
import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.List;

public class Serialization implements Serializable {
    public List<User> users;
    public List<Product> products;
    public List<Order> orders;

    public Serialization(List<User> users, List<Product> products, List<Order> orders) {
        this.users = users;
        this.orders = orders;
        this.products = products;
    }
}
