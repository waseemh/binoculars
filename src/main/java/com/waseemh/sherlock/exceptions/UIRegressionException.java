package com.waseemh.sherlock.exceptions;

/**
 * An AssertionError extension for defining UI regression errors.
 */
public class UIRegressionException extends AssertionError{

	private static final long serialVersionUID = 1L;

	public UIRegressionException(String message) {
		super(message);
	}
	
}
