package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.entities.TestEntity;
import com.github.hiendo.tsa.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Component
@Path("testResource")
public class TestResource {
    final static Logger logger = LoggerFactory.getLogger(TestResource.class);

    @Autowired
    private TestService testService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public TestEntity getTestEntity() throws Exception {
        logger.debug("Sending test entity");
        return testService.getTestEntity();
	}
}
