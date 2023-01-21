/*
PingPong2D
12/19/2022
Arash Khavaran
 */

import javax.swing.*;

public class Main {
    /**
     * load table into a JFrame
     */
    private static void initWindow() {
        JFrame game = new JFrame("PingPong2D");

        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Table table = new Table();
        game.add(table);
        game.addKeyListener(table);

        game.setResizable(false);
        game.pack();
        game.setLocationRelativeTo(null);

        game.setVisible(true);
    } // end initWindow()

    public static void main(String[] args) {
        config.initializePropertyFile();
        SwingUtilities.invokeLater(Main::initWindow);
    } // end main()
} // end class Main