package com.reis.vertxapi.services;

import java.util.List;

import com.reis.vertxapi.entities.Employee;
import com.reis.vertxapi.repositories.EmployeeRepository;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

public class EmployeeService {
	private final EmployeeRepository employeeRepository;

	public EmployeeService(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	public void getAllEmployees(Handler<AsyncResult<List<Employee>>> resultHandler) {
		employeeRepository.getAll(resultHandler);
	}

	public void createEmployee(Employee employee, Handler<AsyncResult<Void>> resultHandler) {
		employeeRepository.create(employee, resultHandler);
	}

	public void getEmployeeById(String id, Handler<AsyncResult<Employee>> resultHandler) {
		employeeRepository.getById(id, resultHandler);
	}

	public void updateEmployee(Employee employee, Handler<AsyncResult<Void>> resultHandler) {
		employeeRepository.update(employee, resultHandler);
	}

	public void deleteEmployee(String id, Handler<AsyncResult<Void>> resultHandler) {
		employeeRepository.delete(id, resultHandler);
	}
}
