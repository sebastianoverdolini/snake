public enum Direction
{
    NORTH,
    SOUTH,
    WEST,
    EAST;

    public Direction opposite()
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
