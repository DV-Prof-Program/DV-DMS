import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dominick Vician, CEN-3024C-24204, March 31st, 2025
 * DMSGUI.java
 * This class provides a Swing-based graphical user interface that allows users to perform CRUD operations and compute
 * the current price of vehicles directly against the MySQL database using the Interface class. The GUI includes
 * buttons for each operation and displays results in a text area.
 */

public class DMSGUI extends JFrame {
    private Interface logic;
    private JTextArea outputArea;

    /**
     * Constructs a new DMSGUI frame and initializes the GUI components.
     */
    public DMSGUI() {
        super("Vehicle Inventory DMS");
        logic = new Interface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLayout(new BorderLayout());

        // Create panel with buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 3, 5, 5));

        JButton addButton = new JButton("Add Vehicle");
        JButton removeButton = new JButton("Remove Vehicle");
        JButton updateButton = new JButton("Update Vehicle");
        JButton displayButton = new JButton("Display Vehicles");
        JButton computeButton = new JButton("Compute Price");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(computeButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.NORTH);

        // Create output area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Button Action Listeners

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVehicleAction();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeVehicleAction();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateVehicleAction();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayVehiclesAction();
            }
        });

        computeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computeCurrentPriceAction();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Prompts the user for input and adds a new vehicle to the MySQL database.
     */
    private void addVehicleAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN:");
            if (vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);

            String make = JOptionPane.showInputDialog(this, "Enter Make:");
            if (make == null) return;
            String model = JOptionPane.showInputDialog(this, "Enter Model:");
            if (model == null) return;
            String yearStr = JOptionPane.showInputDialog(this, "Enter Year:");
            if (yearStr == null) return;
            int year = Integer.parseInt(yearStr);
            String mileageStr = JOptionPane.showInputDialog(this, "Enter Mileage:");
            if (mileageStr == null) return;
            int mileage = Integer.parseInt(mileageStr);
            String priceStr = JOptionPane.showInputDialog(this, "Enter Price:");
            if (priceStr == null) return;
            double price = Double.parseDouble(priceStr);
            String color = JOptionPane.showInputDialog(this, "Enter Color:");
            if (color == null) return;

            boolean result = logic.addVehicle(vin, make, model, year, mileage, price, color);
            if (result) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle. It may already exist.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    /**
     * Prompts the user for a VIN and removes the corresponding vehicle from the MySQL database.
     */
    private void removeVehicleAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle to remove:");
            if (vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);
            boolean result = logic.removeVehicle(vin);
            if (result) {
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No vehicle found with VIN " + vin);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    /**
     * Prompts the user for input and updates an existing vehicle record in the MySQL database.
     */
    private void updateVehicleAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle to update:");
            if (vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);

            // Since we operate directly with the database, we can ask for new values.
            String newMake = JOptionPane.showInputDialog(this, "Enter new Make (leave blank to keep unchanged):");
            String newModel = JOptionPane.showInputDialog(this, "Enter new Model (leave blank to keep unchanged):");
            String newYearStr = JOptionPane.showInputDialog(this, "Enter new Year (leave blank to keep unchanged):");
            String newMileageStr = JOptionPane.showInputDialog(this, "Enter new Mileage (leave blank to keep unchanged):");
            String newPriceStr = JOptionPane.showInputDialog(this, "Enter new Price (leave blank to keep unchanged):");
            String newColor = JOptionPane.showInputDialog(this, "Enter new Color (leave blank to keep unchanged):");

            Integer newYear = (newYearStr != null && !newYearStr.trim().isEmpty()) ? Integer.parseInt(newYearStr) : null;
            Integer newMileage = (newMileageStr != null && !newMileageStr.trim().isEmpty()) ? Integer.parseInt(newMileageStr) : null;
            Double newPrice = (newPriceStr != null && !newPriceStr.trim().isEmpty()) ? Double.parseDouble(newPriceStr) : null;

            boolean result = logic.updateVehicle(vin, newMake, newModel, newYear, newMileage, newPrice, newColor);
            if (result) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update vehicle.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    /**
     * Retrieves all vehicle records from the MySQL database and displays them in the output area.
     */
    private void displayVehiclesAction() {
        List<Vehicle> vehicles = logic.getAllVehicles();
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : vehicles) {
            sb.append(v.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    /**
     * Prompts the user for input to compute the current price of a vehicle and displays the result.
     */
    private void computeCurrentPriceAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle:");
            if (vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);
            String deprRateStr = JOptionPane.showInputDialog(this, "Enter depreciation rate (e.g., 0.1 for 10%):");
            if (deprRateStr == null || deprRateStr.trim().isEmpty()) return;
            double deprRate = Double.parseDouble(deprRateStr);
            String currentYearStr = JOptionPane.showInputDialog(this, "Enter current year:");
            if (currentYearStr == null || currentYearStr.trim().isEmpty()) return;
            int currentYear = Integer.parseInt(currentYearStr);

            Double computedPrice = logic.computeCurrentPrice(vin, deprRate, currentYear);
            if (computedPrice != null) {
                JOptionPane.showMessageDialog(this, "The computed current price is: " + computedPrice);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to compute current price.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    /**
     * Prompts the user for MySQL database connection details and sets the configuration in the DatabaseManager class.
     */
    private static void promptDBConfig() {
        String host = JOptionPane.showInputDialog(null, "Enter MySQL Host (default: localhost):", "localhost");
        String port = JOptionPane.showInputDialog(null, "Enter MySQL Port (default: 3306):", "3306");
        String dbName = JOptionPane.showInputDialog(null, "Enter Database Name (default: vehicle_dms):", "vehicle_dms");
        String username = JOptionPane.showInputDialog(null, "Enter MySQL Username:");
        String password = JOptionPane.showInputDialog(null, "Enter MySQL Password:");

        if (host != null && !host.trim().isEmpty()) {
            DatabaseManager.host = host;
        }
        if (port != null && !port.trim().isEmpty()) {
            DatabaseManager.port = port;
        }
        if (dbName != null && !dbName.trim().isEmpty()) {
            DatabaseManager.dbName = dbName;
        }
        if (username != null && !username.trim().isEmpty()) {
            DatabaseManager.username = username;
        }
        if (password != null && !password.trim().isEmpty()) {
            DatabaseManager.password = password;
        }
    }

    /**
     * The main method prompts the user for MySQL database configuration details and launches the GUI.
     *
     * @param args Command-line arguments (not used, but main methods require this regardless).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            promptDBConfig();
            DMSGUI gui = new DMSGUI();
            gui.setVisible(true);
        });
    }
}
