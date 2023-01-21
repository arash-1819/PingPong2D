import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.*;
import static java.lang.Thread.sleep;

public class Table extends JPanel implements ActionListener, KeyListener {
    /** Variables */
    // properties of the display
        private final static double CONSTANT = parseDouble(config.prop.getProperty("CONSTANT"));
        private final static int WIDTH = (int) (parseInt(config.prop.getProperty("WIDTH"))*CONSTANT);
        private final static int HEIGHT = (int) (parseInt(config.prop.getProperty("HEIGHT"))*CONSTANT);
        private final static int MARGIN = (int) (parseInt(config.prop.getProperty("MARGIN"))*CONSTANT);
        private final static int BOUNDARY = (int) (parseInt(config.prop.getProperty("BOUNDARY"))*CONSTANT);
        private final static Rectangle DISPLAY = new Rectangle(0, 0, WIDTH, HEIGHT);
        private final Color backGroundColor = Color.decode((config.prop.getProperty("backGroundColor")));
    private final int DELAY =  parseInt(config.prop.getProperty("DELAY"));
    private final Timer timer;
    public boolean paused = false;
    // players' variables
        private final int pHeight = (int) (parseInt(config.prop.getProperty("playersHeight"))*CONSTANT);
        private final int pWidth = (int) (parseInt(config.prop.getProperty("playersWidth"))*CONSTANT);
        // left player
            private Player leftP;
            private final String leftPName = config.prop.getProperty("leftPName");
            private final Color leftPColor = Color.decode((config.prop.getProperty("leftPColor")));
            private final Point leftPStartingPoint = new Point(MARGIN, HEIGHT/2-pHeight/2);
            private int[] leftPKeys = {
                    parseInt(config.prop.getProperty("leftPUp")),
                    parseInt(config.prop.getProperty("leftPDown")),
                    parseInt(config.prop.getProperty("leftPLeft")),
                    parseInt(config.prop.getProperty("leftPRight"))
            };
        // right player
            private Player rightP;
            private final String rightPName = config.prop.getProperty("rightPName");
            private final Color rightRColor = Color.decode((config.prop.getProperty("rightPColor")));
            private final Point rightPStartingPoint = new Point(WIDTH-MARGIN-pWidth, HEIGHT/2-pHeight/2);
            private int[] rightPKeys = {
                    parseInt(config.prop.getProperty("rightPUp")),
                    parseInt(config.prop.getProperty("rightPDown")),
                    parseInt(config.prop.getProperty("rightPLeft")),
                    parseInt(config.prop.getProperty("rightPRight"))
            };
    // Ball's Variables
        private Ball ball;
        private static final Color ballColor = Color.decode(config.prop.getProperty("ballColor"));
        private final int diameter = (int) (parseInt(config.prop.getProperty("ballDiameter"))*CONSTANT);
    // UI's Variables
        private UI UI;
        private static final Color UIColor = Color.decode(config.prop.getProperty("UIColor"));
        private final Font CountDownFont = new Font("Monospaced", Font.ITALIC, (int) (72*CONSTANT));
        private int wait = 0;
        private int count = 3;

    /**
     * CONSTRUCTOR
     */
    public Table() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(backGroundColor);

        Rectangle leftPRect = new Rectangle(MARGIN,MARGIN, BOUNDARY, HEIGHT-2*MARGIN);
        leftP = new Player(leftPName, new Point(leftPStartingPoint), leftPColor, leftPKeys, leftPRect);

        Rectangle rightPRect = new Rectangle(WIDTH-MARGIN-BOUNDARY, MARGIN,BOUNDARY, HEIGHT-2*MARGIN);
        rightP = new Player(rightPName, new Point(rightPStartingPoint), rightRColor, rightPKeys, rightPRect);

        ball = new Ball(diameter, ballColor);

        UI = new UI(ball, leftP, rightP);

        timer = new Timer(DELAY, this);
        timer.start();
    } // end Table()

    /**
     * process the changes in the game
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ball.hitPlayer(leftP, leftP.shape().getBounds().x + leftP.shape().getBounds().width);
        ball.hitPlayer(rightP, rightP.shape().getBounds().x - ball.shape().getBounds().width);
        ball.hitEdge();

        leftP.tick();
        rightP.tick();

        // new round if the ball is out
        if(Ball.out() != 0) {
            UI.updateScore();
            leftP.setPosition(new Point(leftPStartingPoint));
            rightP.setPosition(new Point(rightPStartingPoint));
            Ball.hurl();
        }

        repaint(); // draws the new frame using painComponent(Graphics g)

        // freezes the frame if wait does not equal 0
        if (wait!=0) {
            try {
                sleep(wait);
                wait=0;
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    } // end actionPerformed()

    /**
     * repaints the frame if the game is not paused
     * @param g the graphics class to draw the player on
     * @throws RuntimeException
     */
    @Override
    public void paintComponent(Graphics g) throws RuntimeException {
        if (!paused) { // if the game is not paused
            super.paintComponent(g); // erases the previous frame
            if (count > 0) { // new game countDown
                g.setColor(UIColor);
                UI.drawCenteredString(g, String.valueOf(count), DISPLAY, CountDownFont);
                count--;
                wait = 1000;
            } else {
                try {
                    if (UI.resetGame(g)) { // if the games is over
                        count = 3;
                        wait = 5000;
                    } else { // mid game
                        leftP.draw(g);
                        rightP.draw(g);

                        ball.draw(g);

                        UI.draw(g);
                    }
                } catch (InterruptedException e) {
                    super.paintComponent(g);
                    throw new RuntimeException(e);
                }
            }
        }
        // makes the game a little smoother
        Toolkit.getDefaultToolkit().sync();
        setDoubleBuffered(true);
    } // end paintComponent()

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * process the input of pressed keys
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!paused) {
            leftP.keyPressed(e);
            rightP.keyPressed(e);
        }
        if (e.getKeyCode()==27)
            paused=!paused;
    } // end keyPressed()

    /**
     * process the input of released keys
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (!paused) {
            leftP.keyReleased(e);
            rightP.keyReleased(e);
        }
    } // end keyReleased()
} // end Class Table
