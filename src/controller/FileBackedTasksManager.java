package controller;

import model.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static model.Status.NEW;
import static model.TaskType.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private static final String FIELDS_SPLITTER = ",";
    private static final String FIELDS_NAME = "id,type,name,status,description,epic \n";


    @Override
    public void addTask(Task task) {
        super.addTask(task);
        try {
            save();
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
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
        final SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            return null;
        }
        history1.add(subTask);
        save();
        return subTask;
    }

    @Override
    public Task findTaskById(int id) {
        final Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        history1.add(task);
        save();
        return task;
    }

    @Override
    public Epic findEpicById(int id) {
        final Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        history1.add(epic);
        save();
        return epic;
    }

    public void save() throws ManagerSaveException {

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("resources/manager.csv"))){
            bufferedWriter.write(FIELDS_NAME);
            //bufferedWriter.write(System.lineSeparator());
            for(Task task: tasks.values()){
                bufferedWriter.write(task.asString());
            }
            for(Epic epic: epics.values()){
                bufferedWriter.write(epic.asString());
            }
            for(SubTask subTask: subTasks.values()){
                bufferedWriter.write(subTask.asString());
            }
            bufferedWriter.write(System.lineSeparator());
            bufferedWriter.write(historyToString(history1.getHistory()));
        }catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public Task taskFromString(String value){
        String[] fields = value.split(FIELDS_SPLITTER);
        return new Task(
               Integer.parseInt(fields[0]),
                TaskType.byValueOrTrows(fields[1]),
                fields[2],
                Status.valueOf(fields[3]),
                fields[4]
        );
    }

    public static String historyToString(List<Task> history){
        String idTaskInHistory = "";
        for(Task task: history){
            idTaskInHistory += String.format("%s,", task.getId());
        }
        return idTaskInHistory;
    }

    public static List<Integer> listFromString(String value){
        List<Integer> taskIdInHistory = new ArrayList<>();
        String[] id = value.split(FIELDS_SPLITTER);
        for(int i = 0; i < id.length; i++){
            taskIdInHistory.add(Integer.parseInt(id[i]));
        }
        return taskIdInHistory;
    }



    //Files.readString(Path.of(path));

    private static class ManagerSaveException extends RuntimeException{

        private ManagerSaveException(IOException e){
          super(e);
        }

    }
}
