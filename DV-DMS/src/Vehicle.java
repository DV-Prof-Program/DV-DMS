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
     * method: Vehicle (Constructor)
     * parameters: Integer vin, String make, String model, Integer year, Integer mileage, Double price, String color
     * return: None
     * purpose: Initializes a new Vehicle object with the provided vin, make, model, year, mileage, price, and color.
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
     * method: Getters
     * parameters: None
     * return: vin, make, model, year, mileage, price, and color
     * purpose: Returns one of: vin, make, model, year, mileage, price, or color, depending on which is used.
     */
    public Integer getVin() {
        return vin;
    }
    public String getMake() {
        return make;
    }
    public String getModel() {
        return model;
    }
    public Integer getYear() {
        return year;
    }
    public Integer getMileage() {
        return mileage;
    }
    public Double getPrice() {
        return price;
    }
    public String getColor() {
        return color;
    }

    /**
     * method: Setters
     * parameters: Integer vin, String make, String model, Integer year, Integer mileage, Double price, and String color
     * return: None
     * purpose: Sets the value of: vin, make, model, year, mileage, price, or color, depending on which is used.
     */
    public void setVin(Integer vin) {
        this.vin = vin;
    }
    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setMileage(Integer mileage) {
        this.mileage = mileage;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * method: computeCurrentPrice
     * parameters:
     * Computes the current price of the vehicle using a simple depreciation formula.
     * Formula: currentPrice = originalPrice * (1 - (log(mileage)))^(age)
     */
    public double computeCurrentPrice(Integer currentYear) {
        int age = currentYear - this.year;
        if (age < 0) {
            age = 0;
        }
        double deprRate = Math.log10(   (double) this.mileage / 10);
        if (deprRate < 0) {
            deprRate = 0;
        }
        if (deprRate >= 1) {
            deprRate = 0.99;
        }
        return price * Math.pow((1 - deprRate), age);
    }

    /**
     * method: toString
     * parameters: None
     * return: String
     * purpose: Overrides the default toString to return a string representation of the Vehicle object
     * in the format "vin, make, model, year, mileage, price, color".
     */
    @Override
    public String toString() {
        return "VIN: " + getVin() + ", Make: " + getMake() + ", Model: " + getModel() +
                ", Year: " + getYear() + ", Mileage: " + getMileage() +
                ", Price: $" + String.format("%.2f", getPrice()) + ", Color: " + getColor();
    }
}
