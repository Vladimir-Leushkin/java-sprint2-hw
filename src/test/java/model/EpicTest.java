package model;

import controller.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static model.TaskType.EPIC;
import static org.junit.jupiter.api.Assertions.assertFalse;

class EpicTest {

    final FileBackedTasksManager taskManager = new FileBackedTasksManager("resources/test-file.csv");
    @Test
    void EpicWithoutSubtask(){
        Epic epic = new Epic(2, EPIC, "Epic2", "Description epic2");
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);
        List<SubTask> subTaskList = epicInManager.getSubTasks();
        List<SubTask> isEmpty = new ArrayList<>();
        assertFalse(subTaskList.size() > 0);
    }




}