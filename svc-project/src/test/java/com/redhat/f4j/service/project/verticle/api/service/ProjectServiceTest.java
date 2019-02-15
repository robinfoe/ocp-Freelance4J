package com.redhat.f4j.service.project.verticle.api.service;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import com.redhat.f4j.base.constant.ConstantProject;
import com.redhat.f4j.service.project.verticle.dto.WrapFilter;
import com.redhat.f4j.service.project.verticle.dto.WrapProject;
import com.redhat.f4j.service.project.verticle.service.ProjectService;
import com.redhat.f4j.service.project.verticle.service.ProjectServiceImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

/**
 * ProjectServiceTest
 */
@RunWith(VertxUnitRunner.class)
 public class ProjectServiceTest extends MongoTestBase {

    private Vertx vertx;

    

    @Before
    public void setup(TestContext context) throws Exception {
        vertx = Vertx.vertx();
        vertx.exceptionHandler(context.exceptionHandler());
        JsonObject config = getConfig();
        mongoClient = MongoClient.createNonShared(vertx, config);
        Async async = context.async();
        dropCollection(mongoClient, ProjectService.TABLE_NAME, async, context);
        async.await(10000);
    }

    @After
    public void tearDown() throws Exception {
        mongoClient.close();
        vertx.close();
    }



    @Test
    public void testPing(TestContext context) throws Exception {
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);

        Async async = context.async();
        service.ping(ar -> {
            assertThat(ar.succeeded(), equalTo(true));
            async.complete();
        });
    }


    @Test
    public void testAddProject(TestContext context) throws Exception {
        String title = "Awesome Project";
        String id = "123456";
        WrapProject project = new WrapProject();
        project.setId(id);
        project.setDescription("test project");
        project.setStatus(ConstantProject.STS_OPEN);
        project.setTitle(title);
        project.getOwner().setFirstName("Robin");
        project.getOwner().setLastName("Foe");
        project.getOwner().setEmail("test@redhat.com");
        

        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient);
        Async async = context.async();
        service.addProject(project, ar -> {
            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                JsonObject query = new JsonObject().put("_id", id);
                mongoClient.findOne(ProjectService.TABLE_NAME , query, null, ar1 -> {
                    if (ar1.failed()) {
                        context.fail(ar1.cause().getMessage());
                    } else {
                        assertThat(ar1.result().getString("title"), equalTo(title));
                        async.complete();
                    }
                });
            }
        });
    }

    @Test
    public void testGetProjects(TestContext context) throws Exception {

        Async saveAsync = context.async(2);
        this.populateEntry(context, saveAsync);
        saveAsync.await();
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient); 
        Async async = context.async();
        WrapFilter filter = new WrapFilter();
        service.getProjects(filter , ar -> {

            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(2));
                Set<String> itemIds = ar.result().stream().map(p -> p.getId()).collect(Collectors.toSet());
                assertThat(itemIds.size(), equalTo(2));
                assertThat(itemIds, allOf(hasItem("123456"),hasItem("222222")));
                async.complete();
            }

        });
    }

    @Test
    public void testGetProjectById(TestContext context) throws Exception {

        Async saveAsync = context.async(2);
        this.populateEntry(context, saveAsync);
        saveAsync.await();
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient); 
        Async async = context.async();
        WrapFilter filter = new WrapFilter();
        filter.setLabel("id");
        filter.setValue("222222");
        service.getProjects(filter , ar -> {

            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(1));
                WrapProject project = ar.result().get(0);
                assertThat(project.getId(),equalTo("222222"));
                async.complete();
            }

        });
    }


    @Test
    public void testGetProjectByStatus(TestContext context) throws Exception {

        Async saveAsync = context.async(2);
        this.populateEntry(context, saveAsync);
        saveAsync.await();
        ProjectService service = new ProjectServiceImpl(vertx, getConfig(), mongoClient); 
        Async async = context.async();
        WrapFilter filter = new WrapFilter();
        filter.setLabel("status");
        filter.setValue(ConstantProject.STS_COMPLETED);
        service.getProjects(filter , ar -> {

            if (ar.failed()) {
                context.fail(ar.cause().getMessage());
            } else {
                assertThat(ar.result(), notNullValue());
                assertThat(ar.result().size(), equalTo(1));
                WrapProject project = ar.result().get(0);
                assertThat(project.getStatus(),equalTo(ConstantProject.STS_COMPLETED));
                async.complete();
            }

        });
    }


    private void populateEntry(TestContext context, Async saveAsync){

        String title = "Awesome Project";
        String id = "123456";
        WrapProject project = new WrapProject();
        project.setId(id);
        project.setDescription("test project");
        project.setStatus(ConstantProject.STS_OPEN);
        project.setTitle(title);
        project.getOwner().setFirstName("Robin");
        project.getOwner().setLastName("Foe");
        project.getOwner().setEmail("test@redhat.com");

        // JsonObject json1 = project.toJson();
        mongoClient.save(ProjectService.TABLE_NAME, project.toJson(), ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        title = "Awesome Project 002";
        id = "222222";
        WrapProject project2 = new WrapProject();
        project2.setId(id);
        project2.setDescription("test project");
        project2.setStatus(ConstantProject.STS_COMPLETED);
        project2.setTitle(title);
        project2.getOwner().setFirstName("Robin");
        project2.getOwner().setLastName("Foe");
        project2.getOwner().setEmail("test@redhat.com");


        mongoClient.save(ProjectService.TABLE_NAME, project2.toJson(), ar -> {
            if (ar.failed()) {
                context.fail();
            }
            saveAsync.countDown();
        });

        saveAsync.await();

    }

}