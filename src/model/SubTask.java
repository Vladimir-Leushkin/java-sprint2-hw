package model;

public class SubTask extends Task {
    private Epic epic;

    public SubTask(TaskType type, String name, String description, int id, Status status, Epic epic) {
        super(type, name, description, id, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "type='" + getType() + '\'' +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epic=" + getEpic().getId() +
                '}';
    }
}
