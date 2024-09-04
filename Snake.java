import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.Timer;
import java.awt.Rectangle;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author
 */
public class Snake extends JComponent implements ActionListener {

    // Height and Width of our game
    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    // Title of the window
    String title = "Snake";

    // sets the framerate and delay for our game
    // this calculates the number of milliseconds per frame
    // you just need to select an appropriate framerate
    int desiredFPS = 10;
    int desiredTime = Math.round((1000 / desiredFPS));

    // timer used to run the game loop
    // this is what keeps our time running smoothly :)
    Timer gameTimer;

    // YOUR GAME VARIABLES WOULD GO HERE
    // how many long is the snake, how many peices(rectangles)
    int snakeLength = 4;
    // the size of the rectangles of background and snake body
    int rectSize = 20;
    int snakeSpeed = 20;

    // controls
    boolean right = false;
    boolean down = false;
    boolean left = false;
    boolean up = false;
    int direction = 0;

    // apple
    // randomize apple cordinates
    int lowest = 0;
    int highest = 29;
    int randomNumberX = 19;
    int randomNumberY = 19;
    Rectangle apple = new Rectangle(randomNumberX * 20, randomNumberY * 20, rectSize, rectSize);
    // color of apple
    int lowestRGBValue = 60;
    int highestRGBValue = 255;
    // array to store random rgb values
    int[] randomRGBValue = new int[3];
    Color randomColor = new Color(255, 0, 0);

    // rectangle array to make snakes body
    Rectangle[] snakeBody = new Rectangle[450];
    // Snake Color
    Color snakeColor = (Color.BLUE);

    // wich screen to draw, 0= starting, 1= game screen, 2 = how to play, 3 =
    // losing, 4 = game won
    // screen
    int screen = 0;

    // images
    // background
    BufferedImage background = loadImage("StarryBackground.png");
    // start Button
    BufferedImage startButtonPic = loadImage("StartButtonEdit.png");
    Rectangle startButton = new Rectangle(200, 320, 200, 75);
    // how to play Button
    BufferedImage howToPlayButtonPic = loadImage("HowToPlayButtonPic.png");
    Rectangle howToPlayButton = new Rectangle(200, 410, 200, 56);
    // icon
    BufferedImage snakeIconPic = loadImage("SnakeIcon.png");
    // try again image
    BufferedImage tryAgainPic = loadImage("TryAgainPic.png");
    Rectangle tryAgainButton = new Rectangle(200, 180, 200, 100);
    // you lose image
    BufferedImage youLosePic = loadImage("YouLosePic.png");
    // snake background image
    BufferedImage snakePic = loadImage("snakeImage .png");
    // How to play screen instructions
    BufferedImage instructions = loadImage("HowToPLayScreenPic.jpg");
    Rectangle gameScreen = new Rectangle(480, 10, 100, 40);

    // rectangle for mouse to cheak for collisons with buttons
    Rectangle mouse = new Rectangle(0, 0, 10, 10);

    // scoring
    int score = 0;
    int highScore = 0;

