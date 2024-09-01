import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import model.*;
import model.Payment.CreditCardPaymentProcessor;
import model.Payment.PayPalPaymentProcessor;
import model.Payment.PaymentProcessor;
public class App {
    public static void main(String[] args) throws Exception {
        Scanner scn=new Scanner(System.in);
        RentalSystem rentalSystem=new RentalSystem();
        System.out.println("WELCOME TO CARZZZZ!!!");
        Car car1=new Car("Toyota","Glenza",2022,"TS07124",600,true);
        Car car2=new Car("Honda","Civic",2021,"AP07124",700,true);
        Car car3=new Car("Ford","Mustang",2023,"MH07124",1000,true);
        rentalSystem.addCar(car1);
        rentalSystem.addCar(car2);
        rentalSystem.addCar(car3);

        //Make reservation
        LocalDate today = LocalDate.now();
        LocalDate startDate = null;
        LocalDate endDate = null;

        // Get start date from user
        while (startDate == null) {
            System.out.println("Enter a Start date (YYYY-MM-DD):");
            String input1 = scn.nextLine();
            try {
                startDate = LocalDate.parse(input1);
                if (startDate.isBefore(today)) {
                    System.out.println("Start date cannot be before today's date. Please enter a valid date.");
                    startDate = null; // Reset to null to prompt user again
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        // Input end date
        while (endDate == null) {
            System.out.println("Enter an End date (YYYY-MM-DD):");
            String input2 = scn.nextLine();
            try {
                endDate = LocalDate.parse(input2);
                if (endDate.isBefore(startDate)) {
                    System.out.println("End date cannot be before start date. Please enter a valid date.");
                    endDate = null; // Reset to null to prompt user again
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please try again.");
            }
        }

        List<Car> availableCars=rentalSystem.searchCar("Honda", "Civic", startDate, endDate);

         System.out.println("Select payment method:");
        System.out.println("1. Credit Card");
        System.out.println("2. PayPal");
        int choice = scn.nextInt();
        scn.nextLine(); // Consume newline

        PaymentProcessor paymentProcessor;
        switch (choice) {
            case 1:
                paymentProcessor = new CreditCardPaymentProcessor();
                break;
            case 2:
                paymentProcessor = new PayPalPaymentProcessor();
                break;
            default:
                System.out.println("Invalid choice, defaulting to Credit Card.");
                paymentProcessor = new CreditCardPaymentProcessor();
                break;
        }
        rentalSystem.setPaymentProcessor(paymentProcessor);

        System.out.println("Enter customer name:");
        String customerName = scn.nextLine();
        System.out.println("Enter customer phone:");
        String customerPhone = null;
        while (customerPhone == null) {
            System.out.println("Enter customer phone (10 digits):");
            customerPhone = scn.nextLine();
            if (customerPhone.matches("\\d{10}")) {
                break;
            } else {
                System.out.println("Invalid phone number. Please enter exactly 10 digits.");
                customerPhone = null;
            }
        }

        System.out.println("Enter drivingLicence number:");
        String drivingLicence = scn.nextLine();
        Customer customer = new Customer(customerName, customerPhone, drivingLicence);

        if(!availableCars.isEmpty()){
            Car selectedCar=availableCars.get(0);
            Reservation reservation=rentalSystem.makeReservation(customer, selectedCar, startDate, endDate);
            if(reservation!=null){
                boolean isPaymentSuccessful=rentalSystem.processPayment(reservation);
                if(isPaymentSuccessful==true){
                    System.out.println("Reservation successfull with reservtaion id: "+ reservation.getReservationId());
                }
                else{
                    System.out.println("Payment failed, Reservation cancelled"+reservation.getReservationId());
                }
            }
            else{
                System.out.println("selected car is not available for given duration");
            }
        }
        else{
            System.out.println("No car available eith given specification");
        }
    }   
}
