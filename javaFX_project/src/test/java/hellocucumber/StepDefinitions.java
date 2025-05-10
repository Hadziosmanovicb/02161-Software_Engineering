package hellocucumber;

import dtu.example.ui.domain.*;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StepDefinitions {
    private List<Activity> activitiesViewed;
    private String lastErrorMessage;
    private Employee tempEmp1;
    private Employee tempEmp2;
    private Collection<Employee> allEmployees;
    private Set<String> projekterBrugerKanSe;
    private boolean aktivitetEksistererSvar;
    private Map<String, List<String>> assignmentMap;
    private Map<String, List<String>> aktivitetstilknytning;
    private String oprettelsesFejlbesked;
  @ParameterType(".*")
    public LocalDate date(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    private final ProjectManager projectManager = new ProjectManager();
@Given("brugeren {string} er logget ind")
public void brugerenErLoggetInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
    assertEquals(initials, projectManager.getLoggedInUser());
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
public void brugerenErNuLoggetInd(String initials) {
    assertTrue(projectManager.employeeExists(initials));
    assertEquals(initials, projectManager.getLoggedInUser());
}

@Given("en medarbejder med initialerne {string} er registreret")
public void medarbejderErRegistreret(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    assertTrue(projectManager.employeeExists(initials));
}

@Given("brugeren forsøger at oprette en medarbejder med initialer {string}")
public void forsøgerAtOpretteMedarbejder(String initials) {
    try {
        new Employee(initials);
        oprettelsesFejlbesked = null;
    } catch (IllegalArgumentException e) {
        oprettelsesFejlbesked = e.getMessage();
    }
}

@Then("oprettes medarbejderen uden fejl")
public void medarbejderOprettetUdenFejl() {
    assertNull(oprettelsesFejlbesked);
}

@Then("får brugeren en fejlbesked {string}")
public void brugerenFårFejlbesked(String forventet) {
    assertEquals(forventet, oprettelsesFejlbesked);
}

@Then("medarbejderen med initialerne {string} er lig en anden med samme initialer")
public void medarbejdereErLig(String initials) {
    assertEquals(new Employee(initials), new Employee(initials));
}

@Then("medarbejderen med initialerne {string} er ikke lig medarbejderen med initialerne {string}")
public void medarbejdereErIkkeLig(String i1, String i2) {
    assertNotEquals(new Employee(i1), new Employee(i2));
}

@Then("medarbejderen med initialerne {string} er ikke lig en streng")
public void medarbejderErIkkeLigString(String initials) {
    assertNotEquals(new Employee(initials), "ikkeMedarbejder");
}

@Given("medarbejderen er logget ind som {string}")
public void medarbejderenLoggerInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}

@When("projektlederen opretter projektet {string}")
public void projektlederenOpretterProjekt(String projectName) {
    String leader = projectManager.getLoggedInUser();
    projectManager.createProject(projectName, leader);
}

@Then("medarbejderen {string} er projektleder")
public void medarbejderErProjektleder(String initials) {
    assertEquals(initials, projectManager.getProjectLeader("LederProjekt"));
}
@Then("projektet {string} findes")
public void projektetFindes(String projectName) {
    assertTrue(projectManager.projectExists(projectName));
}

@Then("projektlederen for projektet {string} er {string}")
public void projektlederenForProjektetEr(String projectName, String initials) {
    assertEquals(initials, projectManager.getProjectLeader(projectName));
}

@Given("en bruger med initialer {string} er logget ind")
public void enBrugerMedInitialerErLoggetInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}

@When("brugeren opretter projektet {string}")
public void brugerenOpretterProjektet(String projectName) {
    projectManager.createProject(projectName, projectManager.getLoggedInUser());
}

@Then("medarbejderen {string} er projektleder for {string}")
public void medarbejderenErProjektlederFor(String initials, String projectName) {
    assertEquals(initials, projectManager.getProjectLeader(projectName));
}

