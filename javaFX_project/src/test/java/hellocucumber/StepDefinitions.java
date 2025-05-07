package hellocucumber;

import dtu.example.ui.domain.*;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

public class StepDefinitions {
    private List<Activity> activitiesViewed;
    private String lastErrorMessage;
    private Employee tempEmp1;
    private Employee tempEmp2;
    private Collection<Employee> allEmployees;

  @ParameterType(".*")
    public LocalDate date(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private final ProjectManager projectManager = new ProjectManager();

    @Given("en medarbejder med initialerne {string} er registreret")
    public void enMedarbejderErRegistreret(String initials) {
        projectManager.addEmployee(new Employee(initials));
        assertTrue(projectManager.employeeExists(initials));
    }

    @And("medarbejderen er logget ind som {string}")
    public void medarbejderenErLoggetInd(String initials) {
        projectManager.setLoggedInUser(initials);
        assertEquals(initials, projectManager.getLoggedInUser());
    }

    @When("projektlederen opretter projektet {string}")
public void projektlederenOpretterProjekt(String projectName) {
    String leader = projectManager.getLoggedInUser();
    if (!projectManager.projectExists(projectName)) {
        projectManager.createProject(projectName, leader);
    }
    assertTrue(projectManager.projectExists(projectName));
}


    @Then("projektet {string} findes")
    public void projektetFindes(String projectName) {
        assertTrue(projectManager.projectExists(projectName));
    }

    @And("projektlederen for projektet {string} er {string}")
    public void projektlederenEr(String projectName, String expectedLeader) {
        assertEquals(expectedLeader, projectManager.getProjectLeader(projectName));
    }

    @Given("brugeren med initialer {string} ikke findes i systemet")
public void brugerenFindesIkke(String initials) {
    assertFalse(projectManager.employeeExists(initials));
}

@When("brugeren logger ind med initialer {string}")
public void brugerenLoggerInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}

@Then("brugeren {string} bliver oprettet og er logget ind")
public void brugerenBliverOprettetOgErLoggetInd(String initials) {
    assertTrue(projectManager.employeeExists(initials));
    assertEquals(initials, projectManager.getLoggedInUser());
}

@Then("brugeren {string} er logget ind")
public void brugerenErLoggetInd(String initials) {
    assertEquals(initials, projectManager.getLoggedInUser());
}

@When("projektlederen tilføjer en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void projektlederenTilføjerEnAktivitet(
        String activityName, String projectName,
        int startWeek, int startYear, int endWeek, int endYear, int budgetHours) {

    assertTrue(projectManager.isLoggedInUserProjectLeader(projectName));
    projectManager.addActivityToProject(projectName, activityName, startWeek, startYear, endWeek, endYear, budgetHours);
}

@Then("projektet {string} indeholder aktiviteten {string}")
public void projektetIndeholderAktiviteten(String projectName, String activityName) {
    boolean found = projectManager.getActivities(projectName).stream()
            .anyMatch(a -> a.getName().equals(activityName));
    assertTrue(found, "Aktiviteten blev ikke fundet i projektet");
}

@And("aktiviteten {string} har {int} budgetterede timer")
public void aktivitetenHarBudgetteredeTimer(String activityName, int expectedHours) {
    boolean correct = projectManager.getAllProjects().stream()
            .flatMap(p -> projectManager.getActivities(p).stream())
            .filter(a -> a.getName().equals(activityName))
            .anyMatch(a -> a.getBudgetedHours() == expectedHours);
    assertTrue(correct, "Timer matcher ikke for aktiviteten");
}

@And("projektlederen tilføjer en aktivitet {string} til projektet {string} med startuge {int} og startår {int} og slutuge {int} og slutår {int} og {int} budgetterede timer")
public void projektlederenTilføjerAktivitet(String activityName, String projectName, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
    projectManager.addActivityToProject(projectName, activityName, startWeek, startYear, endWeek, endYear, budgetedHours);
    List<Activity> acts = projectManager.getActivities(projectName);
    assertTrue(acts.stream().anyMatch(a -> a.getName().equals(activityName)));
}

@When("projektlederen tildeler medarbejderen {string} til aktiviteten {string} i projektet {string}")
public void projektlederenTildelerMedarbejder(String initials, String activityName, String projectName) {
    Activity activity = projectManager.getActivities(projectName).stream()
        .filter(a -> a.getName().equals(activityName))
        .findFirst()
        .orElseThrow();
    Employee emp = new Employee(initials);
    activity.assignEmployee(emp);
}

@Then("medarbejderen {string} er tilføjet til aktiviteten {string}")
public void medarbejderenErTilføjet(String initials, String activityName) {
    boolean found = projectManager.getAllProjects().stream()
        .flatMap(p -> projectManager.getActivities(p).stream())
        .filter(a -> a.getName().equals(activityName))
        .anyMatch(a -> a.getAssignedEmployees().stream().anyMatch(e -> e.getInitials().equals(initials)));
    assertTrue(found);
}
@And("projektlederen {string} har oprettet projektet {string}")
public void projektlederenHarOprettetProjekt(String leaderInitials, String projectName) {
    projectManager.addEmployee(new Employee(leaderInitials));
    projectManager.setLoggedInUser(leaderInitials);
    projectManager.createProject(projectName, leaderInitials);
}

@And("en aktivitet {string} er oprettet i projektet {string} med start uge {int}, år {int} og slut uge {int}, år {int} og {int} timer")
public void aktivitetOprettet(String activityName, String project, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
    projectManager.addActivityToProject(project, activityName, startWeek, startYear, endWeek, endYear, budgetedHours);
}

@And("medarbejderen {string} er tilføjet til aktiviteten {string} i projektet {string}")
public void medarbejderTilføjetTilAktivitet(String initials, String activityName, String project) {
    Employee emp = new Employee(initials);
    projectManager.addEmployee(emp);
    for (Activity a : projectManager.getActivities(project)) {
        if (a.getName().equals(activityName)) {
            a.assignEmployee(emp);
        }
    }
}

@When("medarbejderen {string} registrerer {int} timer den {date} på {string} i {string}")
public void medarbejderRegistrererTid(String initials, int hours, LocalDate date, String activityName, String project) {
    for (Activity a : projectManager.getActivities(project)) {
        if (a.getName().equals(activityName)) {
            a.registerTime(new Employee(initials), date, hours);
        }
    }
}

@Then("medarbejderen {string} har registreret {int} timer på aktiviteten {string}")
public void medarbejderenHarRegistreretTimer(String initials, int expectedHours, String activityName) {
    int actual = -1;
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                actual = a.getRegisteredTime(initials);
            }
        }
    }
    assertEquals(expectedHours, actual);
}


