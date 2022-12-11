import javax.swing.*;
import java.awt.*;

public final class SnakeGame
{
    public static void main(String[] args)
    {
        final var frameSize = 500;
        final var frame = new JFrame("Snake");
        var game = new Game(10, new Snake(0, 0, Direction.EAST));
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
        private final Snake snake;

        public Game(
                int gridSize,
                Snake snake)
        {
            this.gridSize = gridSize;
            this.snake = snake;
        }

        public void update()
        {
            snake.update();
        }

        public void render(Graphics g, int frameSize)
        {
            renderBackground(g, frameSize);
            snake.render(g, frameSize / gridSize);
        }

        private static void renderBackground(Graphics g, int frameSize)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, frameSize, frameSize);
        }
    }

    static final class Snake
    {
        public int xHead;
        public int yHead;
        public Direction direction;

        public Snake(int xHead, int yHead, Direction direction)
        {
            this.xHead = xHead;
            this.yHead = yHead;
            this.direction = direction;
        }

        public void update()
        {
            switch (direction)
            {
                case NORTH -> yHead--;
                case SOUTH -> yHead++;
                case WEST -> xHead--;
                case EAST -> xHead++;
            }
        }

        private void render(Graphics g, int cellSize)
        {
            g.setColor(Color.WHITE);
            g.fillRect(
                    xHead * cellSize,
                    yHead * cellSize,
                    cellSize,
                    cellSize);
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
