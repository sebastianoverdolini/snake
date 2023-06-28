import java.awt.*;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.LinkedList;
import java.util.List;

public final class FakeGraphics extends Graphics
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
