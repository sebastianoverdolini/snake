import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Stream;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("The field is rendered as a dark and light green grid", () ->
            {
                var field = new Field(2);
                var g = new FakeGraphics();
                field.render(g, 20);
                Assertions.assertEquals(g.logs, List.of(
                        "setColor " + Field.darkGrassColor,
                        "fillRect 0 0 10 10",
                        "setColor " + Field.lightGrassColor,
                        "fillRect 10 0 10 10",
                        "setColor " + Field.darkGrassColor,
                        "fillRect 0 10 10 10",
                        "setColor " + Field.lightGrassColor,
                        "fillRect 10 10 10 10"));
            }),
            new Test("The alive snake is rendered as a purple rectangle", () ->
            {
                var snake = Snake.alive(
                        List.of(
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(0, -1)),
                        null);
                var g = new FakeGraphics();
                snake.render(6, 2, g);
                Assertions.assertEquals(g.logs, List.of(
                        "setColor " + new Color(84, 66, 142),
                        "fillRect 4 2 2 2",
                        "fillRect 2 2 2 2",
                        "fillRect 2 4 2 2"));
            }),
            new Test("The dead snake is rendered as a gray rectangle", () ->
            {
                var snake = new Snake(List.of(new Location(0, 0)), null, false);
                var g = new FakeGraphics();
                snake.render(100, 1, g);
                assert g.logs.get(0).equals("setColor " + Color.GRAY);
            }),
            new Test("""
                    The snake doesn't change direction when
                    the player presses an unknown button
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0)), Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component()
                        {
                        }, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.currentDirection == Direction.EAST;
            }),
            new Test("""
                    The snake doesn't turn when the player tries to
                    reverse its direction
                    """, () ->
            {
                for (var direction : Direction.values())
                {
                    var snake = Snake.alive(
                            List.of(new Location(0, 0)), direction);
                    snake.keyPressed(key(direction.opposite()));
                    assert snake.currentDirection == direction;
                }
            }),
            new Test("The snake dies hitting the north wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 1)), Direction.NORTH);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, 1)));
            }),
            new Test("The snake dies hitting the south wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, -1)), Direction.SOUTH);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, -1)));
            }),
            new Test("The snake dies hitting the west wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(-1, 0)), Direction.WEST);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(-1, 0)));
            }),
            new Test("The snake dies hitting the east wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(1, 0)), Direction.EAST);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(1, 0)));
            }),
            new Test("A dead snake doesn't move", () ->
            {
                var snake = deadSnake(1, 1);
                snake.update(100);
                assert snake.location().equals(List.of(
                        new Location(1, 1)));
            }),
            new Test("A dead snake doesn't change its current direction", () ->
            {
                var snake = deadSnake(Direction.NORTH);
                snake.keyPressed(pressDownArrowKey());
                snake.update(100);
                assert snake.currentDirection == Direction.NORTH;
            }));

    public static List<Test> snakeTests = Stream.of(
            new Example(
                    List.of(new Location(0, 0), new Location(0, -1)),
                    Direction.NORTH,
                    pressUpArrowKey(),
                    List.of(new Location(0, 1), new Location(0, 0))),
            new Example(
                    List.of(new Location(0, 0), new Location(0, 1)),
                    Direction.SOUTH,
                    pressDownArrowKey(),
                    List.of(new Location(0, -1), new Location(0, 0))),
            new Example(
                    List.of(new Location(0, 0), new Location(1, 0)),
                    Direction.WEST,
                    pressLeftArrowKey(),
                    List.of(new Location(-1, 0), new Location(0, 0))),
            new Example(
                    List.of(new Location(0, 0), new Location(-1, 0)),
                    Direction.EAST,
                    pressRightArrowKey(),
                    List.of(new Location(1, 0), new Location(0, 0))),
            new Example(
                    List.of(new Location(0, 0), new Location(0, -1)),
                    Direction.NORTH,
                    pressRightArrowKey(),
                    List.of(new Location(1, 0), new Location(0, 0))),
            new Example(
                    List.of(new Location(1, 0), new Location(0, 0)),
                    Direction.EAST,
                    pressDownArrowKey(),
                    List.of(new Location(1, -1), new Location(1, 0))),
            new Example(
                    List.of(new Location(1, -1), new Location(1, 0)),
                    Direction.SOUTH,
                    pressLeftArrowKey(),
                    List.of(new Location(0, -1), new Location(1, -1))),
            new Example(
                    List.of(new Location(0, -1), new Location(1, -1)),
                    Direction.WEST,
                    pressUpArrowKey(),
                    List.of(new Location(0, 0), new Location(0, -1))),
            new Example(
                    List.of(new Location(0, 0), new Location(0, -1)),
                    Direction.NORTH,
                    pressLeftArrowKey(),
                    List.of(new Location(-1, 0), new Location(0, 0))),
            new Example(
                    List.of(new Location(-1, 0), new Location(0, 0)),
                    Direction.WEST,
                    pressDownArrowKey(),
                    List.of(new Location(-1, -1), new Location(-1, 0))),
            new Example(
                    List.of(new Location(-1, -1), new Location(-1, 0)),
                    Direction.SOUTH,
                    pressRightArrowKey(),
                    List.of(new Location(0, -1), new Location(-1, -1))),
            new Example(
                    List.of(new Location(0, -1), new Location(-1, -1)),
                    Direction.EAST,
                    pressUpArrowKey(),
                    List.of(new Location(0, 0), new Location(0, -1))))
            .map(example -> new Test(
                    String.format("""
                                    The snake at %s and directed towards %s reaches %s when
                                    the player presses %s
                                    """,
                            example.snakeLocation,
                            example.snakeDirection,
                            example.expectedSnakeLocation,
                            KeyEvent.getKeyText(example.keyEvent.getKeyCode())),
                    () ->
                    {
                        var snake = Snake.alive(
                                example.snakeLocation,
                                example.snakeDirection);
                        snake.keyPressed(example.keyEvent);
                        snake.update(6);
                        assert snake.location().equals(
                                example.expectedSnakeLocation);
                    }))
            .toList();

    public static KeyEvent pressLeftArrowKey()
    {
        return new KeyEvent(
                new Component() {}, 0, 0, 0, KeyEvent.VK_LEFT, '\0');
    }

    public static KeyEvent pressRightArrowKey()
    {
        return new KeyEvent(
                new Component() {}, 0, 0, 0, KeyEvent.VK_RIGHT, '\0');
    }

    public static KeyEvent pressDownArrowKey()
    {
        return new KeyEvent(
                new Component() {}, 0, 0, 0, KeyEvent.VK_DOWN, '\0');
    }

    public static KeyEvent pressUpArrowKey()
    {
        return new KeyEvent(
                new Component() {}, 0, 0, 0, KeyEvent.VK_UP, '\0');
    }

    public static KeyEvent key(Direction direction)
    {
        return new KeyEvent(
                new Component() {}, 0, 0, 0,
                switch (direction) {
                    case NORTH -> KeyEvent.VK_UP;
                    case SOUTH -> KeyEvent.VK_DOWN;
                    case WEST -> KeyEvent.VK_LEFT;
                    case EAST -> KeyEvent.VK_RIGHT;
                },
                '\0');
    }

    public static Snake deadSnake(int xHead, int yHead)
    {
        return new Snake(
                List.of(new Location(xHead, yHead)), Direction.EAST, false);
    }

    public static Snake deadSnake(Direction direction)
    {
        return new Snake(
                List.of(new Location(0, 0)), direction, false);
    }

    record Example(
            List<Location> snakeLocation,
            Direction snakeDirection,
            KeyEvent keyEvent,
            List<Location> expectedSnakeLocation) {}
}
