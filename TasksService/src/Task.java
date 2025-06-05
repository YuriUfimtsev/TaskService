public class Task {
    protected final int id;
    protected final String title;
    protected Priority priority;
    protected boolean completed;

    public Task(int id, String title, Priority priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public String getDetails() {
        return title + " [" + priority + "]";
    }
}
