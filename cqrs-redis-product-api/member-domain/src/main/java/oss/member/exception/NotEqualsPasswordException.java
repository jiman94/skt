package oss.member.exception;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public class NotEqualsPasswordException extends RuntimeException {
	public NotEqualsPasswordException(String message) {
		super(message);
	}
}
