package model;

public class Customer {
    private String name;
    private String contactNumber;
    private String drivingLicence;

    // Constructor
    public Customer(String name, String contactNumber, String drivingLicence) {
        this.name = name;
        this.contactNumber = contactNumber;
        this.drivingLicence = drivingLicence;
    }

    // Getters and Setters (optional, if needed)

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDrivingLicence() {
        return drivingLicence;
    }

    public void setDrivingLicence(String drivingLicence) {
        this.drivingLicence = drivingLicence;
    }
}
