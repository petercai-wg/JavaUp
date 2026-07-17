import java.util.*;

record Position(int diff, int index) {
}

public class Test {

    public static void main(String[] args) {
//        int[] input  = {8,  5,  3, 9, 15, 17,4,6 };
//        divid3(input);

        Integer c = 100;
        Integer d = 100;
        System.out.println(c == d);
    }

    public static void divid3(int[] input) {
        Arrays.sort(input);
        System.out.println(Arrays.toString(input));
        ArrayList<Position> p = new ArrayList<>();
        for (int i = 1; i < input.length; i++) {
            int diff = input[i] - input[i - 1];
            p.add(new Position(diff, i));
        }

        p.sort(Comparator.comparingInt(Position::diff).reversed());

        System.out.println(p);

        int split1 = p.get(0).index();
        int split2 = p.get(1).index();

        if (split1 > split2) {
            int temp = split1;
            split1 = split2;
            split2 = temp;
        }

        int[] part1 = Arrays.copyOfRange(input, 0, split1);
        int[] part2 = Arrays.copyOfRange(input, split1, split2);
        int[] part3 = Arrays.copyOfRange(input, split2, input.length);
        System.out.println("Part 1: " + Arrays.toString(part1));
        System.out.println("Part 2: " + Arrays.toString(part2));
        System.out.println("Part 3: " + Arrays.toString(part3));


    }



    public static int findSmall(int[] input) {

        Arrays.sort(input);
        System.out.println(input);
        int result = 0;
        boolean found = false;
        for (int i = 0; i < input.length; i++) {
            if (i == 0) {
                result = input[i];
            } else {
                result++;
                if (result < input[i] && result > 0) {
                    System.out.println(result);
                    found = true;
                    return result;

                }

            }
        }
        if (!found) {
            result++;
            while (result < 1) {
                result++;
            }
            System.out.println(result);
            return result;
        }
        return 0;
    }

    public static int findSmallestMissingPositive(int[] nums) {
        Set<Integer> set = new HashSet<>();

        // Store all positive numbers
        for (int num : nums) {
            if (num > 0) {
                set.add(num);
            }
        }

        // Find the first missing positive number
        int smallest = 1;

        while (set.contains(smallest)) {
            smallest++;
        }

        return smallest;
    }

}
