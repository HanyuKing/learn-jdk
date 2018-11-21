package jvm;

import com.alibaba.fastjson.JSONObject;

import java.io.*;

/**
 * @author Hanyu King
 * @since 2018-09-17 17:44
 */
public class ReadGCLog {
    public static void main(String[] args) throws Exception {
        File file = new File("D:\\work\\projects\\learn-jdk\\src\\main\\java\\jvm\\catalina.out.2018-09-18-16_14_58.log");
        File writeFile = new File("D:\\work\\projects\\learn-jdk\\src\\main\\java\\jvm\\catalina_json.out");
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(writeFile));

        String str = null;
        while((str = bufferedReader.readLine()) != null){
            if(str.indexOf("{\"happened_time\"") == 0 && str.endsWith("\"time_unit\":\"milliseconds\"},")) {
                if(str.indexOf("SharedHeap::process_strong_roots") > 0) {

                    JSONObject jsonObject = JSONObject.parseObject(str.substring(0, str.length() - 1));

                    bufferedWriter.write(jsonObject.getInteger("time") + ",");
                }
            }
        }
        bufferedWriter.flush();

        bufferedWriter.close();
        bufferedReader.close();
    }
}
