package com.termoncs.demo.service;

import com.termoncs.demo.model.MartialStatus;
import com.termoncs.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

// ------------------------- Jdbc implementation ---------------------------------
public class PersonJdbcService implements IPersonService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public void initialise() {
		this.deleteAll();

		this.addPerson( Person.builder().name("Joe").age(23).status(MartialStatus.SINGLE).build() );
		this.addPerson( Person.builder().name("Fred").age(41).status(MartialStatus.MARRIED).build() );
		this.addPerson( Person.builder().name("Mary").age(70).status(MartialStatus.WIDOWED).build() );
	}

	public long getCount() {
		return jdbcTemplate.queryForObject("select count(*) from person",new MapSqlParameterSource(), Long.class).longValue();
	}


	public List<Person> getAll() {
		return jdbcTemplate.query("select id, name, age, status from person", (rs,row) ->
				Person.builder()
						.id(rs.getInt("id") )
						.name( rs.getString("name") )
						.age( rs.getInt("age") )
						.status(MartialStatus.valueOf(rs.getString("status")))
						.build()
		);
	}

	public List<Person> getByStatus(MartialStatus status) {
		return jdbcTemplate.query("select * from person where status = :status",
				new MapSqlParameterSource("status", status.toString()), (rs, row) ->
						Person.builder()
								.id(rs.getInt("id"))
								.name(rs.getString("name"))
								.age(rs.getInt("age"))
								.status(MartialStatus.valueOf(rs.getString("status")))
								.build()
		);
	}

	public Person getById(int id) {
		return jdbcTemplate.queryForObject("select * from person where id = :id",
				new MapSqlParameterSource("id", id), (rs, row) ->
						Person.builder()
						.id(rs.getInt("id"))
						.name(rs.getString("name"))
						.age(rs.getInt("age"))
						.status(MartialStatus.valueOf(rs.getString("status")))
						.build()
		);
	}

	public Person addPerson(Person p) {
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("name", p.getName())
				.addValue("age", p.getAge())
				.addValue("status", p.getStatus().toString());

		// keyholder used to retrieve auto-incremented PK on insert
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(
				"insert into person (name, age, status) values " +
						"( :name, :age, :status)",
				params,
				keyHolder
		);
		return p.withId( keyHolder.getKey().intValue() );
	}

	public Person updatePerson(int id, Person updated) {
		// ensure id of updated person matches the id used to find the person
		var p = updated.withId(id);

		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("id", p.getId())
				.addValue("name", p.getName())
				.addValue("age", p.getAge())
				.addValue("status", p.getStatus().toString());

		int status = jdbcTemplate.update("update person set name=:name, age=:age, status=:status where id = :id", params );
		return 	(status == 0) ? null : p;
	}

	public Person deletePerson(int id) {
		// check person exists
		var p = getById(id);
		if (p == null) { return  null; }

		// delete person
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
		int status = jdbcTemplate.update("delete from person where id = :id", params );

		// check that deletion worked. if not return null otherwise return the person deleted
		return (status == 0) ? null : p;
	}

	public long deleteAll() {
		return jdbcTemplate.update("delete from person",  new MapSqlParameterSource());
	}

}
