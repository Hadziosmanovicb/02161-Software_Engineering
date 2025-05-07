Feature: Status på aktivitet

  Background:
    Given en medarbejder med initialerne "ab" er registreret
    And medarbejderen er logget ind som "ab"
    And projektlederen "ab" har oprettet projektet "ProjektY"
    And en aktivitet "TestAktivitet" er oprettet i projektet "ProjektY" med start uge 10, år 2025 og slut uge 20, år 2025 og 40 timer
    And medarbejderen "ab" er tilføjet til aktiviteten "TestAktivitet" i projektet "ProjektY"

  Scenario: Medarbejder markerer aktivitet som færdig
    When medarbejderen "ab" markerer aktiviteten "TestAktivitet" i "ProjektY" som færdig
    Then status for aktiviteten "TestAktivitet" i "ProjektY" er "Afventer"

 Scenario: Projektleder godkender aktivitet
  When medarbejderen "ab" markerer aktiviteten "TestAktivitet" i "ProjektY" som færdig
  And projektlederen godkender aktiviteten "TestAktivitet" i "ProjektY"
  Then status for aktiviteten "TestAktivitet" i "ProjektY" er "Godkendt"

  Scenario: Projektleder afviser aktivitet
    Given status for aktiviteten "TestAktivitet" i "ProjektY" er "Afventer"
    When projektlederen afviser aktiviteten "TestAktivitet" i "ProjektY"
    Then status for aktiviteten "TestAktivitet" i "ProjektY" er "Afvist"

  Scenario: Medarbejder markerer afvist aktivitet som færdig igen
    Given status for aktiviteten "TestAktivitet" i "ProjektY" er "Afvist"
    When medarbejderen "ab" markerer aktiviteten "TestAktivitet" i "ProjektY" som færdig
    Then status for aktiviteten "TestAktivitet" i "ProjektY" er "Afventer"