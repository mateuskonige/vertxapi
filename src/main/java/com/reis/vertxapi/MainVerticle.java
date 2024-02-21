package com.reis.vertxapi;

import com.reis.vertxapi.controllers.EmployeeController;
import com.reis.vertxapi.repositories.EmployeeRepository;
import com.reis.vertxapi.services.EmployeeService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Promise<Void> startPromise) {

		EmployeeRepository employeeRepository = new EmployeeRepository(vertx);
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		EmployeeController employeeController = new EmployeeController(employeeService);

		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

		router.route().handler(BodyHandler.create());

		router.get("/api/employees").handler(employeeController::listAllEmployees);
		router.post("/api/employees").handler(employeeController::createEmployee);
		router.get("/api/employees/:id").handler(employeeController::getEmployee);
		router.put("/api/employees/:id").handler(employeeController::updateEmployee);
		router.delete("/api/employees/:id").handler(employeeController::deleteEmployee);

		server.requestHandler(router).listen(8080, http -> {
			if (http.succeeded()) {
				startPromise.complete();
				System.out.println("HTTP server started on port 8080");
			} else {
				startPromise.fail(http.cause());
			}
		});
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}
}
