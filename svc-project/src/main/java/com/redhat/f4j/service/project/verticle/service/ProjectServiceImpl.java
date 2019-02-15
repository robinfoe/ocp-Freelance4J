package com.redhat.f4j.service.project.verticle.service;

import java.util.List;
import java.util.stream.Collectors;

import com.redhat.f4j.service.project.verticle.dto.WrapFilter;
import com.redhat.f4j.service.project.verticle.dto.WrapProject;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

public class ProjectServiceImpl implements ProjectService {

    private MongoClient client;

    public ProjectServiceImpl(Vertx vertx, JsonObject config, MongoClient client) {
        this.client = client;
    }

    @Override
    public void ping(Handler<AsyncResult<String>> resultHandler) {
        resultHandler.handle(Future.succeededFuture("OK"));
    }


    @Override
    public void addProject(WrapProject project, Handler<AsyncResult<String>> resulthandler) {
        JsonObject document = project.toJson();
        document.put("_id", project.getId());
        client.save(ProjectService.TABLE_NAME, document, resulthandler);
    }
    
    @Override
    public void getProjects(WrapFilter filter, Handler<AsyncResult<List<WrapProject>>> resulthandler) {

        JsonObject query = new JsonObject();

        if(filter.isApplicable())
            query.put(filter.getLabel(), filter.getValue());
        

        client.find(ProjectService.TABLE_NAME, query, ar -> {
            if (ar.succeeded()) {
                List<WrapProject> projects = ar.result().stream()
                                           .map(json -> new WrapProject(json))
                                           .collect(Collectors.toList());
                resulthandler.handle(Future.succeededFuture(projects));
            } else {
                resulthandler.handle(Future.failedFuture(ar.cause()));
            }
        });
    }
    
}