package org.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class BookServiceGrpcServer {
    public static void main(String[] args) throws Exception {
        Server server = ServerBuilder.forPort(8080)
                .addService(new BookServiceImpl())
                .build()
                .start();

        System.out.println("Server started on port 8080");

        server.awaitTermination();
    }
}
