public class Assertions
{
    private Assertions() {}

    public static <T> void assertEquals(T actual, T expected)
    {
        assert actual.equals(expected) : actual;
    }
}
