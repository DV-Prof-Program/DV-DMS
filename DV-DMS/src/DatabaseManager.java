import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dominick Vician, CEN-3024C-24204, April 1st, 2025
 * DatabaseManager.java
 * This class is responsible for managing connections to the MySQL database,
 * ensuring that the vehicles table exists, and providing methods to load and save vehicle data.
 * It handles direct database operations such as creating the table, retrieving all vehicle records,
 * and synchronizing in-memory data with the database.
 */

public class DatabaseManager {
    private Connection conn; // Handles the sql statements and queries

    // These static fields are configured at runtime based on user input.
    public static String host = "localhost";  // default value
    public static String port = "3306";         // default value
    public static String dbName = "vehicle_dms";  // default value
    public static String username = "";
    public static String password = "";

    /**
     * Constructs a new DatabaseManager object.
     * This constructor loads the MySQL JDBC driver, builds the connection URL using the configured static fields,
     * establishes a connection, and ensures that the vehicles table exists by creating it if necessary.
     *
     * @throws SQLException if a database access error occurs or the JDBC driver is not found.
     */
    public DatabaseManager() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.", e);
        }
        // Build the connection URL using the static configuration fields.
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?useSSL=false&serverTimezone=UTC";
        conn = DriverManager.getConnection(url, username, password);
        createTableIfNotExists();
    }

    /**
     * Creates the vehicles table in the database if it does not already exist.
     *
     * @throws SQLException if a database access error occurs.
     */
    private void createTableIfNotExists() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS vehicles ("
                + "vin INT PRIMARY KEY, "
                + "make VARCHAR(50), "
                + "model VARCHAR(50), "
                + "year INT, "
                + "mileage INT, "
                + "price DOUBLE, "
                + "color VARCHAR(30)"
                + ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    /**
     * Inserts a vehicle record into the database.
     *
     * @param vehicle The specified vehicle object to add.
     * @return true if the vehicle was added successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean addVehicle(Vehicle vehicle) throws SQLException {
        String insertSQL = "INSERT INTO vehicles (vin, make, model, year, mileage, price, color) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
            pstmt.setInt(1, vehicle.getVin());
            pstmt.setString(2, vehicle.getMake());
            pstmt.setString(3, vehicle.getModel());
            pstmt.setInt(4, vehicle.getYear());
            pstmt.setInt(5, vehicle.getMileage());
            pstmt.setDouble(6, vehicle.getPrice());
            pstmt.setString(7, vehicle.getColor());
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Removes a vehicle record from the database.
     *
     * @param vin The VIN of the vehicle to remove.
     * @return true if the vehicle was removed successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean removeVehicle(int vin) throws SQLException {
        String deleteSQL = "DELETE FROM vehicles WHERE vin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteSQL)) {
            pstmt.setInt(1, vin);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Updates a vehicle record in the database.
     *
     * @param vehicle The vehicle object to be updated
     * @return true if the vehicle was updated successfully, false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    public boolean updateVehicle(Vehicle vehicle) throws SQLException {
        String updateSQL = "UPDATE vehicles SET make=?, model=?, year=?, mileage=?, price=?, color=? WHERE vin=?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSQL)) {
            pstmt.setString(1, vehicle.getMake());
            pstmt.setString(2, vehicle.getModel());
            pstmt.setInt(3, vehicle.getYear());
            pstmt.setInt(4, vehicle.getMileage());
            pstmt.setDouble(5, vehicle.getPrice());
            pstmt.setString(6, vehicle.getColor());
            pstmt.setInt(7, vehicle.getVin());
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }

    /**
     * Retrieves all vehicle records from the database.
     *
     * @return a List of Vehicle objects representing all vehicles in the database.
     * @throws SQLException if a database access error occurs.
     */
    public List<Vehicle> getAllVehicles() throws SQLException {
        List<Vehicle> list = new ArrayList<>();
        String querySQL = "SELECT * FROM vehicles";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(querySQL)) {
            while(rs.next()){
                int vin = rs.getInt("vin");
                String make = rs.getString("make");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                int mileage = rs.getInt("mileage");
                double price = rs.getDouble("price");
                String color = rs.getString("color");
                Vehicle vehicle = new Vehicle(vin, make, model, year, mileage, price, color);
                list.add(vehicle);
            }
        }
        return list;
    }

    /**
     * method: computeCurrentPrice
     * parameters: int vin, double depRate, int currentYear
     * return: Double - the computed current price based on depreciation, or null if the vehicle is not found
     * purpose: Retrieves the vehicle with the specified VIN and computes its current price using the depreciation formula.
     */
    /**
     * Retrieves the vehicle with the specified VIN and computes its current price using the depreciation formula.
     *
     * @param vin An integer representing the Vehicle Identification Number.
     * @param depRate A double representing the depreciation rate (e.g., 0.1 for 10%).
     * @param currentYear An int representing the current year.
     * @throws SQLException if a database access error occurs.
     * @return The computed current price if successful, or null if the vehicle is not found or an error occurs.
     */
    public Double computeCurrentPrice(int vin, double depRate, int currentYear) throws SQLException {
        String querySQL = "SELECT * FROM vehicles WHERE vin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setInt(1, vin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()){
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    int age = currentYear - year;
                    if(age < 0) age = 0;
                    return price * Math.pow((1 - depRate), age);
                }
            }
        }
        return null;
    }

    /**
     * Closes the connection to the database.
     *
     * @throws SQLException if a database access error occurs.
     */
    public void close() throws SQLException {
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }
}
