package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Task {
    private int id;
    private TaskType type;
    private String name;
    private Status status;
    private String description;
    private static final String NULL_STRING = "null";
    private LocalDateTime startTime;
    private Duration duration;

    public Task(int id, TaskType type, String name, Status status, String description,
                LocalDateTime startTime, Duration duration) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;

    }

    public Task(int id, TaskType type, String name, Status status, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = null;
        this.duration = null;
    }

    public Task(Task task) {
        this.type = task.type;
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = task.status;
    }

    public Task(int id, TaskType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id + '\'' +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    public String asString() {
        return String.format(
                "%s;%s;%s;%s;%s;%s;%s\n",
                id,
                type,
                name.isEmpty() ? NULL_STRING : name,
                status,
                description.isEmpty() ? NULL_STRING : description,
                startTime,
                getEndTime()
        );
    }
}
