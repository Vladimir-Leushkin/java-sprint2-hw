package model;

public class SubTask extends Task {
    Epic epic;

    public SubTask(String name, String description, Integer ID, String status, Epic epic) {
        super(name, description, ID, status);
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
                ", ID=" + getID() +
                ", status='" + getStatus() + '\'' +
                ", epic=" + getEpic().getID() +
                '}';
    }
}
