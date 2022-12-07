import java.util.List;

public final class Tests
{
    public final static List<Test> tests = List.of(
            new Test("assert works", () ->
            {
                assert true;
            }));

    public static void main(String[] args)
    {
        tests.forEach(Test::run);
    }

    public record Test(
            String description,
            Runnable runnable) implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                runnable.run();
            }
            catch (AssertionError e)
            {
                System.out.println("FAILED: " + description);
            }
        }
    }
}
