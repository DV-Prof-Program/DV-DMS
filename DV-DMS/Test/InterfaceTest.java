import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;

public class InterfaceTest {
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    /**
     * Test for adding a vehicle manually.
     */
    @Test
    public void testAddVehicleManually_Success() {
        String simulatedInput = "1001\nToyota\nCorolla\n2010\n50000\n12000\nBlue\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Interface iface = new Interface();
        boolean result = iface.addVehicleManually();
        assertTrue(result, "Vehicle should be added successfully.");
    }

    /**
     * Test for removing a vehicle.
     */
    @Test
    public void testRemoveVehicle_Success() {
        // Add a vehicle
        String addInput = "1002\nHonda\nCivic\n2012\n45000\n13000\nRed\n";
        System.setIn(new ByteArrayInputStream(addInput.getBytes()));
        Interface iface = new Interface();
        boolean addResult = iface.addVehicleManually();
        assertTrue(addResult, "Vehicle should be added successfully.");

        // Set new input for removal
        String removeInput = "1002\n";
        System.setIn(new ByteArrayInputStream(removeInput.getBytes()));
        iface.updateScanner();  // Update scanner to use new System.in
        boolean removeResult = iface.removeVehicle();
        assertTrue(removeResult, "Vehicle should be removed successfully.");
    }

    /**
     * Test for updating a vehicle.
     */
    @Test
    public void testUpdateVehicle_Success() {
        // Add a vehicle
        String addInput = "1003\nFord\nFocus\n2011\n55000\n11000\nWhite\n";
        System.setIn(new ByteArrayInputStream(addInput.getBytes()));
        Interface iface = new Interface();
        boolean addResult = iface.addVehicleManually();
        assertTrue(addResult, "Vehicle should be added successfully.");

        // Set new input for update: update Make to "FordUpdated" and keep other fields unchanged.
        String updateInput = "1003\nFordUpdated\n\n\n\n\n\n";
        System.setIn(new ByteArrayInputStream(updateInput.getBytes()));
        iface.updateScanner();  // Update scanner
        boolean updateResult = iface.updateVehicle();
        assertTrue(updateResult, "Vehicle should be updated successfully.");

        // Check if the vehicle's make was updated.
        String vehiclesList = iface.displayAllVehicles().toString();
        assertTrue(vehiclesList.contains("FordUpdated"), "Updated vehicle make should be reflected.");
    }

    /**
     * Test for computing the current price.
     */
    @Test
    public void testComputeCurrentPrice_Success() {
        // Add a vehicle with known values.
        String addInput = "1004\nChevrolet\nMalibu\n2015\n30000\n15000\nBlack\n";
        System.setIn(new ByteArrayInputStream(addInput.getBytes()));
        Interface iface = new Interface();
        boolean addResult = iface.addVehicleManually();
        assertTrue(addResult, "Vehicle should be added successfully.");

        // Set new input for computing current price.
        // For VIN 1004: original price = 15000, age = 2020 - 2015 = 5, deprRate = 0.1.
        String computeInput = "1004\n0.1\n2020\n";
        System.setIn(new ByteArrayInputStream(computeInput.getBytes()));
        iface.updateScanner();  // Update scanner
        Double computedPrice = iface.computeCurrentPrice();
        double expectedPrice = 15000 * Math.pow(0.9, 5);
        assertNotNull(computedPrice, "Computed price should not be null.");
        assertEquals(expectedPrice, computedPrice, 0.001, "Computed price should match expected value.");
    }

    /**
     * Test for adding vehicles from a file.
     */
    @Test
    public void testAddVehiclesFromFile_Success() throws IOException {
        // Create a temporary file with three vehicle records.
        File tempFile = File.createTempFile("vehicles", ".txt");
        tempFile.deleteOnExit();
        String content = "2001,Toyota,Corolla,2010,50000,12000,Blue\n" +
                "2002,Honda,Civic,2012,45000,13000,Red\n" +
                "2003,Ford,Focus,2011,55000,11000,White\n";
        Files.write(tempFile.toPath(), content.getBytes());

        // Set input for file path.
        String filePathInput = tempFile.getAbsolutePath() + "\n";
        System.setIn(new ByteArrayInputStream(filePathInput.getBytes()));
        Interface iface = new Interface();
        iface.updateScanner();  // Update scanner to pick up file path input
        int countLoaded = iface.addVehiclesFromFile();
        assertEquals(3, countLoaded, "Should load 3 vehicles from the file.");
    }

    /**
     * Test for displayAllVehicles method.
     */
    @Test
    public void testDisplayAllVehicles() {
        // Create an Interface instance and add two vehicles.
        Interface iface = new Interface();
        // Add first vehicle.
        String addInput1 = "3001\nNissan\nAltima\n2012\n48000\n14000\nBlack\n";
        System.setIn(new ByteArrayInputStream(addInput1.getBytes()));
        iface.updateScanner();
        boolean addResult1 = iface.addVehicleManually();
        assertTrue(addResult1, "First vehicle should be added successfully.");

        // Add second vehicle.
        String addInput2 = "3002\nToyota\nCamry\n2014\n40000\n17000\nGray\n";
        System.setIn(new ByteArrayInputStream(addInput2.getBytes()));
        iface.updateScanner();
        boolean addResult2 = iface.addVehicleManually();
        assertTrue(addResult2, "Second vehicle should be added successfully.");

        // Test displayAllVehicles.
        // Capture the returned list.
        java.util.List<Vehicle> vehicleList = iface.displayAllVehicles();
        assertEquals(2, vehicleList.size(), "Display should return 2 vehicles.");
        // Optionally, check if the list contains vehicles with the given VINs.
        boolean contains3001 = vehicleList.stream().anyMatch(v -> v.getVin().equals(3001));
        boolean contains3002 = vehicleList.stream().anyMatch(v -> v.getVin().equals(3002));
        assertTrue(contains3001 && contains3002, "The vehicle list should contain both vehicles.");
    }
}
