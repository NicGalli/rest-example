package com.examples;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.examples.model.Employee;
import com.examples.repository.InMemoryEmployeeRepository;

class InMemoryEmployeeRepositoryTest {

	InMemoryEmployeeRepository repository;
	private Map<String, Employee> map;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {}

	@BeforeEach
	void setUp() throws Exception {
		map = new HashMap<>();
		repository = new InMemoryEmployeeRepository(map);
		map.clear();
	}

	@AfterEach
	void tearDown() throws Exception {}

	@Test
	void testDeleteById() {
		map.put("ID1", new Employee("ID1", "Jim", 1000));
		assertThat(repository.deleteById("ID1"))
			.isEqualTo(new Employee("ID1", "Jim", 1000));
		assertThat(map.get("ID1")).isEqualTo(null);
	}

	@Test
	void testDeleteByIdWhenEmployeeNotFound() {
		assertThat(map.get("ID1")).isEqualTo(null);
		assertThat(repository.deleteById("ID1")).isEqualTo(null);
		assertThat(map.get("ID1")).isEqualTo(null);
	}
}
