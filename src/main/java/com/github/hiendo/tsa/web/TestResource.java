package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.entities.TestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


@Path("testresource")
public class TestResource {
    final static Logger logger = LoggerFactory.getLogger(TestResource.class);

    @GET
    @Produces("application/json")
    public TestEntity getTestEntity() throws Exception {
        logger.debug("Sending test entity");
        return new TestEntity("testing");
	}
}
