package com.termoncs.demo.service;

import com.termoncs.demo.model.MartialStatus;
import com.termoncs.demo.model.Person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

// ------------------------- In memory Map implementation of IPersonService -------------------------------
public class PersonMapService implements IPersonService {
	// using a map makes it easy to find person by id
	private final Map<Integer, Person> repo = new HashMap<>();

	// used internally to keep a record of next available id (used when adding new person)
	private final AtomicInteger next = new AtomicInteger();

	// by default we fill the map with dummy data
	public PersonMapService() {
		this.initialise();
	}

	public void initialise() {
		this.deleteAll();

		// use builder pattern (provided by lomboq @builder) to create a person and add to repo
		this.addPerson( Person.builder().name("Joe").age(23).status(MartialStatus.SINGLE).build());
		this.addPerson( Person.builder().name("Fred").age(41).status(MartialStatus.MARRIED).build());
		this.addPerson( Person.builder().name("Mary").age(70).status(MartialStatus.WIDOWED).build());
	}

	public Collection<Person> getAll() { return this.repo.values(); }

	public long getCount() { return repo.size(); }

	public Person getById(int id) { return this.repo.get(id); }

	public Collection<Person> getByStatus(MartialStatus status) {
		// using java streams api to perform query
		return this.repo.values() 						// use values collection from map (people)
				.stream()								// convert to a stream
				.filter(p -> p.getStatus() == status)	// filter stream elements using lambda function
				.collect(Collectors.toList());			// collect filter results and covert to a list
	}

	public Person addPerson(Person p) {
		// get next available id
		var id = next.incrementAndGet();
		// use the lomboq wither pattern to create a new person (from p) with an updated id - promotes concept of immutability
		this.repo.put(id, p.withId(id));

		// return newly added person using service method
		return this.getById(id);
	}

	public Person updatePerson(int id, Person p) {
		// ensure id of updated person matches the id used to find the person
		var up = p.withId(id);

		// replace entry with new updated person
		this.repo.replace(id, up);

		// return updated person
		return this.getById(id);
	}

	public Person deletePerson(int id) { return repo.remove(id); }

	public long deleteAll() {
		var count = this.repo.size();
		this.repo.clear();
		return count;
	}
}
