/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Models;

/**
 *
 * @author Katana
 */


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private UUID id;
    private UUID customerId;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double totalAmount;

    public Order() {
        this.id = UUID.randomUUID();
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public Order(UUID customerId) {
        this.id = UUID.randomUUID();
        this.customerId = customerId;
        this.orderDate = LocalDateTime.now();
        this.items = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        calculateTotalAmount();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private void calculateTotalAmount() {
        this.totalAmount = items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}
