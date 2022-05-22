package controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static jdk.internal.util.xml.XMLStreamWriter.DEFAULT_CHARSET;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final HttpServer server;
    private final Gson gson;
    private final TaskManager taskManager;

    public HttpTaskServer() throws IOException {
        this(Managers.getDefault());
    }

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = new Gson();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/tasks", this::handler);
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(1);
    }

    public static void main(String[] args) throws IOException {
        final HttpTaskServer server = new HttpTaskServer();
        server.start();
        server.stop();
    }

    private void handler(HttpExchange h) {
        try {
            System.out.println("\n/tasks: " + h.getRequestURI());
            //final String path = h.getRequestURI().getPath().substring(7);
            String path = h.getRequestURI().getPath();
            String[] splitStrings = path.split("/");
            switch (/*path*/splitStrings[1]) {
                case "":
                    if (!h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ждет GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(485, 0);
                    }
                    final String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(h, response);
                    break;
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
                    handleSubtask(h);
                    break;
                case "subtask/epic":
                    if (h.getRequestMethod().equals("GET")) {
                        System.out.println("/ Ждет GET-запрос, а получил: " + h.getRequestMethod());
                        h.sendResponseHeaders(485, 0);
                    }
                    final String query = h.getRequestURI().getQuery();
                    String idParam = query.substring(3);
                    final int id = Integer.parseInt(idParam);
                    final List<SubTask> subTasks = taskManager.returnAllSubTasksByEpic(id);
                    final String response2 = gson.toJson(subTasks);
                    sendText(h, response2);
                    break;
                case "epic":
                    handleEpic(h);
                    break;
                default:
                    System.out.println("Неизвестный запрос: " + h.getRequestURI());
                    h.sendResponseHeaders(404, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleTask(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        String idTask = query.substring(3);
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                if (idTask.isEmpty()) {
                    response = gson.toJson(taskManager.returnAllTask());
                } else {
                    response = gson.toJson(taskManager.findTaskById(Integer.parseInt(idTask)));
                }
                sendText(httpExchange, response);
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Task task = gson.fromJson(body, Task.class);
                taskManager.updateTask(task);
                response = "Задача обновлена";
                sendText(httpExchange, response);
            case "DELETE":
                if (idTask.isEmpty()) {
                    taskManager.deleteAllTasks();
                    response = "Все задачи удалены";
                } else {
                    taskManager.deleteTask(Integer.parseInt(idTask));
                    response = "Удалена задача с id: " + idTask;
                }
                sendText(httpExchange, response);
        }
    }

    private void handleSubtask(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        String idTask = query.substring(3);
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                response = gson.toJson(taskManager.findSubTaskById(Integer.parseInt(idTask)));
                sendText(httpExchange, response);
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                SubTask subTask = gson.fromJson(body, SubTask.class);
                taskManager.updateSubTask(subTask);
                response = "Подзадача обновлена";
                sendText(httpExchange, response);
            case "DELETE":
                if (idTask.isEmpty()) {
                    taskManager.deleteAllSubTasks();
                    response = "Все подзадачи удалены";
                } else {
                    taskManager.deleteSubTask(Integer.parseInt(idTask));
                    response = "Удалена подзадача с id: " + idTask;
                }
                sendText(httpExchange, response);
        }
    }

    private void handleEpic(HttpExchange httpExchange) throws IOException {
        final String query = httpExchange.getRequestURI().getQuery();
        String idTask = query.substring(3);
        switch (httpExchange.getRequestMethod()) {
            case "GET":
                String response;
                response = gson.toJson(taskManager.findEpicById(Integer.parseInt(idTask)));
                sendText(httpExchange, response);
            case "POST":
                InputStream inputStream = httpExchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
                Epic epic = gson.fromJson(body, Epic.class);
                taskManager.updateEpic(epic);
                response = "Подзадача обновлена";
                sendText(httpExchange, response);
            case "DELETE":
                if (idTask.isEmpty()) {
                    taskManager.deleteAllEpics();
                    response = "Все эпики удалены";
                } else {
                    taskManager.deleteEpic(Integer.parseInt(idTask));
                    response = "Удалена эпики с id: " + idTask;
                }
                sendText(httpExchange, response);
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
