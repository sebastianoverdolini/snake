import javax.swing.*;
import java.awt.*;

public final class Snake
{
    public static void main(String[] args)
    {
        final var frame = new JFrame("Snake");
        final var frameSize = 500;
        frame.setSize(frameSize, frameSize);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new Panel(new Game(10, 0, 0)));
        frame.setVisible(true);
    }

    public static final class Panel extends JPanel
    {
        private final Game game;


        public Panel(Game game)
        {
            this.game = game;
        }

        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            game.render(g, getWidth());
        }
    }

    public record Game(int gridSize, int snakeHeadX, int snakeHeadY)
    {
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
}
