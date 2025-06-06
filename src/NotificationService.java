import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Сервис уведомлений, отвечающий за проверку высокоприоритетных задачи
 */
class NotificationService {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

    /**
     * Запускает фоновую проверку задач каждые 10 секунд
     * @param manager менеджер задач
     */
    public void startMonitoring(TaskManager manager) {
        scheduler.scheduleAtFixedRate(() -> {
            long highPriorityCount = manager.getTasksByPriority(Priority.HIGH).size();
            if (highPriorityCount > 0) {
                System.out.printf(
                        "\nУведомление: установлено %d высокоприоритетных задач!\n",
                        highPriorityCount
                );
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    /**
     * Останавливает фоновую проверку
     */
    public void stop() {
        scheduler.shutdown();
    }
}
