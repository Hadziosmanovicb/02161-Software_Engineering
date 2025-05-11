/**
 * Filnavn: Employee.java
 * Relaterede filer: ProjectManager.java, Activity.java
 *
 * Formål:
 * Repræsenterer en medarbejder identificeret via initialer.
 * Indeholder validering af initialer samt markering og forespørgsel
 * på projektlederstatus. Bruges som kerneobjekt i projekt- og aktivitetsstyring.
 */

package dtu.example.ui.domain;

import java.util.Objects;

public class Employee {
    // Ansvarlig: Ali
    private String initials;
    // Ansvarlig: Younes
    private boolean isProjectLeader = false;
// Ansvarlig: Benjamin
    public Employee(String initials) {
        if (initials == null || !initials.matches("[a-zA-Z]{2,4}")) {
    throw new IllegalArgumentException("Initialer skal være 2-4 bogstaver og kun indeholde bogstaver.");
}

        this.initials = initials;
    }
// Ansvarlig: Younes
    public String getInitials() {
        return initials;
    }
// Ansvarlig: Benjamin
    public boolean isProjectLeader() {
        return isProjectLeader;
    }
// Ansvarlig: Ali
    public void setProjectLeader(boolean isProjectLeader) {
        this.isProjectLeader = isProjectLeader;
    }
// Ansvarlig: Benjamin
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee e)) return false;
        return Objects.equals(initials, e.initials);
    }
// Ansvarlig: Younes
    @Override
    public int hashCode() {
        return Objects.hash(initials);
    }
}