    // GAME VARIABLES END HERE

    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Snake() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Keyboard());
        Mouse m = new Mouse();
        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        // Set things up for the game at startup

        setup();

        // Start the game loop
        gameTimer = new Timer(desiredTime, this);
        gameTimer.setRepeats(true);
        gameTimer.start();
    }

    // method to load in images
    public BufferedImage loadImage(String filename) {
        // creating a holding variable
        BufferedImage image = null;
        // loading files may have errors
        try {
            // try to load in image
            image = ImageIO.read(new File(filename));
        } catch (Exception e) {
            // print out red error message
            e.printStackTrace();
        }
        return image;
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // GAME DRAWING GOES HERE

        // starting screen
        if (screen == 0) {
            g.drawImage(background, 0, 0, 600, 680, null);
            g.drawImage(snakeIconPic, 60, 40, 480, 260, null);
            // buttons
            g.drawImage(startButtonPic, startButton.x, startButton.y, startButton.width, startButton.height, null);
            g.drawImage(howToPlayButtonPic, howToPlayButton.x, howToPlayButton.y, howToPlayButton.width,
                    howToPlayButton.height, null);
        } else if (screen == 1) {
            // game screen
            // draw a black Background
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            // Draw the grid
            g.setColor(Color.DARK_GRAY);
            // drawing square across the screen
            for (int x = 0; x < 600; x = x + rectSize) {
                // drawing square down the screen
                for (int y = 0; y < 600; y = y + rectSize) {
                    // draw square
                    g.drawRect(x, y, rectSize, rectSize);

                }
            }

            // Draw the Apple
            g.setColor(randomColor);
            g.fillRect(apple.x, apple.y, apple.width, apple.height);

            // Draw the snake depending on how long the snake is
            g.setColor(snakeColor);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(snakeBody[i].x, snakeBody[i].y, snakeBody[i].width, snakeBody[i].height);
            }

        } else if (screen == 2) {
            // how to play screen
            g.drawImage(background, 0, 0, 600, 680, null);
            g.drawImage(instructions, 0, 0, 600, 600, null);
            g.setColor(Color.WHITE);
            // button to go to game screen
            g.drawRect(gameScreen.x, gameScreen.y, gameScreen.width, gameScreen.height);
            // Make the font bigger
            g.setFont(g.getFont().deriveFont(23f));
            g.drawString("PLAY", 500, 35);

        } else if (screen == 3) {
            // try again screen
            g.drawImage(background, 0, 0, 600, 680, null);
            g.drawImage(snakePic, 0, 385, 600, 215, null);
            g.drawImage(youLosePic, 200, 50, 200, 100, null);
            // try again button
            g.drawImage(tryAgainPic, tryAgainButton.x, tryAgainButton.y, tryAgainButton.width, tryAgainButton.height,
                    null);
            g.setColor(Color.WHITE);
            // make font size bigger
            g.setFont(g.getFont().deriveFont(30f));
            // display scores
            g.drawString("Your Score is: " + score, 200, 320);
            g.drawString("Your High Score is: " + highScore, 160, 360);

        } else if (screen == 4) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 600);
            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(50f));
            g.drawString("YOU WIN!! CONGRATS", 30, 200);
            g.drawImage(snakePic, 0, 385, 600, 215, null);

        }

        // GAME DRAWING ENDS HERE
    }

    // This method is used to do any pre-setup you might need to do
    // This is run before the game loop begins!
    public void setup() {
        // Any of your pre setup before the loop starts should go here

        // Creat the 4 begining peices of the snake
        snakeBody[0] = new Rectangle(60, 0, rectSize, rectSize);
        snakeBody[1] = new Rectangle(40, 0, rectSize, rectSize);
        snakeBody[2] = new Rectangle(20, 0, rectSize, rectSize);
        snakeBody[3] = new Rectangle(0, 0, rectSize, rectSize);

        // create the rest of the snakes body, not drawn
        for (int i = 4; i < 450; i++) {
            snakeBody[i] = new Rectangle(600, 0, rectSize, rectSize);
        }


    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void loop() {

        // controls of the snake
        if (right) {
            // if snake is moving right
            // the snakes body exept the head goes to the position of the rectangle before
            // it
            for (int i = snakeLength - 1; i > 0; i--) {
                snakeBody[i].x = snakeBody[i - 1].x;
                snakeBody[i].y = snakeBody[i - 1].y;
            }
            // head of the snake moves right
            snakeBody[0].x = snakeBody[0].x + rectSize;

        } else if (down) {
            // if snake is going down
            // the snakes body exept the head goes to the position of the rectangle before
            // it
            // make it look like the snake is turning
            for (int i = snakeLength - 1; i > 0; i--) {
                snakeBody[i].x = snakeBody[i - 1].x;
                snakeBody[i].y = snakeBody[i - 1].y;
            }
            // head of the snake moves down
            snakeBody[0].y = snakeBody[0].y + rectSize;

        } else if (left) {
            // if snake is moving left
            // the snakes body exept the head goes to the position of the rectangle before
            // it
            // make it look like the snake is turning
            for (int i = snakeLength - 1; i > 0; i--) {
                snakeBody[i].x = snakeBody[i - 1].x;
                snakeBody[i].y = snakeBody[i - 1].y;
            }
            // head of the snake moves to the left
            snakeBody[0].x = snakeBody[0].x - rectSize;

        } else if (up) {
            // if snake is moving up
            // the snakes body exept the head goes to the position of the rectangle before
            // it
            // make it look like the snake is turning
            for (int i = snakeLength - 1; i > 0; i--) {
                snakeBody[i].x = snakeBody[i - 1].x;
                snakeBody[i].y = snakeBody[i - 1].y;
            }
            // head of the snake moves upward
            snakeBody[0].y = snakeBody[0].y - rectSize;
        }

        // make apple reapear in a diffrent spot if the head of the snake and the apple
        // collide
        if (collisionDectionWithApple(apple) == true) {
            // make the snake longer
            snakeLength = snakeLength + 3;
            // randomiz the apples corinates
            repositionApple();
            // create a new random color for apple
            for (int i = 0; i < 2; i++) {
                randomRGBValue[i] = (int) (Math.random() * (highestRGBValue - lowestRGBValue + 1) + lowestRGBValue);
            }
            randomColor = new Color(randomRGBValue[0], randomRGBValue[1], randomRGBValue[2]);

            // make sure new apple dosent spawn on the snakes body
            for (int i = 0; i < snakeLength - 1; i++) {
                if (apple.x == snakeBody[i].x && apple.y == snakeBody[i].y) {
                    repositionApple();
                }
            }

        }

        // if the snakes head hits the snakes body go to the game over screen
        for (int i = 1; i < snakeLength; i++) {
            if (snakeBody[0].x == snakeBody[i].x && snakeBody[0].y == snakeBody[i].y) {
                screen = 3;
            }

        }

        // cheak of the snake collides with the wall of the frames, if it does go to
        // game over screen
        if (snakeBody[0].x > WIDTH) {
            screen = 3;
        } else if (snakeBody[0].x < 0) {
            screen = 3;
        } else if (snakeBody[0].y > HEIGHT) {
            screen = 3;
        } else if (snakeBody[0].y < 0) {
            screen = 3;
        }

        // if the new score is heigher than the old one set the high score to the higher
        // number
        if (screen == 3) {
            if (highScore < score) {
                highScore = score;
            }
        }

        // make the snake flasj with diffrent colors every 30 points
        for (int i = 10; i < 450; i = i + 10) {
            if (score == i * 3) {
                for (int j = 0; j < 2; j++) {
                    randomRGBValue[j] = (int) (Math.random() * (highestRGBValue - lowestRGBValue + 1) + lowestRGBValue);
                }
                snakeColor = new Color(randomRGBValue[0], randomRGBValue[1], randomRGBValue[2]);
            } else if (score == (i * 3) + 3) {
                snakeColor = (Color.BLUE);
            }
        }

        // has the player won the game
        // to win the gam the snake must be 450 peices long
        if (snakeLength == 450) {
            screen = 4;
        }
    }

    // randomiz apple cordinates
    public void repositionApple() {
        randomNumberX = (int) (Math.random() * (highest - lowest + 1) + lowest);
        randomNumberY = (int) (Math.random() * (highest - lowest + 1) + lowest);
        apple.x = randomNumberX * rectSize;
        apple.y = randomNumberY * rectSize;

    }

    // cheak is snakes head collides with apple
    public boolean collisionDectionWithApple(Rectangle apple) {
        // if the head of snake colides with the apple
        if (snakeBody[0].intersects(apple)) {
            // make the score go up by 3
            score = score + 3;
            // shapes are colliding
            return true;
        }
        // shapes are not colliding
        return false;
    }

    // cheak if the start button is pressed
    public boolean mouseCollidesStartButton(Rectangle startButton) {
        // comparing
        if (mouse.intersects(startButton)) {
            // shapes are colliding
            return true;
        } else {
            // shapes arent colliding
            return false;
        }

    }

    // cheak if the how to play button is pressed
    public boolean mouseCollidesHowToPlayButton(Rectangle howToPlayButton) {
        // comparing
        if (mouse.intersects(howToPlayButton)) {
            // shapes are colliding
            return true;
        } else {
            // shapes arent colliding
            return false;
        }
    }

    // cheak if the try again button is pressed
    public boolean mouseCollidesTryAgain(Rectangle tryAgainButton) {
        // comparing
        if (mouse.intersects(tryAgainButton)) {
            // shapes are colliding
            return true;
        } else {
            // shapes arent colliding
            return false;
        }
    }

    // cheak if the bake to main screen button is pressed
    public boolean mouseCollidesbackToMainScreen(Rectangle backToMainScreen) {
        // comparing
        if (mouse.intersects(gameScreen)) {
            // shapes are colliding
            return true;
        } else {
            // shapes arent colliding
            return false;
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {

        // if a mouse button has been pressed down
        @Override
        public void mousePressed(MouseEvent e) {
            // switch screens according to wotch button is pressed
            if (mouseCollidesStartButton(startButton) == true) {
                // go to game screen, start game
                screen = 1;
            } else if (mouseCollidesHowToPlayButton(howToPlayButton) == true) {
                // go to how to play the game instructions
                screen = 2;
            } else if (mouseCollidesTryAgain(tryAgainButton) == true) {
                // set score back to 0
                score = 0;
                // switch back to game screen
                screen = 1;
                // make the snakes body to 4 again
                snakeLength = 4;
                for (int i = 0; i < 4; i++) {
                    snakeBody[i].x = 0;
                    snakeBody[i].y = 0;
                }

                // turn all controls off
                right = false;
                left = false;
                up = false;
                down = false;
                direction = 5;
                // put the snakes body somwhere you cannt see it while its not drawn.
                for (int i = 1; i < 450; i++) {
                    snakeBody[i].x = 600;
                    snakeBody[i].y = 0;
                }

            } else if (mouseCollidesbackToMainScreen(gameScreen) == true) {
                // go to game screen
                screen = 1;
            }
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {

        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {

            // get cordinates of the mouse
            mouse.x = e.getX();
            mouse.y = e.getY();
        }
    }

    // Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {

        // if a key has been pressed down
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_RIGHT) {
                // if the snake was going left before dont go rigth
                // because the snake would collide into itself
                if (direction == 3) {
                    right = false;
                    up = false;
                    down = false;
                } else {
                    direction = 0;
                    right = true;
                    up = false;
                    down = false;
                }

            } else if (key == KeyEvent.VK_DOWN) {
                // if the snake was going up before dont go rigth
                // because the snake would collide into itself
                if (direction == 2) {
                    down = false;
                    right = false;
                    left = false;
                } else {
                    direction = 1;
                    down = true;
                    right = false;
                    left = false;
                }

            } else if (key == KeyEvent.VK_UP) {
                // if the snake was going down before dont go rigth
                // because the snake would collide into itself
                if (direction == 1) {
                    up = false;
                    right = false;
                    left = false;
                } else {
                    direction = 2;
                    up = true;
                    right = false;
                    left = false;
                }
            } else if (key == KeyEvent.VK_LEFT) {
                // if the snake was going right before dont go rigth
                // because the snake would collide into itself
                if (direction == 0) {
                    left = false;
                    up = false;
                    down = false;
                } else {
                    direction = 3;
                    left = true;
                    up = false;
                    down = false;
                }
            }
        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        loop();
        repaint();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Snake game = new Snake();
    }
}