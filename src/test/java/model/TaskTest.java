package model;

import controller.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.DONE;
import static model.Status.NEW;
import static model.TaskType.TASK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class TaskTest {

    final FileBackedTasksManager taskManager = new FileBackedTasksManager("resources/TaskTest-file.csv");
    LocalDateTime taskTime = LocalDateTime.of(2022, 01, 1, 00,
            00);
    Duration taskDuration = Duration.ofHours(10);
    Task task = new Task(1, TASK, "Task1", NEW, "Description task1", taskTime,
            taskDuration);
    Task task2 = new Task(2, TASK, "Task2", NEW, "Description task2");


    @Test
    void addNewTask() {
        //Подготовка
        taskManager.addTask(task);

        //Исполнение
        final int taskId = task.getId();
        final Task savedTask = taskManager.findTaskById(taskId);
        final List<Task> tasks = taskManager.returnAllTask();

        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertEquals(task.toString(), savedTask.toString());
    }

    @Test
    void addNewTaskWithoutStartTime() {
        //Подготовка
        taskManager.addTask(task2);

        //Исполнение
        final int taskId = task2.getId();
        final Task savedTask = taskManager.findTaskById(taskId);

        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");
    }

    @Test
    void updateTaskWithoutStartTime() {
        //Подготовка
        taskManager.addTask(task2);

        //Исполнение
        task2.setName("task22");
        task2.setDescription("Description task22");
        task2.setStatus(DONE);
        task2.setStartTime(taskTime);
        task2.setDuration(taskDuration);
        taskManager.updateTask(task2);
        final int taskId = task2.getId();
        final Task savedTask = new Task(taskManager.findTaskById(taskId));

        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task2, savedTask, "Задачи не совпадают.");
    }
}