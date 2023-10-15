import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Stream;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("""
                    A new game has a three units long snake placed at the center
                    of the field, directed towards EAST
                    """, () ->
            {
                var game = new Game(3);
                assert game.snake.location().equals(List.of(
                        new Location(0, 0),
                        new Location(-1, 0),
                        new Location(-2, 0)));
                assert game.snake.currentDirection == Snake.Direction.EAST;
            }),
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
                    An alive snake, lined and directed to north, increases
                    its y coordinates by one at each update
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0), new Location(0, -1)),
                        Snake.Direction.NORTH);
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(0, 1), new Location(0, 0)));
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(0, 2), new Location(0, 1)));
            }),
            new Test("""
                    An alive snake, lined and directed to south, decreases
                    its y coordinates by one at each update
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0), new Location(0, 1)),
                        Snake.Direction.SOUTH);
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(0, -1), new Location(0, 0)));
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(0, -2), new Location(0, -1)));
            }),
            new Test("""
                    An alive snake, lined and directed to west, decreases
                    its x coordinates by one at each update
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0), new Location(1, 0)),
                        Snake.Direction.WEST);
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(-1, 0), new Location(0, 0)));
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(-2, 0), new Location(-1, 0)));
            }),
            new Test("""
                    An alive snake, lined and directed to east, increases
                    its x coordinates by one at each update
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0), new Location(-1, 0)),
                        Snake.Direction.EAST);
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(1, 0), new Location(0, 0)));
                snake.update(100);
                Assertions.assertEquals(
                        snake.location(),
                        List.of(new Location(2, 0), new Location(1, 0)));
            }),
            new Test("The snake's body follows its head when it turns", () ->
            {
                record Example(
                        List<Location> snakeLocation,
                        Snake.Direction snakeDirection,
                        KeyEvent keyEvent,
                        List<Location> expectedSnakeLocation) {}
                Stream.of(
                        new Example(
                                List.of(new Location(0, 0), new Location(0, -1)),
                                Snake.Direction.NORTH,
                                pressRightArrowKey(),
                                List.of(new Location(1, 0), new Location(0, 0))),
                        new Example(
                                List.of(new Location(1, 0), new Location(0, 0)),
                                Snake.Direction.EAST,
                                pressDownArrowKey(),
                                List.of(new Location(1, -1), new Location(1, 0))),
                        new Example(
                                List.of(new Location(1, -1), new Location(1, 0)),
                                Snake.Direction.SOUTH,
                                pressLeftArrowKey(),
                                List.of(new Location(0, -1), new Location(1, -1))),
                        new Example(
                                List.of(new Location(0, -1), new Location(1, -1)),
                                Snake.Direction.WEST,
                                pressUpArrowKey(),
                                List.of(new Location(0, 0), new Location(0, -1))),
                        new Example(
                                List.of(new Location(0, 0), new Location(0, -1)),
                                Snake.Direction.NORTH,
                                pressLeftArrowKey(),
                                List.of(new Location(-1, 0), new Location(0, 0))),
                        new Example(
                                List.of(new Location(-1, 0), new Location(0, 0)),
                                Snake.Direction.WEST,
                                pressDownArrowKey(),
                                List.of(new Location(-1, -1), new Location(-1, 0))),
                        new Example(
                                List.of(new Location(-1, -1), new Location(-1, 0)),
                                Snake.Direction.SOUTH,
                                pressRightArrowKey(),
                                List.of(new Location(0, -1), new Location(-1, -1))),
                        new Example(
                                List.of(new Location(0, -1), new Location(-1, -1)),
                                Snake.Direction.EAST,
                                pressUpArrowKey(),
                                List.of(new Location(0, 0), new Location(0, -1)))
                ).map(example -> new Test(example.toString(), () ->
                    {
                        var snake = Snake.alive(
                                example.snakeLocation,
                                example.snakeDirection);
                        snake.keyPressed(example.keyEvent);
                        snake.update(6);
                        assert snake.location().equals(
                                example.expectedSnakeLocation);
                    })
                ).forEach(Test::run);
            }),
            new Test("""
                    The snake doesn't change direction when
                    the player presses an unknown button
                    """, () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0)), Snake.Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component()
                        {
                        }, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.currentDirection == Snake.Direction.EAST;
            }),
            new Test("""
                    The snake doesn't turn when the player tries to
                    reverse its direction
                    """, () ->
            {
                for (var direction : Snake.Direction.values())
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
                        List.of(new Location(0, 1)), Snake.Direction.NORTH);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, 1)));
            }),
            new Test("The snake dies hitting the south wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, -1)), Snake.Direction.SOUTH);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, -1)));
            }),
            new Test("The snake dies hitting the west wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(-1, 0)), Snake.Direction.WEST);
                snake.update(3);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(-1, 0)));
            }),
            new Test("The snake dies hitting the east wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(1, 0)), Snake.Direction.EAST);
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
            new Test("A dead snake can't change its current direction", () ->
            {
                var snake = deadSnake(Snake.Direction.NORTH);
                snake.keyPressed(pressDownArrowKey());
                snake.update(100);
                assert snake.currentDirection == Snake.Direction.NORTH;
            }));

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

    public static KeyEvent key(Snake.Direction direction)
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
                List.of(new Location(xHead, yHead)), Snake.Direction.EAST, false);
    }

    public static Snake deadSnake(Snake.Direction direction)
    {
        return new Snake(
                List.of(new Location(0, 0)), direction, false);
    }
}
