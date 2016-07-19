package com.waseemh.sherlock.exceptions;

/**
 * Wrapper for both checked and unchecked exceptions thrown during execution.
 */

public class BinocularsWrappedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public BinocularsWrappedException(String msg) {
		super(msg);
	}

	public BinocularsWrappedException(Throwable e) {
		super(e);
	}

	public BinocularsWrappedException(String msg, Throwable e) {
		super(msg,e);
	}

}