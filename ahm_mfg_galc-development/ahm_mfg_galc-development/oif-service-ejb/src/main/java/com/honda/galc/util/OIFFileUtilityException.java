package com.honda.galc.util;

public class OIFFileUtilityException  extends Exception {

	private static final long serialVersionUID = 3602768476048016476L;
	
	/**
	 * Constructor
	 * <p>
	 */
	public OIFFileUtilityException() {
		super();
	}
	
	/**
	 * Constructor
	 * <p>
	 * @param s MessageID
	 */
	public OIFFileUtilityException(String s) {
		super(s);
	}

	/**
	 * <p>
	 * @return  Exception string
	 */
	public String toString() {
		return "OIFFileUtilityException";
	}
	
}
