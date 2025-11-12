/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Resource;

/**
 *
 * @author Katana
 */


import com.mycompany.csacoursework.Exception.CartNotFoundException;
import com.mycompany.csacoursework.Exception.InvalidInputException;
import com.mycompany.csacoursework.Exception.OutOfStockException;
import com.mycompany.csacoursework.Models.Book;
import com.mycompany.csacoursework.Models.Cart;
import com.mycompany.csacoursework.Models.CartItem;
import com.mycompany.csacoursework.Models.Customer;
import com.mycompany.csacoursework.Models.Order;
import com.mycompany.csacoursework.Models.OrderItem;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/customers/{customerId}/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    // In-memory storage for orders
    private static final List<Order> orders = new ArrayList<>();

    @POST
    public Response createOrder(@PathParam("customerId") UUID customerId) {
        // Validate customer
        Customer customer = CustomerResource.findCustomerById(customerId);
        
        // Get customer's cart
        Cart cart = CartResource.getCarts().stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException(customerId));
        
        // Check if cart is empty
        if (cart.getItems().isEmpty()) {
            throw new InvalidInputException("Cannot create order with an empty cart");
        }
        
        // Create new order
        Order order = new Order(customerId);
        
        // Convert cart items to order items
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItem cartItem : cart.getItems()) {
            Book book = BookResource.findBookById(cartItem.getBookId());
            
            // Check stock availability
            if (book.getStock() < cartItem.getQuantity()) {
                throw new OutOfStockException(book.getId());
            }
            
            // Reduce book stock
            book.setStock(book.getStock() - cartItem.getQuantity());
            
            // Create order item
            OrderItem orderItem = new OrderItem(
                    book.getId(),
                    book.getTitle(),
                    cartItem.getQuantity(),
                    book.getPrice()
            );
            
            orderItems.add(orderItem);
        }
        
        order.setItems(orderItems);
        
        // Save order
        orders.add(order);
        
        // Clear the cart
        cart.getItems().clear();
        
        return Response.status(Response.Status.CREATED)
                .entity(order)
                .build();
    }

    @GET
    public Response getCustomerOrders(@PathParam("customerId") UUID customerId) {
        // Validate customer
        CustomerResource.findCustomerById(customerId);
        
        // Get customer's orders
        List<Order> customerOrders = orders.stream()
                .filter(o -> o.getCustomerId().equals(customerId))
                .collect(Collectors.toList());
        
        return Response.ok(customerOrders).build();
    }

    @GET
    @Path("/{orderId}")
    public Response getOrderById(
            @PathParam("customerId") UUID customerId,
            @PathParam("orderId") UUID orderId) {
        
        // Validate customer
        CustomerResource.findCustomerById(customerId);
        
        // Find the order
        Order order = orders.stream()
                .filter(o -> o.getId().equals(orderId) && o.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Order not found"));
        
        return Response.ok(order).build();
    }
}
