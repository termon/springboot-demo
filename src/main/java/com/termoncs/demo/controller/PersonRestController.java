package com.termoncs.demo.controller;

import com.termoncs.demo.model.MartialStatus;
import com.termoncs.demo.model.Person;
import com.termoncs.demo.service.IPersonService;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

// ======================= Rest API Controller Layer ============================
@RestController()
class PersonRestController {

	private IPersonService service;

	// constructor injection will initialise the service using the service() bean
	public PersonRestController(IPersonService service) {
		this.service = service;
	}

	// generic technique to run seed method on service initialisation - only used in development
	@Bean
	InitializingBean initialise() {
		return () -> {
			service.initialise();
		};
	}

	// alternatively create a post endpoint that seeds the database when called
	@PostMapping("/people/seed")
	public Collection<Person> seedPeople() {
		service.initialise();
		return service.getAll();
	}

	@GetMapping("/people")
	public Collection<Person> getAllPeople(@RequestParam(required=false) String status) {
		// as param is optional we check if status is null and call appropriate service method
		return status != null ?
				service.getByStatus(MartialStatus.valueOf(status)) :
				service.getAll();
	}

	// here we demonstrate the use of a ResponseEntity that wraps the result and also allows us to specify
	// the correct http response type based on whether the specified person exists
	@GetMapping("/people/{id}")
	public ResponseEntity<Person> getPerson(@PathVariable int id) {
		var person = service.getById(id);
		return person != null ?
				new ResponseEntity<>(person, HttpStatus.OK) :
				new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}

	@PostMapping("/people")
	public ResponseEntity<Person> createPerson(@RequestBody Person resource) {
		var person =  service.addPerson(resource);
		return person != null ?
				new ResponseEntity<>(person, HttpStatus.CREATED) :
				new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/people/{id}")
	public  ResponseEntity<Person> updatePerson(@PathVariable int id, @RequestBody Person resource) {
		var person = service.updatePerson(id, resource);
		return person != null ?
				new ResponseEntity<>(person, HttpStatus.OK) :
				new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/people/{id}")
	public ResponseEntity<Person> delete(@PathVariable int id) {
		var person =  service.deletePerson(id);
		return person != null ?
				new ResponseEntity<>(person, HttpStatus.OK) :
				new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
	}

}
