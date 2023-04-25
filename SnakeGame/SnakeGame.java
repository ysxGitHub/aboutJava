package com.sankegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnakeGame extends JPanel implements Runnable, KeyListener {

    private static final long serialVersionUID = 1L;

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int DOT_SIZE = 10;
    public static final int MAX_DOTS = WIDTH * HEIGHT / DOT_SIZE / DOT_SIZE;
    public static final int RAND_POS = WIDTH / DOT_SIZE;
    public static final int DELAY = 140;

    private List<Point> snake;
    private int dots;
    private Point apple;
    private boolean running;
    private Thread gameThread;
    private Random random;
    private int direction;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    private void initGame() {
        snake = new ArrayList<Point>();
        dots = 3;
        for (int i = 0; i < dots; i++) {
            snake.add(new Point(50 - i * DOT_SIZE, 50));
        }
        random = new Random();
        apple = new Point(random.nextInt(RAND_POS) * DOT_SIZE, random.nextInt(RAND_POS) * DOT_SIZE);
        running = true;
        direction = KeyEvent.VK_RIGHT;
        gameThread = new Thread(this);
        gameThread.start();
    }

    private void move() {
        for (int i = dots - 1; i > 0; i--) {
            snake.get(i).x = snake.get(i - 1).x;
            snake.get(i).y = snake.get(i - 1).y;
        }
        if (direction == KeyEvent.VK_LEFT) {
            snake.get(0).x -= DOT_SIZE;
        } else if (direction == KeyEvent.VK_RIGHT) {
            snake.get(0).x += DOT_SIZE;
        } else if (direction == KeyEvent.VK_UP) {
            snake.get(0).y -= DOT_SIZE;
        } else if (direction == KeyEvent.VK_DOWN) {
            snake.get(0).y += DOT_SIZE;
        }
    }

    private void checkCollision() {
        for (int i = dots - 1; i > 0; i--) {
            if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
                running = false;
                break;
            }
        }
        if (snake.get(0).x < 0 || snake.get(0).x > WIDTH - DOT_SIZE || snake.get(0).y < 0 || snake.get(0).y > HEIGHT - DOT_SIZE) {
            running = false;
        }
        if (snake.get(0).x == apple.x && snake.get(0).y == apple.y) {
            dots++;
            snake.add(new Point(snake.get(dots - 2)));
            apple.setLocation(random.nextInt(RAND_POS) * DOT_SIZE, random.nextInt(RAND_POS) * DOT_SIZE);
        }
    }

    private void paintSnake(Graphics g) {
        g.setColor(Color.WHITE);
        for (int i = 0; i < dots; i++) {
            g.fillRect(snake.get(i).x, snake.get(i).y, DOT_SIZE, DOT_SIZE);
        }
    }

    private void paintApple(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(apple.x, apple.y, DOT_SIZE, DOT_SIZE);
    }

    private void paintGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (running) {
            paintSnake(g);
            paintApple(g);
        } else {
            paintGameOver(g);
        }
    }

    @Override
    public void run() {
        while (running) {
            move();
            checkCollision();
            repaint();
            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) {
            direction = key;
        } else if (key == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) {
            direction = key;
        } else if (key == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) {
            direction = key;
        } else if (key == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP) {
            direction = key;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(new SnakeGame());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}