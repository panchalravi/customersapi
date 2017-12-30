package com.ca.restdemo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Message", description = "A message containing more info why the operation failed")
public class Message {
	@ApiModelProperty(value = "The message itself", readOnly = true)
	private String message;

	
	public Message(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
