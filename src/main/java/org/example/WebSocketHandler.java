package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.javalin.websocket.WsContext;
import io.javalin.websocket.WsMessageContext;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler {

    private static final GameDataManager gameDataManager = GameDataManager.getInstance();
    private static final Gson gson = new Gson();
    private static final Set<WsContext> connectedClients = ConcurrentHashMap.newKeySet();

    public static void onConnect(WsContext ctx) {
        connectedClients.add(ctx);
        gameDataManager.addSession(ctx.session);
        System.out.println("Client connected: " + ctx.session.getRemoteAddress());
    }

    public static void onMessage(WsMessageContext ctx) {
        String message = ctx.message();
        try {
            GameConfigJson gameConfig = gson.fromJson(message, GameConfigJson.class);
            String payload = gameConfig.getPayload();

            switch (gameConfig.getType()) {
                case "GameConfig" -> {
                    if ("reset".equals(payload)) {
                        gameDataManager.resetGame();
                        System.out.println("Game reset successfully");
                    } else if ("start".equals(payload)) {
                        gameDataManager.restartGame();
                        System.out.println("Game restarted successfully");
                    } else {
                        System.out.println("Unknown payload: " + message);
                    }
                }
                case "adjust-score" -> {
                    switch (payload) {
                        case "A+" -> gameDataManager.UpdateScore(true, gameDataManager.ScoreA() + 1);
                        case "A-" -> gameDataManager.UpdateScore(true, gameDataManager.ScoreA() - 1);
                        case "B+" -> gameDataManager.UpdateScore(false, gameDataManager.ScoreB() + 1);
                        case "B-" -> gameDataManager.UpdateScore(false, gameDataManager.ScoreB() - 1);
                        case null, default -> System.out.println("(AdjustScore) unknown payload");
                    }
                    System.out.println("current-score : " + gameDataManager.ScoreA() + " - " + gameDataManager.ScoreB());
                }
                case "adjust-time" ->gameDataManager.UpdateGameDuration(Integer.parseInt(payload));
                case "heartbeat" -> System.out.println("heartbeat from: " + ctx.session.getRemoteAddress());
                case null, default -> System.out.println("unknown Message" + message);
            }

        } catch (JsonSyntaxException e) {
            System.out.println("Invalid message format: " + message);
            ctx.send("Invalid message format");
        }
    }

    public static void onClose(WsContext ctx) {
        connectedClients.remove(ctx);
        System.out.println("Client disconnected: " + ctx.session.getRemoteAddress());
    }
}
