package com.mycompany.csacoursework.Resource;

import com.mycompany.csacoursework.Exception.AuthorNotFoundException;
import com.mycompany.csacoursework.Exception.InvalidInputException;
import com.mycompany.csacoursework.Models.Author;
import com.mycompany.csacoursework.Models.Book;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/authors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorResource {

    private static final List<Author> authors = new ArrayList<>();

    // Static initializer block to add initial authors
    static {
        addInitialAuthors();
    }

    private static void addInitialAuthors() {
        Author author1 = new Author();
        author1.setId(UUID.randomUUID());
        author1.setName("George Orwell");
        author1.setBiography("George Orwell was an English novelist, essayist, journalist and critic.");

        Author author2 = new Author();
        author2.setId(UUID.randomUUID());
        author2.setName("Jane Austen");
        author2.setBiography("Jane Austen was an English novelist known primarily for her six major novels.");

        Author author3 = new Author();
        author3.setId(UUID.randomUUID());
        author3.setName("Mark Twain");
        author3.setBiography("Mark Twain was an American writer, humorist, entrepreneur, publisher, and lecturer.");

        authors.add(author1);
        authors.add(author2);
        authors.add(author3);
    }

    @POST
    public Response createAuthor(Author author) {
        if (author.getName() == null || author.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }

        if (author.getId() == null) {
            author.setId(UUID.randomUUID());
        }

        authors.add(author);

        return Response.status(Response.Status.CREATED)
                .entity(author)
                .build();
    }

    @GET
    public Response getAllAuthors() {
        return Response.ok(authors).build();
    }

    @GET
    @Path("/{id}")
    public Response getAuthorById(@PathParam("id") UUID id) {
        Author author = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        return Response.ok(author).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAuthor(@PathParam("id") UUID id, Author updatedAuthor) {
        Author existingAuthor = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        if (updatedAuthor.getName() == null || updatedAuthor.getName().trim().isEmpty()) {
            throw new InvalidInputException("Author name cannot be empty");
        }

        existingAuthor.setName(updatedAuthor.getName());
        existingAuthor.setBiography(updatedAuthor.getBiography());

        return Response.ok(existingAuthor).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAuthor(@PathParam("id") UUID id) {
        boolean removed = authors.removeIf(author -> author.getId().equals(id));

        if (!removed) {
            throw new AuthorNotFoundException(id);
        }

        return Response.noContent().build();
    }

    @GET
    @Path("/{id}/books")
    public Response getAuthorBooks(@PathParam("id") UUID id) {
        Author author = authors.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AuthorNotFoundException(id));

        List<Book> authorBooks = BookResource.getBooks().stream()
                .filter(book -> book.getAuthorId().equals(id))
                .collect(Collectors.toList());

        return Response.ok(authorBooks).build();
    }

    public static List<Author> getAuthors() {
        return authors;
    }
}
