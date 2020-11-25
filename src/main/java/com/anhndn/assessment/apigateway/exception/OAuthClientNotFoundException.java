package com.anhndn.assessment.apigateway.exception;

public class OAuthClientNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 8509353534379085171L;
	
	private static final String message = "OAuth Client was not found";

    public OAuthClientNotFoundException(){
        super(message);
    }
    
    public OAuthClientNotFoundException(String message) {
    	super(message);
    }
    
}
