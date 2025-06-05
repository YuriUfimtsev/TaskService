import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class TaskManager {
    private final Map<Integer, Task> tasks;
    private int nextId = 1;

    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    public TaskManager(String dataFilePath)
            throws IOException, ClassNotFoundException {
        try (var inputStream = new ObjectInputStream(
                new FileInputStream(dataFilePath))) {
            this.tasks = (Map<Integer, Task>) inputStream.readObject();
        }
    }

    public void addTask(Task task) throws IllegalArgumentException {
        if (task.title.isBlank()) {
            logger.warning("Ошибка добавления: заголовок не может быть пустым");
            throw new IllegalArgumentException("Заголовок не может быть пустым");
        }

        tasks.put(nextId++, task);
        logger.info("Добавлена задача: " + task.title);
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

    public List<Task> searchTasks(String keyword) {
        final var pattern = ".*" + keyword.toLowerCase() + ".*";
        return tasks.values().stream()
                .filter(task -> task.title.toLowerCase().matches(pattern))
                .toList();
    }

    public void saveToFile(String filename) throws IOException {
        try (var outputStream = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            outputStream.writeObject(tasks);
        }
    }
}
