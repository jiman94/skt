package oss.member.exception;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public class NotMatchPasswordException extends RuntimeException {
	public NotMatchPasswordException(String message) {
		super(message);
	}
}
