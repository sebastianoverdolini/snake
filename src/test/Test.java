import java.util.List;

public final class Test
{
    public final String description;
    private final Runnable test;

    public Test(String description, Runnable test)
    {
        this.description = description;
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
