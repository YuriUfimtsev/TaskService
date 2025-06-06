import lombok.Getter;
import lombok.Setter;

/**
 * Базовый класс задачи
 */
@Getter @Setter
public class Task {
    private int id;
    protected final String title;
    protected Priority priority;

    /**
     * Создает новую задачу
     * @param title Заголовок задачи
     * @param priority Приоритет задачи
     */
    public Task(String title, Priority priority) {
        this.title = title;
        this.priority = priority;
    }

    /**
     * Возвращает детали задачи в виде строки с названием и приоритетом
     * @return строковое представление задачи
     */
    public String getDetails() {
        return title + " [" + priority + "]";
    }
}
