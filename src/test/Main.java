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
            }
        }
        System.exit(success ? 0 : 1);
    }
}
