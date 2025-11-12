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

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(UUID bookId) {
        super("Book with ID " + bookId + " is out of stock");
    }
}