package com.termoncs.demo.model;

import lombok.*;
import javax.persistence.*;

@Builder @With @Data @NoArgsConstructor @AllArgsConstructor // lomboq decorators
@Entity 													// needed by Jpa to identify an entity under control
public class Person {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 	// required by jpa
	private int id;
	private String name;
	private int age;
	@Enumerated(EnumType.STRING) 							// required by jpa
	private MartialStatus status;
}
