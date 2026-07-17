package org.example;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ArrayDemo {

    public static void main(String[] args) {
        //arraytransverse();

        // print2DArray();
//        mergeIntervals();
//        arrayExpand();
        arrayUtil();
    }
    public static void arrayUtil(){
        String[] names = {new String("Alice"), "Bob", "Charlie"};
        String[] names2 = {new String("Alice"), "Bob", "Charlie"};

        System.out.println(Arrays.equals(names, names2)); // true
        System.out.println(names == names2); // false

        int[][] intervals = {{8, 10}, {1, 3}, {2, 6},  {7, 18}};
        int[][] intervals2 = {{8, 10}, {1, 3}, {2, 6},  {7, 18}};
        int[][] intervals3 = Arrays.copyOf(intervals, intervals.length);


        System.out.println(Arrays.deepEquals(intervals, intervals2)); // true
        System.out.println(Arrays.equals(intervals, intervals2)); // false

        System.out.println(Arrays.equals(intervals, intervals3)); // true



    }
    public static void mergeIntervals(){
        int[][] intervals = {{8, 10}, {1, 3}, {2, 6},  {7, 18}};
        System.out.println(Arrays.deepToString(intervals));
        //Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        System.out.println(Arrays.deepToString(intervals));
        List<int[]> result = new ArrayList<>();

        //int[] currentInterval = intervals[0];
        int[] currentInterval = new int[2];
        currentInterval[0] = intervals[0][0];
        currentInterval[1] = intervals[0][1];

        result.add(currentInterval);

        for (int[] interval : intervals) {

            int starti = interval[0];
            int endi = interval[1];

            int currentEnd = currentInterval[1];

            if (starti <= currentEnd) {
                // 2. Overlap found, update the end time
                currentInterval[1] = Math.max(currentEnd, endi);
            } else {
                // 3. No overlap, move to the next interval
                //currentInterval = interval;
                currentInterval = new int[2];
                currentInterval[0] = interval[0];
                currentInterval[1] = interval[1];
                result.add(currentInterval);
            }
        }

        for(int[] interval : result){
            System.out.println(Arrays.toString(interval));
        }
    }

    public static void arraytransverse() {
        int[][] matrix = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };
        System.out.println("Number of rows: " + matrix.length);
        System.out.println("Number of columns: " + matrix[0].length);

        int i = matrix.length -1;
        int j = matrix[0].length -1;
        int direction = -1;

        while(j >=0 && i>=0){

            System.out.println(matrix[i][j]);
            i+=direction;

            if(i==matrix.length-1){
                System.out.println(matrix[i][j]);
                direction = -1;
                j--;
            }

            if(i==0){
                System.out.println(matrix[i][j]);
                direction = 1;
                j--;
            }
        }


    }

    public static void print2DArray() {
            int[][] library = {
                    {1, 3, 5, 7},
                    {2, 4, 6, 8},
                    {9, 11, 13, 15}
            };

            // Starting from the bottom right corner of the bookshelf
            int row = library.length - 1;
            int col = library[0].length - 1;
            boolean goingUp = true;

            ArrayList<Integer> result = new ArrayList<>();

            while (col >= 0) {

                result.add(Integer.valueOf(library[row][col]));
                if (goingUp) {
                    if (row == 0) {
                        goingUp = false;
                        col -= 1;
                    } else {
                        row -= 1;
                    }
                } else {
                    if (row == library.length - 1) {
                        goingUp = true;
                        col -= 1;
                    } else {
                        row += 1;
                    }
                }
            }

            System.out.println(result);
        }


    public static void arrayExpand() {
        // 1. Original 2D array
        String[][] apartments = { {"Apt 101", "Apt 102"}, {"Apt 201", "Apt 202"} };

        // 2. Define the new floor to add
        String[] newFloor = {"Apt 301", "Apt 302"};

        String[] groundFloor = {"Apt 001", "Apt 002"};

        // 3. Create a copies of the original array with 1 extra row
        apartments = Arrays.copyOf(apartments, apartments.length + 1);

        // 4. Insert the new row into the last index
        apartments[apartments.length - 1] = newFloor;

        // Print to verify
        System.out.println(Arrays.deepToString(apartments));

        String[][] updatedApartments = new String[apartments.length + 1][];

        updatedApartments[0] = groundFloor;

//        for (int i = 0; i < apartments.length; i++) {
//            updatedApartments[i + 1] = apartments[i];
//        }

        System.arraycopy(apartments, 0, updatedApartments, 1, apartments.length);

        System.out.println(Arrays.deepToString(updatedApartments));



    }


}
