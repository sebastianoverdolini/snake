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
                        10, SnakeGame.Snake.alive(0, 0, null));
                var g = new FakeGraphics();
                game.render(g, 500);
                assert g.logs.subList(0, 2).equals(List.of(
                        "setColor " + Color.BLACK,
                        String.format("fillRect %d %d %d %d", 0, 0, 500, 500)));
            }),
            new Test("A game paints the snake when rendered", () ->
            {
                var snake = SnakeGame.Snake.alive(1, 2, null);
                var game = new SnakeGame.Game(20, snake);
                var g = new FakeGraphics();
                game.render(g, 100);
                assert g.logs.subList(2, 4).equals(List.of(
                        "setColor " + Color.WHITE,
                        String.format(
                                "fillRect %d %d %d %d",
                                snake.xHead * (100 / 20),
                                snake.yHead * (100 / 20),
                                100 / 20,
                                100 / 20)));
            }),
            new Test("The snake moves when game updated", () ->
            {
                record TestCase(
                        SnakeGame.Snake snake,
                        SnakeGame.Snake expectedSnakeAfterUpdate) {}
                for (var testCase : List.of(
                        new TestCase(
                                SnakeGame.Snake.alive(1, 1, SnakeGame.Direction.NORTH),
                                SnakeGame.Snake.alive(1, 0, SnakeGame.Direction.NORTH)),
                        new TestCase(
                                SnakeGame.Snake.alive(1, 1, SnakeGame.Direction.SOUTH),
                                SnakeGame.Snake.alive(1, 2, SnakeGame.Direction.SOUTH)),
                        new TestCase(
                                SnakeGame.Snake.alive(1, 1, SnakeGame.Direction.WEST),
                                SnakeGame.Snake.alive(0, 1, SnakeGame.Direction.WEST)),
                        new TestCase(
                                SnakeGame.Snake.alive(1, 1, SnakeGame.Direction.EAST),
                                SnakeGame.Snake.alive(2, 1, SnakeGame.Direction.EAST))))
                {
                    new SnakeGame.Game(3, testCase.snake()).update();
                    assert testCase.snake().xHead == testCase.expectedSnakeAfterUpdate().xHead;
                    assert testCase.snake().yHead == testCase.expectedSnakeAfterUpdate().yHead;
                    assert testCase.snake().direction == testCase.expectedSnakeAfterUpdate().direction;
                }
            }),
            new Test("The snake keeps its direction when a player presses an unknown button", () ->
            {
                var snake = SnakeGame.Snake.alive(0, 0, SnakeGame.Direction.EAST);
                snake.keyPressed(new KeyEvent(
                        new Component() {}, 0, 0, 0, KeyEvent.VK_B, '\0'));
                assert snake.direction == SnakeGame.Direction.EAST;
            }),
            new Test("The snake keeps its direction when a player tries to reverse it", () ->
            {
                record Case(
                        SnakeGame.Direction initialSnakeDirection,
                        KeyEvent playerPressedKey)
                { }
                for (var testCase : List.of(
                        new Case(SnakeGame.Direction.EAST, pressLeftArrowKey()),
                        new Case(SnakeGame.Direction.WEST, pressRightArrowKey()),
                        new Case(SnakeGame.Direction.NORTH, pressDownArrowKey()),
                        new Case(SnakeGame.Direction.SOUTH, pressUpArrowKey())))
                {
                    var snake = SnakeGame.Snake.alive(
                            0, 0, testCase.initialSnakeDirection());
                    snake.keyPressed(testCase.playerPressedKey());
                    assert snake.direction == testCase.initialSnakeDirection();
                }
            }),
            new Test("The snake dies when it hits a wall", () ->
            {
                for (var direction : SnakeGame.Direction.values())
                {
                    var snake = SnakeGame.Snake.alive(0, 0, direction);
                    var game = new SnakeGame.Game(1, snake);
                    game.update();
                    assert !snake.isAlive();
                }
            }),
            new Test("A dead snake doesn't move", () ->
            {
                var snake = new SnakeGame.Snake(
                        1, 1, SnakeGame.Direction.EAST, false);
                var game = new SnakeGame.Game(3, snake);
                game.update();
                assert snake.xHead == 1;
                assert snake.yHead == 1;
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

    public static final class FakeGraphics extends Graphics
    {
        public final List<String> logs = new LinkedList<>();

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
            throw new UnsupportedOperationException();
        }

        @Override
        public void setColor(Color c)
        {
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
            catch (AssertionError e)
            {
                success = false;
                System.out.println("FAILED: " + test.description);
            }
        }
        if (!success) System.exit(1);
    }

    public record Test(
            String description,
            Runnable runnable) implements Runnable
    {
        @Override
        public void run()
        {
            runnable.run();
        }
    }
}
