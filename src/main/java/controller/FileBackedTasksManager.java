package controller;

import model.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static final String FIELDS_SPLITTER = ";";
    private static final String NULL_STRING = "null";
    private final String file;

    public FileBackedTasksManager(String file) {
        this.file = file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask newSubTask) {
        super.updateSubTask(newSubTask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public List<Task> history() {
        return super.history();
    }

    @Override
    public SubTask findSubTaskById(int id) {
        final SubTask subTask = getSubTasks().get(id);
        if (subTask == null) {
            return null;
        }
        historyManager.add(subTask);
        save();
        return subTask;
    }

    @Override
    public Task findTaskById(int id) {
        final Task task = getTasks().get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        save();
        return task;
    }

    @Override
    public Epic findEpicById(int id) {
        final Epic epic = getEpics().get(id);
        if (epic == null) {
            return null;
        }
        historyManager.add(epic);
        save();
        return epic;
    }

    public void save() throws ManagerSaveException {

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(
                file))) {
            for (Task task : getTasks().values()) {
                bufferedWriter.write(task.asString());
            }
            for (Epic epic : getEpics().values()) {
                bufferedWriter.write(epic.asString());
            }
            for (SubTask subTask : getSubTasks().values()) {
                bufferedWriter.write(subTask.asString());
            }
            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.write(historyToString(historyManager.getHistory()));
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public FileBackedTasksManager loadFromFile(String file, String newFile) {
        FileBackedTasksManager manager = new FileBackedTasksManager(newFile);
        Map<Integer, Task> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String value = br.readLine();
                if (!value.isEmpty()) {
                    String[] fields = value.split(FIELDS_SPLITTER);
                    if (fields.length >= 2 && !fields[1].isEmpty() && fields[1].equals("TASK")) {
                        Task task = taskFromString(value);
                        manager.addTask(task);
                        map.put(task.getId(), task);
                    } else if (fields.length >= 2 && !fields[1].isEmpty() &&
                            fields[1].equals("EPIC")) {
                        Epic epic = epicFromString(value);
                        manager.addEpic(epic);
                        map.put(epic.getId(), epic);
                    } else if (fields.length >= 2 && !fields[1].isEmpty() &&
                            fields[1].equals("SUBTASK")) {
                        SubTask subTask = subTaskFromString(value);
                        manager.addSubTask(subTask);
                        map.put(subTask.getId(), subTask);
                    } else {
                        List<Integer> history = historyFromString(value);
                        for (Integer id : history) {
                            historyManager.add(map.get(id));
                        }
                        for (Task task : historyManager.getHistory()) {
                            if (task instanceof SubTask) {
                                manager.findSubTaskById(task.getId());
                            } else if (task instanceof Epic) {
                                manager.findEpicById(task.getId());
                            } else if (task instanceof Task) {
                                manager.findTaskById(task.getId());
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return manager;
    }

    public static Task taskFromString(String value) {
        String[] fields = value.split(FIELDS_SPLITTER);
        return new Task(
                Integer.parseInt(fields[0]),
                TaskType.valueOf(fields[1]),
                NULL_STRING.equals(fields[2]) ? "" : fields[2],
                Status.valueOf(fields[3]),
                NULL_STRING.equals(fields[4]) ? "" : fields[4],
                NULL_STRING.equals(fields[5]) ? null : LocalDateTime.parse(fields[5]),
                NULL_STRING.equals(fields[5]) || NULL_STRING.equals(fields[6]) ? null :
                        Duration.between(LocalDateTime.parse(fields[5]),
                                LocalDateTime.parse(fields[6]))
        );
    }

    public SubTask subTaskFromString(String value) {
        String[] fields = value.split(FIELDS_SPLITTER);
        return new SubTask(
                Integer.parseInt(fields[0]),
                TaskType.valueOf(fields[1]),
                NULL_STRING.equals(fields[2]) ? "" : fields[2],
                Status.valueOf(fields[3]),
                NULL_STRING.equals(fields[4]) ? "" : fields[4],
                NULL_STRING.equals(fields[5]) ? null : LocalDateTime.parse(fields[5]),
                NULL_STRING.equals(fields[5]) || NULL_STRING.equals(fields[6]) ? null :
                        Duration.between(LocalDateTime.parse(fields[5]),
                                LocalDateTime.parse(fields[6])),
                Integer.parseInt(fields[7])
        );
    }

    public static Epic epicFromString(String value) {
        String[] fields = value.split(FIELDS_SPLITTER);
        return new Epic(
                Integer.parseInt(fields[0]),
                TaskType.valueOf(fields[1]),
                NULL_STRING.equals(fields[2]) ? "" : fields[2],
                NULL_STRING.equals(fields[3]) ? "" : fields[3]
        );
    }

    public static String historyToString(List<Task> history) {
        String idTaskInHistory = "";
        for (Task task : history) {
            idTaskInHistory += String.format("%s;", task.getId());
        }
        return idTaskInHistory;
    }

    public static List<Integer> historyFromString(String value) {
        List<Integer> taskIdInHistory = new ArrayList<>();
        String[] idInHistory = value.split(FIELDS_SPLITTER);
        for (String id : idInHistory) {
            taskIdInHistory.add(Integer.parseInt(id));
        }
        return taskIdInHistory;
    }

    private static class ManagerSaveException extends RuntimeException {

        private ManagerSaveException(IOException e) {
            super(e);
        }

    }
}
