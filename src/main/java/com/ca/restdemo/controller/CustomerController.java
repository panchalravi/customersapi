package com.ca.restdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ca.restdemo.exception.CustomerNotFoundException;
import com.ca.restdemo.model.Customer;
import com.ca.restdemo.model.Message;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/customers")
@Api("/customers")
public class CustomerController {
	private static List<Customer> customers = new ArrayList<Customer>();
	@Autowired
    private MessageSource messageSource;
	private static int counter = 4;
	static {
		customers.add(new Customer(1, "ravi", "panchal", "ravi@mail.com"));
		customers.add(new Customer(2, "mike", "smith", "mike@mail.com"));
		customers.add(new Customer(3, "john", "fabien", "john@mail.com"));
		customers.add(new Customer(4, "paul", "adams", "paul@mail.com"));
	}

	// @GetMapping(path="/customers")
	@RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get all customers", notes = "Retrieving the collection of customers", response = Customer[].class)
	@ApiResponses({ @ApiResponse(code = 200, message = "Success", response = Customer[].class) })
	@CrossOrigin(origins = "*")
	public ResponseEntity<List<Customer>> getCustomers() {
		return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
	}

	// @PostMapping(path="/customers")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Create customer", notes = "Creating a new customer", response = Customer.class)
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "Success", response = Customer.class),
			@ApiResponse(code = 400, message = "Bad request", response = Message.class) })
	@CrossOrigin(origins = "*")
	public Customer addCustomer(
			@ApiParam(required = true, name = "customer", value = "New Customer") 
			@Valid @RequestBody Customer customer) {
		customer.setId(++counter);
		customers.add(customer);
		return customer;
	}

	// @DeleteMapping(path="/customers/{id}")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Delete customer", notes = "Deleting an existing customer")
	@ApiResponses({ 
			@ApiResponse(code = 204, message = "Success"),
			@ApiResponse(code = 404, message = "Not found", response = Message.class) })
	@CrossOrigin(origins = "*")
	public void deleteCustomer(
			@ApiParam(required = true, name = "id", value = "ID of the customer you want to delete") 
			@PathVariable int id) {
		customers = customers.stream().filter(c -> c.getId() != id).collect(Collectors.toList());
		// return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// @PutMapping(path="/customers/{id}")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update customer", notes = "Updating an existing customer")
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "Success", response = Customer.class),
			@ApiResponse(code = 400, message = "Bad request", response = Message.class),
			@ApiResponse(code = 404, message = "Not found", response = Message.class) })
	@CrossOrigin(origins = "*")
	public Customer updateCustomer(
			@ApiParam(required = true, name = "id", value = "ID of the customer you want to update", defaultValue = "0") 
			@PathVariable int id,
			@ApiParam(required = true, name = "customer", value = "Updated customer") 
			@Valid @RequestBody Customer customer) {
		Customer filteredCustomer = customers.stream().filter(c -> c.getId() == id).findFirst().get();
		if (filteredCustomer == null) {
			throw new CustomerNotFoundException(new Long(id));
		}

		filteredCustomer.setFirstName(customer.getFirstName());
		filteredCustomer.setLastName(customer.getLastName());
		filteredCustomer.setEmail(customer.getEmail());
		return filteredCustomer;
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Message handleValidationException(MethodArgumentNotValidException ex) {
		Locale locale = LocaleContextHolder.getLocale();
		String code = ex.getBindingResult().getFieldError().getDefaultMessage();
		return new Message(messageSource.getMessage(code, null, locale));
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(CustomerNotFoundException.class)
	public Message handleNotFoundException(CustomerNotFoundException ex) {
		String[] parameters = { Long.toString(ex.getId()) };
		Locale locale = LocaleContextHolder.getLocale();
		return new Message(messageSource.getMessage("exception.CustomerNotFound.description", parameters, locale));
	}

}
