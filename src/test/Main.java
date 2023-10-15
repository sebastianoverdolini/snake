import java.util.Collection;
import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args)
    {
        var success = true;
        var tests = Stream.of(Tests.tests, Tests.snakeTests, DirectionTest.tests)
                .flatMap(Collection::stream)
                .toList();
        for (var test : tests)
        {
            try
            {
                test.run();
            }
            catch (AssertionError error)
            {
                success = false;
            }
        }
        System.exit(success ? 0 : 1);
    }
}
