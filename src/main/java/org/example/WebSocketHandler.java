package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;


public class WebSocketHandler implements WebSocketListener {

    private final GameDataManager gameDataManager = GameDataManager.getInstance();
    Gson gson = new Gson();


    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {

        try {
            GameConfigJson message = gson.fromJson(s, GameConfigJson.class);

            if ("reset".equals(message.getPayload())) {
                gameDataManager.resetGame();
                System.out.println("Game reset successfully");
            } else if ("start".equals(message.getPayload())) {
                gameDataManager.restartGame();
                System.out.println("Game restarted successfully");
            } else {
                System.out.println("Unknown message: " + s);
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Unknown message: " + s);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onWebSocketClose(int i, String s) {
        System.out.println("Client disconnected");
    }

    @Override
    public void onWebSocketConnect(Session session) {
        gameDataManager.addSession(session);
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        System.out.println(throwable.toString());
    }

}
