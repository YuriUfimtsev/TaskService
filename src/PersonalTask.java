import java.time.LocalDate;

class PersonalTask extends Task {
    private final LocalDate dueDate;

    public PersonalTask(int id, String title, Priority priority, LocalDate dueDate) {
        super(id, title, priority);
        this.dueDate = dueDate;
    }

    @Override
    public String getDetails() {
        return "[Личное] " + super.getDetails() + " | Срок: " + dueDate;
    }
}