package model;

import controller.FileBackedTasksManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.*;
import static model.TaskType.EPIC;
import static model.TaskType.SUBTASK;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    final FileBackedTasksManager taskManager = new FileBackedTasksManager("resources/EpicTest-file.csv");
    Epic epic = new Epic(2, EPIC, "Epic2", "Description epic2");
    LocalDateTime firstSubTaskTime = LocalDateTime.of(2022, 01, 2,
            0, 00);
    Duration firstSubTaskDuration = Duration.ofHours(11);
    SubTask firstSubTask = new SubTask(3, SUBTASK, "Sub Task1", NEW,
            "Description sub task3", firstSubTaskTime, firstSubTaskDuration,
            epic.getId());
    LocalDateTime secondSubTaskTime = LocalDateTime.of(2022, 1, 3,
            00, 00);
    Duration secondSubTaskDuration = Duration.ofHours(12);
    SubTask secondSubTask = new SubTask(4, SUBTASK, "Sub Task2", NEW,
            "Description sub task4", secondSubTaskTime, secondSubTaskDuration,
            epic.getId());


    @Test
    void EpicWithoutSubtask() {

        //Подготовка
        taskManager.addEpic(epic);
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);

        //Исполнение
        List<SubTask> subTaskList = epicInManager.getSubTasks();

        //Проверка
        assertFalse(subTaskList.size() > 0);
        assertEquals(epic, epicInManager);
        assertEquals(epic.toString(), epicInManager.toString());
    }

    @Test
    void EpicWithNewSubtask() {

        //Подготовка
        taskManager.addEpic(epic);
        taskManager.addSubTask(firstSubTask);
        taskManager.addSubTask(secondSubTask);

        //Исполнение
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);
        List<SubTask> subTaskList = epicInManager.getSubTasks();

        //Проверка
        assertEquals(NEW, epicInManager.getStatus());
        assertArrayEquals(subTaskList.toArray(), epicInManager.getSubTasks().toArray());
    }

    @Test
    void EpicWithDoneSubtask() {

        //Подготовка
        taskManager.addEpic(epic);
        taskManager.addSubTask(firstSubTask);
        taskManager.addSubTask(secondSubTask);

        //Исполнение
        firstSubTask.setStatus(DONE);
        taskManager.updateSubTask(firstSubTask);
        secondSubTask.setStatus(DONE);
        taskManager.updateSubTask(secondSubTask);
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);

        //Проверка
        assertEquals(DONE, epicInManager.getStatus());
    }

    @Test
    void EpicWithNewAndDoneSubtask() {

        //Подготовка
        taskManager.addEpic(epic);
        taskManager.addSubTask(firstSubTask);
        taskManager.addSubTask(secondSubTask);

        //Исполнение
        firstSubTask.setStatus(NEW);
        taskManager.updateSubTask(firstSubTask);
        secondSubTask.setStatus(DONE);
        taskManager.updateSubTask(secondSubTask);
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);

        //Проверка
        assertEquals(IN_PROGRESS, epicInManager.getStatus());
    }

    @Test
    void EpicWithInProgressSubtask() {

        //Подготовка
        taskManager.addEpic(epic);
        taskManager.addSubTask(firstSubTask);
        taskManager.addSubTask(secondSubTask);

        //Исполнение
        firstSubTask.setStatus(IN_PROGRESS);
        taskManager.updateSubTask(firstSubTask);
        secondSubTask.setStatus(IN_PROGRESS);
        taskManager.updateSubTask(secondSubTask);
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);

        //Проверка
        assertEquals(IN_PROGRESS, epicInManager.getStatus());
    }

    @Test
    void EpicTime() {

        //Подготовка
        taskManager.addEpic(epic);
        taskManager.addSubTask(firstSubTask);
        taskManager.addSubTask(secondSubTask);
        Duration epicDuration = Duration.between(firstSubTask.getStartTime(),
                secondSubTask.getEndTime());

        //Исполнение
        final int epicId = epic.getId();
        Epic epicInManager = taskManager.findEpicById(epicId);

        //Проверка
        assertEquals(firstSubTask.getStartTime(), epicInManager.getStartTime());
        assertEquals(secondSubTask.getEndTime(), epicInManager.getEndTime());
        assertEquals(epicDuration, epicInManager.getDuration());
    }

    @Test
    void EpicDeleteAllSubTask() {

        //Подготовка
        epic.addSubTask(firstSubTask);
        epic.addSubTask(secondSubTask);

        //Исполнение
        epic.deleteAllSubTaskByEpic();
        List<SubTask> subTaskList = epic.getSubTasks();

        //Проверка
        assertTrue(subTaskList.size() == 0);
    }
}