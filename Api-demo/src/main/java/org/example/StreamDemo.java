package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

            splitChunk(list);

//        ArrayList<String> list1 = new ArrayList<>();
//        ArrayList<String> list2 = new ArrayList<>();
//        ArrayList<String> list3 = new ArrayList<>();
//
//        splitIntoThreeEqual(list, list1, list2, list3);
//
//        System.out.println("List 1:");
//        list1.forEach(System.out::println);
//        System.out.println("................................");
//        System.out.println("List 2:");
//        list2.forEach(System.out::println);
//        System.out.println("................................");
//        System.out.println("List 3:");
//        list3.forEach(System.out::println);
    }

    public static void splitChunk(ArrayList<String> source){
        int totalSize = source.size();
        int chunkSize = totalSize / 3;

        AtomicInteger counter = new AtomicInteger(0);

        Collection<List<String>> chunks = source.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values();

//        List<List<String>> chunks = new ArrayList<>();
//
//        for (int i = 0; i < totalSize; i += chunkSize) {
//            chunks.add(new ArrayList<>(
//                    source.subList(i, Math.min(i + chunkSize, totalSize))
//            ));
//        }
        chunks.forEach(chunk -> {
            System.out.println("Chunk:");
            chunk.forEach(System.out::println);
            System.out.println("................................");
        });
    }

    public static <T> void splitIntoThreeEqual(ArrayList<T> source, ArrayList<T> list1, ArrayList<T> list2, ArrayList<T> list3) {
        int totalSize = source.size();
        int chunkSize = totalSize / 3;
        int remainder = totalSize % 3;

        Spliterator<T> spliterator = source.spliterator();
        int[] count = {0};
        int[] currentList = {1}; // Track which list to add to

        spliterator.forEachRemaining(element -> {
            // Distribute remainder elements to first lists
            int limit = (currentList[0] <= remainder) ? chunkSize + 1 : chunkSize;

            if (currentList[0] == 1) {
                list1.add(element);
            } else if (currentList[0] == 2) {
                list2.add(element);
            } else {
                list3.add(element);
            }

            count[0]++;
            if (count[0] == limit) {
                currentList[0]++;
                count[0] = 0;
            }
        });
    }
}
