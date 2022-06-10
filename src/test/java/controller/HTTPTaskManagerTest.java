package controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTTPTaskManagerTest extends TaskManagerTest<HTTPTaskManager> {

    protected KVServer kvServer;
    protected HttpTaskServer server;

    @BeforeEach
    void beforeEach() {
        try {
            kvServer = new KVServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        kvServer.start();
        manager = new HTTPTaskManager(8078);
        try {
            server = new HttpTaskServer(manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
        kvServer.stop();
    }

    @Test
    void shouldCreateEmptyManagerHTTPTasksManager() {
        //Подготовка

        //Исполнение
        HTTPTaskManager newManager = manager.load(8078);
        //Проверка
        System.out.println(newManager.returnAllTask());
        System.out.println(newManager.returnAllEpic());
        System.out.println(newManager.history());
        assertEquals(newManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
    }

    @Test
    void shouldCreateManagerHTTPTasksManager() {
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
        HTTPTaskManager newManager = manager.load(8078);
        //Проверка
        assertEquals(newManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEpicWithoutSubtaskHTTPTaskManager() {
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
        HTTPTaskManager newManager = manager.load(8078);
        //Проверка
        assertEquals(newManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEmptyHistoryHTTPTaskManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addTask(task2);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        HTTPTaskManager newManager = manager.load(8078);
        //Проверка
        assertEquals(newManager.history(), manager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                manager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                manager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                manager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }
}
