package com.examples.service;

import static io.restassured.RestAssured.when;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.examples.model.Employee;
import com.examples.repository.EmployeeRepository;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

	@Mock
	private EmployeeRepository repository;
	@InjectMocks
	private EmployeeServiceImpl service;

	@Test
	void testAllEmployees() {
		Employee e1 = new Employee();
		Employee e2 = new Employee();
		when(repository.findAll()).thenReturn(asList(e1, e2));
		assertThat(service.allEmployees()).containsExactly(e1, e2);
	}

	@Test
	void testGetEmployeeById() {
		Employee employee = new Employee("id23", "James", 1000);
		when(repository.findOne("id23")).thenReturn(Optional.of(employee));
		assertThat(service.getEmployeeById("id23")).isEqualTo(employee);
	}

	@Test
	void testAddEmployee() {
		when(repository.save(new Employee(null, "Jim", 3000)))
			.thenReturn(new Employee("ID23", "Jim", 3000));
		Employee savedEmployee = service
			.addEmployee(new Employee(null, "Jim", 3000));
		assertThat(savedEmployee).isEqualTo(new Employee("ID23", "Jim", 3000));
	}

	@Test
	void testThatNonExistingIdInPutThrows() {
		when(repository.findOne("ID23")).thenReturn(Optional.empty());
		assertThatExceptionOfType(NotFoundException.class).isThrownBy(() -> {
			service.replaceEmployeeById("ID23",
				new Employee(null, "Jim", 2000));
		});
	}

	@Test
	void testReplaceEmployeeById() {
		when(repository.save(new Employee("ID23", "Jim", 2000)))
			.thenReturn(new Employee("ID23", "Jim", 2000));
		when(repository.findOne("ID23"))
			.thenReturn(Optional.of(new Employee()));
		Employee employee = service.replaceEmployeeById("ID23",
			new Employee(null, "Jim", 2000));
		assertThat(employee).isEqualTo(new Employee("ID23", "Jim", 2000));
	}

	@Test
	void testThatNullEmployeeInPostThrows() {
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			service.addEmployee(null);
		});
	}

	@Test
	void testThatPostWithIdThrows() {
		assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> {
			service.addEmployee(new Employee("id", "Joe", 1000));
		});
	}

	@Test
	void testDelete() {
		when(repository.deleteById("ID1"))
			.thenReturn(new Employee("ID1", "John", 1000));
		assertThat(service.deleteEmployeeById("ID1")).isEqualTo(new Employee("ID1", "John", 1000));
		verify(repository).deleteById("ID1");
	}
	void testDeleteWhenEmployeeNotPresent() {
		when(repository.deleteById("ID1"))
			.thenReturn(null);
		assertThat(service.deleteEmployeeById("ID1")).isEqualTo(null);
		verify(repository).deleteById("ID1");
	}
}
