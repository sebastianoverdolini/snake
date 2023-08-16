import java.util.Optional;
import java.util.stream.Collectors;

public final class Test
{
    public final String description;
    private final Runnable test;

    public Test(String description, Runnable test)
    {
        this.description = description.lines().collect(Collectors.joining(" "));
        this.test = test;
    }

    public void run() throws AssertionError
    {
        try
        {
            test.run();
        }
        catch (AssertionError error)
        {
            System.out.println("FAILED: " + description);
            Optional.ofNullable(error.getMessage()).ifPresent(System.out::println);
            throw error;
        }
    }
}
