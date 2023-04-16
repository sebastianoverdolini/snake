import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.List;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("A game paints the background when rendered", () ->
            {
                var game = new SnakeGame.Game(
                        4, SnakeGame.Snake.alive(0, 0, 2, null));
                var g = new FakeGraphics();
                game.render(g);
                assert g.logs.subList(0, 8).equals(List.of(
                        "setColor " + new Color(128, 185, 24),
                        String.format("fillRect %d %d %d %d", 0, 0, 2, 2),
                        "setColor " + new Color(170, 204, 0),
                        String.format("fillRect %d %d %d %d", 2, 0, 2, 2),
                        "setColor " + new Color(128, 185, 24),
                        String.format("fillRect %d %d %d %d", 0, 2, 2, 2),
                        "setColor " + new Color(170, 204, 0),
                        String.format("fillRect %d %d %d %d", 2, 2, 2, 2)));
            }),
            new Test("A game paints the snake when rendered", () ->
            {
                var snake = SnakeGame.Snake.alive(1, 2, 10, null);
                var game = new SnakeGame.Game(100, snake);
                var g = new FakeGraphics();
                game.render(g);
                assert g.hasPaintedTheSnake(snake);
            }),
            new Test("A dead snake is gray", () ->
            {
                var snake = new SnakeGame.Snake(0, 0, 10, null, false);
                var game = new SnakeGame.Game(100, snake);
                var g = new FakeGraphics();
                game.render(g);
                assert g.hasPaintedTheDeadSnake(snake);
            }),
            new Test("The snake continuously crawls towards its direction ", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.NORTH);
                        new SnakeGame.Game(3, snake).update();
                        assert snake.xHead == 1;
                        assert snake.yHead == 0;
                        assert snake.currentDirection == SnakeGame.Direction.NORTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.SOUTH);
                        new SnakeGame.Game(3, snake).update();
                        assert snake.xHead == 1;
                        assert snake.yHead == 2;
                        assert snake.currentDirection == SnakeGame.Direction.SOUTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.WEST);
                        new SnakeGame.Game(3, snake).update();
                        assert snake.xHead == 0;
                        assert snake.yHead == 1;
                        assert snake.currentDirection == SnakeGame.Direction.WEST;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 1, 10, SnakeGame.Direction.EAST);
                        new SnakeGame.Game(3, snake).update();
                        assert snake.xHead == 2;
                        assert snake.yHead == 1;
                        assert snake.currentDirection == SnakeGame.Direction.EAST;
                    })),
            new Test("The snake turns only when it completely fills a cell ", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                1, 0, 2, SnakeGame.Direction.EAST);
                        var game = new SnakeGame.Game(6, snake);
                        snake.nextDirection = SnakeGame.Direction.SOUTH;
                        game.update();
                        assert snake.xHead == 2;
                        assert snake.yHead == 0;
                        game.update();
                        assert snake.xHead == 2;
                        assert snake.yHead == 1;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(
                                0, 1, 2, SnakeGame.Direction.SOUTH);
                        var game = new SnakeGame.Game(6, snake);
                        snake.nextDirection = SnakeGame.Direction.EAST;
                        game.update();
                        assert snake.xHead == 0;
                        assert snake.yHead == 2;
                        game.update();
                        assert snake.xHead == 1;
                        assert snake.yHead == 2;
                    })),
            new Test("The snake keeps its direction when a player presses an unknown button", () ->
            {
                var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component()
                        {
                        }, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.currentDirection == SnakeGame.Direction.EAST;
            }),
            new Test("The snake keeps its direction when a player tries to reverse it", List.of(
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.NORTH);
                        var game = new SnakeGame.Game(100, snake);
                        snake.keyPressed(pressDownArrowKey());
                        game.update();
                        assert snake.currentDirection == SnakeGame.Direction.NORTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.SOUTH);
                        var game = new SnakeGame.Game(100, snake);
                        snake.keyPressed(pressUpArrowKey());
                        game.update();
                        assert snake.currentDirection == SnakeGame.Direction.SOUTH;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.WEST);
                        var game = new SnakeGame.Game(100, snake);
                        snake.keyPressed(pressRightArrowKey());
                        game.update();
                        assert snake.currentDirection == SnakeGame.Direction.WEST;
                    },
                    () ->
                    {
                        var snake = SnakeGame.Snake.alive(0, 0, 10, SnakeGame.Direction.EAST);
                        var game = new SnakeGame.Game(100, snake);
                        snake.keyPressed(pressLeftArrowKey());
                        game.update();
                        assert snake.currentDirection == SnakeGame.Direction.EAST;
                    }
            )),
            new Test("The snake dies when it hits a wall", () ->
            {
                for (var direction : SnakeGame.Direction.values())
                {
                    var snake = SnakeGame.Snake.alive(0, 0, 10, direction);
                    var game = new SnakeGame.Game(10, snake);
                    game.update();
                    assert !snake.isAlive();
                }
            }),
            new Test("A dead snake doesn't move", () ->
            {
                var snake = deadSnake(1, 1);
                var game = new SnakeGame.Game(3, snake);
                game.update();
                assert snake.xHead == 1;
                assert snake.yHead == 1;
            }),
            new Test("A player can't change the direction of a dead snake", () ->
            {
                var snake = deadSnake(SnakeGame.Direction.NORTH);
                snake.keyPressed(pressDownArrowKey());
                new SnakeGame.Game(100, snake).update();
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

        public boolean hasPaintedTheSnake(SnakeGame.Snake snake)
        {
            return logs.subList(logs.size() - 2, logs.size()).equals(List.of(
                    "setColor " + new Color(84, 66, 142),
                    String.format(
                            "fillRect %d %d %d %d",
                            snake.xHead, snake.yHead, 10, 10)));
        }

        public boolean hasPaintedTheDeadSnake(SnakeGame.Snake snake)
        {
            return logs.subList(logs.size() - 2, logs.size()).equals(List.of(
                    "setColor " + Color.GRAY,
                    String.format(
                            "fillRect %d %d %d %d",
                            snake.xHead, snake.yHead, 10, 10)));
        }

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
        if (tests.stream().allMatch(Test::succeeds))
            System.out.println("SUCCESS");
        else
        {
            tests.stream()
                    .filter(Test::fails)
                    .map(test -> "FAILED: " + test.description)
                    .forEach(System.err::println);
            System.exit(1);
        }
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

        public boolean succeeds()
        {
            try
            {
                test.run();
                return true;
            }
            catch (AssertionError e)
            {
                return false;
            }
        }

        public boolean fails()
        {
            return !succeeds();
        }
    }
}
