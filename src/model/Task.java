package model;

public class Task {
    private TaskType type;
    private String name;
    private String description;
    private int id;
    private Status status;

    public Task(TaskType type, String name, String description, int id, Status status) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(Task task) {
        this.type = task.type;
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = task.status;
    }

    public Task(TaskType type, String name, String description, int id) {
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
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
