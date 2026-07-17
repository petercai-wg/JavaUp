package org.example;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        List<Integer> lst = IntStream.rangeClosed(1, 1000)
                .parallel().boxed()
                .collect(Collectors.toList());

        System.out.println(lst.size());


//        IntStream.rangeClosed(1, 20).forEach(t-> System.out.println(Thread.currentThread().getName() + " : " + t));
//        System.out.printf("Hello paralle !");
//        IntStream.rangeClosed(1, 20).parallel().forEach( t-> System.out.println(Thread.currentThread().getName()  + " : " +  t));
//
//        System.out.println("hello");


    }
}

