package com.github.hiendo.tsa.web;

import com.github.hiendo.tsa.entities.TestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TestController {
    final static Logger logger = LoggerFactory.getLogger(TestController.class);

	@RequestMapping(value = "testController", method = RequestMethod.GET)
	@ResponseBody
    public TestEntity getTestEntity() throws Exception {
        logger.debug("Sending test entity");
        return new TestEntity("testing");
	}
}
