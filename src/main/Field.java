import java.awt.*;

public class Field
{
    public static Color darkGrassColor = new Color(128, 185, 24);
    public static Color lightGrassColor = new Color(170, 204, 0);
    public final int size;

    public Field(int size)
    {
        this.size = size;
    }

    public void render(Graphics g, int frameSize)
    {
        String previousColor = null;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                if (previousColor == null)
                {
                    g.setColor(darkGrassColor);
                    previousColor = "dark";
                }
                else if (previousColor.equals("dark"))
                {
                    g.setColor(lightGrassColor);
                    previousColor = "light";
                }
                else
                {
                    g.setColor(darkGrassColor);
                    previousColor = "dark";
                }
                g.fillRect(
                        j * (frameSize / size),
                        i * (frameSize / size),
                        frameSize / size,
                        frameSize / size);
            }
        }
    }
}
