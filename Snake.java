import javax.swing.*;
import java.awt.*;

public final class Snake
{
    public static void main(String[] args)
    {
        final var frameSize = 500;
        final var frame = new JFrame("Snake");
        var game = new Game(10, 0, 0, Direction.EAST);
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                game.render(g, frameSize);
            }
        });
        frame.setVisible(true);
        new Timer(1000, e -> {
            game.update();
            frame.repaint();
        }).start();
    }

    public static final class Game
    {
        private final int gridSize;
        public int snakeHeadX;
        public int snakeHeadY;
        public Direction snakeDirection;

        public Game(
                int gridSize,
                int snakeHeadX,
                int snakeHeadY,
                Direction snakeDirection)
        {
            this.gridSize = gridSize;
            this.snakeHeadX = snakeHeadX;
            this.snakeHeadY = snakeHeadY;
            this.snakeDirection = snakeDirection;
        }

        public void update()
        {
            switch (snakeDirection)
            {
                case NORTH -> snakeHeadY--;
                case SOUTH -> snakeHeadY++;
                case WEST -> snakeHeadX--;
                case EAST -> snakeHeadX++;
            }
        }

        public void render(Graphics g, int frameSize)
        {
            renderBackground(g, frameSize);
            renderSnake(g, frameSize);
        }

        private static void renderBackground(Graphics g, int frameSize)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, frameSize, frameSize);
        }

        private void renderSnake(Graphics g, int frameSize)
        {
            g.setColor(Color.WHITE);
            g.fillRect(
                    snakeHeadX * cellSize(frameSize),
                    snakeHeadY * cellSize(frameSize),
                    cellSize(frameSize),
                    cellSize(frameSize));
        }

        private int cellSize(int frameSize)
        {
            return frameSize / gridSize;
        }
    }

    enum Direction
    {
        NORTH,
        SOUTH,
        WEST,
        EAST,
    }
}
