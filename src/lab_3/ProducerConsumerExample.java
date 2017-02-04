package lab_3;

import java.io.File;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerExample {
    private int wordCount = 0;
    private boolean readingData = true;
    public static String WORD_TO_FIND;
    public static String PATH_TO_TEXTS = "src/lab_3/texts/";

    public synchronized int getWordCount() {
        return wordCount;
    }

    public synchronized void addWordCount(int wordCount) {
        this.wordCount += wordCount;
    }

    public synchronized boolean isReadingData() {
        return readingData;
    }

    public synchronized void setReadingData(boolean readingData) {
        this.readingData = readingData;
    }

    private static void startThreads(Thread[] threads) {
        for (Thread thread :
                threads) {
            thread.start();
        }
    }

    private static void joinThreads(Thread[] threads) {
        for (Thread thread :
                threads) {
            try {
                thread.join(200);
            } catch (InterruptedException ex) {

            }
        }
    }


    // contains all operations to calculate 1 set of input parameters: producers and consumers amount
    private static void mainRoutine(int producersAmount, int consumersAmount) throws InterruptedException {
        long start = System.currentTimeMillis();
        ProducerConsumerExample pce = new ProducerConsumerExample();

        // create a queue with files than should be processed
        File textsDir = new File(PATH_TO_TEXTS);
        File[] allFiles = textsDir.listFiles();
        BlockingQueue filesToSearch = new LinkedBlockingQueue();
        for (File file : allFiles) {
            filesToSearch.put(file);
        }

        // queue with lines to be processed
        BlockingQueue queue = new LinkedBlockingQueue();

        Thread[] prodThreads = new Thread[producersAmount];
        for (int i = 0; i < producersAmount; i++) {
            prodThreads[i] = new Thread(new Producer(pce, queue, filesToSearch));
        }
        startThreads(prodThreads);
        joinThreads(prodThreads);

        Thread[] consumers = new Thread[consumersAmount];
        for (int i = 0; i < consumersAmount; i++) {
            consumers[i] = new Thread(new Consumer(pce, queue));
        }
        startThreads(consumers);
        joinThreads(consumers);

        long end = System.currentTimeMillis();
        System.out.println(consumersAmount + "\t\t\t\t" + (end - start) + "\t\t\t\t" + pce.getWordCount());

        // reset producer's ID
        Producer.totalID = 0;
    }

    public static void main(String[] args) {

        System.out.println("Enter word to find (blank to exit):");
        Scanner scanner = new Scanner(System.in);
        while (!(WORD_TO_FIND = scanner.nextLine()).equals("")) {

            System.out.println("Consumers     Exec time    Words found");
            try {
                System.out.println("Producers: 2");
                mainRoutine(2, 2);
                mainRoutine(2, 3);
                mainRoutine(2, 4);
                mainRoutine(2, 10);

                System.out.println("Producers: 3");
                mainRoutine(3, 2);
                mainRoutine(3, 3);
                mainRoutine(3, 4);
                mainRoutine(3, 10);
            } catch (InterruptedException ex) {

            }
            System.out.println("Enter word to find (blank to exit):");
        }
    }
}
