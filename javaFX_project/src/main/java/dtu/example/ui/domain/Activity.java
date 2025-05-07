package dtu.example.ui.domain;

import java.time.LocalDate;
import java.util.*;

public class Activity {
    private String name;
    private List<Employee> assignedEmployees = new ArrayList<>();
    private Map<String, Map<LocalDate, Integer>> timeRegistration = new HashMap<>();

    private int startWeek;
    private int startYear;
    private int endWeek;
    private int endYear;
    private int budgetedHours;
    
    private String status = "Ikke klar"; // "Ikke klar", "Afventer", "Godkendt", "Afvist"

    public Activity(String name, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
        this.name = name;
        this.startWeek = startWeek;
        this.startYear = startYear;
        this.endWeek = endWeek;
        this.endYear = endYear;
        this.budgetedHours = budgetedHours;
    }

    public String getName() { return name; }
    public List<Employee> getAssignedEmployees() { return assignedEmployees; }
    public void assignEmployee(Employee employee) { assignedEmployees.add(employee); }
    public int getStartWeek() { return startWeek; }
    public int getStartYear() { return startYear; }
    public int getEndWeek() { return endWeek; }
    public int getEndYear() { return endYear; }
    public int getBudgetedHours() { return budgetedHours; }

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

    public int getCompletionPercentage() {
        int totalLoggedHours = timeRegistration.values().stream()
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue)
                .sum();
        if (budgetedHours == 0) return 0;
        return Math.min(100, (totalLoggedHours * 100) / budgetedHours);
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    
    public boolean isCompleted() {
        return "Godkendt".equals(status);
    }
}
