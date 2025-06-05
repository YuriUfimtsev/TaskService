import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class NotificationService {
    private final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(1);

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

    public void stop() {
        scheduler.shutdown();
    }
}
