import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

import static java.awt.event.KeyEvent.*;

public final class SnakeGame
{
    public static void main(String[] args)
    {
        final var frameSize = 600;
        final var frame = new JFrame("Snake");
        var game = new Game(frameSize, 40);
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.addKeyListener(game.snake);
        var panel = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                game.render(g);
            }
        };
        panel.setPreferredSize(new Dimension(frameSize, frameSize));
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        new Timer(5, e -> {
            game.update();
            frame.repaint();
        }).start();
    }

    public static final class Game
    {
        private final int frameSize;
        public final Snake snake;

        public Game(int frameSize, int tileSize)
        {
            this.frameSize = frameSize;
            this.snake = Snake.alive(
                    ((frameSize / tileSize) / 2) * tileSize,
                    ((frameSize / tileSize) / 2) * tileSize,
                    tileSize,
                    Direction.EAST);
        }

        public void update()
        {
            snake.update(frameSize);
        }

        public void render(Graphics g)
        {
            renderBackground(g, frameSize, snake.size);
            snake.render(g);
        }

        public static Color darkGrassColor = new Color(128, 185, 24);
        public static Color lightGrassColor = new Color(170, 204, 0);

        private static void renderBackground(Graphics g, int frameSize, int snakeSize)
        {
            g.setColor(darkGrassColor);
            for (int i = 0; i < frameSize; i = i + snakeSize)
            {
                for (int j = 0; j < frameSize; j = j + snakeSize)
                {
                    g.fillRect(j, i, snakeSize, snakeSize);
                    if (g.getColor().equals(darkGrassColor))
                    {
                        g.setColor(lightGrassColor);
                    }
                    else
                    {
                        g.setColor(darkGrassColor);
                    }
                }
            }
        }
    }

    static final class Snake implements KeyListener
    {
        public int xHead;
        public int yHead;
        public int size;
        public Direction currentDirection;
        public Direction nextDirection;
        private boolean isAlive;

        Snake(
                int xHead,
                int yHead,
                int size,
                Direction currentDirection,
                boolean isAlive)
        {
            this.xHead = xHead;
            this.yHead = yHead;
            this.size = size;
            this.currentDirection = currentDirection;
            this.isAlive = isAlive;
        }

        public static Snake alive(
                int xHead,
                int yHead,
                int size,
                Direction direction)
        {
            return new Snake(xHead, yHead, size, direction, true);
        }

        public void update(int frameSize)
        {
            if (isAlive())
            {
                if (wantsTurn() && hasCompletedTheCurrentCrawl())
                {
                    currentDirection = nextDirection;
                    nextDirection = null;
                }
                switch (currentDirection)
                {
                    case NORTH ->
                    {
                        if (isNearNorthWall()) die();
                        else yHead--;
                    }
                    case SOUTH ->
                    {
                        if (isNearSouthWall(frameSize)) die();
                        else yHead++;
                    }
                    case WEST ->
                    {
                        if (isNearWestWall()) die();
                        else xHead--;
                    }
                    case EAST ->
                    {
                        if (isNearEastWall(frameSize)) die();
                        else xHead++;
                    }
                }
            }
        }

        private void setNextDirection(Direction direction)
        {
            if (isAlive && direction != currentDirection.opposite())
            {
                nextDirection = direction;
            }
        }

        private boolean wantsTurn()
        {
            return nextDirection != null;
        }

        private boolean hasCompletedTheCurrentCrawl()
        {
            return xHead % size == 0 && yHead % size == 0;
        }

        private boolean isNearNorthWall()
        {
            return yHead == 0;
        }

        private boolean isNearSouthWall(int frameSize)
        {
            return yHead + size == frameSize;
        }

        private boolean isNearWestWall()
        {
            return xHead == 0;
        }

        private boolean isNearEastWall(int frameSize)
        {
            return xHead + size == frameSize;
        }

        private void die()
        {
            isAlive = false;
        }

        public boolean isAlive()
        {
            return isAlive;
        }

        public void render(Graphics g)
        {
            renderHead(g);
        }

        private void renderHead(Graphics g)
        {
            g.setColor(isAlive() ? new Color(84, 66, 142) : Color.GRAY);
            g.fillRect(xHead, yHead, size, size);
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            Direction.keyEvent(e).ifPresentOrElse(
                    this::setNextDirection,
                    () -> {});
        }

        @Override
        public void keyTyped(KeyEvent e)
        {}

        @Override
        public void keyReleased(KeyEvent e)
        {}
    }

    enum Direction
    {
        NORTH,
        SOUTH,
        WEST,
        EAST;

        private static Optional<Direction> keyEvent(KeyEvent e)
        {
            return Optional.ofNullable(switch (e.getKeyCode())
                    {
                        case VK_UP -> NORTH;
                        case VK_DOWN -> SOUTH;
                        case VK_LEFT -> WEST;
                        case VK_RIGHT -> EAST;
                        default -> null;
                    });
        }

        private Direction opposite()
        {
            return switch (valueOf(name()))
                    {
                        case NORTH -> SOUTH;
                        case SOUTH -> NORTH;
                        case WEST -> EAST;
                        case EAST -> WEST;
                    };
        }
    }
}
