package com.waseemh.sherlock.exceptions;

/**
 * Wrapper for both checked and unchecked exceptions thrown during execution.
 */

public class SherlockWrappedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SherlockWrappedException(String msg) {
		super(msg);
	}

	public SherlockWrappedException(Throwable e) {
		super(e);
	}

}