@Given("aktiviteten {string} i projektet {string} er godkendt")
public void aktivitetErGodkendt(String activityName, String project) {
    for (Activity a : projectManager.getActivities(project)) {
        if (a.getName().equals(activityName)) {
            a.setStatus("Godkendt");
        }
    }
}
@Then("der er registreret {int} timer for {string} på {string}")
public void derErRegistreretTimerForPå(Integer forventedeTimer, String medarbejderInitialer, String aktivitetNavn) {
    int faktiskeTimer = -1;
    for (String projekt : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(projekt)) {
            if (a.getName().equals(aktivitetNavn)) {
                faktiskeTimer = a.getRegisteredTime(medarbejderInitialer);
            }
        }
    }
    assertEquals(forventedeTimer.intValue(), faktiskeTimer);
}

@Then("der er stadig kun {int} timer registreret for {string} på {string}")
public void derErStadigKunTimerRegistreretForPå(Integer forventedeTimer, String medarbejderInitialer, String aktivitetNavn) {
    derErRegistreretTimerForPå(forventedeTimer, medarbejderInitialer, aktivitetNavn);
}

@When("medarbejderen {string} forsøger at registrere {int} timer den {date} på {string} i {string}")
public void medarbejderenForsøgerRegistrereTid(String initials, int hours, LocalDate date, String activityName, String project) {
    for (Activity a : projectManager.getActivities(project)) {
        if (a.getName().equals(activityName)) {
            if (!"Godkendt".equals(a.getStatus())) {
                a.registerTime(new Employee(initials), date, hours);
            }
        }
    }
}
@When("brugeren åbner projektet {string}")
public void brugerenAabnerProjektet(String projectName) {
   
    activitiesViewed = projectManager.getActivities(projectName);
}

