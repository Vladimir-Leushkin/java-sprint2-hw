package model;

public class SubTask extends Task {
    Epic epic;

    public SubTask(String name, String description, int id, Status status, Epic epic) {
        super(name, description, id, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epic=" + getEpic().getId() +
                '}';
    }
}
