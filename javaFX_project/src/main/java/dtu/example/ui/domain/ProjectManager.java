package dtu.example.ui.domain;

import java.util.*;

public class ProjectManager {

    private Map<String, Employee> employees = new HashMap<>();
    private Map<String, String> projects = new HashMap<>(); // projektNavn → lederInitialer
    private String loggedInUser;

    // Til login
    public void addEmployee(Employee emp) {
        employees.put(emp.getInitials(), emp);
    }

    public boolean employeeExists(String initials) {
        return employees.containsKey(initials);
    }

    public void setLoggedInUser(String initials) {
        this.loggedInUser = initials;
    }

    public String getLoggedInUser() {
        return loggedInUser;
    }

    // Projekt
    public void createProject(String name, String leaderInitials) {
        if (projects.containsKey(name)) {
            throw new RuntimeException("Projekt eksisterer allerede");
        }
        projects.put(name, leaderInitials);
    }

    public boolean projectExists(String name) {
        return projects.containsKey(name);
    }

    public String getProjectLeader(String name) {
        return projects.get(name);
    }

    
    public List<String> getAllProjectNames() {
        return new ArrayList<>(projects.keySet());
    }

   
    public Set<String> getAllProjects() {
        return projects.keySet();
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }
}
