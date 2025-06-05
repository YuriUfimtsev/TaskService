import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final String TASKS_BACKUP_FILE_PATH = "tasks_backup.ser";

    public static void main(String[] args) {
        try {
            runApplication();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Критическая ошибка приложения", e);
        }
    }

    private static void runApplication() throws InterruptedException {
        var manager = new TaskManager();
        var notifier = new NotificationService();

        try {
            // Запускаем сервис уведомлений
            notifier.startMonitoring(manager);

            // Добавляем задачи с обработкой исключений
            addTasks(manager);

            // Выводим в консоль список всех задач и статистику
            printTasksAndStatistics(manager);

            // Удаляем одну задачу (для примера)
            removeFirstHighPriorityTask(manager);

            // Наблюдаем за уведомлениями
            waitForNotifications();
        } finally {
            notifier.stop();
            saveTasks(manager);
        }
    }

    private static void addTasks(TaskManager manager) {
        manager.addTask(new WorkTask("Завершить проект", Priority.HIGH, "веб-сервис на Java"));
        manager.addTask(new WorkTask("Начать новый проект", Priority.HIGH, "консольная утилита на Java"));
        manager.addTask(new PersonalTask("Подготовить портфолио", Priority.MEDIUM, LocalDate.now().plusDays(1)));
        manager.addTask(new PersonalTask("Работы по дому", Priority.MEDIUM, LocalDate.now().plusDays(1)));
    }

    private static void printTasksAndStatistics(TaskManager manager) {
        System.out.println("=== Все задачи ===");
        manager.processTasks(task ->
                System.out.println("• #" + task.getId() + " " + task.getDetails())
        );

        System.out.println("\n=== Статистика ===");
        manager.getPriorityStatistics().forEach((p, count) ->
                System.out.println(p + ": " + count + " задач")
        );
    }

    private static void removeFirstHighPriorityTask(TaskManager manager) {
        var highPriorityTasks = manager.getTasksByPriority(Priority.HIGH);
        if (highPriorityTasks.isEmpty()) {
            logger.warning("Нет высокоприоритетных задач для удаления");
            return;
        }

        var firstTaskId = highPriorityTasks.getFirst().getId();
        if (!manager.removeTask(firstTaskId)) {
            logger.warning("Задача #" + firstTaskId + " не найдена для удаления");
        }
    }

    private static void waitForNotifications() throws InterruptedException {
        Thread.sleep(30000);
    }

    private static void saveTasks(TaskManager manager) {
        try {
            manager.saveToFile(TASKS_BACKUP_FILE_PATH);
            logger.info("Состояние задач сохранено");
        } catch (IOException e) {
            logger.log(Level.WARNING, "Ошибка сохранения задач", e);
        }
    }
}