package com.example;

import java.util.*;

public class BattleTimer {
    static Timer battleTimer;
    static Thread minuteTimer;
    static int timerLength = 45;
    static int seconds = 0;

    public static void startTimer() {
        Window.battleStarted = true;
        seconds = 0;

        battleTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Blabble.doChaos();
            }
        };
        battleTimer.scheduleAtFixedRate(task, (long) 3000, (long) 4000);

        Runnable minuteCount = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < timerLength; i++) {
                try {
                    Thread.sleep(1000);
                    seconds++;
                    Blabble.window.updateWindow();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                stopTimer();
            }
        };
        minuteTimer = new Thread(minuteCount);
        minuteTimer.start();
    }

    public static void stopTimer() {
        battleTimer.cancel();
        Window.battleStarted = false;
        Blabble.gameTransition();
    }
}