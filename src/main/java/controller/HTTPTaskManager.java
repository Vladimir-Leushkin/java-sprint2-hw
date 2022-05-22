package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final Gson gson;
    private final KVTaskClient client;

    public HTTPTaskManager(int port) {
        super(null);
        gson = new Gson();
        client = new KVTaskClient(port);
    }

    @Override
    protected void save(){
        String jsonTasks = gson.toJson(new ArrayList<>(getTasks().values()));
        client.put("tasks", jsonTasks);
        String jsonSubTasks = gson.toJson(new ArrayList<>(getSubTasks().values()));
        client.put("subtasks", jsonSubTasks);
        String jsonEpics = gson.toJson(new ArrayList<>(getEpics().values()));
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson((new ArrayList<>(historyManager.getHistory())));
        client.put("history", jsonHistory);
    }

    protected void load() {
        ArrayList<Task> tasks = gson.fromJson(client.load("tasks"),
                new TypeToken<ArrayList<Task>>(){}.getType());
        for (Task task: tasks){
            addTask(task);
        }
        ArrayList<SubTask> subTasks = gson.fromJson(client.load("subtasks"),
                new TypeToken<ArrayList<SubTask>>(){}.getType());
        for (SubTask subTask: subTasks){
            addSubTask(subTask);
        }
        ArrayList<Epic> epics = gson.fromJson(client.load("epics"),
                new TypeToken<ArrayList<Epic>>(){}.getType());
        for (Epic epic: epics){
            addEpic(epic);
        }
        ArrayList<Task> history = gson.fromJson(client.load("history"),
                new TypeToken<ArrayList<Task>>(){}.getType());
        for (Task task: history){
            if (task instanceof SubTask) {
                findSubTaskById(task.getId());
            } else if (task instanceof Epic) {
                findEpicById(task.getId());
            } else if (task instanceof Task) {
                findTaskById(task.getId());
            }
        }

    }
}
