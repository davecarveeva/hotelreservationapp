package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import Service.CustomerService;
import Service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private static HotelResource hotelResource;
    public static CustomerService customerService = CustomerService.getInstance();
    public static ReservationService reservationService = ReservationService.getInstance();

    private HotelResource(){
    }
    public static HotelResource getInstance() {
        if(hotelResource == null){
            hotelResource = new HotelResource();
        }
        return hotelResource;
    }
    public void createACustomer(String email, String firstName, String lastName){
        customerService.addCustomer(email, firstName, lastName);
    }
    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }
    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
        Customer customer = customerService.getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("No customer with that account exists");
        }
        return reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    }
    public Collection<Reservation> getCustomerReservation(String customerEmail){
        Customer customer = customerService.getCustomer(customerEmail);
        return reservationService.getCustomerReservation(customer);
    }
    public Collection<IRoom> findARoom(Date sampleInDate, Date sampleOutDate){
        return reservationService.findRooms(sampleInDate, sampleOutDate);
    }
}

