package model.Payment;

public interface PaymentProcessor {
    boolean processPayment(double amount);
    String getPaymentMethod(); // Add this method
}
