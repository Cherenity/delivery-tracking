package harjoitus.deliverytracking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Transactional
public class OrderRestControllerTests {

  @Autowired
  private MockMvc mockMvc;

  // Testaa kaikkien tilausten hakemista REST-rajapinnasta
  @Test
  public void findAllOrders() throws Exception {
    mockMvc.perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(5));
  }

  // Testaa tietyn kuljettajan tilausten hakemista
  @Test
  public void findOrdersByUserId() throws Exception {
    mockMvc.perform(get("/api/orders/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(3));
  }

  // Testaa uuden tilauksen lisäämistä REST-rajapinnan kautta
  @Test
  public void createOrder() throws Exception {
    String json = """
        {
            "customer": "Mocca & Tupla Pet Shop",
            "address": "Kissankuja 5, Helsinki",
            "deliveryDate": "2026-10-22"
        }
        """;

    mockMvc.perform(post("/api/orders")
        .contentType("application/json")
        .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.customer").value("Mocca & Tupla Pet Shop"))
        .andExpect(jsonPath("$.status").value("UNASSIGNED"));
  }
}