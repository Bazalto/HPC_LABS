package lab_1;

public class PiCalculations extends Thread {

    static int STEP_NUM;

    static double STEP_VALUE;

    static int THREADS_NUM;

    static PiThread[] THREADS;

    public static void main(String[] args) throws Exception {

        System.out.println("      | Method  |         PI        | Calc Time | Acceleration");
        setGlobalValues(10_000_000, 2);
        mainRoutine();

        setGlobalValues(10_000_000, 4);
        mainRoutine();

        setGlobalValues(1_000_000, 4);
        mainRoutine();

        setGlobalValues(100_000, 4);
        mainRoutine();

        setGlobalValues(1_000_000_000, 4);
        mainRoutine();

    }

    public static void mainRoutine() throws Exception {

        if (STEP_NUM == 0 || STEP_VALUE == 0 || THREADS_NUM == 0 || THREADS == null) {
            System.out.println("Please, set global variables properly");
            System.exit(228);
        }

        System.out.println("Steps: " + STEP_NUM + " Threads: " + THREADS_NUM);
        PiCalculations obj = new PiCalculations();

        float seqTime = seqPi();
        float paralelTime = obj.paralelPi();

        System.out.println("\t\t\t\t\t\t\t\t\t\t\t\t\t"
                + String.format("%.2f", (seqTime / paralelTime)) + "\n");
    }

    static void setGlobalValues(int stepNum, int threadsNum) {
        STEP_NUM = stepNum;
        STEP_VALUE = 1.0 / (double) STEP_NUM;

        THREADS_NUM = threadsNum;
        THREADS = new PiThread[THREADS_NUM];
    }

    class PiThread extends Thread {

        double sum;
        int begin, end;

        public void run() {

            sum = 0.0;

            for (int i = begin; i < end; i++) {
                double x = (i + 0.5) * STEP_VALUE;
                sum += 4.0 / (1.0 + x * x);
            }
        }
    }

    private void startThreads(PiThread[] threads) {
        for (PiThread thread :
                threads) {
            thread.start();
        }
    }

    private void joinThreads(PiThread[] threads) throws Exception {
        for (PiThread thread :
                threads) {
            thread.join();
        }
    }

    private long paralelPi() throws Exception {
        long startTime = System.currentTimeMillis();
        int numPoint = 0;

        for (int i = 0; i < THREADS_NUM; i++) {
            THREADS[i] = new PiThread();

            THREADS[i].begin = numPoint;
            numPoint = THREADS[i].end = numPoint + STEP_NUM / THREADS_NUM;
        }

        startThreads(THREADS);
        joinThreads(THREADS);

        long endTime = System.currentTimeMillis();
        double threadsSum = 0;
        for (PiThread thread :
                THREADS) {
            threadsSum += thread.sum;
        }

        double pi = STEP_VALUE * threadsSum;
        long timeElapsed = endTime - startTime;
        System.out.println("        Paralel | " + pi + " | " + timeElapsed);

        return timeElapsed;
    }

    static long seqPi() {
        long startTime = System.currentTimeMillis();
        double sum = 0.0;

        for (int i = 0; i < STEP_NUM; i++) {
            double x = (i + 0.5) * STEP_VALUE;
            sum += 4.0 / (1.0 + x * x);
        }

        double pi = STEP_VALUE * sum;
        long endTime = System.currentTimeMillis();
        long timeElapsed = endTime - startTime;

        System.out.println("        Sequent | " + pi + " | " + timeElapsed);

        return timeElapsed;
    }

}