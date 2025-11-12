/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Exception;

/**
 *
 * @author Katana
 */


import java.util.UUID;

public class CartNotFoundException extends RuntimeException {
    public CartNotFoundException(UUID customerId) {
        super("Cart for customer with ID " + customerId + " not found");
    }
}
