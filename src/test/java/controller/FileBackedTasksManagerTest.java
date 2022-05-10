package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        manager = new FileBackedTasksManager("resources/ManagerTest-file.csv");
        manager.deleteAllTasks();
        manager.deleteAllEpics();
    }

    @Test
    void shouldCreateEmptyManagerFileBackedTasksManager() {
        //Подготовка
        //Исполнение
        final FileBackedTasksManager newFileTaskManager = manager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
    }

    @Test
    void shouldCreateManagerFileBackedTasksManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task2);
        final int epicId = epic.getId();
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        //Исполнение
        final FileBackedTasksManager newFileTaskManager = manager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEpicWithoutSubtaskFileBackedTasksManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addTask(task2);
        final int epicId = epic.getId();
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        manager.findEpicById(epicId);
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        //Исполнение
        final FileBackedTasksManager newFileTaskManager = manager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEmptyHistoryFileBackedTasksManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addTask(task2);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        final FileBackedTasksManager newFileTaskManager = manager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

}