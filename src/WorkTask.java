import lombok.Getter;

@Getter
class WorkTask extends Task {
    private final String project;

    public WorkTask(String title, Priority priority, String project) {
        super(title, priority);
        this.project = project;
    }

    @Override
    public String getDetails() {
        return "[Рабочее] " + super.getDetails() + " | Проект: " + project;
    }
}
