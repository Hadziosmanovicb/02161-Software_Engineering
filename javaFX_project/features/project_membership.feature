Feature: Projektmedlemskab og tildeling

  Scenario: Projektlederen bliver automatisk tilføjet til sit eget projekt
    Given en bruger med initialer "abc" er logget ind
    When brugeren opretter projektet "ProjektX"
    Then medarbejderen "abc" er projektleder for "ProjektX"

  Scenario: Kan ikke tildele medarbejder som ikke er en del af projektet
    Given en bruger med initialer "led" er logget ind
    And brugeren opretter projektet "ProjektY"
    And brugeren tilføjer medarbejderen "emp" globalt, men ikke til projektet
    And brugeren opretter aktiviteten "Aktiv1" i projektet "ProjektY"
    When brugeren forsøger at tildele "emp" til aktiviteten "Aktiv1" i "ProjektY"
    Then vises en fejl om at medarbejderen ikke er en del af projektet
