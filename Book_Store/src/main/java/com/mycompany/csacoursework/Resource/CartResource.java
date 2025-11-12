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
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/customers/{customerId}/cart")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CartResource {
    // In-memory storage for carts
    private static final List<Cart> carts = new ArrayList<>();

    @POST
    @Path("/items")
    public Response addItemToCart(@PathParam("customerId") UUID customerId, CartItem cartItem) {
        // Validate customer
        Customer customer = CustomerResource.findCustomerById(customerId);
        
        // Validate cart item
        if (cartItem.getBookId() == null) {
            throw new InvalidInputException("Book ID cannot be null");
        }
        
        if (cartItem.getQuantity() <= 0) {
            throw new InvalidInputException("Quantity must be greater than zero");
        }
        
        // Validate book exists and has enough stock
        Book book = BookResource.findBookById(cartItem.getBookId());
        
        if (book.getStock() < cartItem.getQuantity()) {
            throw new OutOfStockException(book.getId());
        }
        
        // Get or create cart for the customer
        Cart cart = getOrCreateCart(customerId);
        
        // Check if the book already exists in the cart
        boolean bookExistsInCart = cart.getItems().stream()
                .anyMatch(item -> item.getBookId().equals(cartItem.getBookId()));
        
        if (bookExistsInCart) {
            // Update quantity if book already in cart
            cart.getItems().forEach(item -> {
                if (item.getBookId().equals(cartItem.getBookId())) {
                    item.setQuantity(item.getQuantity() + cartItem.getQuantity());
                }
            });
        } else {
            // Add new item with book price
            cartItem.setPrice(book.getPrice());
            cart.addItem(cartItem);
        }
        
        return Response.status(Response.Status.CREATED)
                .entity(cart)
                .build();
    }

    @GET
    public Response getCart(@PathParam("customerId") UUID customerId) {
        // Validate customer
        CustomerResource.findCustomerById(customerId);
        
        // Find cart or create a new one
        Cart cart = getOrCreateCart(customerId);
        
        return Response.ok(cart).build();
    }

@PUT
@Path("/items/{bookId}")
public Response updateCartItem(
        @PathParam("customerId") UUID customerId,
        @PathParam("bookId") UUID bookId,
        CartItem updatedItem) {

    // Validate customer
    CustomerResource.findCustomerById(customerId);

    // Validate quantity
    if (updatedItem.getQuantity() <= 0) {
        throw new InvalidInputException("Quantity must be greater than zero");
    }

    // Validate book exists and has enough stock
    Book book = BookResource.findBookById(bookId);

    if (book.getStock() < updatedItem.getQuantity()) {
        throw new OutOfStockException(book.getId());
    }

    // Get cart
    Cart cart = findCartByCustomerId(customerId);

    // Check if the book exists in the cart
    boolean bookExistsInCart = cart.getItems().stream()
            .anyMatch(item -> item.getBookId().equals(bookId));

    if (!bookExistsInCart) {
        throw new InvalidInputException("Book not found in cart");
    }

    // Update item quantity
    cart.updateItemQuantity(bookId, updatedItem.getQuantity());

    return Response.ok(cart).build();
}


    @DELETE
    @Path("/items/{bookId}")
    public Response removeCartItem(
            @PathParam("customerId") UUID customerId,
            @PathParam("bookId") UUID bookId) {
        
        // Validate customer
        CustomerResource.findCustomerById(customerId);
        
        // Validate book exists
        BookResource.findBookById(bookId);
        
        // Get cart
        Cart cart = findCartByCustomerId(customerId);
        
        // Check if the book exists in the cart
        boolean bookExistsInCart = cart.getItems().stream()
                .anyMatch(item -> item.getBookId().equals(bookId));
        
        if (!bookExistsInCart) {
            throw new InvalidInputException("Book not found in cart");
        }
        
        // Remove item from cart
        cart.removeItem(bookId);
        
        return Response.ok(cart).build();
    }

    // Helper method to find a cart by customer ID
    private Cart findCartByCustomerId(UUID customerId) {
        return carts.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseThrow(() -> new CartNotFoundException(customerId));
    }
    
    // Helper method to get or create a cart for a customer
    private Cart getOrCreateCart(UUID customerId) {
        // Look for existing cart
        return carts.stream()
                .filter(c -> c.getCustomerId().equals(customerId))
                .findFirst()
                .orElseGet(() -> {
                    // Create new cart if not found
                    Cart newCart = new Cart(customerId);
                    carts.add(newCart);
                    return newCart;
                });
    }
    
    // Helper method to get carts
    public static List<Cart> getCarts() {
        return carts;
    }
}