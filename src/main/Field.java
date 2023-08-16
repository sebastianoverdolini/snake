import java.awt.*;

public class Field
{
    public static Color darkGrassColor = new Color(128, 185, 24);
    public static Color lightGrassColor = new Color(170, 204, 0);
    private final int frameSize;
    private final int tileSize;

    public Field(int frameSize, int tileSize)
    {
        this.frameSize = frameSize;
        this.tileSize = tileSize;
    }

    public void render(Graphics g)
    {
        g.setColor(darkGrassColor);
        for (int i = 0; i < frameSize; i = i + tileSize)
        {
            for (int j = 0; j < frameSize; j = j + tileSize)
            {
                g.fillRect(j, i, tileSize, tileSize);
                if (g.getColor().equals(darkGrassColor))
                {
                    g.setColor(lightGrassColor);
                } else
                {
                    g.setColor(darkGrassColor);
                }
            }
        }
    }
}
