package mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariCPDemo {

    private static HikariDataSource dataSource;

    public static void main(String[] args) {
        // Initialize the connection pool
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        config.setUsername("root");
        config.setPassword("root");
        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(1000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSource = new HikariDataSource(config);

        try {
            for (int i = 0; i < 10; i++) {
                // Use the connection pool to get a connection and execute a query
                Connection connection = dataSource.getConnection();
                String sql = "SELECT * FROM customer";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                     ResultSet resultSet = preparedStatement.executeQuery()) {

                    while (resultSet.next()) {
                        // Process the result set
                        String id = resultSet.getString("customer_id");
                        System.out.println("ID: " + id);
                    }
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        } finally {
            if (dataSource != null) {
                dataSource.close();
            }
        }
    }
}