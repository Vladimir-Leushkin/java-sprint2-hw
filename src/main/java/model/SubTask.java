package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;
    private static final String NULL_STRING = "null";

    public SubTask(int id, TaskType type, String name, Status status, String description,
                   LocalDateTime startTime, Duration duration, int epicId) {
        super(id, type, name, status, description, startTime, duration);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "type='" + getType() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", epicId=" + getEpicId() +
                ", startTime='" + getStartTime() + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                '}';
    }

    @Override
    public String asString() {
        return String.format(
                "%s;%s;%s;%s;%s;%s;%s;%s\n",
                super.getId(),
                super.getType(),
                super.getName().isEmpty() ? NULL_STRING : super.getName(),
                super.getStatus(),
                super.getDescription().isEmpty() ? NULL_STRING : super.getDescription(),
                super.getStartTime(),
                super.getEndTime(),
                getEpicId()
        );
    }
}
