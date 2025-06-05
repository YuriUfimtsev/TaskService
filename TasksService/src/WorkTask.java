class WorkTask extends Task {
    private final String project;

    public WorkTask(int id, String title, Priority priority, String project) {
        super(id, title, priority);
        this.project = project;
    }

    @Override
    public String getDetails() {
        return "[Рабочее] " + super.getDetails() + " | Проект: " + project;
    }
}
