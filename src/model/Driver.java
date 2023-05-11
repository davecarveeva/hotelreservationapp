package model;

public class Driver {
    public static void main(String[] args){
        Customer customer = new Customer("Dave", "Carron", "dcarron11@gmail.com");
        System.out.println(customer);
        Room room1 = new Room("100", 90.0, RoomType.SINGLE);
        System.out.println(room1);
    }
}
