package com.github.hiendo.tsa.web.entities;

public class TestEntity {

    private String testValue;

    // Json serialization
    private TestEntity(){}

    public TestEntity(String testValue) {
        super();
        this.testValue = testValue;
    }

    public String getTestValue() {
        return testValue;
    }
}
