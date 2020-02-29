package utils;

import java.util.ArrayDeque;
import java.util.Queue;

public class ErrorHandler {
	
	private static final Object LOCK = new Object();
	
	private static Queue<String> errors = new ArrayDeque<String>();
	
	public static void addError(Exception error) {
		ErrorHandler.addError(error.getMessage());
	}
	
	public static void addError(String error) {
		
		synchronized(LOCK) {
			ErrorHandler.errors.add("ERR: "+error);
		}
		
	}
	
	public static boolean hasNextError(){
		
		synchronized(LOCK) {
			return !errors.isEmpty();
		}
		
	}
	
	public static String consumeNextError(){
		
		synchronized(LOCK) {
			return errors.remove();
		}
		
	}

}
