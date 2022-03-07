package model;

public class SubTask extends Task {
    private Epic epic;
    private static final String NULL_STRING = "null";

    public SubTask( int id, TaskType type, String name, Status status, String description, Epic epic) {
        super( id, type, name, status, description);
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

    @Override
    public String asString() {
        return String.format(
                "%s, %s, %s, %s, %s, %s\n",
                super.getId(),
                super.getType(),
                super.getName().isEmpty() ? NULL_STRING : super.getName(),
                super.getStatus(),
                super.getDescription().isEmpty() ? NULL_STRING : super.getDescription(),
                epic != null ? Integer.toString(epic.getId()) : NULL_STRING
        );
    }
}
