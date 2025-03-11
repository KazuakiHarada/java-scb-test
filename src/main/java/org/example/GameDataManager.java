package org.example;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GameDataManager {
    private static final Gson gson = new Gson();
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static GameDataManager instance;

    private int scoreA;
    private int scoreB;
    private String gameID;
    private int gameDuration;
    private int infoCode;

    private GameDataManager() {
        // デフォルトの試合データ
        this.scoreA = 0;
        this.scoreB = 0;
        this.gameID = "";
        this.gameDuration = 60;
        this.infoCode = 0;
    }

    public static synchronized GameDataManager getInstance() {
        if (instance == null) {
            instance = new GameDataManager();
        }
        return instance;
    }

    public synchronized void updateGameData(int scoreA, int scoreB, String gameID, int gameDuration, int infoCode) {
        this.scoreA = scoreA;
        this.scoreB = scoreB;
        this.gameID = gameID;
        this.gameDuration = gameDuration;
        this.infoCode = infoCode;

        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    public synchronized void resetGame() {
        this.scoreA = 0;
        this.scoreB = 0;
        this.gameID = "";
        this.gameDuration = 60;
        this.infoCode = 0;

        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    public synchronized void restartGame() {
        int random = (int) (Math.random() * 100);
        this.gameID = random + "game";
        this.scoreA = 0;
        this.scoreB = 0;
        this.infoCode = 0;

        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    public synchronized void Goal(boolean is_teamA) {
        if (is_teamA) {
            this.scoreA += 1;
            this.infoCode = 2;
        } else {
            this.scoreB += 1;
            this.infoCode = 3;
        }
        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    public synchronized void UpdateScore(boolean is_teamA, int score) {
        if (is_teamA) {
            this.scoreA = score;
        } else {
            this.scoreB = score;
        }
        this.infoCode = 0;
        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    public synchronized void UpdateGameDuration(int gameDuration) {
        this.gameDuration = gameDuration;
        // データ更新時に全セッションにメッセージ送信
        broadcastGameData();
    }

    private synchronized void broadcastGameData() {
        String jsonMessage = gson.toJson(this);
        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getRemote().sendString(jsonMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // WebSocketセッションを管理
    public synchronized void addSession(Session session) {
        sessions.add(session);
    }

    public synchronized void removeSession(Session session) {
        sessions.remove(session);
    }

    // ゲッター（必要なら提供）
    public synchronized int ScoreA() {
        return scoreA;
    }

    public synchronized int ScoreB() {
        return scoreB;
    }

    public synchronized String getGameID() {
        return gameID;
    }

    public synchronized int getGameDuration() {
        return gameDuration;
    }

    public synchronized int getInfoCode() {
        return infoCode;
    }
}

