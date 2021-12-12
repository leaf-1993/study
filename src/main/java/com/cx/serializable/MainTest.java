package com.cx.serializable;

import java.io.*;
import java.net.URLEncoder;

/**
 * @author chenxiang
 * @create 2021-11-26 10:45
 *
 * 将一个对象序列化到文件的时候需要继承Serializable接口
 * ObjectInputStream ObjectOutputStream
 */
public class MainTest {

    public static void main(String[] args) {
        try {
            final String utf8 = URLEncoder.encode(":", "utf8");
            System.out.println(utf8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        writeSerializableObject();
//        readSerializableObject();
    }


    // Serializable：把对象序列化
    public static void writeSerializableObject() {
        try {
            Man man = new Man("huhx", "123456");
            Person person = new Person(man, "刘力", 21);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("output.txt"));
            objectOutputStream.writeObject(person);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Serializable：反序列化对象
    public static void readSerializableObject() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("output.txt"));
            Person person = (Person) objectInputStream.readObject();
            objectInputStream.close();
            System.out.println(" age: " + person.getAge() + ", man username: " + person.getMan().getUsername());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
