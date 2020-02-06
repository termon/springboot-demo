package com.termoncs.demo.service;

import com.termoncs.demo.model.MartialStatus;
import com.termoncs.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

// ------------------------- JPA implementation ---------------------------------
@Repository
interface PersonRepository extends JpaRepository<Person, Integer> {
    // custom repository query
    public List<Person> getByStatus(MartialStatus status);
}

public class PersonJpaService implements IPersonService {

    @Autowired
    private PersonRepository repository;

    public void initialise() {
        this.deleteAll();

        this.addPerson( Person.builder().name("Joe").age(23).status(MartialStatus.SINGLE).build());
        this.addPerson( Person.builder().name("Fred").age(41).status(MartialStatus.MARRIED).build());
        this.addPerson( Person.builder().name("Mary").age(70).status(MartialStatus.WIDOWED).build());
    }

    public Collection<Person> getAll() {
        return repository.findAll();
    }

    public long getCount() {
        return repository.count();
    }

    public Person getById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Collection<Person> getByStatus(MartialStatus status) {
        return repository.getByStatus(status);
    }

    public Person addPerson(Person p) {
        return repository.saveAndFlush(p);
    }

    public Person updatePerson(int id, Person p) {
        var old = getById(id);
        if (old != null) {
            return repository.saveAndFlush(p);
        }
        return null;
    }

    public Person deletePerson(int id) {
        var p = getById(id);
        if (p != null) {
            repository.delete(p);
        }
        return p;
    }

    public long deleteAll() {
        var count = repository.count();
        repository.deleteAll();
        return count;
    }

}
