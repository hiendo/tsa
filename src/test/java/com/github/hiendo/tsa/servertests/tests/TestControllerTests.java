package com.github.hiendo.tsa.servertests.tests;

import com.github.hiendo.tsa.entities.TestEntity;
import com.github.hiendo.tsa.servertests.AbstractServerTests;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


public class TestControllerTests extends AbstractServerTests {

    @Test
    public void canGetTestEntity() throws Exception {
        TestEntity testEntity = TestControllerOperations.getTestEntity();

        assertThat(testEntity, notNullValue());
        assertThat(testEntity.getTestValue(), notNullValue());
    }
}
