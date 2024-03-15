package jol.Header_Array_Length;

public class SimpleInliningTest
{
    public SimpleInliningTest()
    {
        int sum = 0;

        // 1_000_000 is F4240 in hex
        for (int i = 0 ; i < 1_000_000; i++)
        {
            sum = this.add(sum, 99); // 63 hex
        }

        System.out.println("Sum:" + sum);
    }

    public int add(int a, int b)
    {
        return a + b;
    }

    public static void main(String[] args)
    {
        new SimpleInliningTest();
    }
}