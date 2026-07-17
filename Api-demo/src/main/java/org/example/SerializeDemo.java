package org.example;

import java.io.*;

class Message implements Serializable{
    public static final long serialVersionUID = 1L;
    public static String category = "message";
    public transient String transientField = "This is a transient field";

    String name;
    int age;

    public Message(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }
}

public class SerializeDemo {

    public static void main(String[] args) throws Exception {

        Message m1 = new Message("Alice", 30);
        FileOutputStream fout=new FileOutputStream("emp.txt");
        ObjectOutputStream out=new ObjectOutputStream(fout);
        out.writeObject(m1);
        out.flush();
        out.close();
        fout.close();
        System.out.println("success");

        FileInputStream fis=new FileInputStream("emp.txt");
        ObjectInputStream in=new ObjectInputStream(fis);
        Message e1=(Message)in.readObject();
        in.close();
        fis.close();
        System.out.println("Deserialized Message...");
        System.out.println("Name: " + e1.getName());
        System.out.println("Age: " + e1.getAge());
        System.out.println("category: " + e1.category);
        System.out.println("transientField: " + e1.transientField);
        System.out.println("serialVersionUID: " + Message.serialVersionUID);

    }
}
