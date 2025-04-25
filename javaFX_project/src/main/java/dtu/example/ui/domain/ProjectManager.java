package dtu.example.ui.domain;

import java.util.*;

public class ProjectManager {

    private Map<String, Employee> employees = new HashMap<>();
    private Map<String, String> projects = new HashMap<>(); // projektNavn → lederInitialer
    private String loggedInUser;
    private Map<String, List<Activity>> activities = new HashMap<>();

    public void addActivityToProject(String projectName, String activityName) {
        if (!projects.containsKey(projectName)) {
            throw new RuntimeException("Projektet findes ikke");
        }
        activities.computeIfAbsent(projectName, k -> new ArrayList<>())
                  .add(new Activity(activityName));
    }
    
    public List<Activity> getActivities(String projectName) {
        return activities.getOrDefault(projectName, new ArrayList<>());
    }
    
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
