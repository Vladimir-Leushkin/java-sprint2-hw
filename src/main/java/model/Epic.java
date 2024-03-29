package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static model.Status.*;

public class Epic extends Task {
    private List<SubTask> subTasks = new ArrayList<>();
    private static final String NULL_STRING = "null";

    public Epic(int id, TaskType type, String name, String description) {
        super(id, type, name, description);
    }

    public Epic(Task task) {
        super(task);
    }

    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void deleteAllSubTaskByEpic() {
        subTasks.clear();
    }

    public void deleteSubTaskByEpic(SubTask subTask) {
        subTasks.remove(subTask);

    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime epicStartTime = LocalDateTime.MAX;
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getStartTime().isBefore(epicStartTime)) {
                epicStartTime = subTasks.get(i).getStartTime();
            }

        }
        return epicStartTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime epicEndTime = LocalDateTime.MIN;
        for (int i = 0; i < subTasks.size(); i++) {
            if (subTasks.get(i).getEndTime().isAfter(epicEndTime)) {
                epicEndTime = subTasks.get(i).getEndTime();
            }
        }
        return epicEndTime;
    }

    @Override
    public Duration getDuration() {
        return Duration.between(getStartTime(), getEndTime());
    }

    @Override
    public Status getStatus() {
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
                "type='" + getType() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subTasks=" + getSubTasks().size() +
                /*", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +*/
                '}';
    }

    @Override
    public String asString() {
        return String.format(
                "%s;%s;%s;%s\n",
                super.getId(),
                super.getType(),
                super.getName().isEmpty() ? NULL_STRING : super.getName(),
                super.getDescription().isEmpty() ? NULL_STRING : super.getDescription()
        );

    }
}
