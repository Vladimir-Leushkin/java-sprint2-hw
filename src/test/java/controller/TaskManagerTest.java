package controller;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static model.Status.*;
import static model.TaskType.*;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest <T extends TaskManager> {

    protected T manager;
    LocalDateTime taskTime = LocalDateTime.of(2022, 01, 1, 00, 00);
    Duration taskDuration = Duration.ofHours(10);
    Task task = new Task(1, TASK, "Task1", NEW, "Description task1", taskTime,
            taskDuration);
    Epic epic = new Epic(2, EPIC, "Epic2", "Description epic2");
    LocalDateTime firstSubTaskTime = LocalDateTime.of(2022, 01, 2,
            0, 00);
    Duration firstSubTaskDuration = Duration.ofHours(11);
    SubTask firstSubTask = new SubTask(3, SUBTASK, "SubTask3", NEW,
            "Description subtask3", firstSubTaskTime, firstSubTaskDuration,
            epic.getId());
    LocalDateTime secondSubTaskTime = LocalDateTime.of(2022, 1, 3,
            00, 00);
    Duration secondSubTaskDuration = Duration.ofHours(12);
    SubTask secondSubTask = new SubTask(4, SUBTASK, "SubTask4", NEW,
            "Description subtask4", secondSubTaskTime, secondSubTaskDuration,
            epic.getId());
    Task task2 = new Task(5, TASK, "Task2", NEW, "Description task2");
    Task task3 = new Task(6, TASK, "Task3", NEW, "Description task3", taskTime,
            taskDuration);
    SubTask thirdSubTask = new SubTask(7, SUBTASK, "SubTask4", NEW,
            "Description subtask4", firstSubTaskTime, firstSubTaskDuration,
            2);

    @Test
    void shouldReturnListAllTask(){
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        //Исполнение
        final List<Task> allTask = manager.returnAllTask();
        //Проверка
        assertNotNull(allTask, "Список пуст");
        assertEquals(2, allTask.size(), "Неверное количество задач.");
        assertEquals(task, allTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllTask() {
        //Подготовка
        //Исполнение
        final List<Task> allTask = manager.returnAllTask();
        //Проверка
        assertEquals(0, allTask.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllEpic() {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        final List<Task> allEpic = manager.returnAllEpic();
        //Проверка
        assertNotNull(allEpic, "Список пуст");
        assertEquals(1, allEpic.size(), "Неверное количество задач.");
        assertEquals(epic, allEpic.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllEpic() {
        //Подготовка
        //Исполнение
        final List<Task> allEpic = manager.returnAllEpic();
        //Проверка
        assertEquals(0, allEpic.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = manager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(allSubTask, "Список пуст");
        assertEquals(2, allSubTask.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, allSubTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllSubTask() {
        //Подготовка
        manager.addEpic(epic);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = manager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertEquals(0, allSubTask.size(), "Список не пуст");
    }

    @Test
    void shouldFindTask() {
        //Подготовка
        //Исполнение
        manager.addTask(task);
        final int taskId = task.getId();
        //Проверка
        assertEquals(task, manager.findTaskById(taskId));
    }

    @Test
    void shouldFindEmptyTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        //Проверка
        assertEquals(null, manager.findTaskById(taskId));
    }

    @Test
    void shouldFindSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(firstSubTask, manager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEmptySubTask() {
        //Подготовка
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(null, manager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEpic() {
        //Подготовка
        //Исполнение
        manager.addEpic(epic);
        final int epicId = epic.getId();
        //Проверка
        assertEquals(epic, manager.findEpicById(epicId));
    }

    @Test
    void shouldFindEmptyEpic() {
        //Подготовка
        //Исполнение
        final int epicId = epic.getId();
        //Проверка
        assertEquals(null, manager.findEpicById(epicId));
    }

    @Test
    void shouldAddNewTask() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        final Task savedTask = manager.findTaskById(taskId);
        final List<Task> tasks = manager.returnAllTask();
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldAddExistingTask() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addTask(task);
                    }
                });

        assertEquals("Такая задача уже существует", ex.getMessage());
    }

    @Test
    void shouldAddTaskWithBusyTime() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addTask(task3);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldAddNewEpic() {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        final int epicId = epic.getId();
        final Task savedEpic = manager.findEpicById(epicId);
        final List<Task> epics = manager.returnAllEpic();
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldAddExistingEpic() {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addEpic(epic);
                    }
                });

        assertEquals("Такой эпик уже существует", ex.getMessage());
    }


    @Test
    void shouldAddNewSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        final int firstSubtaskId = firstSubTask.getId();
        final SubTask savedSubTaskFirst = manager.findSubTaskById(firstSubtaskId);
        final List<SubTask> subTasks = manager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, subTasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldAddExistingSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSubTask(firstSubTask);
                    }
                });

        assertEquals("Такая подзадача уже существует", ex.getMessage());
    }

    @Test
    void shouldAddSubTaskWithNotExistEpic() {
        //Подготовка
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSubTask(thirdSubTask);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldAddSubTaskWithBusyTime() {
        //Подготовка
        manager.deleteAllTasks();
        manager.deleteAllSubTasks();
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        SubTask fourthSubTask = new SubTask(10, SUBTASK, "SubTask4", NEW,
                "Description subtask4", firstSubTaskTime, firstSubTaskDuration,
                2);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.addSubTask(fourthSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateTask() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        task.setName("updateTask");
        manager.updateTask(task);
        final int taskId = task.getId();
        final Task savedTask = manager.findTaskById(taskId);
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorTask() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateTask(task2);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateTaskWithBusyTime() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        task.setName("updateTask");
        task.setStartTime(firstSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateTask(task);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateEpic() {
        //Подготовка
        manager.addEpic(epic);
        //Исполнение
        epic.setName("updateEpic");
        manager.updateEpic(epic);
        final int epicId = epic.getId();
        final Task savedEpic = manager.findEpicById(epicId);
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorEpic() {
        //Подготовка
        manager.addEpic(epic);
        Epic epic2 = new Epic(8, EPIC, "Epic8", "Description epic8");
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateEpic(epic2);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        firstSubTask.setName("updateSubTask");
        manager.updateSubTask(firstSubTask);
        final SubTask savedSubTaskFirst = manager.findSubTaskById(firstSubtaskId);
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateSubTask(secondSubTask);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
    }


    @Test
    void shouldUpdateSubTaskWithBusyTime() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        firstSubTask.setName("updateSubTask");
        firstSubTask.setStartTime(secondSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.updateSubTask(firstSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldDeleteTask() {
        //Подготовка
        manager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        manager.deleteTask(taskId);
        //Проверка
        assertNull(manager.findTaskById(taskId), "Задача не удалена.");
    }

    @Test
    void shouldDeleteErrorTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        manager.deleteTask(taskId);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteTask(taskId);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        manager.deleteSubTask(firstSubtaskId);
        //Проверка
        assertNull(manager.findSubTaskById(firstSubtaskId), "Задача не удалена.");

    }

    @Test
    void shouldDeleteErrorSubTask() {
        //Подготовка
        //Исполнение
        final int firstSubTaskId = firstSubTask.getId();
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteSubTask(firstSubTaskId);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteEpic() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        manager.deleteEpic(epicId);
        final List<SubTask> allSubTasks = manager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNull(manager.findEpicById(epicId), "Задача не удалена.");
        assertNull(manager.returnAllSubTasksByEpic(epicId));

    }

    @Test
    void shouldDeleteErrorEpic() {
        //Подготовка
        //Исполнение
        final int epicId = epic.getId();
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        manager.deleteEpic(epicId);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteAllTask() {
        //Подготовка
        manager.addTask(task);
        manager.addTask(task2);
        //Исполнение
        manager.deleteAllTasks();
        final List<Task> allTask = manager.returnAllTask();
        //Проверка
        assertTrue(allTask.size() == 0);

    }

    @Test
    void shouldDeleteAllSubTask() {
        //Подготовка
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        manager.deleteAllSubTasks();
        final List<SubTask> subTasks = manager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertTrue(subTasks.size() == 0);

    }

    @Test
    void shouldDeleteAllEpic() {
        //Подготовка
        manager.addEpic(epic);
        Epic epic2 = new Epic(6, EPIC, "Epic3", "Description epic3");
        manager.addEpic(epic2);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        manager.deleteAllEpics();
        final List<Task> allEpics = manager.returnAllEpic();
        //Проверка
        assertTrue(allEpics.size() == 0);
        assertNull(manager.returnAllSubTasksByEpic(epicId), "Задачи не удалены.");

    }

    @Test
    void shouldEmptyHistoryManager() {
        //Подготовка
        //Исполнение
        List<Task> history = manager.history();
        //Проверка
        assertTrue(history.size() == 0);
    }

    @Test
    void shouldAddNewTaskInHistoryManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        final int taskId = task.getId();
        final int epicId = epic.getId();
        final int subTaskId = firstSubTask.getId();
        //Исполнение
        manager.findTaskById(taskId);
        manager.findEpicById(epicId);
        manager.findSubTaskById(subTaskId);
        final List<Task> saveHistory = manager.history();
        //Проверка
        assertNotNull(manager.history(), "Задачи не добавляются.");
        assertEquals(task, saveHistory.get(0),
                "Задачи не совпадают.");
        assertEquals(epic, manager.history().get(1),
                "Задачи не совпадают.");
        assertEquals(firstSubTask, manager.history().get(2),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveDuplicationFirstTaskInHistoryManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.findTaskById(taskId);
        //Проверка
        assertTrue(manager.history().size() == 5);
        assertEquals(task2, manager.history().get(0),
                "Задачи не совпадают.");
        assertEquals(task, manager.history().get(4),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveFirstTaskInHistoryManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.deleteTask(taskId);
        //Проверка
        assertTrue(manager.history().size() == 4);
        assertNotEquals(task, manager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveSecondTaskInHistoryManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.deleteEpic(epicId);
        //Проверка
        assertTrue(manager.history().size() == 2);
        assertNotEquals(epic, manager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveLastTaskInHistoryManager() {
        //Подготовка
        manager.addTask(task);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.deleteTask(task2Id);
        //Проверка
        assertTrue(manager.history().size() == 4);
        assertNotEquals(task2, manager.history().get(3),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveFirstTaskInFullHistoryManager() {
        //Подготовка
        Task task6 = new Task(6, TASK, "Task6", DONE, "Description task6");
        Task task7 = new Task(7, TASK, "Task7", IN_PROGRESS, "Description task7");
        Task task8 = new Task(8, TASK, "Task8", NEW, "Description task8");
        Task task9 = new Task(9, TASK, "Task9", DONE, "Description task9");
        Task task10 = new Task(10, TASK, "Task10", NEW, "Description task10");
        Task task11 = new Task(11, TASK, "Task11", IN_PROGRESS,
                "Description task11");
        manager.addTask(task);
        manager.addTask(task2);
        manager.addEpic(epic);
        manager.addSubTask(firstSubTask);
        manager.addSubTask(secondSubTask);
        manager.addTask(task6);
        manager.addTask(task7);
        manager.addTask(task8);
        manager.addTask(task9);
        manager.addTask(task10);
        manager.addTask(task11);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        final int task6Id = task6.getId();
        final int task7Id = task7.getId();
        final int task8Id = task8.getId();
        final int task9Id = task9.getId();
        final int task10Id = task10.getId();
        final int task11Id = task11.getId();
        //Исполнение
        manager.findEpicById(epicId);
        manager.findSubTaskById(firstSubTaskId);
        manager.findSubTaskById(secondSubTaskId);
        manager.findTaskById(taskId);
        manager.findTaskById(task2Id);
        manager.findTaskById(task6Id);
        manager.findTaskById(task7Id);
        manager.findTaskById(task8Id);
        manager.findTaskById(task9Id);
        manager.findTaskById(task10Id);
        manager.findTaskById(task11Id);
        //Проверка
        assertTrue(manager.history().size() == 10);
        assertNotEquals(epic, manager.history().get(0),
                "Задачи совпадают.");
    }
}
