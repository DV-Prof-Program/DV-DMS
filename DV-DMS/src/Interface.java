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
    private int displayMenu() {
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
     * return: None
     * purpose: Adds a vehicle by prompting the user for details via CLI and performs input validation.
     */
    private void addVehicleManually() {
        try {
            System.out.println("\n--- Add Vehicle ---");
            System.out.print("Enter VIN (integer): ");
            Integer vin = Integer.parseInt(scanner.nextLine());

            // Check if a vehicle with the same VIN already exists
            if (getVehicleByVin(vin) != null) {
                System.out.println("Vehicle with VIN " + vin + " already exists.");
                return;
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
        } catch (NumberFormatException e) {
            System.out.println("Input format error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error adding vehicle: " + e.getMessage());
        }
    }

    /**
     * method: removeVehicle
     * parameters: None
     * return: None
     * purpose: Removes a vehicle from the list by prompting the user for a VIN and validating the input.
     */
    private void removeVehicle() {
        try {
            System.out.println("\n--- Remove Vehicle ---");
            System.out.print("Enter VIN of vehicle to remove: ");
            Integer vin = Integer.parseInt(scanner.nextLine());
            Vehicle vehicle = getVehicleByVin(vin);
            if (vehicle != null) {
                vehicles.remove(vehicle);
                System.out.println("Vehicle removed successfully.");
            } else {
                System.out.println("No vehicle found with VIN " + vin);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid VIN. Please enter a valid integer.");
        } catch (Exception e) {
            System.out.println("Error removing vehicle: " + e.getMessage());
        }
    }

    /**
     * method: updateVehicle
     * parameters: None
     * return: None
     * purpose: Updates the details of an existing vehicle based on user input and validates the updated data.
     */
    private void updateVehicle() {
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
            } else {
                System.out.println("No vehicle found with VIN " + vin);
            }
        } catch (NumberFormatException e) {
            System.out.println("Input format error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error updating vehicle: " + e.getMessage());
        }
    }

    /**
     * method: displayAllVehicles
     * parameters: None
     * return: None
     * purpose: Displays all vehicles currently stored in the system.
     */
    private void displayAllVehicles() {
        System.out.println("\n--- Vehicle List ---");
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles available.");
        } else {
            for (Vehicle v : vehicles) {
                System.out.println(v.toString());
            }
        }
    }

    /**
     * method: computeCurrentPrice
     * parameters: None
     * return: None
     * purpose: Prompts the user for a VIN and current year,
     * then computes and displays the vehicle's estimated current price.
     */
    private void computeCurrentPrice() {
        try {
            System.out.println("\n--- Compute Current Price ---");
            System.out.print("Enter VIN of vehicle: ");
            Integer vin = Integer.parseInt(scanner.nextLine());
            Vehicle vehicle = getVehicleByVin(vin);
            if (vehicle == null) {
                System.out.println("No vehicle found with VIN " + vin);
                return;
            }
            System.out.print("Enter current year: ");
            int currentYear = Integer.parseInt(scanner.nextLine());
            double currentPrice = vehicle.computeCurrentPrice(currentYear);
            System.out.printf("The computed current price is: %.2f\n", currentPrice);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input format: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error computing current price: " + e.getMessage());
        }
    }

    /**
     * method: addVehiclesFromFile
     * parameters: None
     * return: None
     * purpose: Prompts the user for a file path, fixes a common formatting error in Windows,
     * reads vehicle data from the specified text file,and adds valid vehicles to the system.
     */
    private void addVehiclesFromFile() {
        System.out.println("\n--- Load Vehicles from File ---");
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();
        filePath = filePath.replace("\"", "");     //These two lines alter the formatting of the path
        filePath = filePath.replace("\\", "/");    //provided by the 'Copy as Path' option on Windows
                                                                    //to fit the formatting expected by FileReader
        int countLoaded = 0;
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
            int year = Integer.parseInt(tokens[3].trim());
            int mileage = Integer.parseInt(tokens[4].trim());
            double price = Double.parseDouble(tokens[5].trim());
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
    private Vehicle getVehicleByVin(Integer vin) {
        for (Vehicle v : vehicles) {
            if (v.getVin().equals(vin)) {
                return v;
            }
        }
        return null;
    }
}
