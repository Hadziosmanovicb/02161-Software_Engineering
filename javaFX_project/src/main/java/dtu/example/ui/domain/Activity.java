/**
 * Filnavn: Activity.java
 * Relaterede filer: Employee.java, ProjectManager.java, ProjectReportGenerator.java
 *
 * Formål:
 * Repræsenterer en aktivitet i et projekt med navn, tidsperiode, status og budget.
 * Giver mulighed for at tilføje medarbejdere, registrere arbejdstimer og holde
 * styr på aktivitetsstatus og fuldførelsesprocent.
 */

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
// Ansvarlig: Ali
    public Activity(String name, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
        this.name = name;
        this.startWeek = startWeek;
        this.startYear = startYear;
        this.endWeek = endWeek;
        this.endYear = endYear;
        this.budgetedHours = budgetedHours;
    }
// Ansvarlig: Benjamin
    public String getName() { return name; }
    // Ansvarlig: Younes
    public List<Employee> getAssignedEmployees() { return assignedEmployees; }
    // Ansvarlig: Benjamin
    public void assignEmployee(Employee employee) { assignedEmployees.add(employee); }
    // Ansvarlig: Younes
    public int getStartWeek() { return startWeek; }
    // Ansvarlig: Benjamin
    public int getStartYear() { return startYear; }
    // Ansvarlig: Younes
    public int getEndWeek() { return endWeek; }
    // Ansvarlig: Benjamin
    public int getEndYear() { return endYear; }
    // Ansvarlig: Ali
    public int getBudgetedHours() { return budgetedHours; }
// Ansvarlig: Younes
    public void registerTime(Employee employee, LocalDate date, int hours) {
        timeRegistration
            .computeIfAbsent(employee.getInitials(), k -> new HashMap<>())
            .merge(date, hours, Integer::sum);
    }
// Ansvarlig: Benjamin
    public int getRegisteredTime(String employeeInitials) {
        return timeRegistration.getOrDefault(employeeInitials, Map.of())
                               .values().stream().mapToInt(i -> i).sum();
    }
// Ansvarlig: Younes
    public int getRegisteredTimeOnDate(String employeeInitials, LocalDate date) {
        return timeRegistration.getOrDefault(employeeInitials, Map.of())
                               .getOrDefault(date, 0);
    }
// Ansvarlig: Ali
    public int getCompletionPercentage() {
        int totalLoggedHours = timeRegistration.values().stream()
                .flatMap(m -> m.values().stream())
                .mapToInt(Integer::intValue)
                .sum();
        if (budgetedHours == 0) return 0;
        return Math.min(100, (totalLoggedHours * 100) / budgetedHours);
    }
// Ansvarlig: Ali
    public String getStatus() { return status; }
    // Ansvarlig: Younes
    public void setStatus(String status) { this.status = status; }

    // Ansvarlig: Benjamin
    public boolean isCompleted() {
        return "Godkendt".equals(status);
    }

    
}
