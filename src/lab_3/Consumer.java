package lab_3;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Consumer implements Runnable {
    private final BlockingQueue sharedQueue;
    private final ProducerConsumerExample pce;

    Consumer(ProducerConsumerExample pce, BlockingQueue sharedQueue) {
        this.pce = pce;
        this.sharedQueue = sharedQueue;
    }

    private synchronized int countWords(String s) {
        int wordsFound = 0;
        Pattern p = Pattern.compile(ProducerConsumerExample.WORD_TO_FIND);
        Matcher m = p.matcher(s);
        while (m.find()) {
            wordsFound++;
        }
        return wordsFound;
    }

    @Override
    public void run() {
        while (true) {
            String line = (String) sharedQueue.poll();
            if (null != line) {
                pce.addWordCount(countWords(line));
            }
            if (sharedQueue.size() == 0 && !pce.isReadingData()) {
                break;
            }

        }
    }
}
