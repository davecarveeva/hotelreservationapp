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
    public Collection<IRoom> availableRooms = getAllRooms();

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
        if(!checkReservation(room)) {
            throw new IllegalArgumentException("Reservation conflict");
        }
        Reservation reservedRoom = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservedRoom);
        return reservedRoom;
    }
    public Collection<IRoom> findRooms(Date sampleInDate, Date sampleOutDate){

        try {
            if(sampleInDate.before(sampleOutDate)) {
                if (reservations.isEmpty()) {
                    return availableRooms;
                } else for (Reservation reserved : reservations) {
                    Date DBin = reserved.getCheckInDate();
                    Date DBout = reserved.getCheckOutDate();
                    if (DBin.before(sampleOutDate) && DBout.after(sampleOutDate)) {
                        availableRooms.remove(reserved.getRoom());
                    }
                    if (DBin.before(sampleInDate) && DBout.after(sampleInDate)) {
                        availableRooms.remove(reserved.getRoom());
                    }
                    if (DBin.after(sampleInDate) && DBout.before(sampleOutDate)) {
                        availableRooms.remove(reserved.getRoom());
                    }
                }
                return availableRooms;
            } throw new IllegalArgumentException("Dates are not sequential");
        }catch (Exception ex) {
            throw new IllegalArgumentException("No reservations fit this criteria");
        }
    }
    public boolean checkReservation(IRoom room){
        for (IRoom availableRoom : availableRooms){
            if(availableRoom.equals(room)) {
                return true;
            }
        }
        return false;
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
    public Collection<Reservation> getAllReservations(){
        return reservations;
    }
}
