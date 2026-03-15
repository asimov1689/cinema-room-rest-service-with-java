package cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CinemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnSeatsEndpointResponse() throws Exception {
        // Arrange

        // Act + Assert
        mockMvc.perform(get("/seats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rows").value(9))
                .andExpect(jsonPath("$.columns").value(9))
                .andExpect(jsonPath("$.seats.length()").value(81))
                .andExpect(jsonPath("$.seats[0].row").value(1))
                .andExpect(jsonPath("$.seats[0].column").value(1))
                .andExpect(jsonPath("$.seats[0].price").value(10))
                .andExpect(jsonPath("$.seats[80].row").value(9))
                .andExpect(jsonPath("$.seats[80].column").value(9))
                .andExpect(jsonPath("$.seats[80].price").value(8));
    }

    @Test
    void shouldPurchaseTicketSuccessfully() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 3,
                  "column": 4
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.row").value(3))
                .andExpect(jsonPath("$.column").value(4))
                .andExpect(jsonPath("$.price").value(10));
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Act + Assert
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The ticket has been already purchased!"));
    }

    @Test
    void shouldReturnBadRequestWhenRowIsOutOfBounds() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 15,
                  "column": 4
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The number of a row or a column is out of bounds!"));
    }

    @Test
    void shouldReturnBadRequestWhenColumnIsOutOfBounds() throws Exception {
        // Arrange
        String requestBody = """
                {
                  "row": 3,
                  "column": 15
                }
                """;

        // Act + Assert
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("The number of a row or a column is out of bounds!"));
    }
}