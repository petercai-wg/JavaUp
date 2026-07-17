package org.example;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class CollectorDemo {
    public static List<Employee> employees = List.of(
            new Employee("John", 30, "Engineering", List.of("Java", "Python")),
            new Employee("Jane", 25, "Marketing", List.of("Sell", "Buy")),
            new Employee("Mike", 35, "Engineering", List.of("C++", "Go")),
            new Employee("Sara", 48, "Marketing", List.of("SEO", "Content Creation")),
            new Employee("Tom", 40, "Sales", List.of("Negotiation", "Communication"))
    );

    public static void main(String[] args) {
//        System.out.println("Employees:");
//        employees.forEach(System.out::println);

        String names = employees.stream()
                .map(Employee::fName)
                .collect(Collectors.joining(", "));
        System.out.println("Employee Names: " + names);

        IntSummaryStatistics stats = employees.stream()
                .collect(Collectors.summarizingInt(Employee::age));
        System.out.println("Age Statistics: " + stats);

        double averageAge = employees.stream()
                .collect(Collectors.averagingInt(Employee::age));
        System.out.println("Average Age: " + averageAge);

        Map<String, List<Employee>> deptMap = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept));
        System.out.println("Employees by Department: " + deptMap);

        deptMap.entrySet().forEach(entry -> {
            System.out.println("Department: " + entry.getKey() + ", Employees: " + entry.getValue());

        });

        Map<String, Long> deptCount = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept, Collectors.counting()));
        System.out.println("Employee Count by Department: " + deptCount);

        Map<String, List<String>> namesByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept, Collectors.mapping(Employee::fName, Collectors.toList())));
        System.out.println("Employee Names by Department: " + namesByDept);

        Map<String, List<String>> skillsByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept, Collectors.flatMapping(e -> e.skills().stream(), Collectors.toList())));
        System.out.println("Skills by Department: " + skillsByDept);


        // group by department, then by age group (Senior/Junior) and collect employee first names
        Map<String, Map<String, List<String>>> nested = employees.stream()
                .collect(Collectors.groupingBy(Employee::dept,
                        Collectors.groupingBy(e -> e.age() > 30 ? "Senior" : "Junior",
                                Collectors.mapping(Employee::fName, Collectors.toList()))));

        System.out.println("Employees by Department and Age Group: " + nested);

        employees.stream().reduce((Employee e1, Employee e2) -> e1.age() > e2.age() ? e1 : e2)
                .ifPresent(oldest -> System.out.println("Oldest Employee: " + oldest));

        //int sumAge = employees.stream().reduce( 0,    (sum, e) -> sum + e.age(), (sum, e) -> sum - e);
        //int sumAge = employees.stream().reduce( 0,    (sum, e) -> sum + e.age(), Integer::sum);
        int sumAge = employees.stream().mapToInt(Employee::age).reduce(0, Integer::sum);
        System.out.println("Sum of Ages: " + sumAge);

        String skills = employees.stream()
                .filter(e -> e.dept().equals("Engineering"))
                .flatMap(e -> e.skills().stream())
                .collect(Collectors.joining(", "));
        System.out.println("skills: " + skills);

        String skills2 = employees.stream()
                .filter(e -> e.dept().equals("Marketing"))
                .flatMap(e -> e.skills().stream())
                .reduce("", (s1, s2) -> s1.isEmpty() ? s2 : s1 + ", " + s2);
        System.out.println("skills: " + skills2);
    }


}
