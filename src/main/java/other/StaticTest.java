package other;

public class StaticTest
{
   public static void main(String[] args)
   {
       staticFunction();
   }

    // TODO: 2018/6/29 注释看看
   static StaticTest st = new StaticTest();

   static
   {
       System.out.println("1");
   }

   {
       System.out.println("2");
   }

   StaticTest()
   {
       System.out.println("3");
       System.out.println("a="+a+",b="+b);
   }

   public static void staticFunction(){
       System.out.println("4");
   }

   int a=110;
   static int b =112;
}