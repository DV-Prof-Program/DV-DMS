/**
 * Dominick Vician, CEN-3024C-24204, February 17th, 2025
 * Vehicle.java
 * This class contains all the information that is relevant to a specific vehicle,
 * a VIN, a make, a model, production year, current mileage, initial price, and color
 * as well as the methods required to set and access that information.
 */

public class Vehicle {
    private Integer vin;
    private String make;
    private String model;
    private Integer year;
    private Integer mileage;
    private Double price;
    private String color;

    /**
     * Vehicle - Constructs a new Vehicle with the specified attributes.
     * @param vin An Integer containing the Vehicle Identification Number.
     * @param make A String containing the make of the vehicle (e.g., Toyota, Dodge).
     * @param model A String containing the model of the vehicle (e.g., Prius, Charger).
     * @param year An int representing the year the vehicle was manufactured.
     * @param mileage An int representing the total mileage of the vehicle.
     * @param price A double representing the original price of the vehicle.
     * @param color A String representing the color of the vehicle (e.g., Red, Neon Pink).
     */
    public Vehicle(Integer vin, String make, String model, Integer year, Integer mileage, Double price, String color) {
        setVin(vin);
        setMake(make);
        setModel(model);
        setYear(year);
        setMileage(mileage);
        setPrice(price);
        setColor(color);
    }

    /**
     * Returns the vehicle identification number.
     *
     * @return Integer representing the VIN.
     */
    public Integer getVin() {
        return vin;
    }
    /**
     * Returns the make of the vehicle.
     *
     * @return String representing the make.
     */
    public String getMake() {
        return make;
    }
    /**
     * Returns the model of the vehicle.
     *
     * @return String representing the model.
     */
    public String getModel() {
        return model;
    }
    /**
     * Returns the manufacturing year of the vehicle.
     *
     * @return int representing the year.
     */
    public Integer getYear() {
        return year;
    }
    /**
     * Returns the mileage of the vehicle.
     *
     * @return int representing the mileage.
     */
    public Integer getMileage() {
        return mileage;
    }
    /**
     * Returns the original price of the vehicle.
     *
     * @return double representing the price.
     */
    public Double getPrice() {
        return price;
    }
    /**
     * Returns the color of the vehicle.
     *
     * @return String representing the color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the VIN of the vehicle.
     *
     * @param vin An Integer representing the VIN.
     */
    public void setVin(Integer vin) {
        this.vin = vin;
    }
    /**
     * Sets the make of the vehicle.
     *
     * @param make A String representing the vehicle's make.
     */
    public void setMake(String make) {
        this.make = make;
    }
    /**
     * Sets the model of the vehicle.
     *
     * @param model A String representing the vehicle's model.
     */
    public void setModel(String model) {
        this.model = model;
    }
    /**
     * Sets the manufacturing year of the vehicle.
     *
     * @param year An Integer representing the vehicle's production year.
     */
    public void setYear(Integer year) {
        this.year = year;
    }
    /**
     * Sets the current mileage of the vehicle.
     *
     * @param mileage An Integer representing the vehicle's current mileage.
     */
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
    /**
     * Sets the original production price of the vehicle.
     *
     * @param price A Double representing the vehicle's price at production.
     */
    public void setPrice(Double price) {
        this.price = price;
    }
    /**
     * Sets the color of the vehicle.
     *
     * @param color A String representing the vehicle's color.
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Computes the current price of the vehicle using a depreciation formula.
     * Formula: currentPrice = originalPrice * (1 - depRate)^age, where age = currentYear - year.
     *
     * @param depRate A double representing the depreciation rate (e.g., 0.1 for 10%).
     * @param currentYear An int representing the current year.
     * @return double representing the computed current price.
     */
    public double computeCurrentPrice(double depRate, int currentYear) {
        int age = currentYear - this.year;
        if (age < 0) {
            age = 0;
        }
        if (depRate < 0){
            depRate = 0;
        }
        if (depRate > 1){
            depRate = 1;
        }
        return price * Math.pow((1 - depRate), age);
    }

    /**
     * Overrides the default toString to return a string representation of the Vehicle object
     * in the format "vin, make, model, year, mileage, price, color".
     *
     * @return String representing the vehicle's details.
     */
    @Override
    public String toString() {
        return "VIN: " + getVin() + ", Make: " + getMake() + ", Model: " + getModel() +
                ", Year: " + getYear() + ", Mileage: " + getMileage() +
                ", Price: $" + String.format("%.2f", getPrice()) + ", Color: " + getColor();
    }
}
