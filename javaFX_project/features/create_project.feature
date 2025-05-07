Feature: Opret projekt

  Scenario: En projektleder opretter et nyt projekt
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    When projektlederen opretter projektet "SuperProjekt"
    Then projektet "SuperProjekt" findes
    And projektlederen for projektet "SuperProjekt" er "abc"

Scenario: Fejl hvis man forsøger at oprette et projekt der allerede findes
  Given en medarbejder med initialerne "aaa" er registreret
  And medarbejderen er logget ind som "aaa"
  And projektlederen opretter projektet "Eksisterende Projekt"
  When projektlederen forsøger at oprette projektet "Eksisterende Projekt"
  Then får projektlederen en fejlbesked "Projekt eksisterer allerede"

Scenario: Forespørgsel på projektleder for ikke-eksisterende projekt
  Given en medarbejder med initialerne "x1" er registreret
  And medarbejderen er logget ind som "x1"
  When brugeren forsøger at få projektlederen for projektet "Ukendt Projekt"
  Then får brugeren ingen projektleder

Scenario: Brugeren forsøger at hente projektleder på ikke-eksisterende projekt
  Given en medarbejder med initialerne "fake" er registreret
  And medarbejderen er logget ind som "fake"
  Then brugeren er ikke projektleder for projektet "UgyldigtProjekt"

Scenario: Hent alle projektnavne
  Given en medarbejder med initialerne "abc" er registreret
  And medarbejderen er logget ind som "abc"
  And projektlederen opretter projektet "Alpha"
  And projektlederen opretter projektet "Beta"
  When brugeren henter alle projektnavne
  Then listen indeholder "Alpha" og "Beta"