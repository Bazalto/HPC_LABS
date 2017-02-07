package lab_4;

public class Fibonacci implements Runnable {

    private int n;
    private int myId;
    private Integer[] results;
    private long time;

    public Fibonacci(int n, int myId, Integer[] results) {
        this.n = n;
        this.myId = myId;
        this.results = results;
    }

    private int calculate(int fn) {
        if (fn <= 1) {
            return fn;
        }
        return calculate(fn - 1) + calculate(fn - 2);
    }

    @Override
    public void run() {
        System.out.println("Task " + myId + " started");
        long start = System.currentTimeMillis();
        results[myId] = calculate(n);
        this.time = System.currentTimeMillis() - start;
        System.out.println("Task " + myId + " finished. Time: " + time);
    }
}
