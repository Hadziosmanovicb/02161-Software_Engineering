package dtu.example.ui.domain;



import java.util.Objects;

public class Employee {
    private String initials;

    public Employee(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
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
