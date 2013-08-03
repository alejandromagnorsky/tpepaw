package ar.edu.itba.it.paw;

public class DBException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
