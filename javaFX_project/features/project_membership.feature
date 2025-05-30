# Filnavn: project_membership.feature
# Relaterede filer: ProjectManager.java, Employee.java
# Formål:
# Tester oprettelse af projekter, automatisk tildeling af projektleder, håndtering
# af duplikerede projektnavne samt tildeling og validering af projektmedlemskab.
# Sikrer at kun registrerede medarbejdere kan tilknyttes aktiviteter.

Feature: Projektoprettelse og medlemskab
# Ansvarlig: Benjamin
    Scenario: En projektleder opretter et nyt projekt
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    When projektlederen opretter projektet "SuperProjekt"
    Then projektet "SuperProjekt" findes
    And projektlederen for projektet "SuperProjekt" er "abc"
    And "abc" er projektleder
# Ansvarlig: Younes
  Scenario: Projektlederen bliver automatisk tilføjet til sit eget projekt
    Given en bruger med initialer "abc" er logget ind
    When brugeren opretter projektet "ProjektX"
    Then medarbejderen "abc" er projektleder for "ProjektX"
# Ansvarlig: Ali
  Scenario: Fejl hvis man forsøger at oprette et projekt der allerede findes
    Given en medarbejder med initialerne "aaa" er registreret
    And medarbejderen er logget ind som "aaa"
    And projektlederen opretter projektet "Eksisterende Projekt"
    When projektlederen forsøger at oprette projektet "Eksisterende Projekt"
    Then får projektlederen en fejlbesked "Projekt eksisterer allerede"
# Ansvarlig: Benjamin
  Scenario: Forespørgsel på projektleder for ikke-eksisterende projekt
    Given en medarbejder med initialerne "huba" er registreret
    And medarbejderen er logget ind som "huba"
    When brugeren forsøger at få projektlederen for projektet "Ukendt Projekt"
    Then får brugeren ingen projektleder
# Ansvarlig: Younes
  Scenario: Brugeren forsøger at hente projektleder på ikke-eksisterende projekt
    Given en medarbejder med initialerne "fake" er registreret
    And medarbejderen er logget ind som "fake"
    Then brugeren er ikke projektleder for projektet "UgyldigtProjekt"
# Ansvarlig: Ali
  Scenario: Hent alle projektnavne
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    And projektlederen opretter projektet "Alpha"
    And projektlederen opretter projektet "Beta"
    When brugeren henter alle projektnavne
    Then listen indeholder "Alpha" og "Beta"
# Ansvarlig: Ali
  Scenario: Kan ikke tildele medarbejder som ikke er en del af projektet
    Given en bruger med initialer "led" er logget ind
    And brugeren opretter projektet "ProjektY"
    And brugeren tilføjer medarbejderen "emp" globalt, men ikke til projektet
    And brugeren opretter aktiviteten "Aktiv1" i projektet "ProjektY"
    When brugeren forsøger at tildele "emp" til aktiviteten "Aktiv1" i "ProjektY"
    Then vises en fejl om at medarbejderen ikke er en del af projektet
# Ansvarlig: Benjamin
  Scenario: Tilføj medarbejder til projekt og verificer medlemskab
    Given en bruger med initialer "led" er logget ind
    And brugeren opretter projektet "ProjektZ"
    And medarbejderen "emp" findes i systemet
    When brugeren tilføjer "emp" til projektet "ProjektZ"
    Then er medarbejderen "emp" en del af projektet "ProjektZ"
