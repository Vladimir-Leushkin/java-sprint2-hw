package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private List<Task> history = new LinkedList<>();

    public InMemoryTaskManager() {
    }

    @Override
    public List<Task> returnAllTask() {
        final List<Task> allTask = new ArrayList<>(tasks.values());
        return allTask;
    }

    @Override
    public List<Task> returnAllEpic() {
        final List<Task> allEpic = new ArrayList<>(epics.values());
        return allEpic;
    }

    @Override
    public List<SubTask> returnAllSubTasksByEpic(int id) {
        if (!epics.containsKey(id)) {
            return null;
        }
        final List<SubTask> allSubTasksByEpic = epics.get(id).getSubTasks();
        return allSubTasksByEpic;
    }

    @Override
    public Task findTaskById(int id) {
        final Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        checkHistory();
        history.add(task);
        return task;
    }

    @Override
    public SubTask findSubTaskById(int id) {
        final SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        checkHistory();
        history.add(subTask);
        return subTask;
    }

    @Override
    public Epic findEpicsByID(int id) {
        final Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        checkHistory();
        history.add(epic);
        return epic;
    }

    @Override
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

    @Override
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
        epic.getStatus();
        return value;
    }

    @Override
    public Epic addEpic(Epic epic) {
        final Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getId());
        if (epics.containsKey(epic.getId())) {
            return null;
        } else {
            epics.put(epic.getId(), value);
        }
        return value;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        final SubTask saveSubTask = subTasks.get(newSubTask.getId());
        final Epic epic = epics.get(newSubTask.getEpic().getId());
        if (saveSubTask != null) {
            subTasks.put(newSubTask.getId(), newSubTask);
            epic.deleteSubTaskByEpic(saveSubTask);
            epic.addSubTask(newSubTask);
        } else {
            System.out.println("Такая подзадача не существует");
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        final Epic saveEpic = epics.get(newEpic.getId());
        if (saveEpic != null) {
            saveEpic.setName(newEpic.getName());
            saveEpic.setDescription(newEpic.getDescription());
            newEpic.getStatus();
            saveEpic.deleteAllSubTaskByEpic();
        } else {
            System.out.println("Такой эпик не существует");
        }
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        final SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            final Epic epic = epics.get(subTask.getEpic().getId());
            epic.getSubTasks().remove(subTask);
            subTasks.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllSubTasks() {
        subTasks.clear();
        for (Epic epic : epics.values()) {
            epic.deleteAllSubTaskByEpic();
        }
    }

    @Override
    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        if (epic != null) {
            for (SubTask subTaskByEpic : epic.getSubTasks()) {
                subTasks.remove(subTaskByEpic.getId());
            }
            epic.deleteAllSubTaskByEpic();
            epics.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> history() {
        return history;
    }

    private void checkHistory() {
        if (history.size() == 10) {
            history.remove(0);
        }
    }


}

