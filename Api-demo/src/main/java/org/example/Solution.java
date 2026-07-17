package org.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Collections;
import java.util.Comparator;

record BSPrice(int bprice, int sprice) {
}

public class Solution {
    public static void main(String[] args) {
        int[] prices = {7, 2, 9, 1, 6, 4, 3, 5};
//        Arrays.sort(prices,);
//        System.out.println(Arrays.toString(prices));

        int[] sortedPrices = Arrays.stream(prices)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();
        System.out.println(Arrays.toString(sortedPrices));

        //new Solution().buynsellstock(prices);
    }

    public void sortHashMapByValue(HashMap<Integer, BSPrice> map) {
        map.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getKey(), e1.getKey())) // Sort by value in descending order
                .limit(5)
                .forEach(entry -> System.out.println("Profit: " + entry.getKey() + ", Buy Price: " + entry.getValue().bprice() + ", Sell Price: " + entry.getValue().sprice()));
    }



    public void buynsellstock(int[] prices) {
        int minPrice = Integer.MAX_VALUE;
        int maxProfit = 0;
        BSPrice bs = null;
        HashMap<Integer, BSPrice> map = new HashMap<>();

        for (int i = 0; i < prices.length; i++) {
            for (int j = i + 1; j < prices.length; j++) {
                int profile = prices[j] - prices[i];
                map.put(profile, new BSPrice(prices[i], prices[j]));
                if (profile > maxProfit) {
                    maxProfit = profile;
                    bs = new BSPrice(prices[i], prices[j]);

                }
            }

        }
        sortHashMapByValue(map);
        System.out.println("Buy Price: " + bs.bprice() + ", Sell Price: " + bs.sprice() + ", Max Profit: " + maxProfit);
    }

}
