package lab_4;

import java.util.concurrent.*;

public class FibLab {
    private static void mainRoutine(int tasks, ExecutorService executorService) {
        int taskCount = tasks;
        Integer[] results = new Integer[taskCount];
        ExecutorService executorServiceTmp = executorService;

        for (int i = 1; i <= taskCount; i++) {
            executorServiceTmp.submit(new Fibonacci(i + 31, i - 1, results));
        }
        executorServiceTmp.shutdown();
        try {
            executorServiceTmp.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 1; i <= taskCount; i++) {
            System.out.println(i + " : " + results[i - 1]);
        }
    }

    public static void main(String[] args) {

        int taskCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        mainRoutine(taskCount, executorService);

        System.out.println("End1\n");
        executorService = Executors.newSingleThreadExecutor();
        mainRoutine(taskCount, executorService);

        System.out.println("End2\n");
        executorService = Executors.newCachedThreadPool();
        mainRoutine(taskCount, executorService);

        System.out.println("End3\n");
        executorService = new ThreadPoolExecutor(2, 4, 50_000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(taskCount));
        mainRoutine(taskCount, executorService);
        System.out.println("End4\n");

    }
}
