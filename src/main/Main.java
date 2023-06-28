import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;

public final class Main
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
}
