package lab_9;

import mpi.MPI;
import org.jetbrains.annotations.Contract;

public class LifeParalel {
    final static int N = 512;
    final static int CELL_SIZE = 2;
    static int[][] state = new int[N][N];
    //    static int[][] state = null;
    static int stepSpeed = 1000; //default

    // different configurations
    private static int[] live = {2, 3}, born = {3};
//    private static int[] live = {1, 6}, born = {6};
//    private static int[] live = {2, 3}, born = {3, 6};

    private static Display display = null;

    public static void main(String[] args) {

        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

//        int B = N / size;
//        state = new int[B + 2][N];
//
//        for (int i = 0; i < B + 1; i++) {
//            for (int j = 0; j < N; j++) {
//                state[i][j] = Math.random() > 0.5 ? 1 : 0;
//            }
//        }

        for (int i = 0; i < N + 1; i++) {
            for (int j = 0; j < N; j++) {
                state[i][j] = Math.random() > 0.5 ? 1 : 0;
            }
        }

        System.out.println("Current me: " + me);

        if (me == 0) {
            display = new Display();
            display.repaint();
            pause(stepSpeed);
        }

        int iter = 0;
        while (iter++ < 1500) {
            System.out.println("iter = " + iter);

//            for (int i = 1; i < B + 1; i++) {
//                for (int j = 0; j < N; j++) {
//
//                    int jp = (j + 1) % N;
//                    int jm = (j - 1 + N) % N;
//
//                }
//            }
            if (me == 0) {
                setNewState(live, born);
                System.out.println("Current me: " + me);

                display.repaint();
                pause(stepSpeed);
            }
        }

        MPI.Finalize();
    }

    // live / born
    // [2,3]/[3]
    // [1,6]/[6]
    // [2,3]/[3,6]
    private static void setNewState(int[] live, int[] born) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int cellState = state[i][j];
                int cellSumm = getCellSumm(i, j);

                // cell alive
                if (cellState == 1) {
                    // make it dead until alive case appears
                    state[i][j] = 0;
                    // stay alive cases
                    for (int liveNum : live) {
                        if (liveNum == cellSumm) {
                            state[i][j] = 1;
                            break;
                        }
                    }
                }
                // cell dead
                else {
                    // born cases
                    for (int bornNum : born) {
                        if (bornNum == cellSumm) {
                            state[i][j] = 1;
                            break;
                        }
                    }
                }
            }
        }
    }

    @Contract(pure = true)
    private static int getCellSumm(int i, int j) {
        int sum = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k < 0 || k == N || l < 0 || l == N) continue;
                if (state[k][l] == 1) sum++;
            }
        }

        // if THIS cell is alive - subtract 1
        sum -= state[i][j];

        return sum;
    }

    private static void pause(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.exit(228);
        }
    }
}
