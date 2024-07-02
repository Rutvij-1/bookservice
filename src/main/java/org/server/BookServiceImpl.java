package org.server;

import java.util.HashMap;
import org.utils.ISBNValidator;

import io.grpc.stub.StreamObserver;

import bookservice.Book;
import bookservice.BookServiceGrpc;
import bookservice.AddBookRequest;
import bookservice.AddBookResponse;
import bookservice.UpdateBookRequest;
import bookservice.UpdateBookResponse;
import bookservice.DeleteBookRequest;
import bookservice.DeleteBookResponse;
import bookservice.GetBooksRequest;
import bookservice.GetBooksResponse;

public class BookServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    HashMap<String, Book> bookStorage = new HashMap<String, Book>();

    @Override
    public void addBook(AddBookRequest request, StreamObserver<AddBookResponse> responseObserver) {
        AddBookResponse response;
        if (!ISBNValidator.isValidISBN(request.getBook().getIsbn())) {
            // Check if the ISBN number is of a valid format
            response = AddBookResponse.newBuilder().setStatus(false).setMessage("Invalid ISBN number")
                    .build();
        } else if (bookStorage.containsKey(request.getBook().getIsbn())) {
            // Check if the book already exists in storage
            response = AddBookResponse.newBuilder().setStatus(false).setMessage("Book with ISBN number " + request
                    .getBook()
                    .getIsbn() + " already exists")
                    .build();
        } else {
            // Add book to storage
            bookStorage.put(request.getBook().getIsbn(), request.getBook());
            response = AddBookResponse.newBuilder().setStatus(true).setMessage("Book with ISBN number " + request
                    .getBook()
                    .getIsbn() + " added successfully")
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBook(UpdateBookRequest request, StreamObserver<UpdateBookResponse> responseObserver) {
        UpdateBookResponse response;
        if (bookStorage.containsKey(request.getBook().getIsbn())) {
            // Check if the book exists in storage and replace it
            bookStorage.remove(request.getBook().getIsbn());
            bookStorage.put(request.getBook().getIsbn(), request.getBook());
            response = UpdateBookResponse.newBuilder().setStatus(true).setMessage("Book with ISBN number " + request
                    .getBook()
                    .getIsbn() + " updated successfully")
                    .build();
        } else {
            // Book not found in storage
            response = UpdateBookResponse.newBuilder().setStatus(false).setMessage("Book with ISBN number " + request
                    .getBook()
                    .getIsbn() + " not found")
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteBook(DeleteBookRequest request, StreamObserver<DeleteBookResponse> responseObserver) {
        DeleteBookResponse response;
        if (bookStorage.containsKey(request.getIsbn())) {
            // Check if the book exists in storage and remove it
            bookStorage.remove(request.getIsbn());
            response = DeleteBookResponse.newBuilder().setStatus(true).setMessage("Book with ISBN number " + request
                    .getIsbn() + " deleted successfully")
                    .build();
        } else {
            // Book not found in storage
            response = DeleteBookResponse.newBuilder().setStatus(false).setMessage("Book with ISBN number " + request
                    .getIsbn() + " not found")
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getBooks(GetBooksRequest request, StreamObserver<GetBooksResponse> responseObserver) {
        GetBooksResponse response = GetBooksResponse.newBuilder().addAllBooks(bookStorage.values()).build();
        /*
         * Logic to search books by titles/authors to be inserted later.
         */
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
