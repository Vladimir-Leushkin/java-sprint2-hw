package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, SubTask> subTasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HistoryManager history1 = new InMemoryHistoryManager();


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
        history1.add(task);
        return task;
    }

    @Override
    public SubTask findSubTaskById(int id) {
        final SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        history1.add(subTask);
        return subTask;
    }

    @Override
    public Epic findEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        history1.add(epic);
        return epic;
    }

    @Override
    public void addTask(Task task) {
        final Task value = new Task(task.getId(), task.getType(), task.getName(), task.getStatus(),
                task.getDescription(), task.getStartTime(), task.getDuration());
        if (tasks.containsKey(task.getId())) {
            return;
        }
        if (checkTaskTime(task)) {
            System.out.println("Время занято другой задачей");
            return;
        }
        tasks.put(task.getId(), value);
        taskPrioritized.add(value);

    }

    @Override
    public void addSubTask(SubTask subTask) {
        final SubTask value = new SubTask(subTask.getId(), subTask.getType(), subTask.getName(),
                subTask.getStatus(), subTask.getDescription(), subTask.getStartTime(),
                subTask.getDuration(), subTask.getEpicId());
        if (subTasks.containsKey(subTask.getId())) {
            return;
        }
        if (!epics.containsKey(subTask.getEpicId())) {
            return;
        }
        if (checkTaskTime(subTask)) {
            System.out.println("Время занято другой задачей");
            return;
        }
        subTasks.put(value.getId(), value);
        final Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        epic.getStatus();
        taskPrioritized.add(value);
    }

    @Override
    public void addEpic(Epic epic) {
        final Epic value = new Epic(epic.getId(), epic.getType(), epic.getName(),
                epic.getDescription());
        if (epics.containsKey(epic.getId())) {
            return;
        } else {
            epics.put(epic.getId(), value);
        }
    }

    @Override
    public void updateTask(Task task) {
        final Task saveTask = tasks.get(task.getId());
        taskPrioritized.remove(saveTask);
        if (saveTask == null) {
            System.out.println("Такая задача не существует");
            //throw new IllegalArgumentException("Такая задача не существует");
            return;
        }
        if (checkTaskTime(task)) {
            System.out.println("Время занято другой задачей");
            taskPrioritized.add(saveTask);
            return;
        }
        tasks.put(task.getId(), task);
        taskPrioritized.add(task);
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        final SubTask saveSubTask = subTasks.get(newSubTask.getId());
        final Epic epic = epics.get(newSubTask.getEpicId());
        if (saveSubTask == null) {
            System.out.println("Такая подзадача не существует");
            return;
        }
        taskPrioritized.remove(saveSubTask);
        if (checkTaskTime(newSubTask)) {
            System.out.println("Время занято другой задачей");
            taskPrioritized.add(saveSubTask);
            return;
        }
        subTasks.put(newSubTask.getId(), newSubTask);
        epic.deleteSubTaskByEpic(saveSubTask);
        epic.addSubTask(newSubTask);
        taskPrioritized.add(newSubTask);
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
            history1.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            history1.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void deleteSubTask(int id) {
        final SubTask subTask = subTasks.remove(id);
        if (subTask != null) {
            final Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTasks().remove(subTask);
            subTasks.remove(id);
            history1.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            history1.remove(id);
        }
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
                history1.remove(subTaskByEpic.getId());
            }
            epic.deleteAllSubTaskByEpic();
            epics.remove(id);
            history1.remove(id);
        } else {
            System.out.println("Такая задача не существует");
        }
    }

    @Override
    public void deleteAllEpics() {
        for (Integer id : epics.keySet()) {
            history1.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            history1.remove(id);
        }
        epics.clear();
        subTasks.clear();
    }

    @Override
    public List<Task> history() {
        return history1.getHistory();
    }

    public TreeSet<Task> getPrioritizedTasks() {
        return (TreeSet<Task>) taskPrioritized;
    }

    Comparator<Task> startTimeComparator = new Comparator<>() {
        @Override
        public int compare(Task o1, Task o2) {
            if (o1.getStartTime() != null && o2.getStartTime() != null) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
            return o1.getId() - o2.getId();
        }
    };

    Set<Task> taskPrioritized = new TreeSet<>(startTimeComparator);

    public boolean checkTaskTime(Task task) {
        Set<Task> tasksTime = getPrioritizedTasks();
        boolean checkTime = false;

        try {
            for (Task taskValue : tasksTime) {
                if ((task.getStartTime().isAfter(taskValue.getStartTime()) &&
                        task.getStartTime().isBefore(taskValue.getEndTime())) ||
                        (task.getEndTime().isAfter(taskValue.getStartTime()) &&
                                task.getEndTime().isBefore(taskValue.getEndTime())) ||
                        (taskValue.getStartTime().isAfter(task.getStartTime()) &&
                                taskValue.getStartTime().isBefore(task.getEndTime())) ||
                        (taskValue.getEndTime().isAfter(task.getStartTime()) &&
                                taskValue.getEndTime().isBefore(task.getEndTime()))) {
                    checkTime = true;
                }
            }

        } catch (NullPointerException exp) {
            checkTime = false;
        }
        return checkTime;
    }
}

