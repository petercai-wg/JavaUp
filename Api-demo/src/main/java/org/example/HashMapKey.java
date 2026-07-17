package org.example;

import java.util.HashMap;

public class HashMapKey {

    public static void main(String ... args) {
        String s1 = new String("abc");
        String s2 = new String("abc");
        HashMap hm = new HashMap();
        hm.put(s1, 1);
        hm.put(s2, 2);
        System.out.println(hm.size());


        StringBuilder sb1 = new StringBuilder("abc");
        StringBuilder sb2 = new StringBuilder("abc");
        HashMap hm2 = new HashMap();
        hm2.put(sb1, 1);
        hm2.put(sb2, 2);
        System.out.println(hm2.size());

        StringBuffer bf1 = new StringBuffer("abc");
        StringBuffer bf2 = new StringBuffer("abc");
        HashMap hm3 = new HashMap();
        hm3.put(bf1, 1);
        hm3.put(bf2, 2);
        System.out.println(hm3.size());
    }

}
