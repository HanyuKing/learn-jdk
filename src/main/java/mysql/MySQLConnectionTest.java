package mysql;

import com.mysql.cj.conf.DatabaseUrlContainer;
import com.mysql.cj.conf.HostInfo;
import com.mysql.cj.jdbc.ConnectionImpl;
import com.mysql.cj.jdbc.JdbcConnection;

import java.sql.SQLException;

/**
 * @Author Hanyu.Wang
 * @Date 2024/3/18 10:40
 * @Description
 * @Version 1.0
 **/
public class MySQLConnectionTest {
    public static void main(String[] args) throws SQLException {

        HostInfo hostInfo = new HostInfo(null, "123", 123, "123", "123");
        JdbcConnection jdbcConnection = ConnectionImpl.getInstance(hostInfo);
    }
}
