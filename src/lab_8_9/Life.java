package lab_8_9;

public class Life {
    final static int N = 256;
    final static int CELL_SIZE = 4;
    static int[][] state = new int[N][N];
    static int stepSpeed = 200; //default

    // different configurations
//    private static int[] live = {2, 3}, born = {3};
//    private static int[] live = {1, 6}, born = {6};
    private static int[] live = {2, 3}, born = {3, 6};

    private static Display display = null;

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                state[i][j] = Math.random() > 0.5 ? 1 : 0;
            }
        }

        int iter = 0;
        while (true) {
            System.out.println("iter = " + iter);

            setNewState(live, born);

            display.repaint();
            pause(stepSpeed);
            iter++;

            if (iter == 50_000) break;
        }
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

    private static int getCellSumm(int i, int j) {
        int sum = 0;
        for (int k = i - 1; k <= i + 1; k++) {
            for (int l = j - 1; l <= j + 1; l++) {
                if (k < 0 || l < 0) continue;
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
