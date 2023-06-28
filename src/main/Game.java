import java.awt.*;
import java.util.stream.Stream;

public final class Game
{
    private final int frameSize;
    public final Snake snake;

    public Game(int frameSize, int tileSize)
    {
        this.frameSize = frameSize;
        this.snake = Snake.alive(
                Stream.iterate(
                        new Location(0, 0),
                        location -> Math.abs(location.x() - tileSize) <= 3 * tileSize,
                        location -> new Location(
                                location.x() - 1,
                                location.y())
                ).toList(),
                tileSize,
                Snake.Direction.EAST);
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
                } else
                {
                    g.setColor(darkGrassColor);
                }
            }
        }
    }
}
