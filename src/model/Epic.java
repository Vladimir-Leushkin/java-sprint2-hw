package model;

import java.util.ArrayList;

public class Epic extends Task {
    public ArrayList<Task> subTasks = new ArrayList<>();

    public Epic(String name, String description, Integer ID, String status
            , ArrayList<Task> subTasks) {
        super(name, description, ID, status);
        this.subTasks = subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public ArrayList<Task> getSubTasks() {
        return subTasks;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", ID=" + getID() +
                ", status='" + getStatus() + '\'' +
                ", subTasks=" + getSubTasks().size() +
                '}';
    }
}
