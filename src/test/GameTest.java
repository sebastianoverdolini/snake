import java.util.List;

public class GameTest
{
    public static List<Test> tests = List.of(
            new Test("""
                    A new game has a three units long snake placed at the center
                    of the field, directed towards EAST
                    """, () ->
            {
                var game = new Game(3);
                assert game.snake.location().equals(List.of(
                        new Location(0, 0),
                        new Location(-1, 0),
                        new Location(-2, 0)));
                assert game.snake.currentDirection == Direction.EAST;
            }));
}
