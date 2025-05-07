  Background:
    Given projektlederen "abc" har oprettet projektet "DTU Portal"
    And en aktivitet "Implementering" er oprettet i projektet "DTU Portal" med start uge 18, år 2025 og slut uge 22, år 2025 og 40 timer
    And medarbejderen "xyz" er tilføjet til aktiviteten "Implementering" i projektet "DTU Portal"

  Scenario: Projektleder ser alle aktiviteter i projektet
    Given medarbejderen er logget ind som "abc"
    When brugeren åbner projektet "DTU Portal"
    Then skal brugeren kunne se aktiviteten "Implementering"

  Scenario: Medarbejder ser sine tildelte aktiviteter
    Given medarbejderen er logget ind som "xyz"
    When brugeren åbner projektet "DTU Portal"
    Then skal brugeren kunne se aktiviteten "Implementering"