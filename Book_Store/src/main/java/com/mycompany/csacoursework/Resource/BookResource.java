/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.csacoursework.Resource;

/**
 *
 * @author Katana
 */



import com.mycompany.csacoursework.Exception.AuthorNotFoundException;
import com.mycompany.csacoursework.Exception.BookNotFoundException;
import com.mycompany.csacoursework.Exception.InvalidInputException;
import com.mycompany.csacoursework.Models.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    // In-memory storage for books
    private static final List<Book> books = new ArrayList<>();

    @POST
    public Response createBook(Book book) {
        // Validate input
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (book.getAuthorId() == null) {
            throw new InvalidInputException("Author ID cannot be null");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        if (book.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (book.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (book.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Check if author exists (would typically query the database)
        // For the sake of this example, we'll check the hard-coded authors list
        boolean authorExists = AuthorResource.getAuthors().stream()
                .anyMatch(author -> author.getId().equals(book.getAuthorId()));
        
        if (!authorExists) {
            throw new AuthorNotFoundException(book.getAuthorId());
        }
        
        // Generate a new UUID if not provided
        if (book.getId() == null) {
            book.setId(UUID.randomUUID());
        }
        
        books.add(book);
        
        return Response.status(Response.Status.CREATED)
                .entity(book)
                .build();
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(books).build();
    }

    @GET
    @Path("/{id}")
    public Response getBookById(@PathParam("id") UUID id) {
        Book book = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
        
        return Response.ok(book).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") UUID id, Book updatedBook) {
        Book existingBook = books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
        
        // Validate input
        if (updatedBook.getTitle() == null || updatedBook.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Book title cannot be empty");
        }
        
        if (updatedBook.getAuthorId() == null) {
            throw new InvalidInputException("Author ID cannot be null");
        }
        
        // Check if author exists
        boolean authorExists = AuthorResource.getAuthors().stream()
                .anyMatch(author -> author.getId().equals(updatedBook.getAuthorId()));
        
        if (!authorExists) {
            throw new AuthorNotFoundException(updatedBook.getAuthorId());
        }
        
        if (updatedBook.getIsbn() == null || updatedBook.getIsbn().trim().isEmpty()) {
            throw new InvalidInputException("ISBN cannot be empty");
        }
        
        if (updatedBook.getPublicationYear() > LocalDate.now().getYear()) {
            throw new InvalidInputException("Publication year cannot be in the future");
        }
        
        if (updatedBook.getPrice() <= 0) {
            throw new InvalidInputException("Price must be greater than zero");
        }
        
        if (updatedBook.getStock() < 0) {
            throw new InvalidInputException("Stock cannot be negative");
        }
        
        // Update book properties
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthorId(updatedBook.getAuthorId());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPublicationYear(updatedBook.getPublicationYear());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setStock(updatedBook.getStock());
        
        return Response.ok(existingBook).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") UUID id) {
        boolean removed = books.removeIf(book -> book.getId().equals(id));
        
        if (!removed) {
            throw new BookNotFoundException(id);
        }
        
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    // Helper method to get books
    public static List<Book> getBooks() {
        return books;
    }
    
    // Helper method to find a book by ID
    public static Book findBookById(UUID id) {
        return books.stream()
                .filter(b -> b.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(id));
    }
}
