package org.example;

import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.


        IntStream.rangeClosed(1, 20).forEach(t-> System.out.println(Thread.currentThread().getName() + " : " + t));
        System.out.printf("Hello paralle !");
        IntStream.rangeClosed(1, 20).parallel().forEach( t-> System.out.println(Thread.currentThread().getName()  + " : " +  t));


    }
}