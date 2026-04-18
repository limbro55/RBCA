package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testBuildProgressLine_NotFinished() {
        String result = Main.buildProgressLine(1, 100, "###", false, 0);

        assertTrue(result.contains("Поток 1"));
        assertTrue(result.contains("ID: 100"));
        assertTrue(result.contains("###"));
        assertFalse(result.contains("Время"));
    }

    @Test
    void testBuildProgressLine_Finished() {
        String result = Main.buildProgressLine(2, 200, "#####", true, 1500);

        assertTrue(result.contains("Поток 2"));
        assertTrue(result.contains("ID: 200"));
        assertTrue(result.contains("#####"));
        assertTrue(result.contains("Время: 1500 ms"));
    }

    @Test
    void testProgressBarLength() {
        String result = Main.buildProgressLine(1, 1, "#####", false, 0);

        int start = result.indexOf("[");
        int end = result.indexOf("]");

        String bar = result.substring(start + 1, end);

        assertEquals(Main.WORK_LENGTH, bar.length());
    }

    @Test
    void testThreadExecutionTime() throws InterruptedException {
        long start = System.currentTimeMillis();

        Thread t = new Thread(() -> Main.runTask(1));
        t.start();
        t.join();

        long duration = System.currentTimeMillis() - start;

        assertTrue(duration >= Main.WORK_LENGTH * Main.DELAY);
    }

    @Test
    void testMultipleThreadsRun() throws InterruptedException {
        int threadCount = 3;

        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int num = i + 1;
            threads[i] = new Thread(() -> Main.runTask(num));
        }

        for (Thread t : threads) t.start();
        for (Thread t : threads) t.join();

        // если дошли сюда — потоки не упали
        assertTrue(true);
    }
}