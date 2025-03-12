import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DMSGUI extends JFrame {
    private Interface logic;
    private JTextArea outputArea;

    public DMSGUI() {
        super("Vehicle Inventory DMS");
        logic = new Interface();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLayout(new BorderLayout());

        // Create panel with buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));

        JButton addButton = new JButton("Add Vehicle");
        JButton removeButton = new JButton("Remove Vehicle");
        JButton updateButton = new JButton("Update Vehicle");
        JButton displayButton = new JButton("Display Vehicles");
        JButton computeButton = new JButton("Compute Price");
        JButton loadButton = new JButton("Load From File");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(displayButton);
        buttonPanel.add(computeButton);
        buttonPanel.add(loadButton);
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
                addVehicleManually();
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

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadFromFileAction();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void addVehicleManually() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN (integer):");
            if(vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);

            String make = JOptionPane.showInputDialog(this, "Enter Make:");
            if(make == null) return;
            String model = JOptionPane.showInputDialog(this, "Enter Model:");
            if(model == null) return;
            String yearStr = JOptionPane.showInputDialog(this, "Enter Year:");
            if(yearStr == null) return;
            int year = Integer.parseInt(yearStr);
            String mileageStr = JOptionPane.showInputDialog(this, "Enter Mileage:");
            if(mileageStr == null) return;
            int mileage = Integer.parseInt(mileageStr);
            String priceStr = JOptionPane.showInputDialog(this, "Enter Price:");
            if(priceStr == null) return;
            double price = Double.parseDouble(priceStr);
            String color = JOptionPane.showInputDialog(this, "Enter Color:");
            if(color == null) return;

            boolean result = logic.addVehicleManually(vin, make, model, year, mileage, price, color);
            if(result) {
                JOptionPane.showMessageDialog(this, "Vehicle added successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add vehicle. It may already exist.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    private void removeVehicleAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle to remove:");
            if(vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);
            boolean result = logic.removeVehicle(vin);
            if(result) {
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "No vehicle found with VIN " + vin);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    private void updateVehicleAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle to update:");
            if(vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);

            // Get current vehicle details if exists.
            Vehicle vehicle = logic.getVehicleByVin(vin);
            if(vehicle == null) {
                JOptionPane.showMessageDialog(this, "No vehicle found with VIN " + vin);
                return;
            }

            String newMake = JOptionPane.showInputDialog(this, "Enter new Make (leave blank to keep current: " + vehicle.getMake() + "):");
            String newModel = JOptionPane.showInputDialog(this, "Enter new Model (leave blank to keep current: " + vehicle.getModel() + "):");
            String newYearStr = JOptionPane.showInputDialog(this, "Enter new Year (leave blank to keep current: " + vehicle.getYear() + "):");
            String newMileageStr = JOptionPane.showInputDialog(this, "Enter new Mileage (leave blank to keep current: " + vehicle.getMileage() + "):");
            String newPriceStr = JOptionPane.showInputDialog(this, "Enter new Price (leave blank to keep current: " + vehicle.getPrice() + "):");
            String newColor = JOptionPane.showInputDialog(this, "Enter new Color (leave blank to keep current: " + vehicle.getColor() + "):");

            Integer newYear = (newYearStr != null && !newYearStr.trim().isEmpty()) ? Integer.parseInt(newYearStr) : null;
            Integer newMileage = (newMileageStr != null && !newMileageStr.trim().isEmpty()) ? Integer.parseInt(newMileageStr) : null;
            Double newPrice = (newPriceStr != null && !newPriceStr.trim().isEmpty()) ? Double.parseDouble(newPriceStr) : null;

            boolean result = logic.updateVehicle(vin, newMake, newModel, newYear, newMileage, newPrice, newColor);
            if(result) {
                JOptionPane.showMessageDialog(this, "Vehicle updated successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update vehicle.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    private void displayVehiclesAction() {
        List<Vehicle> vehicles = logic.displayAllVehicles();
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : vehicles) {
            sb.append(v.toString()).append("\n");
        }
        outputArea.setText(sb.toString());
    }

    private void computeCurrentPriceAction() {
        try {
            String vinStr = JOptionPane.showInputDialog(this, "Enter VIN of vehicle:");
            if(vinStr == null || vinStr.trim().isEmpty()) return;
            Integer vin = Integer.parseInt(vinStr);
            String deprRateStr = JOptionPane.showInputDialog(this, "Enter depreciation rate (e.g., 0.1 for 10%):");
            if(deprRateStr == null || deprRateStr.trim().isEmpty()) return;
            double deprRate = Double.parseDouble(deprRateStr);
            String currentYearStr = JOptionPane.showInputDialog(this, "Enter current year:");
            if(currentYearStr == null || currentYearStr.trim().isEmpty()) return;
            int currentYear = Integer.parseInt(currentYearStr);

            Double computedPrice = logic.computeCurrentPrice(vin, deprRate, currentYear);
            if(computedPrice != null) {
                JOptionPane.showMessageDialog(this, "The computed current price is: " + computedPrice);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to compute current price.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input format: " + ex.getMessage());
        }
    }

    private void loadFromFileAction() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            int countLoaded = logic.addVehiclesFromFile(filePath);
            JOptionPane.showMessageDialog(this, countLoaded + " vehicles loaded successfully from file.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DMSGUI gui = new DMSGUI();
            gui.setVisible(true);
        });
    }
}
