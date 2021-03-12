package org.glavo.kala.collection;

public interface MapTestTemplate {
    default Integer[][] data1() {
        return TestData.data1;
    }

    default String[][] data1s() {
        return TestData.data1s;
    }
}
