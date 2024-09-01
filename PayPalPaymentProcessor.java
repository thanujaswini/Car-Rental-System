package model.Payment;

public class PayPalPaymentProcessor implements PaymentProcessor {
    @Override
    public boolean processPayment(double amount) {
        // PayPal payment logic here
        return true; // Assume payment is successful
    }

    @Override
    public String getPaymentMethod() {
        return "PayPal";
    }
}
