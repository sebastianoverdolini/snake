import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Optional;

public final class SnakeGame
{
    public static void main(String[] args)
    {
        final var frameSize = 500;
        final var frame = new JFrame("Snake");
        var snake = Snake.alive(0, 0, Direction.EAST);
        var game = new Game(20, snake);
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.addKeyListener(snake);
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
            snake.update(gridSize);
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

    static final class Snake implements KeyListener
    {
        public int xHead;
        public int yHead;
        public Direction direction;
        private boolean isAlive;

        Snake(int xHead, int yHead, Direction direction, boolean isAlive)
        {
            this.xHead = xHead;
            this.yHead = yHead;
            this.direction = direction;
            this.isAlive = isAlive;
        }

        public static Snake alive(int xHead, int yHead, Direction direction)
        {
            return new Snake(xHead, yHead, direction, true);
        }

        public void update(int gridSize)
        {
            if (isAlive)
            {
                switch (direction)
                {
                    case NORTH ->
                    {
                        if (yHead == 0) die();
                        else goNorth(); }
                    case SOUTH ->
                    {
                        if (yHead == gridSize - 1) die();
                        else goSouth();
                    }
                    case WEST ->
                    {
                        if (xHead == 0) die();
                        else goWest();
                    }
                    case EAST ->
                    {
                        if (xHead == gridSize - 1) die();
                        else goEast();
                    }
                }
            }
        }

        private void goNorth() { yHead--; }

        private void goSouth() { yHead++; }

        private void goWest() { xHead--; }

        private void goEast() { xHead++; }

        private void die()
        {
            isAlive = false;
        }

        public boolean isAlive()
        {
            return isAlive;
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

        @Override
        public void keyPressed(KeyEvent e)
        {
            if (Direction.keyEvent(e).isPresent() &&
                    Direction.keyEvent(e).get().isNotOpposite(direction))
                direction = Direction.keyEvent(e).get();
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
            return switch (e.getKeyCode())
                    {
                        case KeyEvent.VK_UP -> Optional.of(Direction.NORTH);
                        case KeyEvent.VK_DOWN -> Optional.of(Direction.SOUTH);
                        case KeyEvent.VK_LEFT -> Optional.of(Direction.WEST);
                        case KeyEvent.VK_RIGHT -> Optional.of(Direction.EAST);
                        default -> Optional.empty();
                    };
        }

        private boolean isNotOpposite(Direction direction)
        {
            return valueOf(name()) != direction.opposite();
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
