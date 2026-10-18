
package harjoitus.deliverytracking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Käsittelee IllegalArgumentException-virheet
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleError(IllegalArgumentException e, Model model) {

    model.addAttribute("errorMessage", e.getMessage());
    model.addAttribute("status", 400);

    return "error";
  }
}
