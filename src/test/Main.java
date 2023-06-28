import java.util.Optional;

public class Main
{
    public static void main(String[] args)
    {
        var success = true;
        for (var test : Tests.tests)
        {
            try
            {
                test.run();
            }
            catch (AssertionError error)
            {
                success = false;
                System.out.println("FAILED: " + test.description);
                Optional.ofNullable(error.getMessage()).ifPresent(System.out::println);
            }
        }
        System.exit(success ? 0 : 1);
    }
}
