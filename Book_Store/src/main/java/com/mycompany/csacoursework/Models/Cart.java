/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Models;

/**
 *
 * @author Katana
 */


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
    private UUID customerId;
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public Cart(UUID customerId) {
        this.customerId = customerId;
        this.items = new ArrayList<>();
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void addItem(CartItem item) {
        this.items.add(item);
    }

    public void removeItem(UUID bookId) {
        this.items.removeIf(item -> item.getBookId().equals(bookId));
    }

    public void updateItemQuantity(UUID bookId, int quantity) {
        for (CartItem item : items) {
            if (item.getBookId().equals(bookId)) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public double getTotalPrice() {
        return items.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }
}