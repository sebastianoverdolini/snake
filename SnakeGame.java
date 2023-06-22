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
                    new Location(0, 0),
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
            snake.render(frameSize, g);
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

    record Location(int x, int y) {}

    static final class Snake implements KeyListener
    {
        private Location headLocation;
        public int size;
        public Direction currentDirection;
        public Direction nextDirection;
        private boolean isAlive;

        Snake(
                Location headLocation,
                int size,
                Direction currentDirection,
                boolean isAlive)
        {
            this.headLocation = headLocation;
            this.size = size;
            this.currentDirection = currentDirection;
            this.isAlive = isAlive;
        }

        public static Snake alive(
                Location headLocation,
                int size,
                Direction direction)
        {
            return new Snake(headLocation, size, direction, true);
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
                        if (isNearNorthWall(frameSize)) die();
                        else this.headLocation = new Location(
                                headLocation.x(), headLocation().y() + 1);
                    }
                    case SOUTH ->
                    {
                        if (isNearSouthWall(frameSize)) die();
                        else this.headLocation = new Location(
                                headLocation.x(), headLocation().y() - 1);
                    }
                    case WEST ->
                    {
                        if (isNearWestWall(frameSize)) die();
                        else this.headLocation = new Location(
                                headLocation.x() - 1, headLocation().y());
                    }
                    case EAST ->
                    {
                        if (isNearEastWall(frameSize)) die();
                        else this.headLocation = new Location(
                                headLocation.x() + 1, headLocation().y());
                    }
                }
            }
        }

        public Location headLocation()
        {
            return headLocation;
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
            return headLocation.x() % size == 0 && headLocation.y() % size == 0;
        }

        private boolean isNearNorthWall(int frameSize)
        {
            return headLocation.y() + (size / 2) == (frameSize / 2);
        }

        private boolean isNearSouthWall(int frameSize)
        {
            return headLocation.y() - (size / 2) == - (frameSize / 2);
        }

        private boolean isNearWestWall(int frameSize)
        {
            return headLocation.x() - (size / 2) == - (frameSize / 2);
        }

        private boolean isNearEastWall(int frameSize)
        {
            return headLocation.x() + (size / 2) == (frameSize / 2);
        }

        private void die()
        {
            isAlive = false;
        }

        public boolean isAlive()
        {
            return isAlive;
        }

        public void render(int frameSize, Graphics g)
        {
            g.setColor(isAlive() ? new Color(84, 66, 142) : Color.GRAY);
            g.fillRect(
                    (frameSize / 2) + headLocation.x() - (size / 2),
                    (frameSize / 2) - headLocation().y() - (size / 2),
                    size,
                    size);
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
