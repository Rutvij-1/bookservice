package org.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class BookServiceGrpcServer {
    private Server server;

    private void start() throws IOException {
        int port = 8080;
        server = ServerBuilder.forPort(port)
                .addService(new BookServiceImpl())
                .build()
                .start();

        System.out.println("Server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            BookServiceGrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final BookServiceGrpcServer server = new BookServiceGrpcServer();
        server.start();
        server.blockUntilShutdown();
    }
}
