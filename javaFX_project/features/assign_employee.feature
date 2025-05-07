Feature: Tildel medarbejder til aktivitet

  Scenario: Projektleder tildeler en medarbejder til en aktivitet
    Given en medarbejder med initialerne "abc" er registreret
    And medarbejderen er logget ind som "abc"
    And projektlederen opretter projektet "DTU App"
    And projektlederen tilføjer en aktivitet "Design UI" til projektet "DTU App" med startuge 18 og startår 2025 og slutuge 22 og slutår 2025 og 50 budgetterede timer
    And en medarbejder med initialerne "xyz" er registreret
    When projektlederen tildeler medarbejderen "xyz" til aktiviteten "Design UI" i projektet "DTU App"
    Then medarbejderen "xyz" er tilføjet til aktiviteten "Design UI"

Scenario: Hent alle registrerede medarbejdere
  Given en medarbejder med initialerne "m1" er registreret
  And en medarbejder med initialerne "m2" er registreret
  When brugeren henter alle medarbejdere
  Then systemet indeholder "m1" og "m2"