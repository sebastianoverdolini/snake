import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("A new game has the snake placed in the center and directed towards EAST", () ->
            {
                var game = new SnakeGame.Game(6, 2);
                assert game.snake.xHead == 2;
                assert game.snake.yHead == 2;
                assert game.snake.currentDirection == SnakeGame.Direction.EAST;
            }),
            new Test("The game screen's background is a dark and light green grid", () ->
            {
                var game = new SnakeGame.Game(6, 2);
                var g = new FakeGraphics();
                game.render(g);
                assert g.logs.subList(0, 12).equals(List.of(
                        "setColor " + SnakeGame.Game.darkGrassColor,
                        String.format("fillRect %d %d %d %d", 0, 0, 2, 2),
                        "setColor " + SnakeGame.Game.lightGrassColor,
                        String.format("fillRect %d %d %d %d", 2, 0, 2, 2),
                        "setColor " + SnakeGame.Game.darkGrassColor,
                        String.format("fillRect %d %d %d %d", 4, 0, 2, 2),
                        "setColor " + SnakeGame.Game.lightGrassColor,
                        String.format("fillRect %d %d %d %d", 0, 2, 2, 2),
                        "setColor " + SnakeGame.Game.darkGrassColor,
                        String.format("fillRect %d %d %d %d", 2, 2, 2, 2),
                        "setColor " + SnakeGame.Game.lightGrassColor,
                        String.format("fillRect %d %d %d %d", 4, 2, 2, 2)));
            }),
            new Test("The alive snake is rendered as a purple rectangle", () ->
            {
                var snake = SnakeGame.Snake.alive(1, 2, 10, null);
                var g = new FakeGraphics();
                snake.render(g);
                assert g.logs.equals(List.of(
                        "setColor " + new Color(84, 66, 142),
                        "fillRect 1 2 10 10"));
            }),
            new Test("The dead snake is rendered as a gray rectangle", () ->
            {
                var snake = new SnakeGame.Snake(0, 0, 10, null, false);
                var g = new FakeGraphics();
                snake.render(g);
                assert g.logs.get(0).equals("setColor " + Color.GRAY);
            }),
            new Test("The alive snake keeps crawling towards its current direction if it doesn't want to turn", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.NORTH);
                        snake.update(3);
                        assert snake.xHead == 1;
                        assert snake.yHead == 0;
                        assert snake.currentDirection == SnakeGame.Direction.NORTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.SOUTH);
                        snake.update(3);
                        assert snake.xHead == 1;
                        assert snake.yHead == 2;
                        assert snake.currentDirection == SnakeGame.Direction.SOUTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.WEST);
                        snake.update(3);
                        assert snake.xHead == 0;
                        assert snake.yHead == 1;
                        assert snake.currentDirection == SnakeGame.Direction.WEST;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.EAST);
                        snake.update(3);
                        assert snake.xHead == 2;
                        assert snake.yHead == 1;
                        assert snake.currentDirection == SnakeGame.Direction.EAST;
                    })),
            new Test("The alive snake turns only when its head has completely filled the tile", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 0, 2, SnakeGame.Direction.EAST);
                        snake.nextDirection = SnakeGame.Direction.SOUTH;
                        snake.update(6);
                        assert snake.xHead == 2;
                        assert snake.yHead == 0;
                        snake.update(6);
                        assert snake.xHead == 2;
                        assert snake.yHead == 1;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                0, 1, 2, SnakeGame.Direction.SOUTH);
                        snake.nextDirection = SnakeGame.Direction.EAST;
                        snake.update(6);
                        assert snake.xHead == 0;
                        assert snake.yHead == 2;
                        snake.update(6);
                        assert snake.xHead == 1;
                        assert snake.yHead == 2;
                    })),
            new Test("The alive snake doesn't change direction when the player presses an unknown button", () ->
            {
                var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component()
                        {
                        }, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.currentDirection == SnakeGame.Direction.EAST;
            }),
            new Test("The alive snake can't reverse its direction", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.NORTH);
                        snake.keyPressed(pressDownArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == SnakeGame.Direction.NORTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.SOUTH);
                        snake.keyPressed(pressUpArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == SnakeGame.Direction.SOUTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.WEST);
                        snake.keyPressed(pressRightArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == SnakeGame.Direction.WEST;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.EAST);
                        snake.keyPressed(pressLeftArrowKey());
                        snake.update(100);
                        assert snake.currentDirection == SnakeGame.Direction.EAST;
                    }
            )),
            new Test("The alive snake dies when it hits a wall", () ->
            {
                for (var direction : SnakeGame.Direction.values())
                {
                    var snake = SnakeGame.Snake.alive(0, 0, 10, direction);
                    snake.update(10);
                    assert !snake.isAlive();
                    assert snake.xHead == 0;
                    assert snake.yHead == 0;
                }
            }),
            new Test("A dead snake can't move", () ->
            {
                var snake = deadSnake(1, 1);
                snake.update(3);
                assert snake.xHead == 1;
                assert snake.yHead == 1;
            }),
            new Test("A dead snake can't change its current direction", () ->
            {
                var snake = deadSnake(SnakeGame.Direction.NORTH);
                snake.keyPressed(pressDownArrowKey());
                snake.update(100);
                assert snake.currentDirection == SnakeGame.Direction.NORTH;
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

    public static SnakeGame.Snake deadSnake(int xHead, int yHead)
    {
        return new SnakeGame.Snake(
                xHead, yHead, 10, SnakeGame.Direction.EAST, false);
    }

    public static SnakeGame.Snake deadSnake(SnakeGame.Direction direction)
    {
        return new SnakeGame.Snake(0, 0, 10, direction, false);
    }

    public static final class FakeGraphics extends Graphics
    {
        public final List<String> logs = new LinkedList<>();
        private Color color;

        @Override
        public Graphics create()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void translate(int x, int y)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Color getColor()
        {
            return color;
        }

        @Override
        public void setColor(Color c)
        {
            color = c;
            logs.add("setColor " + c);
        }

        @Override
        public void setPaintMode()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setXORMode(Color c1)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Font getFont()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setFont(Font font)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public FontMetrics getFontMetrics(Font f)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Rectangle getClipBounds()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clipRect(int x, int y, int width, int height)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClip(int x, int y, int width, int height)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public Shape getClip()
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setClip(Shape clip)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void copyArea(int x, int y, int width, int height, int dx, int dy)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawLine(int x1, int y1, int x2, int y2)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fillRect(int x, int y, int width, int height)
        {
            logs.add(String.format(
                    "fillRect %d %d %d %d", x, y, width, height));
        }

        @Override
        public void clearRect(int x, int y, int width, int height)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawOval(int x, int y, int width, int height)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fillOval(int x, int y, int width, int height)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawString(String str, int x, int y)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void drawString(AttributedCharacterIterator iterator, int x, int y)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int x, int y, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer)
        {
            throw new UnsupportedOperationException();
        }

        @Override
        public void dispose()
        {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args)
    {
        var success = true;
        for (var test : tests)
        {
            try
            {
                test.run();
            }
            catch (AssertionError error)
            {
                success = false;
                System.out.println("FAILED: " + test.description);
                Optional.ofNullable(error.getMessage()).ifPresent(System.out::println);
            }
        }
        System.exit(success ? 0 : 1);
    }

    public static final class Test
    {
        public final String description;
        private final Runnable test;

        public Test(String description, Runnable test)
        {
            this.description = description;
            this.test = test;
        }

        public Test(String description, List<Runnable> tests)
        {
            this(description, () -> tests.forEach(Runnable::run));
        }

        public void run()
        {
            test.run();
        }
    }
}
