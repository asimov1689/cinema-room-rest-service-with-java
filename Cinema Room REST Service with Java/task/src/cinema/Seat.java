package cinema;

public class Seat {
    private int row;
    private int column;

    //Creating a constructor
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
    }

    //Create getters for the seats
    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
