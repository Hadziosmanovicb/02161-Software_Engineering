eature: Lighed mellem medarbejdere

  Scenario: To medarbejdere med samme initialer er lig hinanden
    Given en medarbejder med initialerne "eq1" er registreret
    And en medarbejder med initialerne "eq1" er registreret
    Then medarbejderen med initialerne "eq1" er lig en anden med samme initialer

  Scenario: To medarbejdere med forskellige initialer er ikke lig hinanden
    Given en medarbejder med initialerne "a1" er registreret
    And en medarbejder med initialerne "b2" er registreret
    Then medarbejderen med initialerne "a1" er ikke lig medarbejderen med initialerne "b2"

Scenario: Sammenligning med et objekt af en anden type
  Given en medarbejder med initialerne "zzz" er registreret
  Then medarbejderen med initialerne "zzz" er ikke lig en streng

Scenario: Tjek om en medarbejder er projektleder
  Given en medarbejder med initialerne "pl1" er registreret
  And medarbejderen er logget ind som "pl1"
  When projektlederen opretter projektet "LederProjekt"
  Then medarbejderen "pl1" er projektleder