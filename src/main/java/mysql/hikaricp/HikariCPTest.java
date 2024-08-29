package mysql.hikaricp;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class HikariCPTest {

    /**
     * 重用的是物理连接，connection 执行完关闭
     *
     * @throws Exception
     */
    @Test
    public void multiThreadQuery() throws Exception {
        long startTime = System.currentTimeMillis();

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        List<Future> futureList = new ArrayList<>();
        Set<String> connectionSet = new HashSet<>();
        // 执行多次数据库操作以测试连接池性能
        for (int i = 0; i < 1000; i++) {
            final int j = i;
            Future future = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    // 重用的是物理连接，connection 执行完关闭
                    try (Connection connection = DataSourceManager.getDataSource().getConnection()) {
                        String sql = "SELECT * FROM customer";
                        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                             ResultSet resultSet = preparedStatement.executeQuery()) {

                            while (resultSet.next()) {
                                // Process the result set
                                String id = resultSet.getString("customer_id");
                                System.out.println(j + 1 + "->" + "ID: " + id + " Connection: " + connection);

                                // 重用的是物理连接，connection 执行完关闭
                                connectionSet.add(Long.toHexString(connection.unwrap(Connection.class).hashCode()));
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
            futureList.add(future);
        }

        for (int i = 0; i < futureList.size(); i++) {
            futureList.get(i).get();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("Collection size: " + connectionSet.size());
        System.out.println("Collection list: " + connectionSet);
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");

        // 关闭数据源
        DataSourceManager.closeDataSource();

    }

    @Test
    public void test1() {
        long startTime = System.currentTimeMillis();

        // 执行多次数据库操作以测试连接池性能
        for (int i = 0; i < 1000; i++) {
            try (Connection connection = DataSourceManager.getDataSource().getConnection()) {
                String sql = "SELECT * FROM customer";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        // Process the result set
                        String id = resultSet.getString("customer_id");
                        System.out.println(i + 1 + "->" + "ID: " + id + " Connection: " + connection);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");

        // 关闭数据源
        DataSourceManager.closeDataSource();
    }
}
