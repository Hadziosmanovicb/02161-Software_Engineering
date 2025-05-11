# Filnavn: register_time_project_rapport.feature
# Relaterede filer: Activity.java, ProjectManager.java, ProjectReportGenerator.java
# Formål:
# Tester registrering af arbejdstid på aktiviteter og generering af projektrapporter.
# Indeholder scenarier for korrekt og forhindret tidsregistrering samt betingelser
# for at generere en rapport, herunder adgangskontrol og fejlbeskeder.

Feature: Tidsregistrering og projektrapport
# Ansvarlig: Younes
  Background:
    Given en medarbejder med initialerne "ab" er registreret
    And medarbejderen med initialerne "ab" er logget ind
    And projektlederen "ab" opretter projektet "ProjektX"
    And projektlederen tilføjer en aktivitet "Dokumentation" til projektet "ProjektX" med startuge 10 startår 2025 slutuge 20 slutår 2025 og budget 50 timer
    And projektlederen tildeler medarbejderen "ab" til aktiviteten "Dokumentation" i projektet "ProjektX"
# Ansvarlig: Benjamin
  Scenario: Medarbejder registrerer tid på aktivitet
    When medarbejderen "ab" indrapporterer 5 timer den 2025-05-01 på "Dokumentation" i "ProjektX"
    Then summen af registrerede timer for "ab" på "Dokumentation" er 5
# Ansvarlig: Ali
  Scenario: Ingen yderligere registrering hvis aktiviteten er godkendt
    When medarbejderen "ab" indrapporterer 5 timer den 2025-05-01 på "Dokumentation" i "ProjektX"
    And aktiviteten "Dokumentation" i "ProjektX" har status "Godkendt"
    When medarbejderen "ab" forsøger at indrapportere 3 timer den 2025-05-02 på "Dokumentation" i "ProjektX"
    Then summen af registrerede timer for "ab" på "Dokumentation" er stadig 5
# Ansvarlig: Younes
  Scenario: Rapportfejl ved manglende projekt
    Given medarbejderen "ab" er logget ind
    When medarbejderen anmoder om rapport for projektet "UkendtProjekt"
    Then vises fejlen "Projektet findes ikke."
# Ansvarlig: Ali
  Scenario: Rapportfejl hvis bruger ikke er projektleder
    Given medarbejderen "ab" opretter projektet "ProjektPrivat"
    And medarbejderen logger ud
    And en anden medarbejder "zzz" logger ind
    When medarbejderen forsøger at generere rapport for "ProjektPrivat"
    Then får brugeren besked "Kun projektlederen må generere en rapport for projektet."
# Ansvarlig: Benjamin
  Scenario: Rapportfejl hvis projektet er tomt
    Given medarbejderen "ab" opretter projektet "TomtProjekt"
    When rapport genereres for "TomtProjekt"
    Then vises fejlen "Projektet har ingen aktiviteter."
# Ansvarlig: Benjamin
  Scenario: Udførlig projektrapport for aktivt projekt
    Given medarbejderen "ab" opretter projektet "RapportProjekt"
    And en aktivitet "Analyse" med 10 timer tilføjes projektet
    And medarbejderen registrerer 10 timer på "Analyse" den 2025-05-01
    When rapport genereres for "RapportProjekt"
    Then rapporten indeholder:
      """
      Rapport for projekt 'RapportProjekt':
      - Aktivitet: Analyse
        Budgetterede timer: 10
        Forbrugte timer: 100% (10 timer)
      """
