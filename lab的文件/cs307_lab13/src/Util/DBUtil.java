package Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DBUtil {
    public abstract Connection getConnection();

    public abstract String getConnectState();

    public void closeConnection(Connection con, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (con != null) {
                con.close();//con 是ordinary创建的，直接关闭链接，con是proxoolutil创建的，放进连接池里
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
