package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.LocalDateTimeAdapter;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {
    private Gson gson;
    private KVTaskClient client;

    public HTTPTaskManager() {
        super(null);

    }

    public HTTPTaskManager(int port) {
        super(null);
        gson = getGson();
        client = new KVTaskClient(port);

    }

    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.serializeNulls();
        return gsonBuilder.create();
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(new ArrayList<>(getTasks().values()));
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(getEpics().values()));
        client.put("epics", jsonEpics);
        String jsonSubTasks = gson.toJson(new ArrayList<>(getSubTasks().values()));
        client.put("subtasks", jsonSubTasks);
        String jsonHistory = gson.toJson(new ArrayList<>(historyManager.getHistory().stream()
                .map(Task::getId).collect(Collectors.toList())));
        client.put("history", jsonHistory);
    }

    public HTTPTaskManager load(int port) {
        HTTPTaskManager manager = new HTTPTaskManager(port);
        try {
            Map<Integer, Task> map = new HashMap<>();
            HashMap<Integer, Task> mapTasks = new HashMap<>();
            HashMap<Integer, Epic> mapEpics = new HashMap<>();
            HashMap<Integer, SubTask> mapSubTasks = new HashMap<>();
            String allTasks = client.load("tasks");
            if (allTasks != null) {
                ArrayList<Task> tasks = gson.fromJson(allTasks,
                        new TypeToken<ArrayList<Task>>() {
                        }.getType());
                if (tasks != null) {
                    for (Task task : tasks) {
                        mapTasks.put(task.getId(), task);
                        map.put(task.getId(), task);
                    }
                    manager.setTasks(mapTasks);
                }
            }
            String allEpics = client.load("epics");
            if (allEpics != null) {
                ArrayList<Epic> epics = gson.fromJson(allEpics,
                        new TypeToken<ArrayList<Epic>>() {
                        }.getType());
                if (epics != null) {
                    for (Epic epic : epics) {
                        mapEpics.put(epic.getId(), epic);
                        map.put(epic.getId(), epic);
                    }
                    manager.setEpics(mapEpics);
                }
            }
            String allSubTask = client.load("subtasks");
            if (allSubTask != null) {
                ArrayList<SubTask> subTasks = gson.fromJson(allSubTask,
                        new TypeToken<ArrayList<SubTask>>() {
                        }.getType());
                if (subTasks != null) {
                    for (SubTask subTask : subTasks) {
                        mapSubTasks.put(subTask.getId(), subTask);
                        map.put(subTask.getId(), subTask);
                    }
                    manager.setSubTasks(mapSubTasks);
                }
            }
            String history1 = client.load("history");
            if (history1 != null) {
                List<Integer> history = gson.fromJson(history1,
                        new TypeToken<ArrayList<Integer>>() {
                        }.getType());
                if (history != null) {
                    for (Integer id : history) {
                        manager.historyManager.add(map.get(id));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return manager;
    }

}
