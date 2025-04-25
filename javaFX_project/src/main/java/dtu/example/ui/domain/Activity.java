package dtu.example.ui.domain;

import java.util.*;

public class Activity {
    private String name;
    private List<Employee> assignedEmployees = new ArrayList<>();

    public Activity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getAssignedEmployees() {
        return assignedEmployees;
    }

    public void assignEmployee(Employee employee) {
        assignedEmployees.add(employee);
    }
}
