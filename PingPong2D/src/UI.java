import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;

public class UI {
    /** Variables */
    // properties of the display
        private final static double CONSTANT = Double.valueOf(config.prop.getProperty("CONSTANT"));
        private final static int WIDTH = (int) (Integer.parseInt(config.prop.getProperty("WIDTH"))*CONSTANT);
        private final static int HEIGHT = (int) (Integer.parseInt(config.prop.getProperty("HEIGHT"))*CONSTANT);
        private final static int MARGIN = (int) (Integer.parseInt(config.prop.getProperty("MARGIN"))*CONSTANT);
        private final static int BOUNDARY = (int) (Integer.parseInt(config.prop.getProperty("BOUNDARY"))*CONSTANT);
        private final static Rectangle DISPLAY = new Rectangle(0, 0, WIDTH, HEIGHT);
    Ball ball;
    Player leftP;
    Player rightP;
    private static Color UIColor = Color.decode(config.prop.getProperty("UIColor"));
    // Score Board's variables
        private static String SBtext; // text
        private Rectangle SBRect = new Rectangle(MARGIN, HEIGHT - MARGIN, WIDTH - 2 * MARGIN, MARGIN); // position
        private final Font SBFont = new Font("Monospaced", Font.ITALIC, (int) (10*CONSTANT));
    // font for the final results
    private final Font ResultsFont = new Font("Monospaced", Font.ITALIC, (int) (30*CONSTANT));

    /**
     * CONSTRUCTOR
     * @param b Ball
     * @param LP left Player
     * @param RP right Player
     */
    public UI(Ball b, Player LP, Player RP) {
        ball = b;
        leftP = LP;
        rightP = RP;

        // the Score Board's message
        SBtext = leftP.getName() + ": " + leftP.getScore() + "    " + rightP.getName() + ": " + rightP.getScore();
    } // end UI()

    /**
     * draws the lines for the game
     * and updates the score board
     * @param g the graphics class to draw the player on
     */
    public void draw(Graphics g) {
        g.setColor(UIColor);

        //g.drawRect(MARGIN, MARGIN, WIDTH-2*MARGIN, HEIGHT-2*MARGIN); // Table's Parameter
        g.drawRect(MARGIN, MARGIN, WIDTH/2-MARGIN, HEIGHT-2*MARGIN); // Left Side's Parameter
        g.drawRect(WIDTH/2, MARGIN, WIDTH/2-MARGIN, HEIGHT-2*MARGIN); // Right Side's Parameter
        g.drawRect(MARGIN, MARGIN, BOUNDARY, HEIGHT-2*MARGIN); // Left Player's Bounds
        g.drawRect(WIDTH-MARGIN-BOUNDARY, MARGIN, BOUNDARY, HEIGHT-2*MARGIN); // Right Player's Bounds
        g.drawRect(WIDTH/2-BOUNDARY/2, HEIGHT/2-BOUNDARY/2, BOUNDARY, BOUNDARY); // Ball's Spawn Site

        // draws the updated Score Board
        drawCenteredString(g, SBtext, SBRect, SBFont);
    } // end draw()

    /**
     * updated the Score based on the position of the ball
     * 0: ball is still on the Table
     * -1: ball has gone out from the left side, so right player gets one point
     * 1: ball has gone out from the right side, so left player gets one point
     */
    public void updateScore() {
        switch (ball.out()) {
            case 0:
                break;
            case -1:
                rightP.setScore(rightP.getScore() + 1);
                SBtext = leftP.getName() + ": " + leftP.getScore() + "    " + rightP.getName() + ": " + rightP.getScore();
                break;
            case 1:
                leftP.setScore(leftP.getScore() + 1);
                SBtext = leftP.getName() + ": " + leftP.getScore() + "    " + rightP.getName() + ": " + rightP.getScore();
                break;
        } // end switch
    } // end updateScore()

    /**
     * @param g the graphics class to draw the player on
     * @return whether the game is finish and needs to get a reset
     * and prints the results
     * @throws InterruptedException
     */
    public boolean resetGame(Graphics g) throws InterruptedException {
        if (leftP.getScore() >= 11 && leftP.getScore() - rightP.getScore() >= 2) { // left player won
            announceWinner(g, leftP, rightP);
            return true;
        } else if (rightP.getScore() >= 11 && rightP.getScore() - leftP.getScore() >= 2) { // right player won
            announceWinner(g, rightP, leftP);
            return true;
        } else // game's not finished
            return false;
    } // end resetGame()

    /**
     * draws a text with a specific font in center of a rectangle
     * @param g the graphics class to draw the player on
     * @param t text to draw in the Rectangle r
     * @param r the rectangle to draw the tex in its center
     * @param f font
     */
    public static void drawCenteredString(Graphics g, String t, Rectangle r, Font f) {
        // get the bound of the text written with the font
        FontRenderContext frc = new FontRenderContext(null, true, true);
        Rectangle2D fRect = f.getStringBounds(t, frc);

        // get the values of the rectangle to write in
        int rWidth = (int) Math.round(fRect.getWidth());
        int rHeight = (int) Math.round(fRect.getHeight());
        int rX = (int) Math.round(fRect.getX());
        int rY = (int) Math.round(fRect.getY());

        // calculate the point to draw the string from
        int a = (r.width / 2) - (rWidth / 2) - rX + r.x;
        int b = (r.height / 2) - (rHeight / 2) - rY + r.y;

        // draw the test
        g.setFont(f);
        g.drawString(t, a, b);
    } // end drawCenteredString()

    /**
     * @param g the graphics class to draw the player on
     * @param wonP Won Player
     * @param lostP Lost Player
     */
    private void announceWinner(Graphics g, Player wonP, Player lostP) {
        // draw the results
        g.setColor(UIColor);
        drawCenteredString(g, wonP.getName() + " won!", DISPLAY, ResultsFont);
        drawCenteredString(g, SBtext, SBRect, SBFont);
        // reset the scores for the next game
        lostP.setScore(0);
        wonP.setScore(0);
        SBtext = leftP.getName() + ": " + leftP.getScore() + "    " + rightP.getName() + ": " + rightP.getScore();
    } // end announceWinner()
}