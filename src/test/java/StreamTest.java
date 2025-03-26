import org.example.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class StreamTest {
    List<Employee> employeeList;

    @BeforeEach
    public void setup(){

        employeeList = List.of(

            new Employee("Peter",  53, "DEV", List.of("java", "sql", "python") ),
            new Employee("Petro",  35, "Support", List.of("AZure", "AWS", "ETL") ),
            new Employee("Michael",  38, "DEV", List.of("python", "C#") ),
            new Employee("John",   50, "QA", List.of( "UHN", "WBS")),
            new Employee("Wendy",   40, "QA", List.of( "Sunny"))
        );


    }
    @Test
    public void groupby(){
        Map<String, List<Employee>> empMap = employeeList.stream().collect(groupingBy(Employee::dept));
        System.out.println(empMap);

        Map<String, Optional<Employee>> empMap2 = employeeList.stream().collect(
                groupingBy(Employee::dept, Collectors.reducing( BinaryOperator.maxBy(Comparator.comparing(Employee::age) )) )
                );

        System.out.println(empMap2);
    }
    @Test
    public void mapOperation(){
        employeeList.stream().map(e -> e.fName())
                .forEach(System.out::println);

        employeeList.stream().map(e -> e.skills())
                .forEach(System.out::println);

        employeeList.stream().flatMap(e -> e.skills().stream()).collect(Collectors.toList())
                .forEach(System.out::print);

        List<String> skills_inventory = employeeList.stream()
                .filter(e -> e.dept().equals("DEV"))
                .map(Employee::skills)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        System.out.println(skills_inventory);
    }

    @Test
    public void minMaxOperation() {
        Optional<Employee> element = employeeList.stream().min(Comparator.comparing(Employee::age));

        System.out.println(element.get());
    }
}
