package oss.order.exception;

import org.springframework.validation.Errors;

/**
 * Created by jaceshim on 2017. 3. 24..
 */
public class InvalidRequestException extends RuntimeException {
	private Errors errors;

	public InvalidRequestException(String message, Errors errors) {
		super(message);
		this.errors = errors;
	}

	public Errors getErrors() {
		return errors;
	}
}