@When("projektlederen forsøger at oprette projektet {string}")
public void projektlederenForsøgerAtOpretteProjektet(String projectName) {
    try {
        projectManager.createProject(projectName, projectManager.getLoggedInUser());
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

@Then("får projektlederen en fejlbesked {string}")
public void fårProjektlederenEnFejlbesked(String expectedMessage) {
    assertEquals(expectedMessage, lastErrorMessage);
}

@When("brugeren forsøger at få projektlederen for projektet {string}")
public void brugerenForsøgerAtFåProjektlederenForProjektet(String projectName) {
}

@Then("får brugeren ingen projektleder")
public void fårBrugerenIngenProjektleder() {
    assertNull(projectManager.getProjectLeader("Ukendt Projekt"));
}

@Then("brugeren er ikke projektleder for projektet {string}")
public void brugerenErIkkeProjektlederForProjektet(String projectName) {
    assertNotEquals(projectManager.getLoggedInUser(), projectManager.getProjectLeader(projectName));
}

@When("brugeren henter alle projektnavne")
public void brugerenHenterAlleProjektnavne() {
    projekterBrugerKanSe = projectManager.getAllProjects();
}

@Then("listen indeholder {string} og {string}")
public void listenIndeholderOg(String p1, String p2) {
    assertTrue(projekterBrugerKanSe.contains(p1));
    assertTrue(projekterBrugerKanSe.contains(p2));
}

@Given("brugeren tilføjer medarbejderen {string} globalt, men ikke til projektet")
public void brugerenTilføjerGlobaltIkkeTilProjekt(String initials) {
    projectManager.addEmployee(new Employee(initials));
}

@Given("brugeren opretter aktiviteten {string} i projektet {string}")
public void brugerenOpretterAktiviteten(String activityName, String projectName) {
    projectManager.addActivityToProject(projectName, activityName, 1, 2024, 2, 2024, 10);
}

@When("brugeren forsøger at tildele {string} til aktiviteten {string} i {string}")
public void brugerenForsøgerAtTildele(String initials, String activityName, String projectName) {
    try {
        if (!projectManager.isEmployeePartOfProject(projectName, initials)) {
            throw new RuntimeException("Medarbejderen er ikke en del af projektet");
        }
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

@Then("vises en fejl om at medarbejderen ikke er en del af projektet")
public void fejlOmMedarbejderIkkeDelAfProjekt() {
    assertEquals("Medarbejderen er ikke en del af projektet", lastErrorMessage);
}

@Given("medarbejderen {string} findes i systemet")
public void medarbejderenFindes(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
}

@When("brugeren tilføjer {string} til projektet {string}")
public void brugerenTilføjerTilProjektet(String initials, String projectName) {
    projectManager.addEmployeeToProject(projectName, initials);
}

@Then("er medarbejderen {string} en del af projektet {string}")
public void erMedarbejderDelAfProjekt(String initials, String projectName) {
    assertTrue(projectManager.isEmployeePartOfProject(projectName, initials));
}
@Given("medarbejderen med initialerne {string} er logget ind")
public void medarbejderenMedInitialerneErLoggetInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}

@Given("projektlederen tilføjer en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void projektlederenTilføjerAktivitet(String activity, String project, int sw, int sy, int ew, int ey, int budget) {
    projectManager.addActivityToProject(project, activity, sw, sy, ew, ey, budget);
}

@Given("projektlederen tildeler medarbejderen {string} til aktiviteten {string} i projektet {string}")
public void projektlederenTildelerMedarbejderen(String initials, String activity, String project) {
    Activity a = projectManager.getActivities(project).stream()
        .filter(act -> act.getName().equals(activity)).findFirst().orElseThrow();
    a.assignEmployee(projectManager.getEmployeeByInitials(initials));
}

@Then("projektet {string} indeholder aktiviteten {string}")
public void projektetIndeholderAktiviteten(String project, String activity) {
    assertTrue(projectManager.activityExistsInProject(project, activity));
}

@Then("aktiviteten {string} har startuge {int} og startår {int}")
public void aktivitetHarStartDato(String activityName, int expectedWeek, int expectedYear) {
    Activity a = findActivity(activityName);
    assertEquals(expectedWeek, a.getStartWeek());
    assertEquals(expectedYear, a.getStartYear());
}

@Then("aktiviteten {string} har slutuge {int} og slutår {int}")
public void aktivitetHarSlutDato(String activityName, int expectedWeek, int expectedYear) {
    Activity a = findActivity(activityName);
    assertEquals(expectedWeek, a.getEndWeek());
    assertEquals(expectedYear, a.getEndYear());
}

@Then("aktiviteten {string} har {int} budgetterede timer")
public void aktivitetHarBudget(String activityName, int expected) {
    Activity a = findActivity(activityName);
    assertEquals(expected, a.getBudgetedHours());
}

@When("medarbejderen {string} registrerer {int} timer den {date} på {string} i {string}")
public void medarbejderenRegistrererTid(String initials, int hours, LocalDate date, String activity, String project) {
    Activity a = findActivityInProject(project, activity);
    a.registerTime(projectManager.getEmployeeByInitials(initials), date, hours);
}

@Then("der er registreret {int} timer for {string} på {string} den {date}")
public void registreretTidPåDato(int expected, String initials, String activity, LocalDate date) {
    Activity a = findActivity(activity);
    assertEquals(expected, a.getRegisteredTimeOnDate(initials, date));
}

@Then("færdiggørelsesprocenten for {string} er {int}")
public void færdiggørelseProcent(String activity, int expectedPercent) {
    assertEquals(expectedPercent, findActivity(activity).getCompletionPercentage());
}

@When("medarbejderen {string} markerer aktiviteten {string} i {string} som færdig")
public void markerSomFærdig(String initials, String activity, String project) {
    findActivityInProject(project, activity).setStatus("Afventer");
}

@Then("status for aktiviteten {string} i {string} er {string}")
public void statusEr(String activity, String project, String status) {
    assertEquals(status, findActivityInProject(project, activity).getStatus());
}

@When("projektlederen afviser aktiviteten {string} i {string}")
public void projektlederAfviser(String activity, String project) {
    findActivityInProject(project, activity).setStatus("Afvist");
}

@When("projektlederen godkender aktiviteten {string} i {string}")
public void projektlederGodkender(String activity, String project) {
    findActivityInProject(project, activity).setStatus("Godkendt");
}

@Then("aktiviteten {string} er markeret som færdig")
public void aktivitetErFærdig(String activity) {
    assertTrue(findActivity(activity).isCompleted());
}

@When("medarbejderen forsøger at tilføje en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void ikkeLederForsøgerTilføjelse(String activity, String project, int sw, int sy, int ew, int ey, int budget) {
    try {
        projectManager.addActivityToProject(project, activity, sw, sy, ew, ey, budget);
        lastErrorMessage = null;
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

@Then("får medarbejderen en fejlbesked {string}")
public void fejlbesked(String forventet) {
    assertEquals(forventet, lastErrorMessage);
}

@When("projektlederen forsøger at tilføje en aktivitet {string} til projektet {string} med startuge {int} startår {int} slutuge {int} slutår {int} og budget {int} timer")
public void projektlederFejlVedOprettelse(String activity, String project, int sw, int sy, int ew, int ey, int budget) {
    try {
        projectManager.addActivityToProject(project, activity, sw, sy, ew, ey, budget);
        lastErrorMessage = null;
    } catch (RuntimeException e) {
        lastErrorMessage = e.getMessage();
    }
}

private Activity findActivity(String name) {
    return projectManager.getAllProjects().stream()
        .flatMap(p -> projectManager.getActivities(p).stream())
        .filter(a -> a.getName().equals(name)).findFirst().orElseThrow();
}

private Activity findActivityInProject(String project, String activity) {
    return projectManager.getActivities(project).stream()
        .filter(a -> a.getName().equals(activity)).findFirst().orElseThrow();
}
@When("medarbejderen {string} indrapporterer {int} timer den {date} på {string} i {string}")
public void medarbejderenIndrapportererTimer(String initials, int timer, LocalDate dato, String aktivitet, String projekt) {
    Activity a = findActivityInProject(projekt, aktivitet);
    if (!a.isCompleted()) {
        a.registerTime(projectManager.getEmployeeByInitials(initials), dato, timer);
    }
}

@Then("summen af registrerede timer for {string} på {string} er {int}")
public void summenAfRegistreredeTimer(String initials, String aktivitet, int forventet) {
    assertEquals(forventet, findActivity(aktivitet).getRegisteredTime(initials));
}

@Given("aktiviteten {string} i {string} har status {string}")
public void aktivitetenHarStatus(String aktivitet, String projekt, String status) {
    findActivityInProject(projekt, aktivitet).setStatus(status);
}

@When("medarbejderen forsøger at indrapportere {int} timer den {date} på {string} i {string}")
public void medarbejderForsøgerIndrapportere(int timer, LocalDate dato, String aktivitet, String projekt) {
    Activity a = findActivityInProject(projekt, aktivitet);
    if (!a.isCompleted()) {
        a.registerTime(projectManager.getEmployeeByInitials(projectManager.getLoggedInUser()), dato, timer);
    }
}

@When("medarbejderen anmoder om rapport for projektet {string}")
public void medarbejderenAnmoderOmRapport(String projekt) {
    ProjectReportGenerator prg = new ProjectReportGenerator(projectManager);
    lastErrorMessage = prg.generateReport(projekt);
}

@Then("vises fejlen {string}")
public void visesFejlen(String forventetFejl) {
    assertEquals(forventetFejl, lastErrorMessage);
}

@When("medarbejderen forsøger at generere rapport for {string}")
public void medarbejderForsøgerGenerereRapport(String projekt) {
    ProjectReportGenerator prg = new ProjectReportGenerator(projectManager);
    lastErrorMessage = prg.generateReport(projekt);
}

@Then("får brugeren besked {string}")
public void fårBrugerenBesked(String besked) {
    assertEquals(besked, lastErrorMessage);
}

@When("rapport genereres for {string}")
public void rapportGenereres(String projekt) {
    ProjectReportGenerator prg = new ProjectReportGenerator(projectManager);
    lastErrorMessage = prg.generateReport(projekt);
}

@Then("rapporten indeholder:")
public void rapportenIndeholder(String forventetRapport) {
    assertEquals(forventetRapport.trim(), lastErrorMessage.trim());
}

@And("en aktivitet {string} med {int} timer tilføjes projektet")
public void aktivitetTilføjesProjekt(String aktivitet, int timer) {
    projectManager.addActivityToProject(projectManager.getAllProjectNames().stream().reduce((first, second) -> second).orElseThrow(), aktivitet, 1, 2025, 2, 2025, timer);
}

@And("medarbejderen registrerer {int} timer på {string} den {date}")
public void medarbejderRegistrererTimerPåDato(int timer, String aktivitet, LocalDate dato) {
    String initials = projectManager.getLoggedInUser();
    Activity a = findActivity(aktivitet);
    a.registerTime(projectManager.getEmployeeByInitials(initials), dato, timer);
}
@Given("projektlederen {string} opretter projektet {string}")
public void projektlederenOpretterProjektet(String leder, String projekt) {
    if (!projectManager.employeeExists(leder)) {
        projectManager.addEmployee(new Employee(leder));
    }
    projectManager.setLoggedInUser(leder);
    projectManager.createProject(projekt, leder);
}
@When("medarbejderen {string} forsøger at indrapportere {int} timer den {int}-{int}-{int} på {string} i {string}")
public void medarbejderenForsøgerAtIndrapportereTimerDenPåI(String initials, Integer timer, Integer year, Integer month, Integer day, String aktivitet, String projekt) {
    LocalDate dato = LocalDate.of(year, month, day);
    Activity a = findActivityInProject(projekt, aktivitet);
    if (!a.isCompleted()) {
        a.registerTime(projectManager.getEmployeeByInitials(initials), dato, timer);
    }
}

@Then("summen af registrerede timer for {string} på {string} er stadig {int}")
public void summenAfRegistreredeTimerForPåErStadig(String initials, String aktivitet, Integer forventet) {
    assertEquals(forventet.intValue(), findActivity(aktivitet).getRegisteredTime(initials));
}
@Given("medarbejderen {string} er logget ind")
public void medarbejderenErLoggetIndAlias(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}
@Given("medarbejderen {string} opretter projektet {string}")
public void medarbejderenOpretterProjektet(String initials, String projekt) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
    projectManager.createProject(projekt, initials);
}
@Given("medarbejderen logger ud")
public void medarbejderenLoggerUd() {
    projectManager.setLoggedInUser(null);
}
@Given("en anden medarbejder {string} logger ind")
public void enAndenMedarbejderLoggerInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}
@When("brugeren beder om at se sine projekter")
public void brugerenBederOmAtSeSineProjekter() {
    projekterBrugerKanSe = projectManager.getProjectsForLoggedInUser();
}

@Then("indeholder listen {string}")
public void indeholderListen(String projektnavn) {
    assertTrue(projekterBrugerKanSe.contains(projektnavn));
}

@When("brugeren henter aktivitetstilknytningerne for {string}")
public void brugerenHenterAktivitetstilknytninger(String projekt) {
    aktivitetstilknytning = projectManager.getEmployeeActivityAssignment(projekt);
}

@Then("vises {string} som {string}")
public void visesSomStatus(String medarbejder, String status) {
    assertTrue(aktivitetstilknytning.containsKey(medarbejder));
    assertEquals(List.of(status), aktivitetstilknytning.get(medarbejder));
}

@Then("vises {string} med aktiviteterne {string}")
public void visesMedEnAktivitet(String medarbejder, String aktivitet) {
    assertTrue(aktivitetstilknytning.containsKey(medarbejder));
    assertEquals(List.of(aktivitet), aktivitetstilknytning.get(medarbejder));
}

@Then("vises {string} med aktiviteterne {string}, {string}")
public void visesMedFlereAktiviteter(String medarbejder, String a1, String a2) {
    assertTrue(aktivitetstilknytning.containsKey(medarbejder));
    assertTrue(aktivitetstilknytning.get(medarbejder).containsAll(List.of(a1, a2)));
}

@When("brugeren tjekker om aktiviteten {string} findes i {string}")
public void brugerenTjekkerOmAktivitetenFindes(String aktivitet, String projekt) {
    aktivitetEksistererSvar = projectManager.activityExistsInProject(projekt, aktivitet);
}

@Then("får brugeren svaret {string}")
public void fårBrugerenSvaret(String forventet) {
    assertEquals(Boolean.parseBoolean(forventet), aktivitetEksistererSvar);
}
@Given("brugeren logger ud")
public void brugerenLoggerUd() {
    projectManager.setLoggedInUser(null);
}
@Given("brugeren med initialer {string} logger ind")
public void brugerenMedInitialerLoggerInd(String initials) {
    if (!projectManager.employeeExists(initials)) {
        projectManager.addEmployee(new Employee(initials));
    }
    projectManager.setLoggedInUser(initials);
}
@Given("brugeren tildeler {string} til aktiviteten {string} i projektet {string}")
public void brugerenTildelerTilAktivitetenIProjektet(String medarbejder, String aktivitet, String projekt) {
    Activity a = projectManager.getActivities(projekt).stream()
        .filter(act -> act.getName().equals(aktivitet))
        .findFirst().orElseThrow();
    a.assignEmployee(projectManager.getEmployeeByInitials(medarbejder));
}
@Given("projektlederen tilføjer en aktivitet {string} til projektet {string} med startuge {int} og startår {int} og slutuge {int} og slutår {int} og {int} budgetterede timer")
public void projektlederenTilføjerAktivitet(String a, String p, Integer sw, Integer sy, Integer ew, Integer ey, Integer b) {
    projectManager.addActivityToProject(p, a, sw, sy, ew, ey, b);
}

@Then("medarbejderen {string} er tilføjet til aktiviteten {string}")
public void medarbejderErTilføjet(String emp, String a) {
    assertTrue(findActivity(a).getAssignedEmployees().stream().anyMatch(e -> e.getInitials().equals(emp)));
}

@When("brugeren henter alle medarbejdere")
public void brugerenHenterAlleMedarbejdere() {
    allEmployees = projectManager.getAllEmployees();
}

@Then("systemet indeholder {string} og {string}")
public void systemetIndeholder(String e1, String e2) {
    Set<String> ids = allEmployees.stream().map(Employee::getInitials).collect(java.util.stream.Collectors.toSet());
    assertTrue(ids.containsAll(List.of(e1, e2)));
}

@Given("projektlederen {string} har oprettet projektet {string}")
public void projektlederenHarOprettet(String leader, String proj) {
    if (!projectManager.employeeExists(leader)) projectManager.addEmployee(new Employee(leader));
    projectManager.setLoggedInUser(leader);
    if (!projectManager.projectExists(proj)) projectManager.createProject(proj, leader);
}

@Given("en aktivitet {string} er oprettet i projektet {string} med start uge {int}, år {int} og slut uge {int}, år {int} og {int} timer")
public void aktivitetOprettet(String act, String proj, Integer sw, Integer sy, Integer ew, Integer ey, Integer b) {
    projectManager.addActivityToProject(proj, act, sw, sy, ew, ey, b);
}

@Given("medarbejderen {string} er tilføjet til aktiviteten {string} i projektet {string}")
public void medarbejderTilføjet(String emp, String act, String proj) {
    if (!projectManager.employeeExists(emp)) projectManager.addEmployee(new Employee(emp));
    findActivityInProject(proj, act).assignEmployee(projectManager.getEmployeeByInitials(emp));
}

@When("brugeren åbner projektet {string}")
public void brugerenÅbnerProjektet(String proj) {
    String user = projectManager.getLoggedInUser();
    String leader = projectManager.getProjectLeader(proj);
    activitiesViewed = (leader != null && leader.equals(user))
            ? projectManager.getActivities(proj)
            : projectManager.getActivities(proj).stream()
                .filter(a -> a.getAssignedEmployees().stream()
                        .anyMatch(e -> e.getInitials().equals(user)))
                .toList();
}

@Then("skal brugeren kunne se aktiviteten {string}")
public void skalSeAktivitet(String act) {
    assertTrue(activitiesViewed.stream().anyMatch(a -> a.getName().equals(act)));
}
@Then("{string} er projektleder")
public void erProjektleder(String initials) {
    Employee employee = projectManager.getEmployeeByInitials(initials);
    assertTrue(employee.isProjectLeader(), initials + " er ikke markeret som projektleder");
}

}
