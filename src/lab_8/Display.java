package lab_8;

import javax.swing.*;
import java.awt.*;

import static lab_8.Life.*;


public class Display extends JPanel {
    final static int WINDOW_SIZE = N * CELL_SIZE;

    Display() {
        System.out.println("Display generated");
        setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));
        JFrame frame = new JFrame("Life");

        JToolBar toolBar = new JToolBar();

        JSlider slider = new JSlider(0, 1000);
        slider.setPreferredSize(new Dimension(WINDOW_SIZE, 50));
        slider.addChangeListener(e -> stepSpeed = 1000 - slider.getValue());
        toolBar.add(slider);

        add(toolBar, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);


    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
        g.setColor(Color.WHITE);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (state[i][j] == 1) {
                    g.fillRect(CELL_SIZE * i, CELL_SIZE * j,
                            CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }
}
