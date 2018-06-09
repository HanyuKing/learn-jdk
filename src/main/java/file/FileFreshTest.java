package file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @author Hanyu King
 * @since 2018-06-07 18:46
 */
public class FileFreshTest {
    public static void main(String[] args) throws Exception {
        File file = new File("src\\main\\java\\file\\test_fresh.txt");
        OutputStream os = new FileOutputStream(file);
        os.write("Hanyu King".getBytes());
        os.flush();
    }
}
