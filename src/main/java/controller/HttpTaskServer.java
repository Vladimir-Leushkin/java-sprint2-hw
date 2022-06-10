package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.LocalDateTimeAdapter;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    public static Gson gson;

    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }


    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = getGson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    private void handler(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/tasks: " + h.getRequestURI());
            String path = h.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            if (splitStrings.length == 2 && splitStrings[1].equals("tasks")) {
                if (!h.getRequestMethod().equals("GET")) {
                    System.out.println("/ Ждет GET-запрос, а получил: " + h.getRequestMethod());
                    h.sendResponseHeaders(485, 0);
                    return;
                }
                final String response = gson.toJson(taskManager.getPrioritizedTasks());
                sendText(h, response);
            } else if (splitStrings.length > 2 && splitStrings[1].equals("tasks")) {
                switch (splitStrings[2]) {
                    case "history":
                        if (!h.getRequestMethod().equals("GET")) {
                            System.out.println("/ Ждет GET-запрос, а получил: " + h.getRequestMethod());
                            h.sendResponseHeaders(485, 0);
                        }
                        final String response1 = gson.toJson(taskManager.history());
                        sendText(h, response1);
                        break;
                    case "task":
                        handleTask(h);
                        break;
                    case "subtask":
                        if (splitStrings.length == 4 && splitStrings[3].equals("epic")) {
                            if (!h.getRequestMethod().equals("GET")) {
                                System.out.println("/ Ждет GET-запрос, а получил: " + h.getRequestMethod());
                                h.sendResponseHeaders(485, 0);
                            }
                            final String query = h.getRequestURI().getQuery();
                            String idParam = query.substring(3);
                            final int id = Integer.parseInt(idParam);
                            final List<SubTask> subTasks = taskManager.returnAllSubTasksByEpic(id);
                            final String response = gson.toJson(subTasks);
                            System.out.println("Извлечены подзадачи эпика с id: " + id);
                            sendText(h, response);
                        } else {
                            handleSubtask(h);
                        }
                        break;
                    case "epic":
                        handleEpic(h);
                        break;
                    default:
                        System.out.println("Неизвестный запрос: " + h.getRequestURI());
                        h.sendResponseHeaders(404, 0);
                }
            }
        } finally {
            h.close();
        }
    }

    private void handleTask(HttpExchange httpExchange) throws IOException {
        try {
            final String query = httpExchange.getRequestURI().getQuery();
            String response;
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.returnAllTask());
                        System.out.println("Извлечены все задачи");
                    } else {
                        String idTask = query.substring(3);
                        response = gson.toJson(taskManager.findTaskById(Integer.parseInt(idTask)));
                        System.out.println("Извлечена задача с id: " + idTask);
                    }
                    sendText(httpExchange, response);
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    Task task = gson.fromJson(body, Task.class);
                    String idTask = query.substring(3);
                    if (taskManager.getPrioritizedTasks().contains(task)) {
                        taskManager.updateTask(task);
                        response = "Обновлена задача с id: " + idTask;
                    } else {
                        taskManager.addTask(task);
                        response = "Добавлена задача с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllTasks();
                        response = "Все задачи удалены";
                    } else {
                        idTask = query.substring(3);
                        taskManager.deleteTask(Integer.parseInt(idTask));
                        response = "Удалена задача с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + httpExchange.getRequestURI());
                    httpExchange.sendResponseHeaders(404, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void handleSubtask(HttpExchange httpExchange) throws IOException {
        try {
            final String query = httpExchange.getRequestURI().getQuery();
            String response;
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if (query == null) {
                        response = "Неверный запрос";
                    } else {
                        String idTask = query.substring(3);
                        response = gson.toJson(taskManager.findSubTaskById(Integer.parseInt(idTask)));
                        System.out.println("Извлечена подзадача с id: " + idTask);
                    }
                    sendText(httpExchange, response);
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    SubTask subTask = gson.fromJson(body, SubTask.class);
                    String idTask = query.substring(3);
                    if (taskManager.findSubTaskById(Integer.parseInt(idTask)) != null) {
                        taskManager.updateSubTask(subTask);
                        response = "Обновлена подзадача с id: " + idTask;
                    } else {
                        taskManager.addSubTask(subTask);
                        response = "Добавлена подзадача с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllSubTasks();
                        response = "Все подзадачи удалены";
                    } else {
                        idTask = query.substring(3);
                        taskManager.deleteSubTask(Integer.parseInt(idTask));
                        response = "Удалена подзадача с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + httpExchange.getRequestURI());
                    httpExchange.sendResponseHeaders(404, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    private void handleEpic(HttpExchange httpExchange) throws IOException {
        try {
            final String query = httpExchange.getRequestURI().getQuery();
            String response;
            switch (httpExchange.getRequestMethod()) {
                case "GET":
                    if (query == null) {
                        response = gson.toJson(taskManager.returnAllEpic());
                        System.out.println("Извлечены все эпики");
                    } else {
                        String idTask = query.substring(3);
                        response = gson.toJson(taskManager.findEpicById(Integer.parseInt(idTask)));
                        System.out.println("Извлечен эпик с id: " + idTask);
                    }
                    sendText(httpExchange, response);
                    break;
                case "POST":
                    String body = readText(httpExchange);
                    Epic epic = gson.fromJson(body, Epic.class);
                    String idTask = query.substring(3);
                    if (taskManager.findEpicById(Integer.parseInt(idTask)) != null) {
                        taskManager.updateTask(epic);
                        response = "Обновлен эпик с id: " + idTask;
                    } else {
                        taskManager.addEpic(epic);
                        response = "Добавлен эпик с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                case "DELETE":
                    if (query == null) {
                        taskManager.deleteAllEpics();
                        response = "Все эпики удалены";
                    } else {
                        idTask = query.substring(3);
                        taskManager.deleteEpic(Integer.parseInt(idTask));
                        response = "Удалена эпики с id: " + idTask;
                    }
                    sendText(httpExchange, response);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + httpExchange.getRequestURI());
                    httpExchange.sendResponseHeaders(404, 0);
            }
        } finally {
            httpExchange.close();
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), UTF_8);
    }

}
