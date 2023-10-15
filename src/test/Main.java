import java.util.stream.Stream;

public class Main
{
    public static void main(String[] args)
    {
        var success = true;
        for (var test : Stream.concat(Tests.tests.stream(), DirectionTest.tests.stream()).toList())
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
