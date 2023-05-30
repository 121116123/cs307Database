import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class userDatabaseManager {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/proj_1";
    private static final String DB_USERNAME = "checker";
    private static final String DB_PASSWORD = "123456";

    public void createUser(String username, String password, String role) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            // 创建用户
            String createUserSQL = "CREATE USER ? WITH PASSWORD ?";
            PreparedStatement createUserStatement = conn.prepareStatement(createUserSQL);
            createUserStatement.setString(1, username);
            createUserStatement.setString(2, password);
            createUserStatement.executeUpdate();

            // 授予角色
            String grantRoleSQL = "GRANT ? TO ?";
            PreparedStatement grantRoleStatement = conn.prepareStatement(grantRoleSQL);
            grantRoleStatement.setString(1, role);
            grantRoleStatement.setString(2, username);
            grantRoleStatement.executeUpdate();

            System.out.println("User created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void grantPermission(String role, String schema, String permission) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String grantPermissionSQL = "GRANT ? ON SCHEMA ? TO ?";
            PreparedStatement grantPermissionStatement = conn.prepareStatement(grantPermissionSQL);
            grantPermissionStatement.setString(1, permission);
            grantPermissionStatement.setString(2, schema);
            grantPermissionStatement.setString(3, role);
            grantPermissionStatement.executeUpdate();

            System.out.println("Permission granted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        userDatabaseManager manager = new userDatabaseManager();

        // 创建管理员用户并授予管理员角色
        manager.createUser("admin", "adminpassword", "admin");

        // 创建普通用户并授予普通用户角色
        manager.createUser("user", "userpassword", "user");

        // 授予管理员角色的权限
        manager.grantPermission("admin", "public", "ALL PRIVILEGES");

        // 授予普通用户角色的权限
        manager.grantPermission("user", "public", "SELECT");

        // 其他操作...
    }
}