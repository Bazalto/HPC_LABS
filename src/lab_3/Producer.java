package lab_3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    public static int totalID = 0;
    private final BlockingQueue sharedQueue;
    private final BlockingQueue filesQueue;
    private final ProducerConsumerExample pce;
    private final int producerID;

    Producer(ProducerConsumerExample pce, BlockingQueue sharedQueue, BlockingQueue filesQueue) {
        this.pce = pce;
        this.sharedQueue = sharedQueue;
        this.filesQueue = filesQueue;
        producerID = totalID++;
    }

    @Override
    public synchronized void run() {
        File file;
        while ((file = (File) filesQueue.poll()) != null) {
            if (file.isFile()) {
//                System.out.println("Producer #" + producerID + " got " + file.getName() + " file");
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sharedQueue.put(line);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        pce.setReadingData(false);
    }
}
