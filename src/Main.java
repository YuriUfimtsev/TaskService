import java.time.LocalDate;

public class Main {
    public static void main(String[] args) throws Exception {
        var manager = new TaskManager();
        var notifier = new NotificationService();

        // Запускаем сервис уведомлений
        notifier.startMonitoring(manager);

        // Добавляем задачи
        manager.addTask(new WorkTask(1, "Завершить проект", Priority.HIGH, "веб-сервис на Java"));
        manager.addTask(new PersonalTask(2, "Работа по дому", Priority.MEDIUM, LocalDate.now().plusDays(1)));

        // Выводим в консоль список задач
        System.out.println("=== Все задачи ===");
        manager.processTasks(task ->
                System.out.println("• " + task.getDetails())
        );

        // Выводим в консоль статистику в формате количества задач по приоритетам
        System.out.println("\n=== Статистика ===");
        manager.getPriorityStatistics().forEach((p, count) ->
                System.out.println(p + ": " + count + " задач")
        );

        // Через некоторое время получим уведомления
        Thread.sleep(30000);
        notifier.stop();
    }
}