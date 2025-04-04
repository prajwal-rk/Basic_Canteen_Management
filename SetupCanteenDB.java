import java.sql.*;

public class SetupCanteenDB {

    // Connection details (no database selected initially)
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root"; // Change if hosting with different credentials
    private static final String PASSWORD = "root"; // Change if hosting with different credentials

    public static void main(String[] args) {
        try {
            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL server (without DB)
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // 1. Create the main canteen_db
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS canteen_db");
            System.out.println("✅ Database 'canteen_db' created or already exists.");

            // 2. Reconnect using the new database
            conn = DriverManager.getConnection(URL + "canteen_db", USER, PASSWORD);
            stmt = conn.createStatement();

            // 3. Create 'menu' table
            String createMenu = "CREATE TABLE IF NOT EXISTS menu ("
                    + "item_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "item_name VARCHAR(100) NOT NULL UNIQUE, "
                    + "price DOUBLE NOT NULL"
                    + ")";
            stmt.executeUpdate(createMenu);
            System.out.println("✅ Table 'menu' created or already exists.");

            // 4. Create 'orders' table
            String createOrders = "CREATE TABLE IF NOT EXISTS orders ("
                    + "order_id INT AUTO_INCREMENT PRIMARY KEY, "
                    + "user_id INT NOT NULL, "
                    + "user_name VARCHAR(100) NOT NULL, "
                    + "item_name VARCHAR(100) NOT NULL, "
                    + "quantity INT NOT NULL, "
                    + "price DOUBLE NOT NULL, "
                    + "FOREIGN KEY (item_name) REFERENCES menu(item_name) "
                    + "ON DELETE CASCADE ON UPDATE CASCADE"
                    + ")";
            stmt.executeUpdate(createOrders);
            System.out.println("✅ Table 'orders' created or already exists.");

            System.out.println("\n🎉 Canteen Database setup completed successfully!");

            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("❌ JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ SQL Error:");
            e.printStackTrace();
        }
    }
}
