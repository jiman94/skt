package oss.member.exception;

/**
 * Created by jaceshim on 2017. 3. 26..
 */
public class DuplicateMemberEmailException extends RuntimeException {
	public DuplicateMemberEmailException(String message) {
		super(message);
	}
}
