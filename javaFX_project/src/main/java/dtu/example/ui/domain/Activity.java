package dtu.example.ui.domain;

import java.time.LocalDate;
import java.util.*;

public class Activity {
    private String name;
    private List<Employee> assignedEmployees = new ArrayList<>();
private Map<String, Map<LocalDate, Integer>> timeRegistration = new HashMap<>();

public void registerTime(Employee employee, LocalDate date, int hours) {
    timeRegistration
        .computeIfAbsent(employee.getInitials(), k -> new HashMap<>())
        .merge(date, hours, Integer::sum);
}

public int getRegisteredTime(String employeeInitials) {
    return timeRegistration.getOrDefault(employeeInitials, Map.of())
                           .values().stream().mapToInt(i -> i).sum();
}

public int getRegisteredTimeOnDate(String employeeInitials, LocalDate date) {
    return timeRegistration.getOrDefault(employeeInitials, Map.of())
                           .getOrDefault(date, 0);
}

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
