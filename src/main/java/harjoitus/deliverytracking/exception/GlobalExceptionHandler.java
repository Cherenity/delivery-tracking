
package harjoitus.deliverytracking.exception;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Käsittelee controller-luokissa syntyvät IllegalArgumentException-poikkeukset.
 * Spring ohjaa poikkeukset automaattisesti tähän luokkaan, joka välittää
 * virheviestin error.html-sivulle ja palauttaa HTTP 400 -virhekoodin.
 * https://medium.com/@roshanfarakate/global-exception-handling-in-spring-boot-712593159a26
 */


@ControllerAdvice
public class GlobalExceptionHandler {

  // Käsittelee virheelliset pyynnöt (400)
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleError(IllegalArgumentException e, Model model) {

    model.addAttribute("errorMessage", e.getMessage());
    model.addAttribute("status", 400);

    return "error";
  }

  // Käsittelee tilanteet, joissa tietuetta ei löydy (404)
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String handleNotFound(NoSuchElementException e, Model model){
    model.addAttribute("errorMessage", e.getMessage());
    model.addAttribute("status", 404);

    return "error";

  }

}
