package org.example;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FailFastExample {
    /*
    Fail Fast and Fail Safe Iterators in Java
    Hash map throws throw ConcurrentModificationException if we try to modify the map while iterating over it. This is because the iterator of HashMap is fail-fast. It checks for any structural modification in the map and throws an exception if it detects any.
    it is called fail-fast because it fails immediately when it detects any modification in the map. This is done to prevent any unexpected behavior while iterating over the map.
    ConcurrentHashMap, on the other hand, does not throw ConcurrentModificationException if we try to modify the map while iterating over it. This is because the iterator of ConcurrentHashMap is fail-safe.
     */
    public static void main(String[] args)
    {
        Map<String, String> cityCode
                = new ConcurrentHashMap<>();
        cityCode.put("Delhi", "India");
        cityCode.put("Moscow", "Russia");
        cityCode.put("New York", "USA");

        Iterator iterator = cityCode.keySet().iterator();
        int i=0;

        while (iterator.hasNext()) {
            System.out.println(
                    cityCode.get(iterator.next()));

            // adding an element to Map
            // exception will be thrown on next call
            // of next() method.
            i++;
            cityCode.put("city" + i, "Country" + i);
        }
    }
}
