package org.example;

import spark.Spark;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        Spark.port(8083);
        Spark.staticFiles.location("/public");
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.init();

        Spark.before("/ws", (request, response) -> {
            System.out.println("connection established");
        });

        Spark.get("/view", (request, response) -> {
            response.type("text/html");
            try (InputStream resourceStream = Main.class.getResourceAsStream("public/index.html")) {
                if (resourceStream == null) {
                    response.status(404);
                    return "404 Not Found";
                }
                return new String(resourceStream.readAllBytes());
            }
        });

        System.out.println("Server is running on http://localhost:8083");
    }
}