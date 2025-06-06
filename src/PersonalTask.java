import java.time.LocalDate;

/**
 * Личная задача с указанием срока выполнения
 */
class PersonalTask extends Task {
    private final LocalDate dueDate;

    /**
     * Создает личную задачу
     * @param title Название задачи
     * @param priority Приоритет
     * @param dueDate Срок выполнения
     */
    public PersonalTask(String title, Priority priority, LocalDate dueDate) {
        super(title, priority);
        this.dueDate = dueDate;
    }

    @Override
    public String getDetails() {
        return "[Личное] " + super.getDetails() + " | Срок: " + dueDate;
    }
}