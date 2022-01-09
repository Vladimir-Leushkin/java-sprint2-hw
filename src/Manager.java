import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

import static model.Status.*;

public class Manager {
    //таблица для хранения задач
    HashMap<Integer, Task> tasks = new HashMap<>();
    //таблица для хранения подзадач
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    //таблица для хранения эпиков
    HashMap<Integer, Epic> epics = new HashMap<>();

    //получить список всех задач
    public ArrayList<Task> returnAllTask() {
        final ArrayList<Task> allTask = new ArrayList<>(tasks.values());
        return allTask;
    }

    //получить список всех эпиков
    public ArrayList<Task> returnAllEpic() {
        final ArrayList<Task> allEpic = new ArrayList<>(epics.values());
        return allEpic;
    }

    //получить список всех подзадач эпика по id
    public ArrayList<Task> returnAllSubTasksByEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        final ArrayList<Task> allSubTasksByEpic = epics.get(id).getSubTasks();
        return allSubTasksByEpic;
    }

    //получить задачу по id
    public Task findTaskById(int id) {
        return tasks.get(id);
    }

    //получить подзадачу по id
    public Task findSubTaskById(int id) {
        return subTasks.get(id);
    }

    //получить эпик по id
    public Task findEpicsByID(int id) {
        return epics.get(id);
    }

    //добавить задачу
    public Task addTask(Task task) {
        final Task value = new Task(task.getName(), task.getDescription()
                , task.getId(), task.getStatus());
        if (tasks.containsKey(task.getId())) {
            return null;
        } else {
            tasks.put(task.getId(), value);
        }
        return value;
    }

    //добавить подзадачу
    public SubTask addSubTask(SubTask subTask) {
        final SubTask value = new SubTask(subTask.getName(), subTask.getDescription()
                , subTask.getId(), subTask.getStatus(), subTask.getEpic());
        if (subTasks.containsKey(subTask.getId())) {
            return null;
        }
        if (!epics.containsKey(subTask.getEpic().getId())) {
            return null;
        }
        subTasks.put(subTask.getId(), value);
        final Epic epic = epics.get(subTask.getEpic().getId());
        epic.addSubTask(subTask);
        updateEpic(value.getEpic());
        return value;
    }

    //добавить эпик
    public Epic addEpic(Epic epic) {
        final Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getId()
                , epic.getStatus(), epic.getSubTasks());
        if (epics.containsKey(epic.getId())) {
            return null;
        } else {
            epics.put(epic.getId(), value);
        }
        return value;
    }

    //обновить задачу
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //обновить подзадачу
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            updateEpic(subTask.getEpic());
        } else {
            System.out.println("Такая подзадача не существует");
        }
    }

    //обновить эпик
    public Epic updateEpic(Epic epic) {
        int newTask = 0;
        int doneTask = 0;
        ArrayList<Task> epicSubTasks = epics.get(epic.getId()).getSubTasks();

        for (int i = 0; i < epicSubTasks.size(); i++) {
            if (epicSubTasks.get(i).getStatus().equals(NEW)) {
                newTask++;
            } else if (epicSubTasks.get(i).getStatus().equals(DONE)) {
                doneTask++;
            }
        }
        if (newTask == epicSubTasks.size()) {
            epic.setStatus(NEW);
        } else if (doneTask == epicSubTasks.size()) {
            epic.setStatus(DONE);
        } else {
            epic.setStatus(IN_PROGRESS);
        }
        return epic;
    }

    //удалить задачу по id
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все задачи
    public void deleteAllTasks() {
        tasks.clear();
    }

    //удалить подзадачу по id
    public void deleteSubTask(int id) {
        if (subTasks.containsKey(id)) {
            epics.get(subTasks.get(id).getEpic().getId()).deleteSubTaskByEpic(subTasks.get(id));
            subTasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все подзадачи
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    //удалить эпик по id
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).deleteAllSubTaskByEpic();
            epics.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все эпики
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

}
