package com.zoley;

import com.zoley.entity.Employee;
import com.zoley.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ReggieSystemApplicationTests {
	@Autowired
	private EmployeeService employeeService;
	@Test
	void contextLoads() {
		List<Employee> list = employeeService.list();
		System.out.println(list);
	}

}
