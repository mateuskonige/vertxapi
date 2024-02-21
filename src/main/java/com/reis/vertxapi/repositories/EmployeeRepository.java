package com.reis.vertxapi.repositories;

import com.reis.vertxapi.entities.Employee;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class EmployeeRepository {
	private final MongoClient mongoClient;

	public EmployeeRepository(Vertx vertx) {
		JsonObject config = new JsonObject().put("connection_string", "mongodb://localhost:27017").put("db_name",
				"test.employees");

		this.mongoClient = MongoClient.createShared(vertx, config);
	}

	public void create(Employee employee, Handler<AsyncResult<Void>> resultHandler) {
		JsonObject document = JsonObject.mapFrom(employee);
		mongoClient.insert("employees", document, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void read(String id, Handler<AsyncResult<Employee>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", id);
		mongoClient.findOne("employees", query, null, ar -> {
			if (ar.succeeded()) {
				JsonObject json = ar.result();
				if (json != null) {
					Employee employee = json.mapTo(Employee.class);
					resultHandler.handle(Future.succeededFuture(employee));
				} else {
					resultHandler.handle(Future.failedFuture("Employee not found"));
				}
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void update(Employee employee, Handler<AsyncResult<Void>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", employee.getId());
		JsonObject update = JsonObject.mapFrom(employee);
		mongoClient.updateCollection("employees", query, update, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}

	public void delete(String id, Handler<AsyncResult<Void>> resultHandler) {
		JsonObject query = new JsonObject().put("_id", id);
		mongoClient.removeDocument("employees", query, ar -> {
			if (ar.succeeded()) {
				resultHandler.handle(Future.succeededFuture());
			} else {
				resultHandler.handle(Future.failedFuture(ar.cause()));
			}
		});
	}
}
