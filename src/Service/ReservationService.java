package Service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {
    private static ReservationService reservationService;
    public Collection<Reservation> reservations = new HashSet<>();
    public static Collection<IRoom> rooms = new HashSet<>();

    private ReservationService() {}
    public static ReservationService getInstance(){
        if(reservationService == null){
            reservationService = new ReservationService();
        }
        return reservationService;
    }
    public static void addRoom(IRoom room){
        rooms.add(room);
    }
    public IRoom getARoom(String roomNumber) {
        for (IRoom room : rooms) {
            if (roomNumber.equals(room.getRoomNumber())) {
                return room;
            }
        }
        return null;
    }
    public Collection<IRoom> getAllRooms() {
        return rooms;
    }
    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation reservedRoom = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservedRoom);
        return reservedRoom;

    }
    public Collection<IRoom> findRooms(Date sampleInDate, Date sampleOutDate){
        Collection<IRoom> availableRooms = new HashSet<>();
        try {
            if(reservations.isEmpty()) {
                availableRooms=getAllRooms();
            }else for (Reservation reserved : reservations) {
                Date DBin = reserved.getCheckInDate();
                Date DBout = reserved.getCheckOutDate();
                if (DBout.after(sampleInDate) && DBout.after(sampleOutDate) && sampleInDate.before(sampleOutDate)) {
                    availableRooms.add(reserved.getRoom());
                } else if (DBin.before(sampleInDate) && DBin.before(sampleOutDate) && sampleInDate.before(sampleOutDate)) {
                    availableRooms.add(reserved.getRoom());
                } else System.out.println("Sorry, no available rooms with selected Dates");
            }
        }catch (Exception ex) {
            throw new IllegalArgumentException("No reservations fit this criteria");
        }
        return availableRooms;
    }
    public Collection<Reservation> getCustomerReservation(Customer customer){
        Collection<Reservation> custReserves = new HashSet<>();
        for (Reservation reser : reservations){
            if(reser.getCustomer().equals(customer)){
                custReserves.add(reser);
            }
        }
        return custReserves;
    }
    public void printAllReservations(){
        for(Reservation reservation : reservations){
            System.out.println(reservation.toString());
        }
    }
    public Collection<Reservation> getAllReservations(){
        return reservations;
    }
}
