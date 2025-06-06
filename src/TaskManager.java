import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Менеджер задач, предоставляющий функциональность для работы с задачами:
 * добавление, получение и удаление, сохранение в файл и инициализация задач из файла
 */
class TaskManager {
    private final ConcurrentHashMap<Integer, Task> tasks;
    private final AtomicInteger nextId = new AtomicInteger(1);
    private static final Logger logger = Logger.getLogger(TaskManager.class.getName());

    /**
     * Конструктор по умолчанию (пустая коллекция задач)
     */
    public TaskManager() {
        this.tasks = new ConcurrentHashMap<>();
    }

    /**
     * Конструктор, выполняющий инициализацию задач из файла
     * @param dataFilePath путь к файлу с сохраненными задачами
     */
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

    /**
     * Добавляет новую задачу
     * @param task объект задачи
     * @throws IllegalArgumentException если заголовок задачи пуст
     */
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

    /**
     * Возвращает задачи по указанному приоритету
     * @param neededPriority фильтр приоритета
     * @return список задач
     */
    public List<Task> getTasksByPriority(Priority neededPriority) {
        return tasks.values().stream()
                .filter(task -> task.priority == neededPriority)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает статистику по количеству задач каждого приоритета
     */
    public Map<Priority, Long> getPriorityStatistics() {
        return tasks.values().stream()
                .collect(Collectors.groupingBy(
                        task -> task.priority,
                        Collectors.counting()
                ));
    }

    /**
     * Применяет действие ко всем задачам
     */
    public void processTasks(Consumer<Task> processor) {
        tasks.values().forEach(processor);
    }

    /**
     * Удаляет задачу по идентификатору
     * @param id идентификатор задачи
     * @return true если задача успешно удалена, false если не найдена
     */
    public boolean removeTask(int id) {
        var removedTask = tasks.remove(id);
        if (removedTask != null) {
            logger.info("Удалена задача #" + id + ": " + removedTask.title);
            return true;
        }
        return false;
    }

    /**
     * Выполняет поиск задач по ключевому слову
     * @param keyword ключевое слово, по которому выполняется поиск
     * @return список задач, заголовки которых содержат указанное ключевое слово
     */
    public List<Task> searchTasks(String keyword) {
        final var pattern = ".*" + keyword.toLowerCase() + ".*";
        return tasks.values().stream()
                .filter(task -> task.title.toLowerCase().matches(pattern))
                .toList();
    }

    /**
     * Сохраняет все задачи в файл
     * @param filename название файла
     */
    public void saveToFile(String filename) throws IOException {
        try (var outputStream = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            outputStream.writeObject(tasks);
        }
    }
}
