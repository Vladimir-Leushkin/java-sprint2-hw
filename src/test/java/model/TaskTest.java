package model;

import controller.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.NEW;
import static model.TaskType.TASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    @Test
    void addNewTask() {
        //Подготовка
        final FileBackedTasksManager taskManager = new FileBackedTasksManager("resources/test-file.csv");
        LocalDateTime taskTime = LocalDateTime.of(2022, 01, 1, 00,
                00);
        Duration taskDuration = Duration.ofHours(10);
        Task task = new Task(1,TASK, "Task1", NEW,"Description task1", taskTime,
                taskDuration);

        //Исполнение
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.findTaskById(taskId);
        final List<Task> tasks = taskManager.returnAllTask();

        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }


}