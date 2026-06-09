package odontograme.patientrecords.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Id not found")  // 400
public class PracticeIdNotFoundException extends RuntimeException {
}
