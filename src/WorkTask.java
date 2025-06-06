/**
 * Рабочая задача с указанием проекта
 */
class WorkTask extends Task {
    private final String project;

    /**
     * Создает рабочую задачу
     * @param title Название задачи
     * @param priority Приоритет
     * @param project Название проекта
     */
    public WorkTask(String title, Priority priority, String project) {
        super(title, priority);
        this.project = project;
    }

    @Override
    public String getDetails() {
        return "[Рабочее] " + super.getDetails() + " | Проект: " + project;
    }
}
