Feature: Tilføj aktivitet til projekt og udvidet funktionalitet

  Background:
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    And projektlederen opretter projektet "DTU App"

  Scenario: Projektleder tilføjer en ny aktivitet
    When projektlederen tilføjer en aktivitet "Design UI" til projektet "DTU App" med startuge 18 startår 2025 slutuge 22 slutår 2025 og budget 50 timer
    Then projektet "DTU App" indeholder aktiviteten "Design UI"
    And aktiviteten "Design UI" har 50 budgetterede timer

  Scenario: Aktivitetens start- og slutdatoer er korrekte
    Given projektlederen tilføjer en aktivitet "Analyse" til projektet "DTU App" med startuge 10 startår 2025 slutuge 15 slutår 2025 og budget 40 timer
    Then aktiviteten "Analyse" har startuge 10 og startår 2025
    And aktiviteten "Analyse" har slutuge 15 og slutår 2025

  Scenario: Registreret tid på bestemt dato
    Given projektlederen tilføjer en aktivitet "Test" til projektet "DTU App" med startuge 18 startår 2025 slutuge 20 slutår 2025 og budget 30 timer
    And projektlederen tildeler medarbejderen "abc" til aktiviteten "Test" i projektet "DTU App"
    When medarbejderen "abc" registrerer 5 timer den 2025-05-05 på "Test" i "DTU App"
    Then der er registreret 5 timer for "abc" på "Test" den 2025-05-05

  Scenario: Beregn færdiggørelsesprocent
    Given projektlederen tilføjer en aktivitet "Dokumentation" til projektet "DTU App" med startuge 12 startår 2025 slutuge 20 slutår 2025 og budget 20 timer
    And projektlederen tildeler medarbejderen "abc" til aktiviteten "Dokumentation" i projektet "DTU App"
    And medarbejderen "abc" registrerer 10 timer den 2025-05-06 på "Dokumentation" i "DTU App"
    Then færdiggørelsesprocenten for "Dokumentation" er 50

  Scenario: Aktiviteten er markeret som færdig
    Given projektlederen tilføjer en aktivitet "Færdiggørelse" til projektet "DTU App" med startuge 10 startår 2025 slutuge 12 slutår 2025 og budget 10 timer
    And projektlederen tildeler medarbejderen "abc" til aktiviteten "Færdiggørelse" i projektet "DTU App"
    And status for aktiviteten "Færdiggørelse" i "DTU App" er "Godkendt"
    Then aktiviteten "Færdiggørelse" er markeret som færdig

  Scenario: Fejl hvis projekt ikke findes
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    When projektlederen forsøger at tilføje en aktivitet "Ghost Task" til projektet "UFO Projekt" med startuge 10 startår 2025 slutuge 12 slutår 2025 og budget 20 timer
    Then får projektlederen en fejlbesked "Projektet findes ikke"

  Scenario: Kun projektleder må tilføje aktiviteter
    Given en medarbejder med initialerne "abc" er registreret
    And en medarbejder med initialerne "xyz" er registreret
    And medarbejderen er logget ind som "xyz"
    And projektlederen opretter projektet "DTU App"
    When medarbejderen forsøger at tilføje en aktivitet "Sniger" til projektet "DTU App" med startuge 11 startår 2025 slutuge 13 slutår 2025 og budget 25 timer
    Then får medarbejderen en fejlbesked "Kun projektlederen må tilføje aktiviteter"
