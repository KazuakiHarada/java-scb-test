package org.example;

import java.util.Timer;
import java.util.TimerTask;

//テスト用に点数を増やす
public class SetScore {
    private final GameDataManager gameDataManager = GameDataManager.getInstance();

    Timer timer = new Timer();
    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            int random = (int) (Math.random() * 100);
            if (random % 5 == 0) {
                gameDataManager.Goal(true);
            } else if (random % 5 == 1) {
                gameDataManager.Goal(false);
            } else if (random % 5 == 2) {
                gameDataManager.UpdateScore(false, gameDataManager.ScoreB() + 1);
            }
        }
    };
    public void start() {
        timer.scheduleAtFixedRate(timerTask, 100, 6000);
    }
}
