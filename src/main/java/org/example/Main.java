package org.example;

import io.javalin.Javalin;
import io.javalin.websocket.WsConfig;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public");
        }).start(8083);

        app.ws("/ws", ws -> {
            ws.onConnect(WebSocketHandler::onConnect);
            ws.onMessage(WebSocketHandler::onMessage);
            ws.onClose(WebSocketHandler::onClose);
            ws.onError(ctx -> System.out.println("Errored"));
        });

        app.before("/ws", ctx -> {
            System.out.println("connection established");
        });

        app.get("/view", ctx -> {
            try (InputStream resourceStream = Main.class.getResourceAsStream("/public/index.html")) {
                if (resourceStream == null) {
                    ctx.status(404).result("404 Not Found");
                } else {
                    ctx.contentType("text/html").result(new String(resourceStream.readAllBytes()));
                }
            }
        });

        QRHandler qrHandler = new QRHandler();
        app.get("/qrcode", qrHandler::handleQrCode);
        app.get("/qr", qrHandler::handleQrPage);
        app.get("/url", qrHandler::handleQRUrl);

        System.out.println("Server is running on http://localhost:8083");
    }
}
