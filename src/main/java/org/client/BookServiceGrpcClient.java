package org.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import bookservice.Book;
import bookservice.BookServiceGrpc;

import java.util.Arrays;
import java.util.List;

import bookservice.AddBookRequest;
import bookservice.AddBookResponse;
import bookservice.UpdateBookRequest;
import bookservice.UpdateBookResponse;
import bookservice.DeleteBookRequest;
import bookservice.DeleteBookResponse;
import bookservice.GetBooksRequest;
import bookservice.GetBooksResponse;

public class BookServiceGrpcClient {

    private ManagedChannel channel;
    private final BookServiceGrpc.BookServiceBlockingStub blockingStub;

    public BookServiceGrpcClient(ManagedChannel channel) {
        blockingStub = BookServiceGrpc.newBlockingStub(channel);
    }

    public void addBook(String isbn, String title, List<String> authors, Integer pageCount) {
        Book book = Book.newBuilder().setIsbn(isbn).setTitle(title).setPageCount(pageCount).addAllAuthors(authors)
                .build();
        AddBookRequest request = AddBookRequest.newBuilder().setBook(book).build();
        try {
            AddBookResponse response = blockingStub.addBook(request);
            if (response.getStatus()) {
                System.out.println("Book added successfully with response from server: " + response.getMessage());
            } else {
                System.out.println("Adding book failed with response from server: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed with error message: " + e.getStatus());
        }
    }

    public void updateBook(String isbn, String title, List<String> authors, Integer pageCount) {
        Book book = Book.newBuilder().setIsbn(isbn).setTitle(title).setPageCount(pageCount).addAllAuthors(authors)
                .build();
        UpdateBookRequest request = UpdateBookRequest.newBuilder().setBook(book).build();
        try {
            UpdateBookResponse response = blockingStub.updateBook(request);
            if (response.getStatus()) {
                System.out.println("Book updated successfully with response from server: " + response.getMessage());
            } else {
                System.out.println("Updating book failed with response from server: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed with error message: " + e.getStatus());
        }
    }

    public void deleteBook(String isbn) {
        DeleteBookRequest request = DeleteBookRequest.newBuilder().setIsbn(isbn).build();
        try {
            DeleteBookResponse response = blockingStub.deleteBook(request);
            if (response.getStatus()) {
                System.out.println("Book deleted successfully with response from server: " + response.getMessage());
            } else {
                System.out.println("Deleting book failed with response from server: " + response.getMessage());
            }
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed with error message: " + e.getStatus());
        }
    }

    public void getBooks(String searchQuery) {
        GetBooksRequest request = GetBooksRequest.newBuilder().setSearchQuery(searchQuery).build();
        try {
            GetBooksResponse response = blockingStub.getBooks(request);
            Integer counter = 1;
            System.out.println("The following books were found in storage:");
            for (Book book : response.getBooksList()) {
                System.out.println(String.valueOf(counter) + ". ISBN: " + book.getIsbn());
                System.out.println(" ".repeat(String.valueOf(counter).length() + 2) + "Title: " + book.getTitle());
                String authorListString = "";
                for (String author : book.getAuthorsList()) {
                    authorListString += author + ", ";
                }
                if (authorListString.length() > 0) {
                    authorListString = authorListString.substring(0, authorListString.length() - 2);
                }
                System.out.println(" ".repeat(String.valueOf(counter).length() + 2) + "Authors: " + authorListString);
                System.out.println(" ".repeat(String.valueOf(counter).length() + 2) + "Page Count: " + book.getPageCount());
                counter++;
            }
        } catch (StatusRuntimeException e) {
            System.err.println("RPC failed with error message: " + e.getStatus());
        }
    }

    public static void main(String[] args) {
        String target = "localhost:8080";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();

        try {
            BookServiceGrpcClient client = new BookServiceGrpcClient(channel);
            client.addBook("0596009208", "Programming Python", List.of("Mark Lutz"), 220);
            System.out.println("-----------------------------------------");

            client.addBook("9780201633610", "Design Patterns: Elements of Reusable Object-Oriented Software", Arrays.asList("Erich Gamma", "John Vlissides", "Richard Helm", "Ralph Johnson"), 395);
            System.out.println("-----------------------------------------");

            client.addBook("978020163361", "Design Patterns: Elements of Reusable Object-Oriented Software", Arrays.asList("Erich Gamma", "John Vlissides", "Richard Helm", "Ralph Johnson"), 395);
            System.out.println("-----------------------------------------");

            client.getBooks("");
            System.out.println("-----------------------------------------");

            client.updateBook("0596009208", "Harry Potter and the Sorcerer's Stone", List.of("J.K. Rowling"), 309);
            System.out.println("-----------------------------------------");

            client.updateBook("9780134685991", "Effective Java", List.of("Joshua Bloch"), 400);
            System.out.println("-----------------------------------------");

            client.getBooks("");
            System.out.println("-----------------------------------------");

            client.deleteBook("9780201633610");
            System.out.println("-----------------------------------------");

            client.deleteBook("978020633610");
            System.out.println("-----------------------------------------");

            client.getBooks("");
        } finally {
            channel.shutdown();
        }
    }
}
