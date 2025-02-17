package exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author wanghanyu
 * @create 2018-03-09 10:17
 */
public class ExceptionProblem {
    public static void main(String[] args) {

    }

    public void checkedException() throws IOException {
        throw new IOException();
        // throw new Exception();
    }

    public void noCheckedException() {
        throw new NullPointerException();
        // throw new RuntimeException();
    }
}
