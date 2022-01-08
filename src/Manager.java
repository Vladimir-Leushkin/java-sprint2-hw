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

    //получить список всех подзадач эпика по ID
    public ArrayList<Task> returnAllSubTasksByEpic(Integer ID) {
        final ArrayList<Task> allSubTasksByEpic = epics.get(ID).getSubTasks();
        return allSubTasksByEpic;
    }

    //получить задачу по ID
    public Task findTaskByID(Integer ID) {
        return tasks.get(ID);
    }

    //получить подзадачу по ID
    public Task findSubTaskByID(Integer ID) {
        return subTasks.get(ID);
    }

    //получить эпик по ID
    public Task findEpicsByID(Integer ID) {
        return epics.get(ID);
    }

    //добавить задачу
    public Task createTask(Task task) {
        final Task value = new Task(task.getName(), task.getDescription()
                , task.getID(), task.getStatus());
        if (tasks.containsKey(task.getID())) {
            System.out.println("Такая задача существует " + task.getID());
            return null;
        } else {
            tasks.put(task.getID(), value);
        }
        return value;
    }

    //добавить подзадачу
    public SubTask createSubTask(SubTask subTask) {
        final SubTask value = new SubTask(subTask.getName(), subTask.getDescription()
                , subTask.getID(), subTask.getStatus(), subTask.getEpic());
        if (subTasks.containsKey(subTask.getID())) {
            System.out.println("Такая задача существует " + subTask.getID());
            return null;
        }
        if (!epics.containsKey(subTask.getEpic().getID())) {
            System.out.println("Не найден эпик ид=" + subTask.getEpic().getID());
            return null;
        }
        subTasks.put(subTask.getID(), value);
        final Epic epic = epics.get(subTask.getEpic().getID());
        epic.addSubTask(subTask);
        return value;
    }

    //добавить эпик
    public Epic createEpic(Epic epic) {
        final Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getID()
                , epic.getStatus(), epic.getSubTasks());
        if (epics.containsKey(epic.getID())) {
            System.out.println("Такая задача существует " + epic.getID());
            return null;
        } else {
            epics.put(epic.getID(), value);
        }
        return value;
    }

    //обновить задачу
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //обновить подзадачу
    public void updateSubTask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getID())) {
            subTasks.put(subTask.getID(), subTask);
        } else {
            System.out.println("Такая подзадача не существует");
        }
    }

    //обновить эпик
    public void updateEpic(Epic epic) {
        int newTask = 0;
        int doneTask = 0;
        ArrayList<Task> epicSubTasks = epics.get(epic.getID()).getSubTasks();

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
    }

    //удалить задачу по ID
    public void deleteTask(Integer ID) {
        if (tasks.containsKey(ID)) {
            tasks.remove(ID);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все задачи
    public void deleteAllTasks() {
        tasks.clear();
    }

    //удалить задачу по ID
    public void deleteSubTask(Integer ID) {
        if (subTasks.containsKey(ID)) {
            subTasks.remove(ID);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все подзадачи
    public void deleteAllSubTasks() {
        subTasks.clear();
    }

    //удалить эпик по ID
    public void deleteEpic(Integer ID) {
        if (epics.containsKey(ID)) {
            epics.remove(ID);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    //удалить все эпики
    public void deleteAllEpics() {
        epics.clear();
    }

}
