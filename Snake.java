import javax.swing.*;
import java.awt.*;

public final class Snake
{
    public static void main(String[] args)
    {
        final var frame = new JFrame("Snake");
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(new Panel(new Game(frame.getWidth(), frame.getHeight())));
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
            game.render(g);
        }
    }

    public record Game(int width, int height)
    {
        public void render(Graphics g)
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
        }
    }
}
