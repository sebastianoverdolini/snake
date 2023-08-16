import java.util.List;
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

    public Test(String description, List<Runnable> tests)
    {
        this(description, () -> tests.forEach(Runnable::run));
    }

    public void run()
    {
        test.run();
    }

    public static <T> void assertEquals(T actual, T expected)
    {
        assert actual.equals(expected) : actual;
    }
}
