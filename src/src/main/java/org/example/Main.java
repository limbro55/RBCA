package org.example;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int THREAD_COUNT = 5;
    public static final int WORK_LENGTH = 30;
    public static final int DELAY = 100;

    public static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            int threadNumber = i + 1;

            Thread t = new Thread(() -> runTask(threadNumber));
            threads.add(t);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("\nВсе потоки завершены.");
    }

    public static void runTask(int threadNumber) {
        long startTime = System.currentTimeMillis();
        long threadId = Thread.currentThread().getId();

        StringBuilder progress = new StringBuilder();

        for (int i = 0; i < WORK_LENGTH; i++) {
            progress.append("#");

            try {
                Thread.sleep(DELAY + (int)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (lock) {
                printProgress(threadNumber, threadId, progress.toString(), false, 0);
            }
        }

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        synchronized (lock) {
            printProgress(threadNumber, threadId, progress.toString(), true, totalTime);
        }
    }

    public static void printProgress(int num, long id, String bar, boolean finished, long time) {
        StringBuilder line = new StringBuilder();

        line.append("Поток ").append(num)
                .append(" | ID: ").append(id)
                .append(" | [");

        line.append(bar);
        for (int i = bar.length(); i < WORK_LENGTH; i++) {
            line.append(" ");
        }

        line.append("]");

        if (finished) {
            line.append(" | Время: ").append(time).append(" ms");
        }

        System.out.println(buildProgressLine(num, id, bar, finished, time));
    }

    public static String buildProgressLine(int num, long id, String bar, boolean finished, long time) {
        StringBuilder line = new StringBuilder();

        line.append("Поток ").append(num)
                .append(" | ID: ").append(id)
                .append(" | [");

        line.append(bar);
        for (int i = bar.length(); i < WORK_LENGTH; i++) {
            line.append(" ");
        }

        line.append("]");

        if (finished) {
            line.append(" | Время: ").append(time).append(" ms");
        }

        return line.toString();
    }
}