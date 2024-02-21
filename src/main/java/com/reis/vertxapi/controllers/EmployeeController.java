package com.reis.vertxapi.controllers;

import java.util.List;

import com.reis.vertxapi.entities.Employee;
import com.reis.vertxapi.services.EmployeeService;

import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class EmployeeController {
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	public void listAllEmployees(RoutingContext routingContext) {
		employeeService.getAllEmployees(ar -> {
			if (ar.succeeded()) {
				List<Employee> employees = ar.result();
				routingContext.response().putHeader("content-type", "application/json")
						.end(Json.encodePrettily(employees));
			} else {
				routingContext.response().setStatusCode(500).end(ar.cause().getMessage());
			}
		});
	}

	public void createEmployee(RoutingContext routingContext) {
		Employee employee = Json.decodeValue(routingContext.getBodyAsString(), Employee.class);
		employeeService.createEmployee(employee, ar -> {
			if (ar.succeeded()) {
				routingContext.response().setStatusCode(201).end();
			} else {
				routingContext.response().setStatusCode(500).end(ar.cause().getMessage());
			}
		});
	}

	public void getEmployee(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		employeeService.getEmployeeById(id, ar -> {
			if (ar.succeeded()) {
				Employee employee = ar.result();
				if (employee != null) {
					routingContext.response().putHeader("content-type", "application/json")
							.end(Json.encodePrettily(employee));
				} else {
					routingContext.response().setStatusCode(404).end("Employee not found");
				}
			} else {
				routingContext.response().setStatusCode(500).end(ar.cause().getMessage());
			}
		});
	}

	public void updateEmployee(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		Employee employee = Json.decodeValue(routingContext.getBodyAsString(), Employee.class);
		employee.setId(id);
		employeeService.createEmployee(employee, ar -> {
			if (ar.succeeded()) {
				routingContext.response().setStatusCode(200).end();
			} else {
				routingContext.response().setStatusCode(500).end(ar.cause().getMessage());
			}
		});
		routingContext.response().end("Employee updated");
	}

	public void deleteEmployee(RoutingContext routingContext) {
		String id = routingContext.request().getParam("id");
		employeeService.deleteEmployee(id, ar -> {
			if (ar.succeeded()) {
				routingContext.response().setStatusCode(204).end();
			} else {
				routingContext.response().setStatusCode(500).end(ar.cause().getMessage());
			}
		});
		routingContext.response().end("Employee deleted");
	}
}
