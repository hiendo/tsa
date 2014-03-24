package com.github.hiendo.tsa.servertests;

import java.util.Random;



public class DataGenerator extends AbstractServerTests {

    public void canAddDataPoint() throws Exception {
        for ( int i = 0; i < 100; i++) {
            timeSeriesTopicOperations.addData("hello", 5 + new Random().nextInt(10));
        }
    }
}
