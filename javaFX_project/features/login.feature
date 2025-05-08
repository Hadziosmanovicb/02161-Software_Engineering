Feature: Login

  Scenario: En ny bruger logger ind
    Given brugeren med initialer "abc" ikke findes i systemet
    When brugeren logger ind med initialer "abc"
    Then brugeren "abc" bliver oprettet og er logget ind

  Scenario: En eksisterende bruger logger ind
    Given en medarbejder med initialerne "xyz" er registreret
    When brugeren logger ind med initialer "xyz"
    Then brugeren "xyz" er logget ind
