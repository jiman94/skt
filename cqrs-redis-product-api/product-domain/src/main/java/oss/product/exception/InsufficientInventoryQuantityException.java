package oss.product.exception;

/**
 * Created by jaceshim on 2017. 3. 27..
 */
public class InsufficientInventoryQuantityException extends RuntimeException {
	public InsufficientInventoryQuantityException(String message) {
		super(message);
	}
}
