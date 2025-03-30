import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Interface {

    /**
     * method: addVehicle
     * parameters: Integer vin, String make, String model, int year, int mileage, double price, String color
     * return: boolean - true if the vehicle was added successfully, false otherwise
     * purpose: Directly adds the vehicle to the MySQL database.
     */
    public boolean addVehicle(Integer vin, String make, String model, int year, int mileage, double price, String color) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            boolean result = dbManager.addVehicle(new Vehicle(vin, make, model, year, mileage, price, color));
            dbManager.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: removeVehicle
     * parameters: Integer vin
     * return: boolean - true if the vehicle was removed successfully, false otherwise
     * purpose: Removes the vehicle record with the specified VIN directly from the database.
     */
    public boolean removeVehicle(Integer vin) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            boolean result = dbManager.removeVehicle(vin);
            dbManager.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error removing vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: updateVehicle
     * parameters: Integer vin, String newMake, String newModel, Integer newYear, Integer newMileage, Double newPrice, String newColor
     * return: boolean - true if the vehicle was updated successfully, false otherwise
     * purpose: Updates the vehicle record in the database using the VIN as the unique identifier.
     *          Loads the existing record, applies changes, and updates it.
     */
    public boolean updateVehicle(Integer vin, String newMake, String newModel, Integer newYear, Integer newMileage, Double newPrice, String newColor) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            // Retrieve all vehicles and find the matching one (alternatively, add a getVehicleByVin method to DatabaseManager)
            List<Vehicle> list = dbManager.getAllVehicles();
            Vehicle vehicle = null;
            for (Vehicle v : list) {
                if (v.getVin().equals(vin)) {
                    vehicle = v;
                    break;
                }
            }
            if (vehicle == null) {
                dbManager.close();
                return false;
            }
            if (newMake != null && !newMake.trim().isEmpty()) {
                vehicle.setMake(newMake);
            }
            if (newModel != null && !newModel.trim().isEmpty()) {
                vehicle.setModel(newModel);
            }
            if (newYear != null) {
                vehicle.setYear(newYear);
            }
            if (newMileage != null) {
                vehicle.setMileage(newMileage);
            }
            if (newPrice != null) {
                vehicle.setPrice(newPrice);
            }
            if (newColor != null && !newColor.trim().isEmpty()) {
                vehicle.setColor(newColor);
            }
            boolean result = dbManager.updateVehicle(vehicle);
            dbManager.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error updating vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: getAllVehicles
     * parameters: None
     * return: List<Vehicle> - a list of all vehicles retrieved directly from the database
     * purpose: Retrieves all vehicle records from the database.
     */
    public List<Vehicle> getAllVehicles() {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            List<Vehicle> list = dbManager.getAllVehicles();
            dbManager.close();
            return list;
        } catch (SQLException e) {
            System.out.println("Error retrieving vehicles: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * method: computeCurrentPrice
     * parameters: Integer vin, double deprRate, int currentYear
     * return: Double - the computed current price of the vehicle, or null if not found/error occurs
     * purpose: Retrieves the vehicle record from the database and computes its current price using the depreciation formula.
     */
    public Double computeCurrentPrice(Integer vin, double deprRate, int currentYear) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            Double result = dbManager.computeCurrentPrice(vin, deprRate, currentYear);
            dbManager.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error computing current price: " + e.getMessage());
            return null;
        }
    }
}
