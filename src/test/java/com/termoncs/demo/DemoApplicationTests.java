package com.termoncs.demo;

import com.termoncs.demo.service.IPersonService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class DemoApplicationTests {

	@Autowired
    IPersonService service;

	@Test
	void testGetAllPeople() {

		var results = service.getAll();

		Assert.assertNotNull(results);
		Assert.assertEquals(3, results.size());
	}

}
