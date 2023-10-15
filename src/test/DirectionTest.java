import java.util.List;
import java.util.stream.Stream;

public class DirectionTest
{
    public static final List<Test> tests = List.of(
            new Test("The opposite of north is south", () ->
            {
                record Example(
                        Snake.Direction direction,
                        Snake.Direction oppositeDirection) {}
                Stream.of(
                        new Example(Snake.Direction.NORTH, Snake.Direction.SOUTH),
                        new Example(Snake.Direction.SOUTH, Snake.Direction.NORTH),
                        new Example(Snake.Direction.EAST, Snake.Direction.WEST),
                        new Example(Snake.Direction.WEST, Snake.Direction.EAST))
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
