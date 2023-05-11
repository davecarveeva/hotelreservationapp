package model;

public class FreeRoom extends Room{
    boolean isFree = true;
    public FreeRoom(String roomNumber, double price, RoomType roomType) {
        super(roomNumber, price, roomType);
        this.price = 0.0;

    }
}
