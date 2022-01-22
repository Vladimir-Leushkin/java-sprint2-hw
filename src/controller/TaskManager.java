package controller;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> returnAllTask();

    List<Task> returnAllEpic();

    List<SubTask> returnAllSubTasksByEpic(int id);

    Task findTaskById(int id);

    Task findSubTaskById(int id);

    Task findEpicsByID(int id);

    Task addTask(Task task);

    SubTask addSubTask(SubTask subTask);

    Epic addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void deleteTask(int id);

    void deleteAllTasks();

    void deleteSubTask(int id);

    void deleteAllSubTasks();

    void deleteEpic(int id);

    void deleteAllEpics();

    List<Task> history();
}
