import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

import static model.Status.*;

public class Manager {
    final HashMap<Integer, Task> tasks = new HashMap<>();
    final HashMap<Integer, SubTask> subTasks = new HashMap<>();
    final HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList<Task> returnAllTask() {
        final ArrayList<Task> allTask = new ArrayList<>(tasks.values());
        return allTask;
    }

    public ArrayList<Task> returnAllEpic() {
        final ArrayList<Task> allEpic = new ArrayList<>(epics.values());
        return allEpic;
    }

    public ArrayList<Task> returnAllSubTasksByEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        final ArrayList<Task> allSubTasksByEpic = epics.get(id).getSubTasks();
        return allSubTasksByEpic;
    }

    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    public Task findSubTaskById(int id) {
        return subTasks.get(id);
    }

    public Task findEpicsByID(int id) {
        return epics.get(id);
    }

    public Task addTask(Task task) {
        final Task value = new Task(task.getName(), task.getDescription(),
                task.getId(), task.getStatus());
        if (tasks.containsKey(task.getId())) {
            return null;
        } else {
            tasks.put(task.getId(), value);
        }
        return value;
    }

    public SubTask addSubTask(SubTask subTask) {
        final SubTask value = new SubTask(subTask.getName(), subTask.getDescription(),
                subTask.getId(), subTask.getStatus(), subTask.getEpic());
        if (subTasks.containsKey(subTask.getId())) {
            return null;
        }
        if (!epics.containsKey(subTask.getEpic().getId())) {
            return null;
        }
        subTasks.put(value.getId(), value);
        final Epic epic = epics.get(subTask.getEpic().getId());
        epic.addSubTask(subTask);
        updateEpic(subTask.getEpic());
        return value;
    }

    public Epic addEpic(Epic epic) {
        final Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getId(),
                epic.getStatus(), epic.getSubTasks());
        if (epics.containsKey(epic.getId())) {
            return null;
        } else {
            epics.put(epic.getId(), value);
        }
        return value;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpic(subTask.getEpic());
            subTask.getEpic().deleteSubTaskByEpic(subTask);
            subTask.getEpic().addSubTask(subTask);
        } else {
            System.out.println("Такая подзадача не существует");
        }
    }

    private void updateEpic(Epic epic) {
        final Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getId(),
                epic.getStatus(), epic.getSubTasks());
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), value);
            epic.getStatus();
        } else {
            System.out.println("Такой эпик не существует");
        }
    }

    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteSubTask(int id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpic().getId()).deleteSubTaskByEpic(subTasks.get(id));
            epics.get(subTasks.get(id).getEpic().getId()).getStatus();
            subTasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteAllSubTaskByEpic();
        }
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).deleteAllSubTaskByEpic();
            epics.remove(id);
            for (SubTask taskByEpic : subTasks.values()) {
                if (taskByEpic.getEpic().getId() == id) {
                    subTasks.remove(taskByEpic.getId());
                }
            }
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

}
