import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.awt.event.KeyEvent.*;

final class Snake implements KeyListener
{
    private List<Location> location;
    public int size;
    public Direction currentDirection;
    public Direction nextDirection;
    private boolean isAlive;

    public Snake(
            List<Location> location,
            int size,
            Direction currentDirection,
            boolean isAlive)
    {
        this.location = new ArrayList<>(location);
        this.size = size;
        this.currentDirection = currentDirection;
        this.isAlive = isAlive;
    }

    public static Snake alive(
            List<Location> location,
            int size,
            Direction direction)
    {
        return new Snake(location, size, direction, true);
    }

    public void update(int frameSize)
    {
        if (isAlive())
        {
            if (wantsTurn() && hasCompletedTheCurrentCrawl())
            {
                currentDirection = nextDirection;
                nextDirection = null;
            }
            if (isHittingWall(frameSize))
                die();
            else location = Stream.concat(Stream.of(
                    switch (currentDirection)
                    {
                        case NORTH -> new Location(headLocation().x(), headLocation().y() + 1);
                        case SOUTH -> new Location(headLocation().x(), headLocation().y() - 1);
                        case WEST -> new Location(headLocation().x() - 1, headLocation().y());
                        case EAST -> new Location(headLocation().x() + 1, headLocation().y());
                    }),
                    location.subList(0, location.size() - 1).stream())
                    .toList();
        }
    }

    private Location headLocation()
    {
        return location.get(0);
    }

    public List<Location> location()
    {
        return location;
    }

    private void setNextDirection(Direction direction)
    {
        if (isAlive && direction != currentDirection.opposite())
        {
            nextDirection = direction;
        }
    }

    private boolean wantsTurn()
    {
        return nextDirection != null;
    }

    private boolean hasCompletedTheCurrentCrawl()
    {
        return headLocation().x() % size == 0 && headLocation().y() % size == 0;
    }

    private boolean isGoingToNorth()
    {
        return currentDirection == Direction.NORTH;
    }

    private boolean isGoingToSouth()
    {
        return currentDirection == Direction.SOUTH;
    }

    private boolean isGoingToWest()
    {
        return currentDirection == Direction.WEST;
    }

    private boolean isGoingToEast()
    {
        return currentDirection == Direction.EAST;
    }

    private boolean isHittingWall(int frameSize)
    {
        return isHittingNorthWall(frameSize) ||
                isHittingSouthWall(frameSize) ||
                isHittingWestWall(frameSize) ||
                isHittingEastWall(frameSize);
    }

    private boolean isHittingNorthWall(int frameSize)
    {
        return isGoingToNorth() && isHeadTouchingNorthWall(frameSize);
    }

    private boolean isHeadTouchingNorthWall(int frameSize)
    {
        return headLocation().y() + (size / 2) == (frameSize / 2);
    }

    private boolean isHittingSouthWall(int frameSize)
    {
        return isGoingToSouth() && isHeadTouchingSouthWall(frameSize);
    }

    private boolean isHeadTouchingSouthWall(int frameSize)
    {
        return headLocation().y() - (size / 2) == -(frameSize / 2);
    }

    private boolean isHittingWestWall(int frameSize)
    {
        return isGoingToWest() && isHeadTouchingWestWall(frameSize);
    }

    private boolean isHeadTouchingWestWall(int frameSize)
    {
        return headLocation().x() - (size / 2) == -(frameSize / 2);
    }

    private boolean isHittingEastWall(int frameSize)
    {
        return isGoingToEast() && isHeadTouchingEastWall(frameSize);
    }

    private boolean isHeadTouchingEastWall(int frameSize)
    {
        return headLocation().x() + (size / 2) == (frameSize / 2);
    }

    private void die()
    {
        isAlive = false;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public void render(int frameSize, Graphics g)
    {
        g.setColor(isAlive() ? new Color(84, 66, 142) : Color.GRAY);
        location.forEach(l -> g.fillRect(
                (frameSize / 2) + l.x() - (size / 2),
                (frameSize / 2) - l.y() - (size / 2),
                size,
                size));
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case VK_UP -> setNextDirection(Direction.NORTH);
            case VK_DOWN -> setNextDirection(Direction.SOUTH);
            case VK_LEFT -> setNextDirection(Direction.WEST);
            case VK_RIGHT -> setNextDirection(Direction.EAST);
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
    }

    enum Direction
    {
        NORTH,
        SOUTH,
        WEST,
        EAST;

        private Direction opposite()
        {
            return switch (valueOf(name()))
            {
                case NORTH -> SOUTH;
                case SOUTH -> NORTH;
                case WEST -> EAST;
                case EAST -> WEST;
            };
        }
    }
}
