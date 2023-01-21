import java.awt.*;
import java.util.Random;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.*;

public class Ball {
    /** Variables */
    // properties of the display
        private final static double CONSTANT = parseDouble(config.prop.getProperty("CONSTANT"));
        private final static int WIDTH = (int) (parseInt(config.prop.getProperty("WIDTH"))*CONSTANT);
        private final static int HEIGHT = (int) (parseInt(config.prop.getProperty("HEIGHT"))*CONSTANT);
        private final static int MARGIN = (int) (parseInt(config.prop.getProperty("MARGIN"))*CONSTANT);
        private final static int BOUNDARY = (int) (parseInt(config.prop.getProperty("BOUNDARY"))*CONSTANT);
    // properties of the Ball
        private static int diameter;
        private final Color color;
        private static Point position;
        private static int speed;
        private static int ySpeed;
        private static int xSpeed;
        // bounds that the ball can be randomly dropped in
            private final int minY;
            private final int maxY;

    /** Methods */
    /**
     *  CONSTRUCTOR
     * @param d diameter of the Ball
     * @param c color of the Ball
     */
    public Ball(int d, Color c) {
        diameter = d;
        color = c;

        minY = MARGIN;
        maxY = HEIGHT-MARGIN-(2*diameter);

        speed = (int) (parseInt(config.prop.getProperty("ballStartingSpeed"))*CONSTANT);

        hurl();
    } // end Ball()

    /**
     * @return the shape of the Ball which includes the position
     */
    public Shape shape() {
        return new Rectangle(position.x, position.y, diameter, diameter);
    } // end shape()

    /**
     * @return an int value to see if the ball is out
     * -1: ball has gone out from the left side
     * 1: ball has gone out from the right side
     * 0: ball is still on the Table
     */
    public static int out() {
        if(position.x<0)
            return -1;
        else if (position.x>WIDTH)
            return 1;
        else
            return 0;
    } // end out()

    /**
     * moves
     * and draws the player on the canvas
     * @param g the graphics class to draw the player on
     */
    public void draw(Graphics g) {
        position.translate(xSpeed, ySpeed);

        g.setColor(color);
        g.fillOval(position.x, position.y, diameter, diameter);
    } // end draw()

    /**
     * randomly drops the ball in the game based on the limits
     */
    public static void hurl() {
        // determines the random position which the ball will be dropped in
        position = new Point(
                (WIDTH / 2) + new Random().nextInt() % (BOUNDARY / 2),
                (HEIGHT / 2) + new Random().nextInt() % (BOUNDARY / 2)
        );

        // determines the vertical and the horizontal speed
        xSpeed = (int) (Math.sqrt(speed * speed - ySpeed * ySpeed));
        ySpeed = (new Random().nextInt()) % (speed / 2);

        // fixed the direction of the ball based on the side that it has been dropped on
        if (position.x+diameter<WIDTH/2)
            xSpeed = -1*xSpeed;

        // makes sure the ball does not just moves horizontally
        if (ySpeed == 0)
            ySpeed = 1;
    } // end hurl()

    /**
     * changed the direction of the ball if it hits the edge
     */
    public void hitEdge() {
        if (position.y < minY) {
            position.y = minY;
            ySpeed = -ySpeed;
        } else if (position.y > maxY) {
            position.y = maxY;
            ySpeed = -ySpeed;
        }
    } // end hitEdge()

    /**
     * changes the direction of the ball if it hits a Player
     * @param p Player
     * @param xLimit the x bound which the ball cannot go cross. This is to prevent the ball from going into the player
     *               and causing issues in the game
     */
    public void hitPlayer(Player p, int xLimit) {
        Rectangle playerR = (Rectangle) p.shape();

        if (playerR.intersects((Rectangle) shape())) {
            if(p.getXSpeed()<0 && xSpeed>0)
                xSpeed+=1;
            if(p.getXSpeed()>0 && xSpeed<0)
                xSpeed-=1;
            xSpeed=-xSpeed;

            //System.out.println(xSpeed); // for testing purposes

            position.x=xLimit;
        }
    } // end hitPlayer()
} // end class Ball
