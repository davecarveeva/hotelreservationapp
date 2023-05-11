package api;
import api.AdminMenu;
import api.AdminResource;
import api.HotelResource;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
public class MainMenu {
    public static MainMenu mainMenuInstance = null;
    private static Collection<IRoom> availableRooms = new HashSet<>();
    private static Collection<Customer> allCustomers = new HashSet<>();
    private static Collection<Reservation> reservationCollection = new HashSet<>();
    private static AdminResource adminResource = AdminResource.getInstance();
    private static HotelResource hotelResource = HotelResource.getInstance();

    private MainMenu() {
    }

    public static void launchMainMenu() {
        boolean keepRunning = true;
        int optionSelect;
        while (keepRunning) {
            try {
                System.out.println("Welcome to the Hotel Reservation Menu");
                System.out.println("1. Find and Reserve a Room");
                System.out.println("2. See My Reservations");
                System.out.println("3. Create an Account");
                System.out.println("4. Admin Menu");
                System.out.println("5. Exit");
                System.out.println("----------------------------");
                System.out.println("Please Select an Option.");

                Scanner optionScan = new Scanner(System.in);
                optionSelect = optionScan.nextInt();
                switch (optionSelect) {
                    case 1:
                        findReserveRoom();
                        break;
                    case 2:
                        seeReservations();
                        break;
                    case 3:
                        String email = getEmail();
                        createCustomer(email);
                        break;
                    case 4:
                        AdminMenu.launchAdminMenu();
                        break;
                    case 5:
                        System.out.println("Thank you for visiting our Reservation Service");
                        keepRunning = false;
                        break;
                    default:
                        System.out.println("Please select a option from the list.");
                }
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        return;
    }

    public static void seeReservations() {
        String customerEmail = getEmail();
        reservationCollection = hotelResource.getCustomerReservation(customerEmail);
        if (reservationCollection.isEmpty()) {
            System.out.println("There are no existing reservations under that email.");
            System.out.println("Create new customer?");
            Scanner scanner = new Scanner(System.in);
            String ysReg = "[ynYN]";
            Pattern pattern = Pattern.compile(ysReg);
            String input = scanner.nextLine().toUpperCase();
            while (!pattern.matcher(input).matches()) {
                System.out.println("Please answer Yes(Y) or No(N)");
                input = scanner.nextLine().toUpperCase();
            }
            if (input.charAt(0) == 'Y') {
                createCustomer(customerEmail);
                findReserveRoom();
            } else launchMainMenu();
        } else {
            for (Reservation currentReservation : reservationCollection) {
                System.out.println(currentReservation.toString());
            }
        }
    }

    public static void findReserveRoom() {
        String yesNo;
        String email = null;
        String ynRegex = "[ynYN]";
        Pattern pattern = Pattern.compile(ynRegex);
        Date checkInDate = getDate("Check In");
        Date checkOutDate = getDate("Check Out");
        availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        for (IRoom room : availableRooms) {
            System.out.println(room.toString());
        }
        Scanner yesNoScan = new Scanner(System.in);
        try {
            System.out.println("Would you like to book one of the available rooms? (Y/N)");
            yesNo = yesNoScan.nextLine().toUpperCase();
            if (yesNo.equals("Y")) {
                System.out.println("Do you have an account with us?");
                yesNo = yesNoScan.nextLine().toUpperCase();
                if (!pattern.matcher(yesNo).matches()) {
                    throw new IllegalArgumentException();
                } else if (yesNo.equals("N")) {
                    email = getEmail();
                    createCustomer(email);
                    callBookRoom(checkInDate, checkOutDate, email);
                } else if (yesNo.equals("Y")) {
                    email = getEmail();
                    Customer customer = adminResource.getCustomer(email);
                    if (customer.equals(null)) {
                        System.out.println("There are no customers with this address");
                        createCustomer(email);
                        callBookRoom(checkInDate, checkOutDate, email);
                    }
                    callBookRoom(checkInDate,checkOutDate, email);
                }
            } else launchMainMenu();
        } catch (Exception ex) {
            System.out.println("Invalid Input");
        }
    }
    public static void callBookRoom(Date sampleInDate, Date sampleOutDate, String email) {
        String roomNumber;
        IRoom room;
        try {
            roomNumber = getRoomNumber();
            room = hotelResource.getRoom(roomNumber);
            hotelResource.bookARoom(email, room, sampleInDate, sampleOutDate);
            System.out.println("Room successfully booked!");
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }

    public static void createCustomer(String email){
        String firstName = getFirstLast("First Name");
        String lastName = getFirstLast("Last Name");
        hotelResource.createACustomer(email, firstName, lastName);
        System.out.println("Customer Added!");
    }
    public static String getRoomNumber(){
        boolean keepRunning = true;
        String roomID = null;
        String roomRegex = "[0-9]+";
        Pattern pattern = Pattern.compile(roomRegex);
        while(keepRunning){
            try {
                Scanner roomScan = new Scanner(System.in);
                System.out.println("Please enter desired room number. (100-1000)");
                roomID = roomScan.nextLine();
                if(!pattern.matcher(roomID).matches() || roomExists(roomID) == false){
                    System.out.println("Please enter a valid roomNumber");
                   throw new IllegalArgumentException();
                } else { keepRunning = false; };
            } catch (Exception ex) {
                System.out.println("Invalid Input");
            }
        }
        return roomID;
    }

    public static String getFirstLast(String nameType){
        boolean keepRunning = true;
        String name = null;
        String nameRegex = "[A-Z]+([ '-]*[a-zA-Z]+)*";
        Pattern pattern = Pattern.compile(nameRegex);

        while(keepRunning){
            try {
                Scanner nameScan = new Scanner(System.in);
                System.out.println(nameType);
                name = nameScan.nextLine();

                if(!pattern.matcher(name).matches()){
                    throw new IllegalArgumentException();
                }
                else keepRunning = false;
            } catch (Exception ex){
                System.out.println("Invalid Input");
                throw new IllegalArgumentException();
            }
        }
        return name;
    }
    public static String getEmail(){
         boolean keepRunning = true;
         String email = null;
         String emailRegex = "^(.+)@(.+)$";
         Pattern pattern = Pattern.compile(emailRegex);
        while (keepRunning) {
            try {
                Scanner emailScan = new Scanner(System.in);
                System.out.println("Please enter you email address.");
                email = emailScan.nextLine().toLowerCase();
                if(!pattern.matcher(email).matches()){
                    System.out.println("Please enter a valid email.");
                    throw new IllegalArgumentException("Invalid input");
                } else { keepRunning = false; }
            } catch (Exception ex) {
                System.out.println("Invalid input");
            }
        }
        return email;
    }
    public static Date getDate(String dateType){
        Date validateDate = null;
        boolean keepRunning = true;
        DateFormat dateFormat = new SimpleDateFormat("mm/dd/yyyy");
        String date;

        while (keepRunning) {
            System.out.println("Enter" + dateType + "\nDate: (mm/dd/yyyy)");
            try {
                Scanner scanDate = new Scanner(System.in);
                date = scanDate.nextLine();
                validateDate = dateFormat.parse(date);
            } catch (Exception ex) {
                System.out.println("Invalid date input");
                continue;
            }
            keepRunning = false;
        }
        return validateDate;
    }
    private static boolean roomExists(String input){
        if(hotelResource.getRoom(input) == null){
            return false;
        }
        return true;
    }

}
