Feature: Aktivitetshåndtering med status og tid

  Background:
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen med initialerne "abc" er logget ind
    And projektlederen opretter projektet "DTU App"
    And projektlederen tilføjer en aktivitet "TestAktivitet" til projektet "DTU App" med startuge 10 startår 2025 slutuge 20 slutår 2025 og budget 40 timer
    And projektlederen tildeler medarbejderen "abc" til aktiviteten "TestAktivitet" i projektet "DTU App"

  Scenario: Aktivitet oprettes korrekt og viser metadata
    Then projektet "DTU App" indeholder aktiviteten "TestAktivitet"
    And aktiviteten "TestAktivitet" har startuge 10 og startår 2025
    And aktiviteten "TestAktivitet" har slutuge 20 og slutår 2025
    And aktiviteten "TestAktivitet" har 40 budgetterede timer

  Scenario: Medarbejder registrerer tid og færdiggørelse spores
    When medarbejderen "abc" registrerer 20 timer den 2025-05-10 på "TestAktivitet" i "DTU App"
    Then der er registreret 20 timer for "abc" på "TestAktivitet" den 2025-05-10
    And færdiggørelsesprocenten for "TestAktivitet" er 50

  Scenario: Statusflow for færdiggørelse, godkendelse og afvisning
    When medarbejderen "abc" markerer aktiviteten "TestAktivitet" i "DTU App" som færdig
    Then status for aktiviteten "TestAktivitet" i "DTU App" er "Afventer"
    When projektlederen afviser aktiviteten "TestAktivitet" i "DTU App"
    Then status for aktiviteten "TestAktivitet" i "DTU App" er "Afvist"
    When medarbejderen "abc" markerer aktiviteten "TestAktivitet" i "DTU App" som færdig
    And projektlederen godkender aktiviteten "TestAktivitet" i "DTU App"
    Then status for aktiviteten "TestAktivitet" i "DTU App" er "Godkendt"
    And aktiviteten "TestAktivitet" er markeret som færdig

  Scenario: Projektlederen må oprette, men andre får fejl
    Given en medarbejder med initialerne "xyz" er registreret
    And medarbejderen med initialerne "xyz" er logget ind
    When medarbejderen forsøger at tilføje en aktivitet "Hemmelig" til projektet "DTU App" med startuge 11 startår 2025 slutuge 13 slutår 2025 og budget 25 timer
    Then får medarbejderen en fejlbesked "Kun projektlederen må tilføje aktiviteter"

  Scenario: Fejl ved ikke-eksisterende projekt
    When projektlederen forsøger at tilføje en aktivitet "Aliens" til projektet "UFO Projekt" med startuge 10 startår 2025 slutuge 12 slutår 2025 og budget 20 timer
    Then får projektlederen en fejlbesked "Projektet findes ikke"
