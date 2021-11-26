package com.cx.serializable;

import java.io.Serializable;

/**
 * @author chenxiang
 * @create 2021-11-26 10:42
 */
public class Person implements Serializable {
    private Man man;
    private String name;
    private transient int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Man getMan() {
        return man;
    }

    public void setMan(Man man) {
        this.man = man;
    }

    public Person(Man man, String name, int age) {
        this.man = man;
        this.name = name;
        this.age = age;
    }

    public Person() {
    }
}
