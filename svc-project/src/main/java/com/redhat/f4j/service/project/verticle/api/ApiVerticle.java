package com.redhat.f4j.service.project.verticle.api;

import java.util.List;

import com.redhat.f4j.base.util.UtilityBean;
import com.redhat.f4j.service.project.verticle.dto.WrapFilter;
import com.redhat.f4j.service.project.verticle.dto.WrapProject;
import com.redhat.f4j.service.project.verticle.service.ProjectService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * ApiVerticle
 */
public class ApiVerticle extends AbstractVerticle{

    public static final String PATH_PROJECTS = "projects" ;
    public static final String PATH_PROJECTS_BY_ID = PATH_PROJECTS+"/:id" ;
    public static final String PATH_PROJECTS_BY_STATUS = PATH_PROJECTS+"/status/:sts" ;


    private ProjectService service;

    public ApiVerticle(ProjectService service) {
        this.service = service;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);
        router.get(ApiVerticle.PATH_PROJECTS).handler(this::getProjects);
        router.get(ApiVerticle.PATH_PROJECTS_BY_ID).handler(this::getProjectById);
        router.get(ApiVerticle.PATH_PROJECTS_BY_STATUS).handler(this::getProjectByStatus);
        // router.get("/product/:itemId").handler(this::getProduct);
        // router.route("/product").handler(BodyHandler.create());
        // router.post("/product").handler(this::addProduct);

        //Health Checks
        router.get("/health/readiness").handler(rc -> rc.response().end("OK"));
        HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx)
                .register("health", f -> health(f));
        router.get("/health/liveness").handler(healthCheckHandler);

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config().getInteger("catalog.http.port", 8080), result -> {
                if (result.succeeded()) {
                    startFuture.complete();
                } else {
                    startFuture.fail(result.cause());
                }
            });
    }


    private void getProjectById(RoutingContext rc) {
        this.retrieveProjects(rc,true);
    }

    private void getProjectByStatus(RoutingContext rc) {
        this.retrieveProjects(rc,false);
    }

    private void getProjects(RoutingContext rc) {
        this.retrieveProjects(rc,false);
    }


    private void retrieveProjects(RoutingContext rc,boolean singleItem){
        WrapFilter filter = this.generateFilter(rc.request());
        this.service.getProjects(filter, ar -> {
            if (ar.succeeded()) {
                List<WrapProject> projects = ar.result();
                if(projects!=null && projects.size() > 0){
                    if(singleItem){
                        rc.response()
                            .putHeader("Content-type", "application/json")
                            .end(projects.get(0).toJson().encodePrettily());
                    }else{
                        JsonArray jsonList = new JsonArray();
                        projects.stream().map(p -> p.toJson()).forEach(p -> jsonList.add(p));
                        rc.response()
                            .putHeader("Content-type", "application/json")
                            .end(jsonList.encodePrettily());
                    }
                }else{
                    rc.fail(404);
                }
            } else {
                rc.fail(ar.cause());
            }
        });
    }

    private  WrapFilter generateFilter(HttpServerRequest request){
        WrapFilter filter = new WrapFilter();
        if(!UtilityBean.isEmptyString(request.getParam("id"))){
            filter.setLabel("id");
            filter.setValue(request.getParam("id"));
        }

        else if(!UtilityBean.isEmptyString(request.getParam("status"))){
            filter.setLabel("status");
            filter.setValue(request.getParam("status"));
        }

        return filter;

    }


    private void health(Future<Status> future) {
        this.service.ping(ar -> {
            if (ar.succeeded()) {
                // HealthCheckHandler has a timeout of 1000s. If timeout is exceeded, the future will be failed
                if (!future.isComplete()) {
                    future.complete(Status.OK());
                }
            } else {
                if (!future.isComplete()) {
                    future.complete(Status.KO());
                }
            }
        });
    }
    
}