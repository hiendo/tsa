package com.github.hiendo.tsa.service;

import com.github.hiendo.tsa.entities.TestEntity;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    public TestEntity getTestEntity() {
        return new TestEntity("testing");
    }
}
