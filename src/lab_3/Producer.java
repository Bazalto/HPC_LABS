package lab_3;

import java.io.File;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue sharedQueue;

    Producer(BlockingQueue sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        File folder = new File("texts/");
        System.out.println(folder.isDirectory()?"Dir":"non");
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles==null) {
            System.out.println("Texts is empty");
            return;
        }
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("Producer opens " + listOfFiles[i].getName());
            }
        }

    }
}
