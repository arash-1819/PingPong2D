import java.awt.*;
import java.awt.event.KeyEvent;

public class Player {
    /** Variables */
    // The Constant changes the size of the resolution and size of the assets
    private final double CONSTANT = Double.parseDouble(config.prop.getProperty("CONSTANT"));

    // properties of the player
        private final String name;
        private Point position;
        private final Color color;
        private int score = 0;
        private final int width;
        private final int height;
        // key values for moving the player
            private final int up;
            private final int down;
            private final int left;
            private final int right;
        private int xSpeed = 0;
        private int ySpeed = 0;
        // boundaries of where the player can move
            private final int minX;
            private final int minY;
            private final int maxX;
            private final int maxY;

    /** Methods */
    /**
     * CONSTRUCTOR
     * @param n name of the player
     * @param p position of the player
     * @param c color of the player
     * @param k array of integer values of up, down, left, and right keys for moving the player
     * @param r boundaries of where the player can move
     */
    public Player(String n, Point p, Color c, int[] k, Rectangle r) {
        name = n;
        position = p;
        color = c;

        width = (int) (Integer.parseInt(config.prop.getProperty("playersWidth"))*CONSTANT);
        height = (int) (Integer.parseInt(config.prop.getProperty("playersHeight"))*CONSTANT);

        up = k[0];
        down = k[1];
        left = k[2];
        right = k[3];

        // boundaries of where the player can move
        minX = r.x;
        minY = r.y;
        maxX = r.x+r.width-width;
        maxY = r.y+r.height-height;
    } // end player()

    /**
     * @return the shape of the player which includes the position
     */
    public Shape shape() {
        return new Rectangle(position.x, position.y, width, height);
    } // end shape()

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    } // end getScore()

    /**
     * @return the name of the player
     */
    public String getName() {
        return name;
    } // end getName()

    /**
     * @return the horizontal speed
     */
    public int getXSpeed() { return xSpeed;} // end getXSpeed()

    /**
     * @return the vertical speed
     */
    //public int getYSpeed() { return ySpeed;} // end getYSpeed

    /**
     * updates the Position
     * @param p new position of the player
     */
    public void setPosition(Point p) {
        position = p;
    } // end setPosition

    /**
     * updates the new score
     * @param s new score for the player
     */
    public void setScore(int s) {
        score = s;
    } // end setScore()

    /**
     * moves the player and updates the position based on its speed
     * and makes sure that it is not outside its limits
     */
    public void tick() {
        position.translate((int) (xSpeed*CONSTANT), (int) (ySpeed*CONSTANT));

        if (position.x < minX) {
            position.x = minX;
        } else if (position.x > maxX) {
            position.x = maxX;
        }

        if (position.y < minY) {
            position.y = minY;
        } else if (position.y > maxY) {
            position.y = maxY;
        }
    } // end tick()

    /**
     * draws the player on the canvas
     * @param g the graphics class to draw the player on
     */
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(position.x, position.y, width, height);
    } // end draw()

    /**
     * updates the speed of the player based on the pressed keys
     * and makes sure the speed is not more than the max speed
     * @param e the entry from the keyboard
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode(); // converting the entry to int value

        // adjust the speed based on the pressed key value
        if (key==up)
            ySpeed -= 4;
        if (key==down)
            ySpeed += 4;
        if (key==left)
            xSpeed -= 2;
        if (key==right)
            xSpeed += 2;

        // making sure the speed does not go beyond the limits
        if (ySpeed>4)
            ySpeed = 4;
        if (ySpeed<-4)
            ySpeed = -4;
        if (xSpeed>2)
            xSpeed = 2;
        if (xSpeed<-2)
            xSpeed = -2;
    } // end keyPressed()

    /**
     * updates the speed of the player based on the released keys
     * @param e – the entry from the keyboard
     */
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode(); // converting the entry to int value

        // changing back the speed to 0 if a key is releaced
        if (key == up)
            ySpeed = 0;
        if (key ==down)
            ySpeed = 0;
        if (key == left)
            xSpeed = 0;
        if (key == right)
            xSpeed = 0;
    } // end keyReleased()
} // end Class Player
