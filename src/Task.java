public class Task {
    private int id;
    protected final String title;
    protected Priority priority;

    public Task(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getDetails() {
        return title + " [" + priority + "]";
    }
}
