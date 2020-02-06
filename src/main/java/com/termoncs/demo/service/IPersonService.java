package com.termoncs.demo.service;

import com.termoncs.demo.model.MartialStatus;
import com.termoncs.demo.model.Person;

import java.util.Collection;

// ====================== Service Layer ==================================
public interface IPersonService {

	// return all people
	public Collection<Person> getAll();

	// return count of people
	public long getCount();

	// used in testing to seed the service
	public  void initialise();

	// retrieve specified person or null if not found
	public Person getById(int id);

	// retrieve collection of people with specified marital status
	public Collection<Person> getByStatus(MartialStatus status);

	// add and return person with identity id field updated
	public Person addPerson(Person p);

	// update the specified person and return if updated or null if not found
	public Person updatePerson(int id, Person p);

	// delete and return person specified or null if not found
	public Person deletePerson(int id);

	// delete all people
	public long deleteAll();
}
