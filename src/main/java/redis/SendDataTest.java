package redis;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Author Hanyu.Wang
 * @Date 2024/10/8 14:40
 * @Description
 * @Version 1.0
 **/
public class SendDataTest {
    @Test
    public void testSendData() throws Exception {
        String ipAddress = "127.0.0.1";
        int port = 6379;

        // 创建套接字连接
        Socket socket = new Socket(ipAddress, port);

        // 获取输出流，用于发送数据
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        // 发送数据
        out.println("*2\r\n$3\r\nGET\r\n$1\r\na\r\n*2\r\n$3\r\nGET\r\n$1\r\nb\r\n*2\r\n$3\r\nGET\r\n$1\r\nc\r\n");

        // 获取输入流，用于读取服务器响应
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        StringBuilder responseBuilder = new StringBuilder();
        while (in.ready()) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            responseBuilder.append(line).append("\n");
        }

        String response = responseBuilder.toString();
        System.out.println("Server response: " + response);

        // 关闭连接
        in.close();
        out.close();
        socket.close();
    }
}
