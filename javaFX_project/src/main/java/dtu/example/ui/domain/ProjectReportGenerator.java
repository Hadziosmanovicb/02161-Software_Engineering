
package dtu.example.ui.domain;

import java.util.List;

public class ProjectReportGenerator {

    private ProjectManager projectManager;

    public ProjectReportGenerator(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public String generateReport(String projectName) {
        if (!projectManager.projectExists(projectName)) {
            return "Projektet findes ikke.";
        }

        if (!projectManager.isLoggedInUserProjectLeader(projectName)) {
            return "Kun projektlederen må generere en rapport for projektet.";
        }

        List<Activity> activities = projectManager.getActivities(projectName);
        if (activities.isEmpty()) {
            return "Projektet har ingen aktiviteter.";
        }

        StringBuilder report = new StringBuilder("Rapport for projekt '" + projectName + "':\n");

        for (Activity activity : activities) {
            report.append("- Aktivitet: ").append(activity.getName()).append("\n");
            report.append("  Budgetterede timer: ").append(activity.getBudgetedHours()).append("\n");
            report.append("  Forbrugte timer: ").append(activity.getCompletionPercentage())
                  .append("% (").append(totalLoggedHours(activity)).append(" timer)\n\n");
        }

        return report.toString();
    }

    private int totalLoggedHours(Activity activity) {
        return activity.getCompletionPercentage() * activity.getBudgetedHours() / 100;
    }
}
