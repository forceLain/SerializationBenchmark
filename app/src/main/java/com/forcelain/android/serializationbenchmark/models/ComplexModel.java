package com.forcelain.android.serializationbenchmark.models;

import java.io.Serializable;
import java.util.List;

public class ComplexModel implements Serializable {
    public String field1;
    public int field2;
    public long field3;
    public double field4;
    public byte[] field5;
    public List<ComplexModel> complexModelList;
    public List<SimpleModel> simpleModelList;
}
