import java.awt.*;
import java.util.stream.Stream;

public final class Game
{
    private final int frameSize;
    private final Field field;
    public final Snake snake;

    public Game(int frameSize, int tileSize)
    {
        this.frameSize = frameSize;
        this.field = new Field(frameSize, tileSize);
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
        field.render(g);
        snake.render(frameSize, g);
    }
}