# Filnavn: medarbejderhåndtering.feature
# Relaterede filer: Employee.java, ProjectManager.java
# Formål:
# Tester medarbejderhåndtering  herunder login, oprettelse og validering af initialer,
# lighed mellem medarbejdere samt tjek af projektlederstatus.
# Fokus er på korrekt opførsel ved brug og sammenligning af Employee-objekter.

Feature: Medarbejderhåndtering
# Ansvarlig: Benjamin
Background:
  Given brugeren "def" er logget ind
# Ansvarlig: Younes
Scenario: En ny bruger logger ind
  Given brugeren med initialer "abc" ikke findes i systemet
  When brugeren logger ind med initialer "abc"
  Then brugeren "abc" bliver oprettet og er logget ind
# Ansvarlig: Ali
Scenario: En eksisterende bruger logger ind
  Given en medarbejder med initialerne "xyz" er registreret
  When brugeren logger ind med initialer "xyz"
  Then brugeren "xyz" er logget ind
# Ansvarlig: Younes
Scenario: Initialer er gyldige
  Given brugeren forsøger at oprette en medarbejder med initialer "abc"
  Then oprettes medarbejderen uden fejl
# Ansvarlig: Benjamin
Scenario Outline: Initialer er ugyldige
  Given brugeren forsøger at oprette en medarbejder med initialer "<initialer>"
  Then får brugeren en fejlbesked "<fejlbesked>"

  Examples:
    | initialer | fejlbesked                                                   |
    | a         | Initialer skal være 2-4 bogstaver og kun indeholde bogstaver. |
    | abcde     | Initialer skal være 2-4 bogstaver og kun indeholde bogstaver. |
    | ab1       | Initialer skal være 2-4 bogstaver og kun indeholde bogstaver. |
    | a_b       | Initialer skal være 2-4 bogstaver og kun indeholde bogstaver. |
    |           | Initialer skal være 2-4 bogstaver og kun indeholde bogstaver. |
# Ansvarlig: Ali
Scenario: To medarbejdere med samme initialer er lig hinanden
  Given en medarbejder med initialerne "lars" er registreret
  And en medarbejder med initialerne "lars" er registreret
  Then medarbejderen med initialerne "lars" er lig en anden med samme initialer
# Ansvarlig: Benjamin
Scenario: To medarbejdere med forskellige initialer er ikke lig hinanden
  Given en medarbejder med initialerne "huba" er registreret
  And en medarbejder med initialerne "huba" er registreret
  Then medarbejderen med initialerne "huba" er ikke lig medarbejderen med initialerne "he"
# Ansvarlig: Younes
Scenario: Sammenligning med et objekt af en anden type
  Given en medarbejder med initialerne "zzz" er registreret
  Then medarbejderen med initialerne "zzz" er ikke lig en streng
# Ansvarlig: Ali
Scenario: Tjek om en medarbejder er projektleder
  Given en medarbejder med initialerne "pl" er registreret
  And medarbejderen er logget ind som "pl"
  When projektlederen opretter projektet "LederProjekt"
  Then medarbejderen "pl" er projektleder
