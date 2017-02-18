package lab_4;

import java.util.concurrent.*;

public class FibLab {
    private static void mainRoutine(int baseFibnum, int tasks, ExecutorService executorService) {
        int taskCount = tasks;
        Integer[] results = new Integer[taskCount];
        ExecutorService executorServiceTmp = executorService;

        for (int i = 1; i <= taskCount; i++) {
            executorServiceTmp.submit(new Fibonacci(i + baseFibnum, i - 1, results));
        }
        executorServiceTmp.shutdown();
        try {
            executorServiceTmp.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Results:");
        for (int i = 1; i <= taskCount; i++) {
            System.out.println(i + "(fib num " + (baseFibnum + i) + ") : " + results[i - 1]);
        }
    }

    private static void testRun(int fibnum, int taskNum) {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        System.out.println("Fixed thread Start\n");
        mainRoutine(fibnum, taskNum, executorService);
        System.out.println("Fixed thread End\n");

        executorService = Executors.newSingleThreadExecutor();
        System.out.println("Single thread Start\n");
        mainRoutine(fibnum, taskNum, executorService);
        System.out.println("Single thread End\n");

        executorService = Executors.newCachedThreadPool();
        System.out.println("Cached thread Start\n");
        mainRoutine(fibnum, taskNum, executorService);
        System.out.println("Cached thread End\n");

        executorService = new ThreadPoolExecutor(2, 4, 50_000L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(taskNum));
        System.out.println("Custom parameters pool Start\n");
        mainRoutine(fibnum, taskNum, executorService);
        System.out.println("Custom parameters pool End\n");
    }


    public static void main(String[] args) {
        int[] taskCountArray = {2, 5, 10};
        int[] baseFibnumArray = {38, 35, 30};

        for (int i = 0; i < taskCountArray.length; i++) {

            System.err.println("\n\nTEST No " + (i + 1) +
                    " Base fib: " + baseFibnumArray[i] +
                    " Tasks : " + taskCountArray[i] + "\n\n");

            testRun(baseFibnumArray[i], taskCountArray[i]);
        }
    }
}
