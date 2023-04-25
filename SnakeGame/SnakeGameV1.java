package com.sankegame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SnakeGameV1 extends GameEngine {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int DOT_SIZE = 10;
    public static final int RAND_POS = WIDTH / DOT_SIZE;

    private static int choose = 0;

    private List<Point> greenSnake;
    private List<Point> blueSnake;
    private int greenDots;
    private int blueDots;
    private Point apple;
    private int greenDirection;
    private int blueDirection;

    private boolean greenIsLive;
    private boolean blueIsLive;
    private boolean allIsLive;

    @Override
    public void init() {
        greenSnake = new ArrayList<Point>();
        blueSnake = new ArrayList<Point>();
        greenDots = 3;
        blueDots = 3;

        for (int i = 0; i < greenDots; i++) {
            greenSnake.add(new Point(50 - i * DOT_SIZE, 50));
            blueSnake.add(new Point(50 - i * DOT_SIZE, 450));
        }

        apple = new Point(rand(RAND_POS) * DOT_SIZE, rand(RAND_POS) * DOT_SIZE);

        greenIsLive = true;
        blueIsLive = true;
        allIsLive = true;

        greenDirection = KeyEvent.VK_RIGHT;
        blueDirection = KeyEvent.VK_D;
    }

    private void greenMove() {
        for (int i = greenDots - 1; i > 0; i--) {
            greenSnake.get(i).x = greenSnake.get(i - 1).x;
            greenSnake.get(i).y = greenSnake.get(i - 1).y;
        }
        if (greenDirection == KeyEvent.VK_LEFT) {
            greenSnake.get(0).x -= DOT_SIZE;
        } else if (greenDirection == KeyEvent.VK_RIGHT) {
            greenSnake.get(0).x += DOT_SIZE;
        } else if (greenDirection == KeyEvent.VK_UP) {
            greenSnake.get(0).y -= DOT_SIZE;
        } else if (greenDirection == KeyEvent.VK_DOWN) {
            greenSnake.get(0).y += DOT_SIZE;
        }
    }

    private void blueMove() {
        for (int i = blueDots - 1; i > 0; i--) {
            blueSnake.get(i).x = blueSnake.get(i - 1).x;
            blueSnake.get(i).y = blueSnake.get(i - 1).y;
        }
        if (blueDirection == KeyEvent.VK_A) {
            blueSnake.get(0).x -= DOT_SIZE;
        } else if (blueDirection == KeyEvent.VK_D) {
            blueSnake.get(0).x += DOT_SIZE;
        } else if (blueDirection == KeyEvent.VK_W) {
            blueSnake.get(0).y -= DOT_SIZE;
        } else if (blueDirection == KeyEvent.VK_S) {
            blueSnake.get(0).y += DOT_SIZE;
        }
    }



//    private boolean checkCollision(Integer dots, List<Point> snake) {
//        for (int i = dots - 1; i > 0; i--) {
//            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
//                return false;
//            }
//        }
//        if (snake.get(0).x < 0 || snake.get(0).x > WIDTH - DOT_SIZE || snake.get(0).y < 0 || snake.get(0).y > HEIGHT - DOT_SIZE) {
//            return false;
//        }
//        if (snake.get(0).x == apple.x && snake.get(0).y == apple.y) {
//            dots++;
//            snake.add(new Point(snake.get(dots - 2)));
//            apple.setLocation(rand(RAND_POS) * DOT_SIZE, rand(RAND_POS) * DOT_SIZE);
//        }
//        return true;
//    }

    private boolean checkGreenCollision() {
        for (int i = greenDots - 1; i > 0; i--) {
            if (greenSnake.get(0).x == greenSnake.get(i).x && greenSnake.get(0).y == greenSnake.get(i).y) {
                return false;
            }
        }
        if (greenSnake.get(0).x < 0 || greenSnake.get(0).x > WIDTH - DOT_SIZE || greenSnake.get(0).y < 0 || greenSnake.get(0).y > HEIGHT - DOT_SIZE) {
            return false;
        }
        if (greenSnake.get(0).x == apple.x && greenSnake.get(0).y == apple.y) {
            greenDots++;
            greenSnake.add(new Point(greenSnake.get(greenDots - 2)));
            apple.setLocation(rand(RAND_POS) * DOT_SIZE, rand(RAND_POS) * DOT_SIZE);
        }
        return true;
    }

    private boolean checkBlueCollision() {
        for (int i = blueDots - 1; i > 0; i--) {
            if (blueSnake.get(0).x == blueSnake.get(i).x && blueSnake.get(0).y == blueSnake.get(i).y) {
                return false;
            }
        }
        if (blueSnake.get(0).x < 0 || blueSnake.get(0).x > WIDTH - DOT_SIZE || blueSnake.get(0).y < 0 || blueSnake.get(0).y > HEIGHT - DOT_SIZE) {
            return false;
        }
        if (blueSnake.get(0).x == apple.x && blueSnake.get(0).y == apple.y) {
            blueDots++;
            blueSnake.add(new Point(blueSnake.get(blueDots - 2)));
            apple.setLocation(rand(RAND_POS) * DOT_SIZE, rand(RAND_POS) * DOT_SIZE);
        }
        return true;
    }


    private boolean checkCollisionAll() {
        greenIsLive = checkGreenCollision();
        blueIsLive = checkBlueCollision();
        for (int i = 0; i < greenDots; i++) {
            if (greenSnake.get(i).x == blueSnake.get(0).x && greenSnake.get(i).y == blueSnake.get(0).y) {
                return false;
            }
        }

        for (int i = 0; i < blueDots; i++) {
            if (greenSnake.get(0).x == blueSnake.get(i).x && greenSnake.get(0).y == blueSnake.get(i).y) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void update(double dt) {
        if (choose == 1) {
            greenMove();
            greenIsLive = checkGreenCollision();
//            blueMove();
//            blueIsLive = checkBlueCollision();
        } else if (choose == 2) {
            greenMove();
            blueMove();
            allIsLive = checkCollisionAll();
        }
    }

    private void paintSnake(String head, String body, List<Point> snake) {
        int dots = 0;
        if (snake == greenSnake) dots = greenDots;
        else dots = blueDots;

        for (int i = 0; i < dots; i++) {
            if (i == 0) {
                drawImage(loadImage(head), snake.get(i).x, snake.get(i).y);
            } else {
                drawImage(loadImage(body), snake.get(i).x, snake.get(i).y);
            }
        }
    }

    private void paintApple() {
        drawImage(loadImage("src/main/resources/apple.png"), apple.x, apple.y);
    }

    private void paintGrid(){
        changeColor(Color.GRAY);
        for (int i = 10; i < 500; i+=10) {
            drawLine(0, i, 500, i);
            drawLine(i, 0, i, 500);
        }
    }

    private void paintGameOver() {
        changeColor(white);
        drawBoldText(WIDTH / 2 - 100, HEIGHT / 2, "Game Over", 30);
        if (choose == 1) drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 + 30, "Your score: " + (greenDots-3), 25);
        else if (choose == 2){
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 + 30, "Green score: " + (greenDots-3), 25);
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 + 60, "Blue score: " + (blueDots-3), 25);
        }
        drawBoldText(WIDTH / 2 - 140, HEIGHT / 2 + 180, "Press r to start the game", 20);
        drawBoldText(WIDTH / 2 - 140, HEIGHT / 2 + 210, "Press q to end the game", 20);
    }

    @Override
    public void paintComponent() {
        if (choose == 0) {
            changeBackgroundColor(white) ;
            clearBackground (500 , 500);
            drawBoldText(WIDTH / 2 - 160, HEIGHT / 2 - 110, "Enter the number to select", 25);
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 - 80, "1.Single player", 25);
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 - 50, "2.Double player", 25);
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 - 20, "3.Help", 25);
            drawBoldText(WIDTH / 2 - 100, HEIGHT / 2 + 10, "4.Exit", 25);
        }
        else if (choose == 1) {
            changeBackgroundColor(black) ;
            clearBackground (500 , 500) ;
            if (greenIsLive) {
                paintGrid();
                paintSnake("src/main/resources/head.png", "src/main/resources/dot.png", greenSnake);
                paintApple();
            } else {
                paintGameOver();
            }
        } else if (choose == 2) {
            changeBackgroundColor(black) ;
            clearBackground (500 , 500) ;
            if (allIsLive&&(blueIsLive||greenIsLive)) {
                paintGrid();
                if (blueIsLive) paintSnake("src/main/resources/head.png", "src/main/resources/dot2.png", blueSnake);
                if (greenIsLive) paintSnake("src/main/resources/head.png", "src/main/resources/dot.png", greenSnake);
                paintApple();
            } else {
                paintGameOver();
            }
        } else if (choose == 3) {
            changeBackgroundColor(white) ;
            clearBackground (500 , 500);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 - 110, "You can choose between single and", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 - 80, "two-player games by the numbers.", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 - 50, "Single-player games are controlled by up, ", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 - 20, "down, left and right buttons", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 + 10, "Two-player game, green by up and down ", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 + 40, "left and right button control,", 25);
            drawBoldText(WIDTH / 2 - 240, HEIGHT / 2 + 70, "Blue is controlled by the AWSD key.", 25);
            drawBoldText(WIDTH / 2 - 120, HEIGHT / 2 + 200, "Press q to quit help", 20);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && greenDirection != KeyEvent.VK_RIGHT) {
            greenDirection = key;
        } else if (key == KeyEvent.VK_RIGHT && greenDirection != KeyEvent.VK_LEFT) {
            greenDirection = key;
        } else if (key == KeyEvent.VK_UP && greenDirection != KeyEvent.VK_DOWN) {
            greenDirection = key;
        } else if (key == KeyEvent.VK_DOWN && greenDirection != KeyEvent.VK_UP) {
            greenDirection = key;
        } else if (key == KeyEvent.VK_A && blueDirection != KeyEvent.VK_D) {
            blueDirection = key;
        } else if (key == KeyEvent.VK_D && blueDirection != KeyEvent.VK_A) {
            blueDirection = key;
        } else if (key == KeyEvent.VK_W && blueDirection != KeyEvent.VK_S) {
            blueDirection = key;
        } else if (key == KeyEvent.VK_S && blueDirection != KeyEvent.VK_W) {
            blueDirection = key;
        } else if (key == KeyEvent.VK_1 && choose!=2 && choose!=3) {
            choose = 1;
        } else if (key == KeyEvent.VK_2 && choose!=1 && choose!=3) {
            choose = 2;
        } else if (key == KeyEvent.VK_3 && choose!=1 && choose!=2) {
            choose = 3;
        }else if (key == KeyEvent.VK_4 && choose!=1 && choose!=2 && choose!=3) {
            System.exit(0);
        }

        if ((choose==2&&(!allIsLive||(!greenIsLive&&!blueIsLive)))||(choose==1&&!greenIsLive)||choose==3) {
            if (key == KeyEvent.VK_R) {
                init();
            } else if (key == KeyEvent.VK_Q) {
                choose = 0;
                init();
            }
        }
    }


    public static void main(String[] args) {
        createGame(new SnakeGameV1(), 15);
    }
}
