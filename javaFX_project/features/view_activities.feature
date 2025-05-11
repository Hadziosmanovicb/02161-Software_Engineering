# Filnavn: view_activities.feature
# Relaterede filer: ProjectManager.java, Activity.java
# Formål:
# Tester adgangen til aktivitetsvisning i et projekt afhængigt af brugerens rolle.
# Projektledere skal kunne se alle aktiviteter, mens medarbejdere kun ser
# de aktiviteter, de er tildelt i det pågældende projekt.

Feature: Se aktiviteter i projektet
# Ansvarlig: Younes
  Background:
    Given projektlederen "abc" har oprettet projektet "DTU Portal"
    And en aktivitet "Implementering" er oprettet i projektet "DTU Portal" med start uge 18, år 2025 og slut uge 22, år 2025 og 40 timer
    And medarbejderen "xyz" er tilføjet til aktiviteten "Implementering" i projektet "DTU Portal"
# Ansvarlig: Ali
  Scenario: Projektleder ser alle aktiviteter i projektet
    Given medarbejderen er logget ind som "abc"
    When brugeren åbner projektet "DTU Portal"
    Then skal brugeren kunne se aktiviteten "Implementering"
# Ansvarlig: Benjamin
  Scenario: Medarbejder ser sine tildelte aktiviteter
    Given medarbejderen er logget ind som "xyz"
    When brugeren åbner projektet "DTU Portal"
    Then skal brugeren kunne se aktiviteten "Implementering"
