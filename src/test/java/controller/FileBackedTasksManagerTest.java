package controller;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.List;

import static model.Status.*;
import static model.TaskType.*;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest {

        private final FileBackedTasksManager fileTaskManager =
                new FileBackedTasksManager("resources/ManagerTest-file.csv");

    @BeforeEach
    public void beforeEach() {
        fileTaskManager.deleteAllTasks();
        fileTaskManager.deleteAllEpics();
    }

    @Test
    void shouldReturnListAllTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addTask(task2);
        //Исполнение
        final List<Task> allTask = fileTaskManager.returnAllTask();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewTask = newFileTaskManager.returnAllTask();
        //Проверка
        assertNotNull(allTask, "Список пуст");
        assertEquals(2, allTask.size(), "Неверное количество задач.");
        assertEquals(task, allTask.get(0), "Задачи не совпадают.");
        assertArrayEquals(allTask.toArray(), allNewTask.toArray());

    }

    @Test
    void shouldReturnEmptyListAllTask() {
        //Подготовка
        //Исполнение
        final List<Task> allTask = fileTaskManager.returnAllTask();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewTask = newFileTaskManager.returnAllTask();
        //Проверка
        assertEquals(0, allTask.size(), "Список не пуст");
        assertArrayEquals(allTask.toArray(), allNewTask.toArray());
    }

    @Test
    void shouldReturnListAllEpic() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        //Исполнение
        final List<Task> allEpic = fileTaskManager.returnAllEpic();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewEpic = newFileTaskManager.returnAllEpic();
        //Проверка
        assertNotNull(allEpic, "Список пуст");
        assertEquals(1, allEpic.size(), "Неверное количество задач.");
        assertEquals(epic, allEpic.get(0), "Задачи не совпадают.");
        assertArrayEquals(allEpic.toArray(), allNewEpic.toArray());
    }

    @Test
    void shouldReturnEmptyListAllEpic() {
        //Подготовка
        //Исполнение
        final List<Task> allEpic = fileTaskManager.returnAllEpic();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewEpic = newFileTaskManager.returnAllEpic();
        //Проверка
        assertEquals(0, allEpic.size(), "Список не пуст");
        assertArrayEquals(allEpic.toArray(), allNewEpic.toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<SubTask> allNewSubTask = newFileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(allSubTask, "Список пуст");
        assertEquals(2, allSubTask.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, allSubTask.get(0), "Задачи не совпадают.");
        assertArrayEquals(allSubTask.toArray(), allNewSubTask.toArray());
    }

    @Test
    void shouldReturnEmptyListAllSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = fileTaskManager.returnAllSubTasksByEpic(epicId);
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<SubTask> allNewSubTask = newFileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertEquals(0, allSubTask.size(), "Список не пуст");
        assertArrayEquals(allSubTask.toArray(), allNewSubTask.toArray());
    }

    @Test
    void shouldFindTask() {
        //Подготовка
        //Исполнение
        fileTaskManager.addTask(task);
        final int taskId = task.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(task, fileTaskManager.findTaskById(taskId));
        assertEquals(task, newFileTaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindEmptyTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(null, fileTaskManager.findTaskById(taskId));
        assertEquals(null, newFileTaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(firstSubTask, fileTaskManager.findSubTaskById(subTaskId));
        assertEquals(firstSubTask, newFileTaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEmptySubTask() {
        //Подготовка
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(null, fileTaskManager.findSubTaskById(subTaskId));
        assertEquals(null, newFileTaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEpic() {
        //Подготовка
        //Исполнение
        fileTaskManager.addEpic(epic);
        final int epicId = epic.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(epic, fileTaskManager.findEpicById(epicId));
        assertEquals(epic, newFileTaskManager.findEpicById(epicId));
    }

    @Test
    void shouldFindEmptyEpic() {
        //Подготовка
        //Исполнение
        final int epicId = epic.getId();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(null, fileTaskManager.findEpicById(epicId));
        assertEquals(null, newFileTaskManager.findEpicById(epicId));
    }

    @Test
    void shouldAddNewTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        final Task savedTask = fileTaskManager.findTaskById(taskId);
        final List<Task> tasks = fileTaskManager.returnAllTask();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> newTasks= newFileTaskManager.returnAllTask();
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
        assertArrayEquals(tasks.toArray(), newTasks.toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> newEpics= newFileTaskManager.returnAllEpic();
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
        assertArrayEquals(epics.toArray(), newEpics.toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<SubTask> newSubTasks = newFileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(2, subTasks.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, subTasks.get(0), "Задачи не совпадают.");
        assertArrayEquals(subTasks.toArray(), newSubTasks.toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
        assertEquals(task, newFileTaskManager.findTaskById(taskId),"Задачи не совпадают.");
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
        assertEquals(epic, newFileTaskManager.findEpicById(epicId), "Задачи не совпадают.");
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
        assertEquals(firstSubTask, newFileTaskManager.findSubTaskById(firstSubtaskId),
                "Задачи не совпадают.");
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNull(fileTaskManager.findTaskById(taskId), "Задача не удалена.");
        assertNull(newFileTaskManager.findTaskById(taskId), "Задача не удалена.");
    }

    @Test
    void shouldDeleteErrorTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        fileTaskManager.deleteTask(taskId);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteSubTask() {
        //Подготовка
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        fileTaskManager.deleteSubTask(firstSubtaskId);
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNull(fileTaskManager.findSubTaskById(firstSubtaskId), "Задача не удалена.");
        assertNull(newFileTaskManager.findSubTaskById(firstSubtaskId), "Задача не удалена.");

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
                        fileTaskManager.deleteSubTask(firstSubTaskId);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertNull(fileTaskManager.findEpicById(epicId), "Задача не удалена.");
        assertNull(fileTaskManager.returnAllSubTasksByEpic(epicId));
        assertNull(newFileTaskManager.findEpicById(epicId), "Задача не удалена.");
        assertNull(newFileTaskManager.returnAllSubTasksByEpic(epicId));

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
                        fileTaskManager.deleteEpic(epicId);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteAllTask() {
        //Подготовка
        fileTaskManager.addTask(task);
        fileTaskManager.addTask(task2);
        //Исполнение
        fileTaskManager.deleteAllTasks();
        final List<Task> allTask = fileTaskManager.returnAllTask();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewTask = newFileTaskManager.returnAllTask();
        //Проверка
        assertEquals(0, allTask.size());
        assertEquals(0, allNewTask.size());

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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<SubTask> newSubTasks = newFileTaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertEquals(0, subTasks.size());
        assertEquals(0, newSubTasks.size());

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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> allNewEpics = fileTaskManager.returnAllEpic();
        //Проверка
        assertEquals(0, allEpics.size());
        assertNull(fileTaskManager.returnAllSubTasksByEpic(epicId));
        assertEquals(0, newFileTaskManager.returnAllEpic().size());
        assertNull(newFileTaskManager.returnAllSubTasksByEpic(epicId));

    }

    @Test
    void shouldEmptyHistoryManager() {
        //Подготовка
        //Исполнение
        List<Task> history = fileTaskManager.history();
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        List<Task> newHistory = fileTaskManager.history();
        //Проверка
        assertEquals(0, history.size());
        assertEquals(0, newHistory.size());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        final List<Task> saveNewHistory = newFileTaskManager.history();
        //Проверка
        assertNotNull(fileTaskManager.history(), "Задачи не добавляются.");
        assertEquals(task, saveHistory.get(0),
                "Задачи не совпадают.");
        assertEquals(epic, fileTaskManager.history().get(1),
                "Задачи не совпадают.");
        assertEquals(firstSubTask, fileTaskManager.history().get(2),
                "Задачи не совпадают.");
        assertArrayEquals(saveHistory.toArray(), saveNewHistory.toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertTrue(fileTaskManager.history().size() == 5);
        assertEquals(task2, fileTaskManager.history().get(0),
                "Задачи не совпадают.");
        assertEquals(task, fileTaskManager.history().get(4),
                "Задачи не совпадают.");
        assertArrayEquals(fileTaskManager.history().toArray(),
                newFileTaskManager.history().toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertTrue(fileTaskManager.history().size() == 4);
        assertNotEquals(task, fileTaskManager.history().get(0),
                "Задачи совпадают.");
        assertArrayEquals(fileTaskManager.history().toArray(),
                newFileTaskManager.history().toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertTrue(fileTaskManager.history().size() == 2);
        assertNotEquals(epic, fileTaskManager.history().get(0),
                "Задачи совпадают.");
        assertArrayEquals(fileTaskManager.history().toArray(),
                newFileTaskManager.history().toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertTrue(fileTaskManager.history().size() == 4);
        assertNotEquals(task2, fileTaskManager.history().get(3),
                "Задачи совпадают.");
        assertArrayEquals(fileTaskManager.history().toArray(),
                newFileTaskManager.history().toArray());
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertTrue(fileTaskManager.history().size() == 10);
        assertNotEquals(epic, fileTaskManager.history().get(0),
                "Задачи совпадают.");
        assertArrayEquals(fileTaskManager.history().toArray(),
                newFileTaskManager.history().toArray());
    }

    @Test
    void shouldCreateEmptyManagerFileBackedTasksManager() {
        //Подготовка
        //Исполнение
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                fileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
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
        final FileBackedTasksManager newFileTaskManager = fileTaskManager.loadFromFile(
                "resources/ManagerTest-file.csv", "resources/NewManagerTest-file.csv");
        //Проверка
        assertEquals(newFileTaskManager.history(), fileTaskManager.history(),
                "Истории не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllTask().toArray(),
                fileTaskManager.returnAllTask().toArray(),
                "Задачи не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllEpic().toArray(),
                fileTaskManager.returnAllEpic().toArray(),
                "Эпики не совпадают.");
        assertArrayEquals(newFileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                fileTaskManager.returnAllSubTasksByEpic(epicId).toArray(),
                "Подзадачи не совпадают.");
    }

    @Test
    void shouldAddTasksInTaskPrioritized() {
        //Подготовка
        //Исполнение
        fileTaskManager.addEpic(epic);
        fileTaskManager.addSubTask(secondSubTask);
        fileTaskManager.addSubTask(firstSubTask);
        fileTaskManager.addTask(task);
        //Проверка
        assertNotNull(fileTaskManager.getPrioritizedTasks(), "Задачи не добавляются.");
        assertEquals(task, fileTaskManager.getPrioritizedTasks().first(),
                "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyTaskPrioritized() {
        //Подготовка
        //Исполнение
        //Проверка
        assertEquals(0, fileTaskManager.getPrioritizedTasks().size());
    }

}