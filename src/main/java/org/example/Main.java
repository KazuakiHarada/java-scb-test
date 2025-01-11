package org.example;

import spark.Spark;

public class Main {
    public static void main(String[] args) {
        Spark.port(8083);
        Spark.staticFiles.location("/public");
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.init();

        Spark.before("/ws", (request, response) -> {
            System.out.println("connection established");
        });

        Spark.get("/view", ((request, response) -> {
            response.type("text/html");
            return Main.class.getResourceAsStream("/public/index.html");
        }));

        System.out.println("Server is running on http://localhost:8083");
        // JVMシャットダウンフックでスケジューラを停止
        //Runtime.getRuntime().addShutdownHook(new Thread(WebSocketHandler::stopScheduler));
    }
}