package cinema;

import java.util.List;

public class TheaterSeatsLayout {
    private int rows;
    private int columns;
    private List<Seats> seats;

    public TheaterSeatsLayout() {
    }

    public TheaterSeatsLayout(int rows, int columns, List<Seats> seats) {
        this.rows = rows;
        this.columns = columns;
        this.seats = seats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public List<Seats> getSeats() {
        return seats;
    }

    public void setSeats(List<Seats> seats) {
        this.seats = seats;
    }
}
