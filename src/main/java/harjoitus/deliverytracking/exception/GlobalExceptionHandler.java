package harjoitus.deliverytracking.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  // Käsittelee kaikkien controllerien IllegalArgumentException-virheet
  @ExceptionHandler(IllegalArgumentException.class)
  public String handleError(IllegalArgumentException e, Model model) {

    model.addAttribute("errorMessage", e.getMessage());

    return "error";
  }
}
