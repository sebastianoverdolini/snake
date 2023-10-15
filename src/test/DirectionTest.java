import java.util.List;
import java.util.stream.Stream;

public class DirectionTest
{
    record Example(
            Direction direction,
            Direction oppositeDirection) {}

    public static final List<Test> tests = Stream.of(
                    new Example(Direction.NORTH, Direction.SOUTH),
                    new Example(Direction.SOUTH, Direction.NORTH),
                    new Example(Direction.EAST, Direction.WEST),
                    new Example(Direction.WEST, Direction.EAST))
            .map(example ->
                    new Test(
                            "The opposite of " + example.direction + " is " + example.oppositeDirection,
                            () -> Assertions.assertEquals(
                                    example.direction.opposite(),
                                    example.oppositeDirection)))
            .toList();
}
