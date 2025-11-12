/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Resource;

/**
 *
 * @author Katana
 */


import com.mycompany.csacoursework.Exception.CustomerNotFoundException;
import com.mycompany.csacoursework.Exception.InvalidInputException;
import com.mycompany.csacoursework.Models.Customer;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {
    // In-memory storage for customers
    private static final List<Customer> customers = new ArrayList<>();

    @POST
    public Response createCustomer(Customer customer) {
        // Validate input
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        
        if (customer.getEmail() == null || customer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be empty");
        }
        
        if (customer.getPassword() == null || customer.getPassword().trim().isEmpty()) {
            throw new InvalidInputException("Customer password cannot be empty");
        }
        
        // Generate a new UUID if not provided
        if (customer.getId() == null) {
            customer.setId(UUID.randomUUID());
        }
        
        customers.add(customer);
        
        return Response.status(Response.Status.CREATED)
                .entity(customer)
                .build();
    }

    @GET
    public Response getAllCustomers() {
        return Response.ok(customers).build();
    }

    @GET
    @Path("/{id}")
    public Response getCustomerById(@PathParam("id") UUID id) {
        Customer customer = customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));
        
        return Response.ok(customer).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") UUID id, Customer updatedCustomer) {
        Customer existingCustomer = customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));
        
        // Validate input
        if (updatedCustomer.getName() == null || updatedCustomer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Customer name cannot be empty");
        }
        
        if (updatedCustomer.getEmail() == null || updatedCustomer.getEmail().trim().isEmpty()) {
            throw new InvalidInputException("Customer email cannot be empty");
        }
        
        // Update customer properties
        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        
        // Only update password if provided
        if (updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().trim().isEmpty()) {
            existingCustomer.setPassword(updatedCustomer.getPassword());
        }
        
        return Response.ok(existingCustomer).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") UUID id) {
        boolean removed = customers.removeIf(customer -> customer.getId().equals(id));
        
        if (!removed) {
            throw new CustomerNotFoundException(id);
        }
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Helper method to get customers
    public static List<Customer> getCustomers() {
        return customers;
    }
    
    // Helper method to find a customer by ID
    public static Customer findCustomerById(UUID id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
