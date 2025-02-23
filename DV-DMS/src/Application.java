/**
 * Dominick Vician, CEN-3024C-24204, February 17th, 2025
 * Application.java
 * This is the main class of the Vehicle Inventory DMS.
 * The DMS will be able to take in an external list of vehicle data
 * and allow a user to view and edit the full list by adding, updating, and removing vehicles manually.
 * The user will also be able to view an estimate of a vehicle's current price given a year and a depreciation rate.
 */

public class Application {
    public static void main(String[] args) {
        Interface appInterface = new Interface();
        appInterface.run();
    }
}