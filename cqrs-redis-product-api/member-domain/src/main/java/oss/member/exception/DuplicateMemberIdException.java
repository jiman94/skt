package oss.member.exception;

/**
 * Created by jaceshim on 2017. 3. 23..
 */
public class DuplicateMemberIdException extends RuntimeException {

	public DuplicateMemberIdException(String message) {
		super(message);
	}
}
