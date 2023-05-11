package api;

import model.*;
import Service.CustomerService;
import Service.ReservationService;

import java.util.*;
import java.util.regex.Pattern;

public class AdminMenu {
    private static Collection<IRoom> roomCollection = new HashSet<>();
    private static Collection<Customer> allCustomers = new HashSet<>();
    private static Collection<Reservation> allReservations = new HashSet<>();
    private static AdminResource adminResource = AdminResource.getInstance();
    private static HotelResource hotelResource = HotelResource.getInstance();

    private AdminMenu() {
    }

    public static void launchAdminMenu() {
        boolean keepRunning = true;
        int optionAdmin;

        while (keepRunning) {
            try {
                System.out.println("Welcome to the Hotel Admin Menu");
                System.out.println("Please make a selection");
                System.out.println("-----------------------------------------------");
                System.out.println("1. Customer List");
                System.out.println("2. Room Listings");
                System.out.println("3. Reservation List");
                System.out.println("4. Add a Room");
                System.out.println("5. Exit to Main Menu");

                Scanner optionScan = new Scanner(System.in);
                optionAdmin = optionScan.nextInt();
                switch (optionAdmin) {
                    case 1: //print customer list
                        System.out.println("You've selected Customer List");
                        allCustomers = adminResource.getAllCustomers();
                        if (allCustomers.isEmpty()) {
                            System.out.println("There are no registered customers.");
                            System.out.println("Create a customer?");
                            createACustomer();
                            continue;
                        } else for (Customer cust : allCustomers) {
                            System.out.println(cust.toString());
                            System.out.println("___________________");
                        }
                        break;

                    case 2:  //print all rooms
                        System.out.println("You've selected Room List");
                        roomCollection = adminResource.getAllRooms();
                        if (roomCollection.isEmpty()) {
                            System.out.println("There are no registered rooms");
                            System.out.println("Would you like to add rooms?");
                            addAnother();
                        } else for (IRoom room : roomCollection) {
                            System.out.println(room.toString());
                            System.out.println("_____________");
                        }
                        break;
                    case 3: //print all reservations
                        System.out.println("You've selected Reservations List");
                        allReservations = adminResource.getAllReservations();
                        if(allReservations.isEmpty()){
                            System.out.println("There are no active reservations");
                            System.out.println("Make a new reservation? (Y/N)");
                            createReservation();
                        } else for (Reservation reservation : allReservations){
                            System.out.println(reservation.toString());
                            System.out.println("_________________");
                        }
                        break;
                    case 4: //add a Room
                        createARoom();
                        break;
                    case 5: //back to Main menu
                        MainMenu.launchMainMenu();
                        break;
                    default:
                        System.out.println("Invalid input");
                        break;
                }

            } catch (Exception ex) {
                System.out.println("Invalid input");
                throw new IllegalArgumentException();
            }
        }
    }
    private static void createARoom(){
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Please select number of beds (1 or 2)");
        RoomType roomType = enterRoomType(scanner);

        System.out.println("Please enter desired room number. (100-1000)");
        String roomNumber = enterRoomNumber(scanner);

        System.out.println("Please enter room price.");
        Double roomPrice = enterRoomPrice(scanner);

        final Room room = new Room(roomNumber, roomPrice, roomType);

        adminResource.addRoom(Collections.singletonList(room));
        System.out.println("Room added successfully!");

        System.out.println("Would you like to add another room? (Y/N)");
        addAnother();
    }
    private static void createACustomer(){
        Scanner scanner = new Scanner(System.in);
        String ynReg = "[ynYN]";
        String ys;
        String email;
        Pattern pattern = Pattern.compile(ynReg);
        try {
            ys = scanner.nextLine().toUpperCase();
            if(!pattern.matcher(ys).matches()){
                System.out.println("Please enter Yes or No to create customer");
                createACustomer();
            } else if (ys.charAt(0) == 'Y'){
                email = MainMenu.getEmail();
                MainMenu.createCustomer(email);
            } else launchAdminMenu();
        } catch (Exception ex){
            throw new IllegalArgumentException("Invalid Input");
        }
    }
    private static void createReservation() {
        Scanner scanner = new Scanner(System.in);
        String ysReg = "[ynYN]";
        String input;
        Pattern pattern = Pattern.compile(ysReg);

        try {
            input = scanner.nextLine().toUpperCase();
            if(!pattern.matcher(input).matches()){
                System.out.println("Please answer Yes(Y) or No(N)");
                createReservation();
            } else if (input.charAt(0) == 'Y'){
                MainMenu.findReserveRoom();
            } else launchAdminMenu();
        } catch (Exception ex){
            throw new IllegalArgumentException("Invalid Input");
        }
    }


    private static RoomType enterRoomType(final Scanner scanner){
        RoomType roomType = RoomType.SINGLE;
        Scanner inputScan = new Scanner(System.in);
        int input;
        try {
            input = inputScan.nextInt();
            while(input != 1 && input != 2){
                System.out.println("Please select between 1 and 2 beds");
                enterRoomType(scanner);
            }
            if (input != 1) {
                roomType = RoomType.DOUBLE;
            }
            return roomType;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid input");
        }
    }
    private static String enterRoomNumber(final Scanner scanner){
        boolean keepRunning = true;
        String roomNumber = null;
        String roomRegex = "[0-9]+";
        Pattern pattern = Pattern.compile(roomRegex);
        while(keepRunning){
            try {
                roomNumber = scanner.nextLine();
                if(!pattern.matcher(roomNumber).matches()){
                    System.out.println("Please enter a valid roomNumber");
                    throw new IllegalArgumentException();
                } else {
                    if(roomExists(roomNumber) == false){
                        return roomNumber;
                    } else System.out.println("This room already exists");
                }
            } catch (Exception ex) {
                System.out.println("Invalid Input");
            }
        }
        return roomNumber;
    }
    private static Double enterRoomPrice(final Scanner scanner){
        Double roomPrice = 0.0;
        String priceReg = "([0-9]+)\\.?([0-9]+)?";
        Pattern pattern = Pattern.compile(priceReg);
        try {
            roomPrice = scanner.nextDouble();
            if(!pattern.matcher(roomPrice.toString()).matches()) {
                System.out.println("Please enter a valid three digit number");
                enterRoomPrice(scanner);
            } return roomPrice;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid input");
        }
    }
    private static void addAnother(){
    Scanner inputScan = new Scanner(System.in);
        try {
            String anotherRoom = null;
            anotherRoom = inputScan.nextLine().toUpperCase();
            while ((anotherRoom.charAt(0) != 'Y' && anotherRoom.charAt(0) != 'N')) {
                System.out.println("Please enter Yes(Y) or No(N)");
                anotherRoom = inputScan.nextLine().toUpperCase();
            }
            if(anotherRoom.charAt(0) == 'Y'){
                createARoom();
            } else if (anotherRoom.charAt(0) == 'N'){
                launchAdminMenu();
            } else addAnother();
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid Input");
        }
    }
    private static boolean roomExists(String input){
            if(hotelResource.getRoom(input) == null){
                return false;
            }
            return true;
    }
}
