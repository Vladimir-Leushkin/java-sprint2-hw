package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static model.Status.NEW;
import static model.TaskType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskServerTest {

    protected HTTPTaskManager manager;
    protected KVServer kvServer;
    protected HttpTaskServer server;
    protected Gson gson ;
    HttpClient client = HttpClient.newHttpClient();
    protected LocalDateTime taskTime = LocalDateTime.of(2022, 01, 1, 00, 00);
    protected Duration taskDuration = Duration.ofHours(10);
    protected Task task = new Task(1, TASK, "Task1", NEW, "Description task1", taskTime,
            taskDuration);
    protected Epic epic = new Epic(2, EPIC, "Epic2", "Description epic2");
    protected LocalDateTime firstSubTaskTime = LocalDateTime.of(2022, 01, 2,
            0, 00);
    protected Duration firstSubTaskDuration = Duration.ofHours(11);
    protected SubTask firstSubTask = new SubTask(3, SUBTASK, "SubTask3", NEW,
            "Description subtask3", firstSubTaskTime, firstSubTaskDuration,
            epic.getId());
    protected LocalDateTime secondSubTaskTime = LocalDateTime.of(2022, 1, 3,
            00, 00);
    protected Duration secondSubTaskDuration = Duration.ofHours(12);
    protected SubTask secondSubTask = new SubTask(4, SUBTASK, "SubTask4", NEW,
            "Description subtask4", secondSubTaskTime, secondSubTaskDuration,
            epic.getId());
    protected Task task2 = new Task(5, TASK, "Task2", NEW, "Description task2");
    protected SubTask thirdSubTask = new SubTask(7, SUBTASK, "SubTask4", NEW,
            "Description subtask4", firstSubTaskTime, firstSubTaskDuration,
            2);


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
        gson = server.getGson();
        server.start();
    }

    @AfterEach
    void afterEach() {
        server.stop();
        kvServer.stop();
    }

    @Test
    void shouldPostTaskWithId1() throws IOException, InterruptedException {
        //Подготовка
        String jsonTask = gson.toJson(task);
        //Исполнение
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/?id=1"))
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Проверка
        System.out.println(task);
        System.out.println(manager.findTaskById(task.getId()));
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(task, manager.findTaskById(task.getId()));
    }

    @Test
    void shouldGetTaskWithId1() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/?id=1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task task1 = gson.fromJson(response.body(), Task.class);
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(task, task1, "Задачи не совпадают.");
    }

    @Test
    void shouldGetAllTask() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> tasks = gson.fromJson(response.body(),
                new TypeToken<ArrayList<Task>>() {
                }.getType());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(tasks, manager.returnAllTask(), "Задачи не совпадают.");
    }

    @Test
    void shouldDeleteTaskWithId1() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/?id=1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertNull(manager.findTaskById(task.getId()), "Задача не удалена.");
    }

    @Test
    void shouldDeleteAllTask() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/task/"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(0, manager.returnAllTask().size(), "Задачи не удалены.");
    }

    @Test
    void shouldPostSubTaskWithId3() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        String jsonSubTask = gson.toJson(firstSubTask);
        //Исполнение
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/?id=3"))
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");

    }

    @Test
    void shouldPostSubTaskWithId4() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        String jsonSubTask = gson.toJson(secondSubTask);
        //Исполнение
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonSubTask);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/?id=4"))
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
    }

    @Test
    void shouldGetSubTaskWithId3() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/?id=3"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        SubTask subTask = gson.fromJson(response.body(), SubTask.class);
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(firstSubTask, subTask, "Задачи не совпадают.");
    }

    @Test
    void shouldGetAllSubTaskByEpic() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/epic/?id=2"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Task> subTasks = gson.fromJson(response.body(),
                new TypeToken<ArrayList<SubTask>>() {
                }.getType());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(subTasks, manager.returnAllSubTasksByEpic(epic.getId()),
                "Задачи не совпадают.");
    }

    @Test
    void shouldDeleteSubTaskWithId3() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/?id=3"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertNull(manager.findSubTaskById(firstSubTask.getId()), "Задача не удалена.");
    }

    @Test
    void shouldDeleteAllSubTasks() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/subtask/"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(0, manager.returnAllSubTasksByEpic(epic.getId()).size(),
                "Задачи не удалены.");
    }

    @Test
    void shouldPostEpicWithId2() throws IOException, InterruptedException {
        //Подготовка
        String jsonEpic = gson.toJson(epic);
        //Исполнение
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(jsonEpic);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/?id=2"))
                .POST(body)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(epic, manager.findEpicById(epic.getId()));
    }

    @Test
    void shouldGetEpicWithId2() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/?id=2"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epic1 = gson.fromJson(response.body(), Epic.class);
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(epic, epic1, "Задачи не совпадают.");
    }

    @Test
    void shouldGetAllEpic() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        ArrayList<Epic> epics = gson.fromJson(response.body(),
                new TypeToken<ArrayList<Epic>>() {
                }.getType());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(epics, manager.returnAllEpic(), "Задачи не совпадают.");
    }

    @Test
    void shouldDeleteEpicWithId2() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/?id=2"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertNull(manager.findEpicById(2), "Задача не удалена.");
    }

    @Test
    void shouldDeleteAllEpics() throws IOException, InterruptedException {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/epic/"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(0, manager.returnAllEpic().size(), "Задачи не удалены.");
    }

    @Test
    void shouldGetAllTasksInManager() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        String jsonTaskPrioritized = gson.toJson(manager.getPrioritizedTasks());
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(jsonTaskPrioritized, response.body(), "Задачи не совпадают.");
    }

    @Test
    void shouldGetHistory() throws IOException, InterruptedException {
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        String jsonHistory = gson.toJson(manager.history());
        //Исполнение
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/history/"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        //Проверка
        assertEquals(200, response.statusCode(), "Неверный статус");
        assertEquals(jsonHistory, response.body(), "Задачи не совпадают.");
    }
}
