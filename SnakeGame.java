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
        var snake = Snake.alive(0, 0, 20, Direction.EAST);
        var game = new Game(500, snake);
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.addKeyListener(snake);
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
        new Timer(10, e -> {
            game.update();
            frame.repaint();
        }).start();
    }

    public static final class Game
    {
        private final int frameSize;
        private final Snake snake;

        public Game(
                int frameSize,
                Snake snake)
        {
            this.frameSize = frameSize;
            this.snake = snake;
        }

        public void update()
        {
            snake.update(frameSize);
        }

        public void render(Graphics g)
        {
            renderBackground(g, frameSize);
            snake.render(g);
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
        public int size;
        public Direction currentDirection;
        public Direction nextDirection;
        private boolean isAlive;

        Snake(int xHead, int yHead, int size, Direction currentDirection, boolean isAlive)
        {
            this.xHead = xHead;
            this.yHead = yHead;
            this.size = size;
            this.currentDirection = currentDirection;
            this.isAlive = isAlive;
        }

        public static Snake alive(int xHead, int yHead, int size, Direction direction)
        {
            return new Snake(xHead, yHead, size, direction, true);
        }

        public void update(int frameSize)
        {
            if (isAlive)
            {
                if (wantsTurn() && canTurn())
                    turn();
                switch (currentDirection)
                {
                    case NORTH ->
                    {
                        if (yHead == 0) die();
                        else goNorth();
                    }
                    case SOUTH ->
                    {
                        if (yHead + size == frameSize) die();
                        else goSouth();
                    }
                    case WEST ->
                    {
                        if (xHead == 0) die();
                        else goWest();
                    }
                    case EAST ->
                    {
                        if (xHead + size == frameSize) die();
                        else goEast();
                    }
                }
            }
        }

        private boolean wantsTurn()
        {
            return nextDirection != null;
        }

        private boolean canTurn()
        {
            return xHead % size == 0 && yHead % size == 0;
        }

        private void turn()
        {
            currentDirection = nextDirection;
            nextDirection = null;
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

        public void render(Graphics g)
        {
            if (isAlive())
                g.setColor(Color.WHITE);
            else
                g.setColor(Color.GRAY);
            g.fillRect(xHead, yHead, size, size);
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            if (Direction.keyEvent(e).isPresent() &&
                    Direction.keyEvent(e).get().isNotOpposite(currentDirection))
                nextDirection = Direction.keyEvent(e).get();
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
