import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("A game paints the background when rendered", () ->
            {
                final var game = new Snake.Game(10, 0, 0);
                final var g = new FakeGraphics();
                game.render(g, 500);
                assert g.logs.get(0).equals(
                        "setColor " + Color.BLACK);
                assert g.logs.get(1).equals(String.format(
                        "fillRect %d %d %d %d", 0, 0, 500, 500));
            }),
            new Test("A game paints the snake when rendered", () ->
            {
                final var snakes = List.of(
                        Map.of("x", 0, "y", 0),
                        Map.of("x", 1, "y", 2));
                for (final var snake : snakes)
                {
                    final var game = new Snake.Game(
                            20, snake.get("x"), snake.get("y"));
                    final var g = new FakeGraphics();
                    game.render(g, 100);
                    assert g.logs.get(2).equals(
                            "setColor " + Color.WHITE);
                    assert g.logs.get(3).equals(String.format(
                            "fillRect %d %d %d %d",
                            snake.get("x") * (100 / 20),
                            snake.get("y") * (100 / 20),
                            100 / 20,
                            100 / 20));
                }
            }));

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
        tests.forEach(Test::run);
    }

    public record Test(
            String description,
            Runnable runnable) implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                runnable.run();
            }
            catch (AssertionError e)
            {
                System.out.println("FAILED: " + description);
            }
        }
    }
}
