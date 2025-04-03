import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dominick Vician, CEN-3024C-24204, March 30th, 2025
 * Interface.java
 * This class contains all the business logic that is used in each method, as well as interfacing with the database via
 * DatabaseManager.java such that the user never directly accesses database-specific details.
 */

public class Interface {

    /**
     * addVehicle - Directly adds the vehicle to the MySQL database.
     *
     * @param vin An integer containing the Vehicle Identification Number of the vehicle
     * @param make A string containing the make of the vehicle e.g. Toyota, Dodge
     * @param model  A string containing the model of the vehicle e.g. Prius, Charger
     * @param year An integer representing the year this car was made
     * @param mileage An integer representing how many miles this car has driven
     * @param price A double representing the price of this vehicle when it was first released
     * @param color A string representing the color of the vehicle e.g. Red, Neon Pink
     * @return boolean - true if the vehicle was added successfully, false otherwise
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
     * Removes the vehicle with the specified VIN from the MySQL database.
     *
     * @param vin An Integer representing the Vehicle Identification Number.
     * @return boolean - true if the vehicle was removed successfully, false otherwise.
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
     * Updates the vehicle record in the MySQL database.
     * The method retrieves the existing record, applies any non-null or non-empty changes, and updates the record.
     *
     * @param vin An integer representing the Vehicle Identification Number of the vehicle to update.
     * @param newMake A String containing the new make of the vehicle (or null/empty to leave unchanged).
     * @param newModel A String containing the new model of the vehicle (or null/empty to leave unchanged).
     * @param newYear An Integer representing the new year (or null to leave unchanged).
     * @param newMileage An Integer representing the new mileage (or null to leave unchanged).
     * @param newPrice A Double representing the new price (or null to leave unchanged).
     * @param newColor A String representing the new color (or null/empty to leave unchanged).
     * @return boolean - true if the vehicle was updated successfully, false otherwise.
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
     * Retrieves all vehicles from the MySQL database.
     *
     * @return a List of Vehicle objects representing all vehicles in the database.
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
     * Computes the current price of a vehicle using a depreciation formula.
     *
     * @param vin An integer representing the Vehicle Identification Number.
     * @param depRate A double representing the depreciation rate (e.g., 0.1 for 10%).
     * @param currentYear An int representing the current year.
     * @return Double - the computed current price if successful, or null if the vehicle is not found or an error occurs.
     */
    public Double computeCurrentPrice(Integer vin, double depRate, int currentYear) {
        try {
            DatabaseManager dbManager = new DatabaseManager();
            Double result = dbManager.computeCurrentPrice(vin, depRate, currentYear);
            dbManager.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error computing current price: " + e.getMessage());
            return null;
        }
    }
}
