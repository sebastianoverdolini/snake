import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("A new game has the snake placed in the center and directed towards EAST", () ->
            {
                var game = new Game(6, 2);
                assert game.snake.location().equals(List.of(
                        new Location(0, 0),
                        new Location(-1, 0),
                        new Location(-2, 0),
                        new Location(-3, 0),
                        new Location(-4, 0)));
                assert game.snake.currentDirection == Snake.Direction.EAST;
            }),
            new Test("The game screen's background is a dark and light green grid", () ->
            {
                var game = new Game(4, 2);
                var g = new FakeGraphics();
                game.render(g);
                Test.assertEquals(g.logs.subList(0, 8), List.of(
                        "setColor " + Game.darkGrassColor,
                        String.format("fillRect %d %d %d %d", 0, 0, 2, 2),
                        "setColor " + Game.lightGrassColor,
                        String.format("fillRect %d %d %d %d", 2, 0, 2, 2),
                        "setColor " + Game.darkGrassColor,
                        String.format("fillRect %d %d %d %d", 0, 2, 2, 2),
                        "setColor " + Game.lightGrassColor,
                        String.format("fillRect %d %d %d %d", 2, 2, 2, 2)));
            }),
            new Test("The alive snake is rendered as a purple rectangle", () ->
            {
                var snake = Snake.alive(
                        List.of(
                                new Location(1, -1),
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(-1, 0),
                                new Location(-1, 1)),
                        10,
                        null);
                var g = new FakeGraphics();
                snake.render(100, g);
                assert g.logs.equals(List.of(
                        "setColor " + new Color(84, 66, 142),
                        "fillRect 46 46 10 10",
                        "fillRect 46 45 10 10",
                        "fillRect 45 45 10 10",
                        "fillRect 44 45 10 10",
                        "fillRect 44 44 10 10"));
            }),
            new Test("The dead snake is rendered as a gray rectangle", () ->
            {
                var snake = new Snake(List.of(new Location(0, 0)), 10, null, false);
                var g = new FakeGraphics();
                snake.render(100, g);
                assert g.logs.get(0).equals("setColor " + Color.GRAY);
            }),
            new Test("The alive snake keeps crawling towards its current direction if it doesn't want to turn", List.of(
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, -1),
                                        new Location(0, -2)),
                                2,
                                Snake.Direction.NORTH);
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, 1),
                                new Location(0, 0),
                                new Location(0, -1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, 2),
                                new Location(0, 1),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, 1),
                                        new Location(0, 2)),
                                2,
                                Snake.Direction.SOUTH);
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -1),
                                new Location(0, 0),
                                new Location(0, 1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -2),
                                new Location(0, -1),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(1, 0),
                                        new Location(2, 0)),
                                2,
                                Snake.Direction.WEST);
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-1, 0),
                                new Location(0, 0),
                                new Location(1, 0)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-2, 0),
                                new Location(-1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(-1, 0),
                                        new Location(-2, 0)),
                                2,
                                Snake.Direction.EAST);
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(-1, 0)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(2, 0),
                                new Location(1, 0),
                                new Location(0, 0)));
                    })),
            new Test("The alive snake doesn't turn when it is in the middle of a tile", List.of(
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(1, 0)), 2, Snake.Direction.EAST);
                        snake.nextDirection = Snake.Direction.NORTH;
                        snake.update(6);
                        assert !snake.location().equals(List.of(
                                new Location(1, 1)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(0, 1)), 2, Snake.Direction.NORTH);
                        snake.nextDirection = Snake.Direction.EAST;
                        snake.update(6);
                        assert !snake.location().equals(List.of(
                                new Location(1, 1)));
                    })),
            new Test("The alive snake turns", List.of(
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, -1),
                                        new Location(0, -2)),
                                2,
                                Snake.Direction.NORTH);
                        snake.keyPressed(pressRightArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(0, -1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(2, 0),
                                new Location(1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, -1),
                                        new Location(0, -2)),
                                2,
                                Snake.Direction.NORTH);
                        snake.keyPressed(pressLeftArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-1, 0),
                                new Location(0, 0),
                                new Location(0, -1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-2, 0),
                                new Location(-1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, -1),
                                        new Location(0, -2)),
                                2,
                                Snake.Direction.NORTH);
                        snake.keyPressed(pressRightArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(0, -1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(2, 0),
                                new Location(1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, 1),
                                        new Location(0, 2)),
                                2,
                                Snake.Direction.SOUTH);
                        snake.keyPressed(pressRightArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(1, 0),
                                new Location(0, 0),
                                new Location(0, 1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(2, 0),
                                new Location(1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(0, 1),
                                        new Location(0, 2)),
                                2,
                                Snake.Direction.SOUTH);
                        snake.keyPressed(pressLeftArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-1, 0),
                                new Location(0, 0),
                                new Location(0, 1)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(-2, 0),
                                new Location(-1, 0),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(-1, 0),
                                        new Location(-2, 0)),
                                2,
                                Snake.Direction.EAST);
                        snake.keyPressed(pressDownArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -1),
                                new Location(0, 0),
                                new Location(-1, 0)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -2),
                                new Location(0, -1),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(1, 0),
                                        new Location(2, 0)),
                                2,
                                Snake.Direction.WEST);
                        snake.keyPressed(pressUpArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, 1),
                                new Location(0, 0),
                                new Location(1, 0)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, 2),
                                new Location(0, 1),
                                new Location(0, 0)));
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(
                                        new Location(0, 0),
                                        new Location(1, 0),
                                        new Location(2, 0)),
                                2,
                                Snake.Direction.WEST);
                        snake.keyPressed(pressDownArrowKey());
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -1),
                                new Location(0, 0),
                                new Location(1, 0)));
                        snake.update(6);
                        assert snake.location().equals(List.of(
                                new Location(0, -2),
                                new Location(0, -1),
                                new Location(0, 0)));
                    }
            )),
            new Test("The alive snake doesn't change direction when the player presses an unknown button", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 0)), 10, Snake.Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component()
                        {
                        }, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.currentDirection == Snake.Direction.EAST;
            }),
            new Test("The alive snake can't reverse its direction", List.of(
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(0, 0)), 10, Snake.Direction.NORTH);
                        snake.keyPressed(pressDownArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == Snake.Direction.NORTH;
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(0, 0)), 10, Snake.Direction.SOUTH);
                        snake.keyPressed(pressUpArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == Snake.Direction.SOUTH;
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(0, 0)), 10, Snake.Direction.WEST);
                        snake.keyPressed(pressRightArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == Snake.Direction.WEST;
                    },
                    () ->
                    {
                        var snake = Snake.alive(
                                List.of(new Location(0, 0)), 10, Snake.Direction.EAST);
                        snake.keyPressed(pressLeftArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == Snake.Direction.EAST;
                    }
            )),
            new Test("The alive snake dies when it hits the north wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, 2)), 2, Snake.Direction.NORTH);
                snake.update(6);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, 2)));
            }),
            new Test("The alive snake dies when it hits the south wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(0, -2)), 2, Snake.Direction.SOUTH);
                snake.update(6);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(0, -2)));
            }),
            new Test("The alive snake dies when it hits the west wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(-2, 0)), 2, Snake.Direction.WEST);
                snake.update(6);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(-2, 0)));
            }),
            new Test("The alive snake dies when it hits the east wall", () ->
            {
                var snake = Snake.alive(
                        List.of(new Location(2, 0)), 2, Snake.Direction.EAST);
                snake.update(6);
                assert !snake.isAlive();
                assert snake.location().equals(List.of(
                        new Location(2, 0)));
            }),
            new Test("A dead snake can't move", () ->
            {
                var snake = deadSnake(1, 1);
                snake.update(3);
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

    public static Snake deadSnake(int xHead, int yHead)
    {
        return new Snake(
                List.of(new Location(xHead, yHead)), 10, Snake.Direction.EAST, false);
    }

    public static Snake deadSnake(Snake.Direction direction)
    {
        return new Snake(
                List.of(new Location(0, 0)), 10, direction, false);
    }
}
