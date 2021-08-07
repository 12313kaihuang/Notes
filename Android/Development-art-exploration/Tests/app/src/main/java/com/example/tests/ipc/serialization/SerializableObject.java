package com.example.tests.ipc.serialization;

import java.io.Serializable;

/**
 * @auther hy
 * create on 2021/08/01 下午1:13
 */
public class SerializableObject implements Serializable {
    private static final long serialVersionUID = -3091935682059836537L;

    int id;
//    int addedField;
    String name;

    public SerializableObject(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "SerializableObject{" +
                "id=" + id +
//                ", addedField='" + addedField  +
                ", name='" + name + '\'' +
                '}';
    }


}