@Then("skal brugeren kunne se aktiviteten {string}")
public void skalBrugerenKunneSeAktivitet(String activityName) {
    boolean found = activitiesViewed.stream()
            .anyMatch(a -> a.getName().equals(activityName));
    assertTrue(found, "Aktiviteten '" + activityName + "' blev ikke fundet i visningen");
}
@When("medarbejderen {string} markerer aktiviteten {string} i {string} som færdig")
public void medarbejderMarkererFærdig(String initials, String activityName, String projectName) {
    for (Activity a : projectManager.getActivities(projectName)) {
        if (a.getName().equals(activityName)) {
            if (a.getAssignedEmployees().stream().anyMatch(e -> e.getInitials().equals(initials))) {
                if (!"Godkendt".equals(a.getStatus())) {
                    a.setStatus("Afventer");
                }
            }
        }
    }
}

@When("projektlederen godkender aktiviteten {string} i {string}")
public void projektlederGodkenderAktivitet(String activityName, String projectName) {
    for (Activity a : projectManager.getActivities(projectName)) {
        if (a.getName().equals(activityName) && "Afventer".equals(a.getStatus())) {
            a.setStatus("Godkendt");
        }
    }
}

@When("projektlederen afviser aktiviteten {string} i {string}")
public void projektlederAfviserAktivitet(String activityName, String projectName) {
    for (Activity a : projectManager.getActivities(projectName)) {
        if (a.getName().equals(activityName) && "Afventer".equals(a.getStatus())) {
            a.setStatus("Afvist");
        }
    }
}


@Given("status for aktiviteten {string} i {string} er {string}")
public void statusForAktivitetErGivet(String activityName, String projectName, String status) {
    for (Activity a : projectManager.getActivities(projectName)) {
        if (a.getName().equals(activityName)) {
            a.setStatus(status);
            return;
        }
    }
    fail("Aktiviteten '" + activityName + "' blev ikke fundet i projektet '" + projectName + "'");
}

@Then("aktiviteten {string} har startuge {int} og startår {int}")
public void aktivitetenHarStartugeOgStartår(String activityName, int expectedWeek, int expectedYear) {
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                assertEquals(expectedWeek, a.getStartWeek());
                assertEquals(expectedYear, a.getStartYear());
            }
        }
    }
}

@Then("aktiviteten {string} har slutuge {int} og slutår {int}")
public void aktivitetenHarSlutugeOgSlutår(String activityName, int expectedWeek, int expectedYear) {
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                assertEquals(expectedWeek, a.getEndWeek());
                assertEquals(expectedYear, a.getEndYear());
            }
        }
    }
}

@Then("der er registreret {int} timer for {string} på {string} den {date}")
public void registreretTidPåDato(int expectedHours, String initials, String activityName, LocalDate date) {
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                int actual = a.getRegisteredTimeOnDate(initials, date);
                assertEquals(expectedHours, actual);
            }
        }
    }
}

@Then("færdiggørelsesprocenten for {string} er {int}")
public void færdiggørelsesprocentEr(String activityName, int expectedPercentage) {
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                assertEquals(expectedPercentage, a.getCompletionPercentage());
            }
        }
    }
}

@Then("aktiviteten {string} er markeret som færdig")
public void aktivitetenErMarkeretSomFærdig(String activityName) {
    for (String project : projectManager.getAllProjects()) {
        for (Activity a : projectManager.getActivities(project)) {
            if (a.getName().equals(activityName)) {
                assertTrue(a.isCompleted());
            }
        }
    }
}

