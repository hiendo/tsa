package com.github.hiendo.tsa.entities;

import org.codehaus.jackson.annotate.JsonCreator;

public class TestEntity {

    private String testValue;

    @JsonCreator
    public TestEntity(String testValue) {
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }
}
