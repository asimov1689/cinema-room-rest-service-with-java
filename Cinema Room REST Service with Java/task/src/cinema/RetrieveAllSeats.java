package cinema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RetrieveAllSeats {

    @GetMapping("/seats")
    public TheaterSeatsLayout getAllTheSeats() {
        int sumRows = 9;
        int sumCols = 9;

        List<Seats> seatList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++) {
                seatList.add(new Seats(i,j));
            }
        }

        return new TheaterSeatsLayout(sumRows, sumCols, seatList);
    }

}
