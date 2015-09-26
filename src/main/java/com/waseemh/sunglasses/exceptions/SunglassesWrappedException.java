package com.waseemh.sunglasses.exceptions;

public class SunglassesWrappedException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SunglassesWrappedException(String msg) {
		super(msg);
	}

	public SunglassesWrappedException(Throwable e) {
		super(e);
	}

}
