import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

class TaskManager {
    private final ConcurrentHashMap<Integer, Task> tasks;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    public TaskManager(String dataFilePath) throws IOException, ClassNotFoundException {
        try (var inputStream = new ObjectInputStream(new FileInputStream(dataFilePath))) {
            var fileObjects = inputStream.readObject();
            if (fileObjects instanceof ConcurrentHashMap<?, ?> rawMap) {
                var hasInvalidTypes = rawMap.entrySet().stream()
                        .limit(5)
                        .anyMatch(entry ->
                                !(entry.getKey() instanceof Integer) ||
                                        !(entry.getValue() instanceof Task)
                        );
                if (hasInvalidTypes) {
                    throw new InvalidObjectException("Некорректная структура файла");
                }
                this.tasks = (ConcurrentHashMap<Integer, Task>) fileObjects;
            } else {
                throw new InvalidObjectException("Некорректная структура файла");
            }
        }
    }

    public void addTask(Task task) throws IllegalArgumentException {
        if (task.title == null || task.title.isBlank()) {
            logger.warning("Попытка добавить задачу с пустым заголовком");
            throw new IllegalArgumentException("Заголовок задачи не может быть пустым");
        }

        int id = nextId.getAndIncrement();
        task.setId(id);
        tasks.put(id, task);
        logger.info("Добавлена задача #" + id + ": " + task.title);
    }

    public List<Task> getTasksByPriority(Priority neededPriority) {
        return tasks.values().stream()
                .filter(task -> task.priority == neededPriority)
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

    public boolean removeTask(int id) {
        var removedTask = tasks.remove(id);
        if (removedTask != null) {
            logger.info("Удалена задача #" + id + ": " + removedTask.title);
            return true;
        }
        return false;
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
