package dtu.example.ui.domain;

import java.util.Objects;

public class Employee {
    private String initials;
    private boolean isProjectLeader = false;

    public Employee(String initials) {
        if (initials == null || !initials.matches("[a-zA-Z]{2,4}")) {
    throw new IllegalArgumentException("Initialer skal være 2-4 bogstaver og kun indeholde bogstaver.");
}

        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public boolean isProjectLeader() {
        return isProjectLeader;
    }

    public void setProjectLeader(boolean isProjectLeader) {
        this.isProjectLeader = isProjectLeader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee e)) return false;
        return Objects.equals(initials, e.initials);
    }

    @Override
    public int hashCode() {
        return Objects.hash(initials);
    }
}
