package cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeats() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/seats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rows", is(9)))
                .andExpect(jsonPath("$.columns", is(9)))
                .andExpect(jsonPath("$.seats.length()", is(81)));
    }

    @Test
    void shouldPurchaseTicket() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 3,
                  "column": 4
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/purchase")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", notNullValue()))
                .andExpect(jsonPath("$.ticket.row", is(3)))
                .andExpect(jsonPath("$.ticket.column", is(4)))
                .andExpect(jsonPath("$.ticket.price", is(10)));
    }

    @Test
    void shouldReturnBadRequestWhenTicketAlreadyPurchased() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 3,
                  "column": 4
                }
                """;

        mockMvc.perform(post("/purchase")
                .contentType("application/json")
                .content(requestBody));

        // Act & Assert
        mockMvc.perform(post("/purchase")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("The ticket has been already purchased!")));
    }

    @Test
    void shouldReturnBadRequestWhenSeatIsOutOfBounds() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 15,
                  "column": 4
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/purchase")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("The number of a row or a column is out of bounds!")));
    }

    @Test
    void shouldReturnTicket() throws Exception {
        // Arrange
        String purchaseRequest = """
                {
                  "row": 3,
                  "column": 4
                }
                """;

        String purchaseResponse = mockMvc.perform(post("/purchase")
                        .contentType("application/json")
                        .content(purchaseRequest))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = purchaseResponse.split("\"token\":\"")[1].split("\"")[0];

        String returnRequest = """
                {
                  "token": "%s"
                }
                """.formatted(token);

        // Act & Assert
        mockMvc.perform(post("/return")
                        .contentType("application/json")
                        .content(returnRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticket.row", is(3)))
                .andExpect(jsonPath("$.ticket.column", is(4)))
                .andExpect(jsonPath("$.ticket.price", is(10)));
    }

    @Test
    void shouldReturnBadRequestWhenTokenIsWrong() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "token": "123e4567-e89b-12d3-a456-426614174000"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/return")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Wrong token!")));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsMissing() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/stats"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("The password is wrong!")));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsWrong() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/stats?password=wrong_password"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error", is("The password is wrong!")));
    }

    @Test
    void shouldReturnStatsWhenPasswordIsCorrect() throws Exception {
        // Arrange

        // Act & Assert
        mockMvc.perform(get("/stats?password=super_secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.income", is(0)))
                .andExpect(jsonPath("$.available", is(81)))
                .andExpect(jsonPath("$.purchased", is(0)));
    }

    @Test
    void shouldReturnUpdatedStatsAfterPurchase() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 1,
                  "column": 1
                }
                """;

        mockMvc.perform(post("/purchase")
                .contentType("application/json")
                .content(requestBody));

        // Act & Assert
        mockMvc.perform(get("/stats?password=super_secret"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.income", is(10)))
                .andExpect(jsonPath("$.available", is(80)))
                .andExpect(jsonPath("$.purchased", is(1)));
    }
}