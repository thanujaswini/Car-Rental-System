import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;



import model.Car;
import model.Customer;
import model.Reservation;
import model.Payment.PaymentProcessor;


public class RentalSystem {
    private Map<String,Car> cars;
    private Map<String,Reservation> reservations;
    private PaymentProcessor paymentProcessor;


    public RentalSystem(){
        cars=new HashMap<>();
        reservations=new HashMap<>();
        paymentProcessor=null;
    }
    public void setPaymentProcessor(PaymentProcessor paymentProcessor){
        this.paymentProcessor=paymentProcessor;
    }
   
    public void addCar(Car car){
        cars.put(car.getLicensePlate(),car);
    }
    private boolean isCarAvailable(Car car,LocalDate startDate,LocalDate endDate){
        for(Reservation reservation:reservations.values()){
            if(reservation.getCar().equals(car)){
                if(startDate.isBefore((reservation.getEndDate())) && endDate.isAfter(reservation.getStartDate())){
                    return false;
                }
            }
        }
        return true;
    }
     public Reservation makeReservation(Customer customer, Car car, LocalDate startDate, LocalDate endDate) {
        if (isCarAvailable(car, startDate, endDate)) {
            String reservationId = generateReservationId();
            long numberOfDays = ChronoUnit.DAYS.between(startDate, endDate) + 1; // +1 to include the end date
            double totalPrice = numberOfDays * car.getRentPerDay();
            Reservation reservation = new Reservation(reservationId, car, customer, startDate, endDate, totalPrice);
            reservations.put(reservationId, reservation);
            car.setAvailable(false);
            return reservation;
        }
        return null;
    }
    public void cancleReservation(String reservationId){
        Reservation reservation=reservations.remove(reservationId);
        if(reservation!=null){
           reservation.getCar().setAvailable(true);
        }
    }
    public void removeCar(String licensePlate){
        cars.remove(licensePlate);
    }
    public List<Car> searchCar(String maker,String model,LocalDate startDate,LocalDate endDate){

        List<Car> availableCars=new ArrayList<>();
        for(Car car:cars.values()){
            if(car.getMaker().equals(maker) && car.getModel().equals(model) && car.isAvailable()){
                if(isCarAvailable(car,startDate,endDate)){
                    availableCars.add(car);
                }
            }
        
        }
        return availableCars;
    }
    //start date > end date and end date after startdate
    
    public String generateReservationId(){
        return "RES"+UUID.randomUUID().toString().substring(0,8).toUpperCase();
    }
    public boolean processPayment(Reservation reservation){
        return paymentProcessor.processPayment(reservation.getTotalPrice());
    }




}
