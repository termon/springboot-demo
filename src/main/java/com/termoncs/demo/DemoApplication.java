package com.termoncs.demo;

import com.termoncs.demo.service.IPersonService;
import com.termoncs.demo.service.PersonMapService;
import com.termoncs.demo.service.PersonJdbcService;
import com.termoncs.demo.service.PersonJpaService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


// ============================ Application Layer =================================
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// bean to provide required instance of IPersonService used in dependency injection in controller
	// three versions are PersonMapService(), PersonJdbcService() and PersonJpaService()
	// when using Jdbc or Jpa implementations enable/disable configuration options in application.properties
	@Bean
	IPersonService service() {
		return new PersonMapService();
	}

}
