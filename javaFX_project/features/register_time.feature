Feature: Registrer tid på en aktivitet

  Background:
    Given en medarbejder med initialerne "ab" er registreret
    And medarbejderen er logget ind som "ab"
    And projektlederen "ab" har oprettet projektet "ProjektX"
    And en aktivitet "Dokumentation" er oprettet i projektet "ProjektX" med start uge 10, år 2025 og slut uge 20, år 2025 og 50 timer
    And medarbejderen "ab" er tilføjet til aktiviteten "Dokumentation" i projektet "ProjektX"

  Scenario: Medarbejderen registrerer tid korrekt
    When medarbejderen "ab" registrerer 5 timer den 2025-05-01 på "Dokumentation" i "ProjektX"
    Then der er registreret 5 timer for "ab" på "Dokumentation"

  Scenario: Medarbejderen forsøger at registrere tid på godkendt aktivitet
  Given medarbejderen "ab" registrerer 5 timer den 2025-05-01 på "Dokumentation" i "ProjektX"
  And aktiviteten "Dokumentation" i projektet "ProjektX" er godkendt
  When medarbejderen "ab" forsøger at registrere 3 timer den 2025-05-02 på "Dokumentation" i "ProjektX"
  Then der er stadig kun 5 timer registreret for "ab" på "Dokumentation"
