import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection conn;
    public static String host = "localhost";  // default value
    public static String port = "3306";         // default value
    public static String dbName = "vehicle_dms";  // default value
    public static String username = "";
    public static String password = "";

    /**
     * method: DatabaseManager (Constructor)
     * parameters: None
     * return: None
     * purpose: Loads the MySQL JDBC driver, establishes a connection, and creates the vehicles table if it doesn't exist.
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
     * method: createTableIfNotExists
     * parameters: None
     * return: None
     * purpose: Creates the vehicles table in the database if it does not already exist.
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
     * method: addVehicle
     * parameters: Vehicle vehicle
     * return: boolean - true if the vehicle was added successfully, false otherwise
     * purpose: Inserts a vehicle record into the database.
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
     * method: removeVehicle
     * parameters: int vin
     * return: boolean - true if the vehicle was removed successfully, false otherwise
     * purpose: Deletes the vehicle record with the specified VIN from the database.
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
     * method: updateVehicle
     * parameters: Vehicle vehicle
     * return: boolean - true if the vehicle was updated successfully, false otherwise
     * purpose: Updates the vehicle record in the database using the VIN as the unique identifier.
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
     * method: getAllVehicles
     * parameters: None
     * return: List<Vehicle> - a list of all vehicles in the database
     * purpose: Retrieves all vehicle records from the database and returns them as a list of Vehicle objects.
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
     * parameters: int vin, double deprRate, int currentYear
     * return: Double - the computed current price based on depreciation, or null if the vehicle is not found
     * purpose: Retrieves the vehicle with the specified VIN and computes its current price using the depreciation formula.
     */
    public Double computeCurrentPrice(int vin, double deprRate, int currentYear) throws SQLException {
        String querySQL = "SELECT * FROM vehicles WHERE vin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(querySQL)) {
            pstmt.setInt(1, vin);
            try (ResultSet rs = pstmt.executeQuery()) {
                if(rs.next()){
                    int year = rs.getInt("year");
                    double price = rs.getDouble("price");
                    int age = currentYear - year;
                    if(age < 0) age = 0;
                    return price * Math.pow((1 - deprRate), age);
                }
            }
        }
        return null;
    }

    /**
     * method: close
     * parameters: None
     * return: None
     * purpose: Closes the database connection.
     */
    public void close() throws SQLException {
        if(conn != null && !conn.isClosed()){
            conn.close();
        }
    }
}
