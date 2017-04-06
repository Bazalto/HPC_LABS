package lab_9;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mpi.MPI;


public class Lmao {

    final static int N = 128;              //size of field
    final static int CELL_SIZE = 4;         //size of one cell
    static int[][] state = null;
    static int[][] sums = null;  //sum of around cells
    static int[][] cells = new int[N][N];

    static Display display = null;


    public static void main(String[] args) {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        int B = N / size;
        state = new int[B + 2][N];
        sums = new int[B + 2][N];

        int next = (me + 1) % size;
        int prev = (me - 1 + size) % size;

        //~~~~~~~~~~~~~~~~~~~
        //Initial state with random values form 0 to 1
        for (int i = 1; i < B + 1; i++) {
            for (int j = 0; j < N; j++) {
                //random() returns double, so we use logic
                // operator to feel values with int literals
                state[i][j] = Math.random() > 0.5 ? 1 : 0;
            }
        }

        MPI.COMM_WORLD.Gather(state, 1, B, MPI.OBJECT,
                cells, 0, B, MPI.OBJECT, 0);

        if (me == 0) {
            display = new Display();
            display.repaint();
            pause();
        }

        //MAIN UPDATE LOOP
        int iter = 0;
        while (iter < 500) {
            System.out.println("iter = " + iter++);

            MPI.COMM_WORLD.Sendrecv(state[B], 0, N, MPI.INT, next, 0,
                    state[0], 0, N, MPI.INT, prev, 0);

            //maybe we need to exchange prev and next arguments!!! chek when compile!
            MPI.COMM_WORLD.Sendrecv(state[0], 0, N, MPI.INT, prev, 0,
                    state[B], 0, N, MPI.INT, next, 0);

            //Program code

            for (int i = 1; i < B + 1; i++) {
                for (int j = 0; j < N; j++) {

                    int jp = (j + 1) % N;
                    int jm = (j - 1 + N) % N;
                    sums[i][j] =
                            state[i - 1][jm] + state[i - 1][j] + state[i - 1][jp] +
                                    state[i][jm] + state[i][jp] + state[i + 1][jm] +
                                    state[i + 1][j] + state[i + 1][jp];
                }
            }
            for (int i = 1; i < B + 1; i++) {
                for (int j = 0; j < N; j++) {
                    switch (sums[i][j]) {
                        case 2:
                            break;
                        case 3:
                            state[i][j] = 1;
                            break;
                        default:
                            state[i][j] = 0;
                            break;
                    }
                }
            }
            MPI.COMM_WORLD.Gather(state, 1, B, MPI.OBJECT,
                    cells, 0, B, MPI.OBJECT, 0);

            if (me == 0) {
                display.repaint();
                pause();
            }
        }

        //~~~~~~~~~~~~~~~~~~~
        MPI.Finalize();
    }

    static class Display extends JPanel {
        final static int WINDOW_SIZE = N * CELL_SIZE;

        Display() {
            setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));   //setup size of window

            JFrame frame = new JFrame("Life");                  //create class of frame "Life"
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);                             //set our panel like content of form
            frame.pack();
            frame.setVisible(true);
        }

        //Graphics g - java give this object herself and we can draw on the panel
        public void paintComponent(Graphics g) {
            g.setColor(Color.black);                         //chose color
            g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);    //fill our rectangular 1024x1024 with color which we set on previous operation
            g.setColor(Color.white);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (cells[i][j] == 1) {
                        g.fillRect(CELL_SIZE * i, CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    static void pause() {
        try {
            Thread.sleep(500);  // sleep on 500 ms
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
