import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class TaskManager {
    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private int nextId = 1;

    public void addTask(Task task) throws IllegalArgumentException {
        if (task.title.isBlank()) {
            throw new IllegalArgumentException("Заголовок не может быть пустым");
        }
        tasks.put(nextId++, task);
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return tasks.values().stream()
                .filter(task -> task.priority == priority)
                .collect(Collectors.toList());
    }

    public Map<Priority, Long> getPriorityStatistics() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(
                        task -> task.priority,
                        Collectors.counting()
                ));
    }

    public void processTasks(Consumer<Task> processor) {
        tasks.values().forEach(processor);
    }
}
