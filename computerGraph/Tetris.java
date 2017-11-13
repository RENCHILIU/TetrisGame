
package computerGraph;



import computerGraph.Display;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.event.ChangeEvent;


public class Tetris extends JFrame {



    // init method
    public Tetris() {
        initUI();
    }

    private void initUI() {
        // init the game board
        Display display = new Display(this);

        add(display);








        // keep the window to be 1000/800  . 22
        setSize(1500, 822);
        setTitle("Tetris Game - RENCHI LIU");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }







    public static void main(String[] args) {
        computerGraph.Tetris game = new computerGraph.Tetris();
        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }
}