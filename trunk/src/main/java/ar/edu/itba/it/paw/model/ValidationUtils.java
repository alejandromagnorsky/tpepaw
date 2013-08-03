package ar.edu.itba.it.paw.model;

public class ValidationUtils {
	
	public static boolean validateLength(String str, int min, int max){
		return str != null && str.length() >= min && str.length() <= max;
	}
	
	public static boolean validateMaxLength(String str, int max){
		return str == null || str.length() <= max;
	}
}
