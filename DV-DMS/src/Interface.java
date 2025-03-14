/**
 * Dominick Vician, CEN-3024C-24204, February 17th, 2025
 * Interface.java
 * This class contains all the user-facing methods related to usage of the DMS itself,
 * including all the menu options, the ability to read an external file, and input validation.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Interface {
    private List<Vehicle> vehicles;
    private Scanner scanner;

    /**
     * method: Interface (Constructor)
     * parameters: None
     * return: None
     * purpose: Initializes the vehicles list and the scanner for CLI input.
     */
    public Interface() {
        vehicles = new ArrayList<>();
        scanner = new Scanner(System.in);
    }

    /**
     * method: updateScanner
     * parameters: None
     * return: None
     * purpose: Reinitializes the scanner to use the current System.in
     * For debugging and testing only. Not used during normal operations.
     */
    public void updateScanner() {
        scanner = new Scanner(System.in);
    }

    /**
     * method: run
     * parameters: None
     * return: None
     * purpose: Main loop for CLI interaction, displaying menu and handling user choices.
     */
    public void run() {
        boolean exit = false;
        while (!exit) {
            try {
                int choice = displayMenu();
                switch (choice) {
                    case 1:
                        addVehicleManually();
                        break;
                    case 2:
                        addVehiclesFromFile();
                        break;
                    case 3:
                        removeVehicle();
                        break;
                    case 4:
                        updateVehicle();
                        break;
                    case 5:
                        displayAllVehicles();
                        break;
                    case 6:
                        computeCurrentPrice();
                        break;
                    case 7:
                        System.out.println("Exiting program...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please select a valid option.");
                }
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    /**
     * method: displayMenu
     * parameters: None
     * return: choice
     * purpose: Displays the main menu and retrieves the user's choice with input validation.
     */
    private Integer displayMenu() {
        System.out.println("\n--- Vehicle Inventory DMS ---");
        System.out.println("1. Add Vehicle");
        System.out.println("2. Load Vehicles from File");
        System.out.println("3. Remove Vehicle");
        System.out.println("4. Update Vehicle");
        System.out.println("5. Display All Vehicles");
        System.out.println("6. Compute Current Price");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
        int choice = 0;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
        }
        return choice;
    }

    /**
     * method: addVehicleManually
     * parameters: None
     * return: Boolean
     * purpose: Adds a vehicle by prompting the user for details via CLI and performs input validation.
     */
    Boolean addVehicleManually() {
        try {
            System.out.println("\n--- Add Vehicle ---");
            System.out.print("Enter VIN (integer): ");
            Integer vin = Integer.parseInt(scanner.nextLine());

            // Check if a vehicle with the same VIN already exists
            if (getVehicleByVin(vin) != null) {
                System.out.println("Vehicle with VIN " + vin + " already exists.");
                return false;
            }

            System.out.print("Enter Make: ");
            String make = scanner.nextLine();
            System.out.print("Enter Model: ");
            String model = scanner.nextLine();
            System.out.print("Enter Year: ");
            Integer year = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Mileage: ");
            Integer mileage = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter Price: ");
            Double price = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter Color: ");
            String color = scanner.nextLine();

            Vehicle vehicle = new Vehicle(vin, make, model, year, mileage, price, color);
            vehicles.add(vehicle);
            System.out.println("Vehicle added successfully.");
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input format error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: removeVehicle
     * parameters: None
     * return: Boolean
     * purpose: Removes a vehicle from the list by prompting the user for a VIN and validating the input.
     */
    Boolean removeVehicle() {
        try {
            System.out.println("\n--- Remove Vehicle ---");
            System.out.print("Enter VIN of vehicle to remove: ");
            Integer vin = Integer.parseInt(scanner.nextLine());
            Vehicle vehicle = getVehicleByVin(vin);
            if (vehicle != null) {
                vehicles.remove(vehicle);
                System.out.println("Vehicle removed successfully.");
                return true;
            } else {
                System.out.println("No vehicle found with VIN " + vin);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid VIN. Please enter a valid integer.");
            return false;
        } catch (Exception e) {
            System.out.println("Error removing vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: updateVehicle
     * parameters: None
     * return: Boolean
     * purpose: Updates the details of an existing vehicle based on user input and validates the updated data.
     */
    Boolean updateVehicle() {
        try {
            System.out.println("\n--- Update Vehicle ---");
            System.out.print("Enter VIN of vehicle to update: ");
            Integer vin = Integer.parseInt(scanner.nextLine());
            Vehicle vehicle = getVehicleByVin(vin);
            if (vehicle != null) {
                System.out.println("Enter new details. Press enter to keep current value.");

                System.out.print("Current Make (" + vehicle.getMake() + ") New Make: ");
                String make = scanner.nextLine();
                if (!make.trim().isEmpty()) {
                    vehicle.setMake(make);
                }

                System.out.print("Current Model (" + vehicle.getModel() + ") New Model: ");
                String model = scanner.nextLine();
                if (!model.trim().isEmpty()) {
                    vehicle.setModel(model);
                }

                System.out.print("Current Year (" + vehicle.getYear() + ") New Year: ");
                String yearInput = scanner.nextLine();
                if (!yearInput.trim().isEmpty()) {
                    int year = Integer.parseInt(yearInput);
                    vehicle.setYear(year);
                }

                System.out.print("Current Mileage (" + vehicle.getMileage() + ") New Mileage: ");
                String mileageInput = scanner.nextLine();
                if (!mileageInput.trim().isEmpty()) {
                    int mileage = Integer.parseInt(mileageInput);
                    vehicle.setMileage(mileage);
                }

                System.out.printf("Current Price (%.2f) New Price: ", vehicle.getPrice());
                String priceInput = scanner.nextLine();
                if (!priceInput.trim().isEmpty()) {
                    double price = Double.parseDouble(priceInput);
                    vehicle.setPrice(price);
                }

                System.out.print("Current Color (" + vehicle.getColor() + ") New Color: ");
                String color = scanner.nextLine();
                if (!color.trim().isEmpty()) {
                    vehicle.setColor(color);
                }

                System.out.println("Vehicle updated successfully.");
                return true;
            } else {
                System.out.println("No vehicle found with VIN " + vin);
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Input format error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error updating vehicle: " + e.getMessage());
            return false;
        }
    }

    /**
     * method: displayAllVehicles
     * parameters: None
     * return: Boolean
     * purpose: Displays all vehicles currently stored in the system.
     */
    public List<Vehicle> displayAllVehicles() {
        System.out.println("\n--- Vehicle List ---");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            for (Vehicle v : vehicles) {
                System.out.println(v.toString());
            }
        }
        return this.vehicles;
    }

    /**
     * method: computeCurrentPrice
     * parameters: None
     * return: Double
     * purpose: Prompts the user for a VIN and current year,
     * then computes and displays the vehicle's estimated current price.
     */
    Double computeCurrentPrice() {
        try {
            System.out.println("\n--- Compute Current Price ---");
            System.out.print("Enter VIN of vehicle: ");
            Integer vin = Integer.parseInt(scanner.nextLine());
            Vehicle vehicle = getVehicleByVin(vin);
            if (vehicle == null) {
                System.out.println("No vehicle found with VIN " + vin);
                return null;
            }
            System.out.print("Enter annual depreciation rate (e.g., 0.1 for 10%): ");
            double depRate = Double.parseDouble(scanner.nextLine());
            System.out.print("Enter current year: ");
            int currentYear = Integer.parseInt(scanner.nextLine());
            double currentPrice = vehicle.computeCurrentPrice(depRate, currentYear);
            System.out.println("The computed current price is: " + currentPrice);
            return currentPrice;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println("Error computing current price: " + e.getMessage());
            return null;
        }
    }

    /**
     * method: addVehiclesFromFile
     * parameters: None
     * return: Integer
     * purpose: Prompts the user for a file path, fixes a common formatting error in Windows,
     * reads vehicle data from the specified text file,and adds valid vehicles to the system.
     */
    Integer addVehiclesFromFile() {
        System.out.println("\n--- Load Vehicles from File ---");
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();
        Integer countLoaded = 0;
        filePath = filePath.replace("\"", "");     //These two lines alter the formatting of the path
        filePath = filePath.replace("\\", "/");    //provided by the 'Copy as Path' option on Windows

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Vehicle vehicle = parseVehicle(line);
                    if (vehicle != null) {
                        // Check for duplicate VIN
                        if (getVehicleByVin(vehicle.getVin()) == null) {
                            vehicles.add(vehicle);
                            countLoaded++;
                        } else {
                            System.out.println("Vehicle with VIN " + vehicle.getVin() + " already exists. Skipping line.");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line + ". Error: " + e.getMessage());
                }
            }
            System.out.println(countLoaded + " vehicles loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return countLoaded;
    }

    /**
     * method: parseVehicle
     * parameters: String line - a comma-separated string containing vehicle data
     * return: Vehicle - the parsed Vehicle object
     * purpose: Parses a line from the text file and returns a Vehicle object if the data is valid.
     */
    // Expected format: vin,make,model,year,mileage,price,color
    private Vehicle parseVehicle(String line) {
        String[] tokens = line.split(",");
        if (tokens.length != 7) {
            throw new IllegalArgumentException("Invalid data format. Expected 7 fields, got " + tokens.length);
        }
        try {
            Integer vin = Integer.parseInt(tokens[0].trim());
            String make = tokens[1].trim();
            String model = tokens[2].trim();
            Integer year = Integer.parseInt(tokens[3].trim());
            Integer mileage = Integer.parseInt(tokens[4].trim());
            Double price = Double.parseDouble(tokens[5].trim());
            String color = tokens[6].trim();
            return new Vehicle(vin, make, model, year, mileage, price, color);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Number format error: " + e.getMessage());
        }
    }

    /**
     * method: getVehicleByVin
     * parameters: Integer vin - the VIN of the vehicle to search for
     * return: Vehicle - the vehicle with the matching VIN, or null if not found
     * purpose: Searches the list of vehicles for a vehicle with the specified VIN.
     */
    Vehicle getVehicleByVin(Integer vin) {
        for (Vehicle v : vehicles) {
            if (v.getVin().equals(vin)) {
                return v;
            }
        }
        return null;
    }
    /**
     * method: addVehicle
     * parameters: Integer vin, String make, String model, int year, int mileage, double price, String color
     * return: boolean
     * purpose: Adds a vehicle using provided parameters.
     */
    public boolean addVehicleManually(Integer vin, String make, String model, int year, int mileage, double price, String color) {
        if (getVehicleByVin(vin) != null) {
            return false;
        }
        Vehicle vehicle = new Vehicle(vin, make, model, year, mileage, price, color);
        vehicles.add(vehicle);
        return true;
    }

    /**
     * method: removeVehicle
     * parameters: Integer vin
     * return: boolean
     * purpose: Removes a vehicle with the given VIN.
     */
    public boolean removeVehicle(Integer vin) {
        Vehicle vehicle = getVehicleByVin(vin);
        if (vehicle != null) {
            vehicles.remove(vehicle);
            return true;
        }
        return false;
    }

    /**
     * method: updateVehicle
     * parameters: Integer vin, String newMake, String newModel, Integer newYear, Integer newMileage, Double newPrice, String newColor
     * return:
     * purpose: Updates the vehicle with the given VIN. Null or empty parameters indicate no change.
     */
    public boolean updateVehicle(Integer vin, String newMake, String newModel, Integer newYear, Integer newMileage, Double newPrice, String newColor) {
        Vehicle vehicle = getVehicleByVin(vin);
        if (vehicle == null) {
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
        return true;
    }

    /**
     * method: computeCurrentPrice
     * parameters: Integer vin, double deprRate, int currentYear
     * return: Double
     * purpose: Computes the current price of the vehicle with given VIN.
     */
    public Double computeCurrentPrice(Integer vin, double deprRate, int currentYear) {
        Vehicle vehicle = getVehicleByVin(vin);
        if (vehicle == null) {
            return null;
        }
        return vehicle.computeCurrentPrice(deprRate, currentYear);
    }

    /**
     * method: loadVehiclesFromFile
     * parameters: String filePath
     * return: Integer
     * purpose: Loads vehicles from a file at the given file path.
     */
    public Integer addVehiclesFromFile(String filePath) {
        int countLoaded = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    Vehicle vehicle = parseVehicle(line);
                    if (vehicle != null) {
                        if (getVehicleByVin(vehicle.getVin()) == null) {
                            vehicles.add(vehicle);
                            countLoaded++;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line + ". Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return countLoaded;
    }
}
