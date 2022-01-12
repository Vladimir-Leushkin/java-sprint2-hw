package model;

import java.util.ArrayList;

import static model.Status.*;

public class Epic extends Task {
    ArrayList<SubTask> subTasks = new ArrayList<>();

    public Epic(String name, String description, int id, ArrayList<SubTask> subTasks) {
        super(name, description, id);
        this.subTasks = subTasks;
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public ArrayList<SubTask> getSubTasks() {
        return subTasks;
    }

    public void deleteAllSubTaskByEpic() {
        subTasks.clear();
    }

    public void deleteSubTaskByEpic(SubTask subTask) {
        subTasks.remove(subTask);

    }

    @Override
    public void setStatus(String status) {
        super.setStatus(status);
    }

    @Override
    public String getStatus() {
        int newTask = 0;
        int doneTask = 0;

        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getStatus().equals(NEW)) {
                newTask++;
            } else if (subTasks.get(i).getStatus().equals(DONE)) {
                doneTask++;
            }
        }
        if (newTask == subTasks.size()) {
            this.setStatus(NEW);
        } else if (doneTask == subTasks.size()) {
            this.setStatus(DONE);
        } else {
            this.setStatus(IN_PROGRESS);
        }
        return super.getStatus();
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
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subTasks=" + getSubTasks().size() +
                '}';
    }
}
