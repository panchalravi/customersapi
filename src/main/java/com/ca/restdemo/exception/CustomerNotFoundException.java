package com.ca.restdemo.exception;

public class CustomerNotFoundException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	
    public CustomerNotFoundException(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
        return id;
    }

    @Override
    public String getMessage() {
        return "Task with ID '" + id + "' not found";
    }
}
