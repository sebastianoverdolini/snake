import javax.swing.*;

public final class Snake
{
    public static void main(String[] args)
    {
        final var frame = new JFrame("Snake");
        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
