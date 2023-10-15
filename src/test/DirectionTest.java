import java.util.List;
import java.util.stream.Stream;

public class DirectionTest
{
    public static final List<Test> tests = List.of(
            new Test("Direction's opposite", () ->
            {
                record Example(
                        Direction direction,
                        Direction oppositeDirection) {}
                Stream.of(
                        new Example(Direction.NORTH, Direction.SOUTH),
                        new Example(Direction.SOUTH, Direction.NORTH),
                        new Example(Direction.EAST, Direction.WEST),
                        new Example(Direction.WEST, Direction.EAST))
                        .map(example ->
                                new Test(String.format("""
                                        The opposite of %s is %s
                                        """, example.direction, example.oppositeDirection), () ->
                                {
                                    assert example.direction.opposite() == example.oppositeDirection;
                                })).forEach(Test::run);
            })
    );
}
