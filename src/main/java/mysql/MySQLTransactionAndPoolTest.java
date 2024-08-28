package mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * @Author Hanyu.Wang
 * @Date 2024/8/28 20:10
 * @Description
 * @Version 1.0
 **/
public class MySQLTransactionAndPoolTest {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "root";

        Connection conn = null;
        Statement stmt = null;

        try {
            // 获取数据库连接
            conn = DriverManager.getConnection(url, user, password);
            // 关闭自动提交
            conn.setAutoCommit(false);

            // 创建Statement对象
            stmt = conn.createStatement();

            // 执行SQL语句
            ResultSet resultSet = stmt.executeQuery("SELECT * from customer");

            System.out.println(resultSet.getString(0));
            // 提交事务
            conn.commit();
        } catch (SQLException e) {
            // 回滚事务
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
