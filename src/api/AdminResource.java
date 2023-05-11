package api;

import Service.CustomerService;
import Service.ReservationService;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static AdminResource adminResource;

    public static CustomerService customerService = CustomerService.getInstance();
    public static ReservationService reservationService = ReservationService.getInstance();
    private AdminResource(){
    }
    public static AdminResource getInstance() {
        if(adminResource == null){
            adminResource = new AdminResource();
        }
        return adminResource;
    }
    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }
    public void addRoom(List<IRoom> rooms){
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }
    public Collection<IRoom> getAllRooms(){
        return reservationService.getAllRooms();
    }
    public Collection<Customer> getAllCustomers(){return customerService.getAllCustomers();}
    public Collection<Reservation> getAllReservations(){return reservationService.getAllReservations();}
}
