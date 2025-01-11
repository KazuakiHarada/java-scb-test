package org.example;

public class GameConfigJson {
    private String type;
    private String payload;

    // デフォルトコンストラクタ
    public GameConfigJson() {}

    // パラメータ付きコンストラクタ
    public GameConfigJson(String type, String payload) {
        this.type = type;
        this.payload = payload;
    }

    // getter/setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    // toStringメソッド (デバッグなど)
    @Override
    public String toString() {
        return "GameConfig{" +
                "type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}