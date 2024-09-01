package model;
public class Car {
    private String maker;
    private String model;
    private int year;
    private String licensePlate;
    private int rentPerDay;
    private boolean isAvailable;

    // Constructor
    public Car(String maker,String model, int year, String licensePlate, int rentPerDay, boolean isAvailable) {
        this.maker=maker;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.rentPerDay = rentPerDay;
        this.isAvailable = isAvailable;
    }

    // Getters and Setters (optional, if needed)

    public void setMaker(String maker){
        this.maker=maker;
    }
    public String getMaker(){
        return maker;
    }
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(int rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}

