import java.awt.*;
import java.util.List;

public final class Game
{
    private final Field field;
    public final Snake snake;

    public Game(int size)
    {
        this.field = new Field(size);
        this.snake = Snake.alive(
                List.of(
                        new Location(0, 0),
                        new Location(-1, 0),
                        new Location(-2, 0)),
                Direction.EAST);
    }

    public void update()
    {
        snake.update(field.size);
    }

    public void render(Graphics g, int frameSize)
    {
        field.render(g, frameSize);
        snake.render(frameSize, frameSize / field.size, g);
    }
}
