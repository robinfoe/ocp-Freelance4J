package com.redhat.f4j.service.project.verticle.service;

import java.util.List;

import com.redhat.f4j.service.project.verticle.dto.WrapFilter;
import com.redhat.f4j.service.project.verticle.dto.WrapProject;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;


@ProxyGen
public interface ProjectService {

    final static String ADDRESS = "project-service";
    public static final String TABLE_NAME = "PROJECT";

    static ProjectService create(Vertx vertx, JsonObject config, MongoClient client) {
        return new ProjectServiceImpl(vertx, config, client);
    }

    static ProjectService createProxy(Vertx vertx) {
        return new ProjectServiceVertxEBProxy(vertx, ADDRESS);
    }

    void getProjects(WrapFilter filter,Handler<AsyncResult<List<WrapProject>>> resulthandler);

    void addProject(WrapProject project, Handler<AsyncResult<String>> resulthandler);

    void ping(Handler<AsyncResult<String>> resultHandler);   

    
}