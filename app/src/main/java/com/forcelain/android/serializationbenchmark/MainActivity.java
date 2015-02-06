package com.forcelain.android.serializationbenchmark;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.forcelain.android.serializationbenchmark.models.ComplexModel;
import com.forcelain.android.serializationbenchmark.models.SimpleModel;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.result);
    }

    public void go(View view) throws IOException, ClassNotFoundException {
        long startTime;
        byte[] bytes;
        long endTime;
        //1
        Result serializeResult = new Result();
        SimpleModel simpleModel = generateSimple();
        startTime = System.nanoTime();
        bytes = serialize(simpleModel);
        endTime = System.nanoTime();
        serializeResult.serializeDiff = endTime - startTime;
        serializeResult.serializeSize = bytes.length;
        startTime = System.nanoTime();
        simpleModel = (SimpleModel) deSerialize(bytes);
        endTime = System.nanoTime();
        serializeResult.deSerializeDiff = endTime - startTime;

        //2
        Result gsonResult = new Result();
        simpleModel = generateSimple();
        Gson gson = new Gson();
        startTime = System.nanoTime();
        String json = gson.toJson(simpleModel);
        endTime = System.nanoTime();
        gsonResult.serializeDiff = endTime - startTime;
        gsonResult.serializeSize = json.getBytes().length;
        startTime = System.nanoTime();
        simpleModel = gson.fromJson(json, SimpleModel.class);
        endTime = System.nanoTime();
        gsonResult.deSerializeDiff = endTime - startTime;

        //3
        Result complexSerializeResult = new Result();
        ComplexModel complexModel = generateComplex(3);
        startTime = System.nanoTime();
        bytes = serialize(complexModel);
        endTime = System.nanoTime();
        complexSerializeResult.serializeDiff = endTime - startTime;
        complexSerializeResult.serializeSize = bytes.length;
        startTime = System.nanoTime();
        complexModel = (ComplexModel) deSerialize(bytes);
        endTime = System.nanoTime();
        complexSerializeResult.deSerializeDiff = endTime - startTime;

        //4
        Result gsonComplexResult = new Result();
        complexModel = generateComplex(3);
        gson = new Gson();
        startTime = System.nanoTime();
        json = gson.toJson(complexModel);
        endTime = System.nanoTime();
        gsonComplexResult.serializeDiff = endTime - startTime;
        gsonComplexResult.serializeSize = json.getBytes().length;
        startTime = System.nanoTime();
        complexModel = gson.fromJson(json, ComplexModel.class);
        endTime = System.nanoTime();
        gsonComplexResult.deSerializeDiff = endTime - startTime;

        textView.setText(serializeResult.toString()+"\n"+gsonResult.toString()+"\n"+complexSerializeResult.toString()+"\n"+gsonComplexResult.toString());
    }

    private ComplexModel generateComplex(int level) {
        ComplexModel complexModel = new ComplexModel();
        complexModel.field1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam";
        complexModel.field2 = Integer.MIN_VALUE;
        complexModel.field3 = Long.MIN_VALUE;
        complexModel.field4 = Double.MIN_VALUE;
        complexModel.field5 = complexModel.field1.getBytes();
        complexModel.simpleModelList = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            complexModel.simpleModelList.add(generateSimple());
        }

        level--;

        if (level > 0){
            complexModel.complexModelList = new ArrayList<>();
            for (int i = 0; i < 3; i++){
                complexModel.complexModelList.add(generateComplex(level));
            }
        }
        return complexModel;
    }

    private byte[] serialize(Serializable object) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        byte[] bytes = baos.toByteArray();
        oos.close();
        baos.close();
        return bytes;
    }

    private Object deSerialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object o = ois.readObject();
        bis.close();
        ois.close();
        return o;
    }

    private SimpleModel generateSimple() {
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.field1 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
        simpleModel.field2 = Integer.MIN_VALUE;
        simpleModel.field3 = Long.MIN_VALUE;
        simpleModel.field4 = Double.MIN_VALUE;
        simpleModel.field5 = (simpleModel.field1 + simpleModel.field1).getBytes();
        return simpleModel;
    }

    private class Result {
        private long serializeDiff;
        private int serializeSize;
        private long deSerializeDiff;

        @Override
        public String toString() {
            return "Result{" +
                    "serializeDiff=" + serializeDiff +
                    ", serializeSize=" + serializeSize +
                    ", deSerializeDiff=" + deSerializeDiff +
                    '}';
        }
    }
}
