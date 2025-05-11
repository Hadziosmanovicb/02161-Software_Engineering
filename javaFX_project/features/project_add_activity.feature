# Filnavn: project_add_activity.feature
# Relaterede filer: ProjectManager.java, Activity.java
# Formål:
# Tester hvilke projekter en bruger har adgang til at se  enten som projektleder
# eller som tildelt medarbejder. Desuden verificeres visningen af aktiviteter
# brugeren er tilknyttet, samt om aktiviteter eksisterer i et givet projekt.

Feature: Projekttilgængelighed og aktivitetstilknytning
# Ansvarlig: Younes
  Scenario: Projektlederen kan se sit eget projekt
    Given en bruger med initialer "abc" er logget ind
    And brugeren opretter projektet "ProjektA"
    When brugeren beder om at se sine projekter
    Then indeholder listen "ProjektA"
# Ansvarlig: Benjamin
  Scenario: En medarbejder tilknyttet til et projekt kan se det
    Given en bruger med initialer "abc" er logget ind
    And brugeren opretter projektet "ProjektB"
    And en medarbejder med initialerne "emp" er registreret
    And brugeren tilføjer "emp" til projektet "ProjektB"
    And brugeren logger ud
    And brugeren med initialer "emp" logger ind
    When brugeren beder om at se sine projekter
    Then indeholder listen "ProjektB"
# Ansvarlig: Ali
  Scenario: Medarbejder uden aktiviteter vises som ledig
    Given en bruger med initialer "pld" er logget ind
    And brugeren opretter projektet "ProjektF"
    And medarbejderen "led" findes i systemet
    And brugeren tilføjer "led" til projektet "ProjektF"
    When brugeren henter aktivitetstilknytningerne for "ProjektF"
    Then vises "led" som "ledig"
# Ansvarlig: Benjamin
  Scenario: Medarbejder med én aktivitet vises korrekt
    Given en bruger med initialer "pld" er logget ind
    And brugeren opretter projektet "ProjektG"
    And medarbejderen "bsy" findes i systemet
    And brugeren tilføjer "bsy" til projektet "ProjektG"
    And brugeren opretter aktiviteten "TestAktivitet" i projektet "ProjektG"
    And brugeren tildeler "bsy" til aktiviteten "TestAktivitet" i projektet "ProjektG"
    When brugeren henter aktivitetstilknytningerne for "ProjektG"
    Then vises "bsy" med aktiviteterne "TestAktivitet"
# Ansvarlig: Younes
  Scenario: Medarbejder med flere aktiviteter vises med alle aktiviteter
    Given en bruger med initialer "pldd" er logget ind
    And brugeren opretter projektet "ProjektH"
    And medarbejderen "mlt" findes i systemet
    And brugeren tilføjer "mlt" til projektet "ProjektH"
    And brugeren opretter aktiviteten "A1" i projektet "ProjektH"
    And brugeren opretter aktiviteten "A2" i projektet "ProjektH"
    And brugeren tildeler "mlt" til aktiviteten "A1" i projektet "ProjektH"
    And brugeren tildeler "mlt" til aktiviteten "A2" i projektet "ProjektH"
    When brugeren henter aktivitetstilknytningerne for "ProjektH"
    Then vises "mlt" med aktiviteterne "A1", "A2"
# Ansvarlig: Ali
  Scenario: Tjek om en aktivitet eksisterer i et projekt
    Given en bruger med initialer "abc" er logget ind
    And brugeren opretter projektet "ProjektC"
    And brugeren opretter aktiviteten "Designfase" i projektet "ProjektC"
    When brugeren tjekker om aktiviteten "Designfase" findes i "ProjektC"
    Then får brugeren svaret "true"
# Ansvarlig: Ali
  Scenario: Tjek at en aktivitet ikke eksisterer i et projekt
    Given en bruger med initialer "abc" er logget ind
    And brugeren opretter projektet "ProjektD"
    When brugeren tjekker om aktiviteten "IkkeEksisterende" findes i "ProjektD"
    Then får brugeren svaret "false"

