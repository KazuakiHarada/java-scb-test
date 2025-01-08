package org.example;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WebSocketHandler implements WebSocketListener {

    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    static {
        scheduler.scheduleAtFixedRate(() -> {
            // JSONメッセージを作成
            String jsonMessage = "{" +
                    "\"scoreA\": 0," +
                    "\"scoreB\": 2," +
                    "\"gameID\": \"game123\"," +
                    "\"gameDuration\": 60," +
                    "\"infoCode\": \"INFO123\"}";

            // 全てのセッションに送信
            for (Session session : sessions) {
                try {
                    if (session.isOpen()) {
                        session.getRemote().sendString(jsonMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5, TimeUnit.SECONDS); // 5秒間隔で送信
    }


    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {

    }

    @Override
    public void onWebSocketClose(int i, String s) {

    }

    @Override
    public void onWebSocketConnect(Session session) {
        sessions.add(session);
        System.out.println("Client connected: " + session.getRemoteAddress());
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        System.out.println(throwable.toString());
    }

    public static void stopScheduler() {
        scheduler.shutdown();
    }
}
