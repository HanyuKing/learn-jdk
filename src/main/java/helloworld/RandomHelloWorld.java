package helloworld;

import java.util.Random;

/**
 * @author Hanyu King
 * @since 2018-03-26 12:15
 */
public class RandomHelloWorld {
    public static void main(String[] args) {
        for(int i = 0; i < 10; i++) {
            Random random = new Random(-314159265);
            System.out.println(random.nextInt(27));
        }
        //System.out.println(randomString(-229985452)+" "+randomString(-147909649));
    }

    public static String randomString(int i)
    {
        Random ran = new Random(i);
        StringBuilder sb = new StringBuilder();
        for (int n = 0; ; n++) {
            int k = ran.nextInt(27);
            if (k == 0)
                break;

            sb.append((char)('`' + k));
        }

        return sb.toString();
    }
}
