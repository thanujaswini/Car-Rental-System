import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import model.Car;
import model.Customer;
import model.Reservation;
import model.Payment.CreditCardPaymentProcessor;
import model.Payment.PayPalPaymentProcessor;
import model.Payment.PaymentProcessor;

public class CarRentalAppSwing {

    private static RentalSystem rentalSystem = new RentalSystem();
    private static LocalDate startDate, endDate;

    public static void main(String[] args) {
        initializeSystem();

        JFrame frame = new JFrame("WELCOME TO CARZZ");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        // Title Label
        JLabel titleLabel = new JLabel("WELCOME TO CARZZ!!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLUE);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Create Panels
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel nameLabel = new JLabel("Customer Name:");
        JTextField nameField = new JTextField(20);

        JLabel phoneLabel = new JLabel("Customer Phone (10 digits):");
        JTextField phoneField = new JTextField(20);

        JLabel licenseLabel = new JLabel("Driving License:");
        JTextField licenseField = new JTextField(20);

        JLabel carLabel = new JLabel("Select Car:");
        String[] cars = {"Toyota Glenza", "Honda Civic", "Ford Mustang"};
        JComboBox<String> carComboBox = new JComboBox<>(cars);

        JLabel startDateLabel = new JLabel("Enter Start Date (YYYY-MM-DD):");
        JTextField startDateField = new JTextField(10);

        JLabel endDateLabel = new JLabel("Enter End Date (YYYY-MM-DD):");
        JTextField endDateField = new JTextField(10);

        JLabel paymentLabel = new JLabel("Select Payment Method:");
        JRadioButton creditCardOption = new JRadioButton("Credit Card");
        JRadioButton paypalOption = new JRadioButton("PayPal");
        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(creditCardOption);
        paymentGroup.add(paypalOption);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);
        inputPanel.add(licenseLabel);
        inputPanel.add(licenseField);
        inputPanel.add(carLabel);
        inputPanel.add(carComboBox);
        inputPanel.add(startDateLabel);
        inputPanel.add(startDateField);
        inputPanel.add(endDateLabel);
        inputPanel.add(endDateField);
        inputPanel.add(paymentLabel);
        inputPanel.add(creditCardOption);
        inputPanel.add(new JLabel()); // Empty label for spacing
        inputPanel.add(paypalOption);

        // Submit Button
        JButton submitButton = new JButton("Submit");
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Result Label for Feedback
        JLabel resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setForeground(Color.RED);

        // Create a panel for the submit button and result label
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(submitButton, BorderLayout.NORTH);
        buttonPanel.add(resultLabel, BorderLayout.SOUTH);

        // Create a JScrollPane and add the input panel to it
        JScrollPane scrollPane = new JScrollPane(inputPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Add the components to the frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set up event handling
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String license = licenseField.getText();
                String car = (String) carComboBox.getSelectedItem();

                // Validate phone number
                if (!phone.matches("\\d{10}")) {
                    resultLabel.setText("Invalid phone number. Must be 10 digits.");
                    return;
                }

                // Validate and parse start date
                try {
                    startDate = LocalDate.parse(startDateField.getText());
                    if (startDate.isBefore(LocalDate.now())) {
                        resultLabel.setText("Start date cannot be before today's date.");
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    resultLabel.setText("Invalid start date format.");
                    return;
                }

                // Validate and parse end date
                try {
                    endDate = LocalDate.parse(endDateField.getText());
                    if (endDate.isBefore(startDate)) {
                        resultLabel.setText("End date cannot be before start date.");
                        return;
                    }
                } catch (DateTimeParseException ex) {
                    resultLabel.setText("Invalid end date format.");
                    return;
                }

                // Validate payment method selection
                PaymentProcessor paymentProcessor;
                if (creditCardOption.isSelected()) {
                    paymentProcessor = new CreditCardPaymentProcessor();
                } else if (paypalOption.isSelected()) {
                    paymentProcessor = new PayPalPaymentProcessor();
                } else {
                    resultLabel.setText("Please select a payment method.");
                    return;
                }

                rentalSystem.setPaymentProcessor(paymentProcessor);

                // Search for car availability
                List<Car> availableCars = rentalSystem.searchCar(car.split(" ")[0], car.split(" ")[1], startDate, endDate);
                if (!availableCars.isEmpty()) {
                    Car selectedCar = availableCars.get(0); // Assuming single car selection logic for simplicity
                    Customer customer = new Customer(name, phone, license);
                    Reservation reservation = rentalSystem.makeReservation(customer, selectedCar, startDate, endDate);

                    // Process reservation and payment
                    if (reservation != null) {
                        boolean isPaymentSuccessful = rentalSystem.processPayment(reservation);
                        if (isPaymentSuccessful) {
                            resultLabel.setText("Reservation successful for " + selectedCar.getMaker() + " " + selectedCar.getModel() +
                                    ". Payment amount: $" + reservation.getTotalPrice() +
                                    ". Payment method: " + (creditCardOption.isSelected() ? "Credit Card" : "PayPal"));
                        } else {
                            resultLabel.setText("Payment failed. Reservation canceled.");
                        }
                    } else {
                        resultLabel.setText("Reservation failed. Car not available for the given dates.");
                    }
                } else {
                    resultLabel.setText("Selected car is not available for the given dates.");
                }
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    private static void initializeSystem() {
        rentalSystem.addCar(new Car("Toyota", "Glenza", 2022, "TS07124", 600, true));
        rentalSystem.addCar(new Car("Honda", "Civic", 2021, "AP07124", 700, true));
        rentalSystem.addCar(new Car("Ford", "Mustang", 2023, "MH07124", 1000, true));
    }
}
