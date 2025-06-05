import lombok.Getter;

import java.time.LocalDate;

@Getter
class PersonalTask extends Task {
    private final LocalDate dueDate;

    public PersonalTask(String title, Priority priority, LocalDate dueDate) {
        super(title, priority);
        this.dueDate = dueDate;
    }

    @Override
    public String getDetails() {
        return "[Личное] " + super.getDetails() + " | Срок: " + dueDate;
    }
}