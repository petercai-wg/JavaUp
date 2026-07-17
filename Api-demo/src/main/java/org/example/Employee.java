package org.example;

import java.util.List;

public record Employee(String fName, int age, String dept, List<String> skills) {

    @Override
    public int hashCode() {
        return fName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return fName.equals(((Employee) obj).fName);
    }
}
