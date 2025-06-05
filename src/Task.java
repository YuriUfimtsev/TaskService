import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Task {
    private int id;
    protected final String title;
    protected Priority priority;

    public Task(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
    }

    public String getDetails() {
        return title + " [" + priority + "]";
    }
}
