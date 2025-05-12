/**
 * Filnavn: ProjectManager.java
 * Relaterede filer: Employee.java, Activity.java, ProjectReportGenerator.java
 *
 * Formål:
 * Hovedklassen for forretningslogik. Styrer oprettelse og administration af
 * projekter, medarbejdere og aktiviteter, samt brugerlogin.
 * Giver adgang til centrale operationer som tildelinger, valideringer og projektdata.
 */

package dtu.example.ui.domain;

import java.util.*;

public class ProjectManager {

    private Map<String, Employee> employees = new HashMap<>();
    private Map<String, String> projects = new HashMap<>(); 
    private String loggedInUser;
    private Map<String, List<Activity>> activities = new HashMap<>();
    private Map<String, Set<String>> projectEmployees = new HashMap<>();

   // Ansvarlig: Younes
    public void addActivityToProject(String projectName, String activityName, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
        if (!projects.containsKey(projectName)) {
            throw new RuntimeException("Projektet findes ikke");
        }
        if (!isLoggedInUserProjectLeader(projectName)) {
            throw new RuntimeException("Kun projektlederen må tilføje aktiviteter");
        }
        activities.computeIfAbsent(projectName, k -> new ArrayList<>()).
                  add(new Activity(activityName, startWeek, startYear, endWeek, endYear, budgetedHours));
    }

    // Ansvarlig: Younes
    public List<Activity> getActivities(String projectName) {
        return activities.getOrDefault(projectName, new ArrayList<>());
    }

   // Ansvarlig: Ali
    public void addEmployee(Employee emp) {
        employees.putIfAbsent(emp.getInitials(), emp);
    }

   // Ansvarlig: Benjamin
    public boolean employeeExists(String initials) {
        return employees.containsKey(initials);
    }

   // Ansvarlig: Younes
    public void setLoggedInUser(String initials) {
        this.loggedInUser = initials;
    }

   // Ansvarlig: Younes
    public String getLoggedInUser() {
        return loggedInUser;
    }

  // Ansvarlig: Ali
    public void createProject(String name, String leaderInitials) {
        if (projects.containsKey(name)) {
            throw new RuntimeException("Projekt eksisterer allerede");
        }
        projects.put(name, leaderInitials);
        Employee leader = employees.get(leaderInitials);
        if (leader != null) {
            leader.setProjectLeader(true);
        }
    }

   // Ansvarlig: Younes
    public boolean projectExists(String name) {
        return projects.containsKey(name);
    }

    // Ansvarlig: Benjamin
    public String getProjectLeader(String name) {
        return projects.get(name);
    }

   // Ansvarlig: ALi
    public boolean isLoggedInUserProjectLeader(String projectName) {
        return getProjectLeader(projectName) != null &&
               getProjectLeader(projectName).equals(loggedInUser);
    }

   // Ansvarlig: Younes
    public List<String> getAllProjectNames() {
        return new ArrayList<>(projects.keySet());
    }

    // Ansvarlig: Benjamin
    public Set<String> getAllProjects() {
        return projects.keySet();
    }

   // Ansvarlig: Ali
    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }

   // Ansvarlig: Younes
    public void addEmployeeToProject(String projectName, String employeeInitials) {
        if (!projects.containsKey(projectName)) {
            throw new RuntimeException("Projektet findes ikke");
        }
        if (!employees.containsKey(employeeInitials)) {
            throw new RuntimeException("Medarbejderen findes ikke");
        }
    
        projectEmployees
            .computeIfAbsent(projectName, k -> new HashSet<>())
            .add(employeeInitials);
    }

   // Ansvarlig: Benjamin
    public boolean isEmployeePartOfProject(String projectName, String employeeInitials) {
        return projectEmployees.containsKey(projectName)
               && projectEmployees.get(projectName).contains(employeeInitials);
    }

// Ansvarlig: ALi
    public Employee getEmployeeByInitials(String initials) {
        return employees.get(initials);
    }
// Ansvarlig: Benjamin
    public Set<String> getProjectsForLoggedInUser() {
        Set<String> result = new HashSet<>();
    
        for (String projectName : projects.keySet()) {
            String leader = projects.get(projectName);
            Set<String> members = projectEmployees.getOrDefault(projectName, new HashSet<>());
    
            if (loggedInUser.equals(leader) || members.contains(loggedInUser)) {
                result.add(projectName);
            }
        }
    
        return result;
    }
    // Ansvarlig: ALi
    public boolean activityExistsInProject(String projectName, String activityName) {
    List<Activity> projectActivities = activities.getOrDefault(projectName, Collections.emptyList());
    return projectActivities.stream()
            .anyMatch(a -> a.getName().equalsIgnoreCase(activityName));
}
// Ansvarlig: Benjamin
public Map<String, List<String>> getEmployeeActivityAssignment(String projectName) {
    Map<String, List<String>> assignment = new HashMap<>();

    Set<String> allEmployees = projectEmployees.getOrDefault(projectName, Set.of());
    List<Activity> projectActivities = activities.getOrDefault(projectName, List.of());

    for (String initials : allEmployees) {
        List<String> assignedActivities = new ArrayList<>();

        for (Activity activity : projectActivities) {
            for (Employee emp : activity.getAssignedEmployees()) {
                if (emp.getInitials().equals(initials)) {
                    assignedActivities.add(activity.getName());
                    break;
                }
            }
        }

        if (assignedActivities.isEmpty()) {
            assignedActivities.add("ledig");
        }

        assignment.put(initials, assignedActivities);
    }

    return assignment;
}

}
