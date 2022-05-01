package controller;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static model.Status.*;
import static model.TaskType.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

        final FileBackedTasksManager fileTaskManager =
                new FileBackedTasksManager("resources/ManagerTest-file.csv");

    @Test
    void shouldReturnListAllTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addTask(task2);
        //Исполнение
        final List<Task> allTask = fileTaskManager.returnAllTask();
        //Проверка
        assertNotNull(allTask, "Список пуст");
        assertEquals(2, allTask.size(), "Неверное количество задач.");
        assertEquals(task, allTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllTask() {
        //Подготовка
        //Исполнение
        final List<Task> allTask = fileTaskManager.returnAllTask();
        //Проверка
        assertEquals(0, allTask.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        //Исполнение
        final List<Task> allEpic = fileTaskManager.returnAllEpic();
        //Проверка
        assertNotNull(allEpic, "Список пуст");
        assertEquals(1, allEpic.size(), "Неверное количество задач.");
        assertEquals(epic, allEpic.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllEpic() {
        //Подготовка
        //Исполнение
        final List<Task> allEpic = fileTaskManager.returnAllEpic();
        //Проверка
        assertEquals(0, allEpic.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = fileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(allSubTask, "Список пуст");
        assertEquals(2, allSubTask.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, allSubTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = fileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertEquals(0, allSubTask.size(), "Список не пуст");
    }

    @Test
    void shouldFindTask() {
        //Подготовка
        //Исполнение
        fileTaskManager.addTask(task);
        final int taskId = task.getId();
        //Проверка
        assertEquals(task, fileTaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindEmptyTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        //Проверка
        assertEquals(null, fileTaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(firstSubTask, fileTaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEmptySubTask() {
        //Подготовка
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(null, fileTaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEpic() {
        //Подготовка
        //Исполнение
        fileTaskManager.addEpic(epic);
        final int epicId = epic.getId();
        //Проверка
        assertEquals(epic, fileTaskManager.findEpicById(epicId));
    }

    @Test
    void shouldFindEmptyEpic() {
        //Подготовка
        //Исполнение
        final int epicId = epic.getId();
        //Проверка
        assertEquals(null, fileTaskManager.findEpicById(epicId));
    }

    @Test
    void shouldAddNewTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        final Task savedTask = fileTaskManager.findTaskById(taskId);
        final List<Task> tasks = fileTaskManager.returnAllTask();
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
        fileTaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.addTask(task);
                    }
                });

        assertEquals("Такая задача уже существует", ex.getMessage());
    }

    @Test
    void shouldAddTaskWithBusyTime() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.addTask(task3);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldAddNewEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        //Исполнение
        final int epicId = epic.getId();
        final Task savedEpic = fileTaskManager.findEpicById(epicId);
        final List<Task> epics = fileTaskManager.returnAllEpic();
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
        fileTaskManager.addEpic(epic);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.addEpic(epic);
                    }
                });

        assertEquals("Такой эпик уже существует", ex.getMessage());
    }


    @Test
    void shouldAddNewSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        final int firstSubtaskId = firstSubTask.getId();
        final SubTask savedSubTaskFirst = fileTaskManager.findSubTaskById(firstSubtaskId);
        final List<SubTask> subTasks = fileTaskManager.returnAllSubTasksByEpic(epicId);
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
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.addSubTask(firstSubTask);
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
                        fileTaskManager.addSubTask(thirdSubTask);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldAddSubTaskWithBusyTime() {
        //Подготовка
        fileTaskManager.deleteAllTasks();
        fileTaskManager.deleteAllSubTasks();
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        SubTask fourthSubTask = new SubTask(10, SUBTASK, "SubTask4", NEW,
                "Description subtask4", firstSubTaskTime, firstSubTaskDuration,
                2);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.addSubTask(fourthSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        task.setName("updateTask");
        fileTaskManager.updateTask(task);
        final int taskId = task.getId();
        final Task savedTask = fileTaskManager.findTaskById(taskId);
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.updateTask(task2);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateTaskWithBusyTime() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        task.setName("updateTask");
        task.setStartTime(firstSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.updateTask(task);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        //Исполнение
        epic.setName("updateEpic");
        fileTaskManager.updateEpic(epic);
        final int epicId = epic.getId();
        final Task savedEpic = fileTaskManager.findEpicById(epicId);
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        Epic epic2 = new Epic(8, EPIC, "Epic8", "Description epic8");
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.updateEpic(epic2);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        firstSubTask.setName("updateSubTask");
        fileTaskManager.updateSubTask(firstSubTask);
        final SubTask savedSubTaskFirst = fileTaskManager.findSubTaskById(firstSubtaskId);
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.updateSubTask(secondSubTask);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
    }


    @Test
    void shouldUpdateSubTaskWithBusyTime() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        firstSubTask.setName("updateSubTask");
        firstSubTask.setStartTime(secondSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.updateSubTask(firstSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldDeleteTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        fileTaskManager.deleteTask(taskId);
        //Проверка
        assertNull(fileTaskManager.findTaskById(taskId), "Задача не удалена.");
    }

    @Test
    void shouldDeleteSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        fileTaskManager.deleteSubTask(firstSubtaskId);
        //Проверка
        assertNull(fileTaskManager.findSubTaskById(firstSubtaskId), "Задача не удалена.");

    }

    @Test
    void shouldDeleteEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        fileTaskManager.deleteEpic(epicId);
        final List<SubTask> allSubTasks = fileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNull(fileTaskManager.findEpicById(epicId), "Задача не удалена.");
        assertNull(fileTaskManager.returnAllSubTasksByEpic(epicId));

    }

    @Test
    void shouldDeleteAllTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addTask(task2);
        //Исполнение
        fileTaskManager.deleteAllTasks();
        final List<Task> allTask = fileTaskManager.returnAllTask();
        //Проверка
        assertTrue(allTask.size() == 0);

    }

    @Test
    void shouldDeleteAllSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        fileTaskManager.deleteAllSubTasks();
        final List<SubTask> subTasks = fileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertTrue(subTasks.size() == 0);

    }

    @Test
    void shouldDeleteAllEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        Epic epic2 = new Epic(6, EPIC, "Epic3", "Description epic3");
        fileTaskManager.addEpic(epic2);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        fileTaskManager.deleteAllEpics();
        final List<Task> allEpics = fileTaskManager.returnAllEpic();
        //Проверка
        assertTrue(allEpics.size() == 0);
        assertNull(fileTaskManager.returnAllSubTasksByEpic(epicId), "Задачи не удалены.");

    }

    @Test
    void shouldEmptyHistoryManager() {
        //Подготовка
        //Исполнение
        List<Task> history = fileTaskManager.history();
        //Проверка
        assertTrue(history.size() == 0);
    }

    @Test
    void shouldAddNewTaskInHistoryManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        final int taskId = task.getId();
        final int epicId = epic.getId();
        final int subTaskId = firstSubTask.getId();
        //Исполнение
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(subTaskId);
        final List<Task> saveHistory = fileTaskManager.history();
        //Проверка
        assertNotNull(fileTaskManager.history(), "Задачи не добавляются.");
        assertEquals(task, saveHistory.get(0),
                "Задачи не совпадают.");
        assertEquals(epic, fileTaskManager.history().get(1),
                "Задачи не совпадают.");
        assertEquals(firstSubTask, fileTaskManager.history().get(2),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveDuplicationFirstTaskInHistoryManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.findTaskById(taskId);
        //Проверка
        assertTrue(fileTaskManager.history().size() == 5);
        assertEquals(task2, fileTaskManager.history().get(0),
                "Задачи не совпадают.");
        assertEquals(task, fileTaskManager.history().get(4),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveFirstTaskInHistoryManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.deleteTask(taskId);
        //Проверка
        assertTrue(fileTaskManager.history().size() == 4);
        assertNotEquals(task, fileTaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveSecondTaskInHistoryManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        fileTaskManager.deleteEpic(epicId);
        //Проверка
        assertTrue(fileTaskManager.history().size() == 2);
        assertNotEquals(epic, fileTaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveLastTaskInHistoryManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        fileTaskManager.deleteTask(task2Id);
        //Проверка
        assertTrue(fileTaskManager.history().size() == 4);
        assertNotEquals(task2, fileTaskManager.history().get(3),
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
        fileTaskManager.addTask(task);
        fileTaskManager.addTask(task2);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task6);
        fileTaskManager.addTask(task7);
        fileTaskManager.addTask(task8);
        fileTaskManager.addTask(task9);
        fileTaskManager.addTask(task10);
        fileTaskManager.addTask(task11);
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
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        fileTaskManager.findTaskById(task6Id);
        fileTaskManager.findTaskById(task7Id);
        fileTaskManager.findTaskById(task8Id);
        fileTaskManager.findTaskById(task9Id);
        fileTaskManager.findTaskById(task10Id);
        fileTaskManager.findTaskById(task11Id);
        //Проверка
        assertTrue(fileTaskManager.history().size() == 10);
        assertNotEquals(epic, fileTaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldCreateEmptyManagerFileBackedTasksManager() {
        //Подготовка
        fileTaskManager.deleteAllTasks();
        fileTaskManager.deleteAllSubTasks();
        fileTaskManager.deleteAllEpics();
        //Исполнение
        final FileBackedTasksManager newManager = fileTaskManager.loadFromFile
                ("resources/ManagerTest-file.csv",
                        "resources/newManagerTest-file.csv");
        //Проверка
        assertEquals(newManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
    }

    @Test
    void shouldCreateManagerFileBackedTasksManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addTask(task2);
        final int epicId = epic.getId();
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findSubTaskById(firstSubTaskId);
        fileTaskManager.findSubTaskById(secondSubTaskId);
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        //Исполнение
        final FileBackedTasksManager newManager = fileTaskManager.loadFromFile
                ("resources/ManagerTest-file.csv",
                        "resources/newManagerTest-file.csv");
        //Проверка
        assertEquals(newManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                fileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEpicWithoutSubtaskFileBackedTasksManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addTask(task2);
        final int epicId = epic.getId();
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        fileTaskManager.findEpicById(epicId);
        fileTaskManager.findTaskById(taskId);
        fileTaskManager.findTaskById(task2Id);
        //Исполнение
        final FileBackedTasksManager newManager = fileTaskManager.loadFromFile
                ("resources/ManagerTest-file.csv",
                        "resources/newManagerTest-file.csv");
        //Проверка
        assertEquals(newManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                fileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                "Позадачи не совпадают.");
    }

    @Test
    void shouldCreateManagerWithEmptyHistoryFileBackedTasksManager() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addEpic(epic);
        fileTaskManager.addTask(task2);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        final FileBackedTasksManager newManager = fileTaskManager.loadFromFile
                ("resources/ManagerTest-file.csv",
                        "resources/newManagerTest-file.csv");
        //Проверка
        assertEquals(newManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newManager.returnAllSubTasksByEpic(epicId).toArray(),
                fileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

}