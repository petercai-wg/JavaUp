package org.example;

import java.util.List;

public record Employee(String fName, int age, String dept, List<String> skills) {
}