@When("projektlederen forsøger at tilføje en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void projektlederenForsøgerTilføjeAktivitetTilUgyldigtProjekt(String activityName, String projectName, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
    try {
        projectManager.addActivityToProject(projectName, activityName, startWeek, startYear, endWeek, endYear, budgetedHours);
        fail("Forventede en fejl fordi projektet ikke findes");
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

@When("medarbejderen forsøger at tilføje en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void medarbejderForsøgerTilføjeAktivitet(String activityName, String projectName, int startWeek, int startYear, int endWeek, int endYear, int budgetedHours) {
    try {
        projectManager.addActivityToProject(projectName, activityName, startWeek, startYear, endWeek, endYear, budgetedHours);
        fail("Forventede en fejl fordi medarbejderen ikke er projektleder");
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

@Then("får projektlederen en fejlbesked {string}")
@Then("får medarbejderen en fejlbesked {string}")
public void fårEnFejlbesked(String forventetBesked) {
    assertEquals(forventetBesked, lastErrorMessage);
}
@When("projektlederen forsøger at oprette projektet {string}")
public void projektlederenForsøgerAtOpretteProjektet(String projectName) {
    try {
        projectManager.createProject(projectName, projectManager.getLoggedInUser());
        fail("Forventede fejl fordi projektet allerede eksisterer");
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}
@Then("medarbejderen med initialerne {string} er lig en anden med samme initialer")
public void medarbejdereErLig(String initials) {
    tempEmp1 = new Employee(initials);
    tempEmp2 = new Employee(initials);
    assertEquals(tempEmp1, tempEmp2);
    assertEquals(tempEmp1.hashCode(), tempEmp2.hashCode());
}

@Then("medarbejderen med initialerne {string} er ikke lig medarbejderen med initialerne {string}")
public void medarbejdereErIkkeLig(String initials1, String initials2) {
    tempEmp1 = new Employee(initials1);
    tempEmp2 = new Employee(initials2);
    assertNotEquals(tempEmp1, tempEmp2);
}
@Then("medarbejderen med initialerne {string} er ikke lig en streng")
public void medarbejderIkkeLigString(String initials) {
    Employee emp = new Employee(initials);
    String notAnEmployee = "notAnEmployee";
    assertNotEquals(emp, notAnEmployee);
}
@Then("medarbejderen {string} er projektleder")
public void medarbejderenErProjektleder(String initials) {
    Employee emp = new Employee(initials);
    emp.setProjectLeader(true);
    assertTrue(emp.isProjectLeader());
}
private String fetchedProjectLeader;

@When("brugeren forsøger at få projektlederen for projektet {string}")
public void forsøgerAtHenteProjektleder(String projectName) {
    fetchedProjectLeader = projectManager.getProjectLeader(projectName);
}

@Then("får brugeren ingen projektleder")
public void fårIngenProjektleder() {
    assertNull(fetchedProjectLeader);
}
@Then("brugeren er ikke projektleder for projektet {string}")
public void brugerErIkkeProjektleder(String projectName) {
    assertFalse(projectManager.isLoggedInUserProjectLeader(projectName));
}
private List<String> projectNames;

@When("brugeren henter alle projektnavne")
public void hentAlleProjektNavne() {
    projectNames = projectManager.getAllProjectNames();
}

@Then("listen indeholder {string} og {string}")
public void listenIndeholderBegge(String p1, String p2) {
    assertTrue(projectNames.contains(p1));
    assertTrue(projectNames.contains(p2));
}
@When("brugeren henter alle medarbejdere")
public void hentAlleMedarbejdere() {
    allEmployees = projectManager.getAllEmployees();
}

@Then("systemet indeholder {string} og {string}")
public void medarbejderListenIndeholderBegge(String i1, String i2) {
    assertTrue(allEmployees.stream().anyMatch(e -> e.getInitials().equals(i1)));
    assertTrue(allEmployees.stream().anyMatch(e -> e.getInitials().equals(i2)));
}
}