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

public class InMemoryTaskManagerTest extends TaskManagerTest{

    final TaskManager TaskManager =
            new InMemoryTaskManager();

    @Test
    void shouldReturnListAllTask(){
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addTask(task2);
        //Исполнение
        final List<Task> allTask = TaskManager.returnAllTask();
        //Проверка
        assertNotNull(allTask, "Список пуст");
        assertEquals(2, allTask.size(), "Неверное количество задач.");
        assertEquals(task, allTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllTask() {
        //Подготовка
        //Исполнение
        final List<Task> allTask = TaskManager.returnAllTask();
        //Проверка
        assertEquals(0, allTask.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        //Исполнение
        final List<Task> allEpic = TaskManager.returnAllEpic();
        //Проверка
        assertNotNull(allEpic, "Список пуст");
        assertEquals(1, allEpic.size(), "Неверное количество задач.");
        assertEquals(epic, allEpic.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllEpic() {
        //Подготовка
        //Исполнение
        final List<Task> allEpic = TaskManager.returnAllEpic();
        //Проверка
        assertEquals(0, allEpic.size(), "Список не пуст");
    }

    @Test
    void shouldReturnListAllSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = TaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNotNull(allSubTask, "Список пуст");
        assertEquals(2, allSubTask.size(), "Неверное количество задач.");
        assertEquals(firstSubTask, allSubTask.get(0), "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyListAllSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        final int epicId = epic.getId();
        //Исполнение
        final List<SubTask> allSubTask = TaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertEquals(0, allSubTask.size(), "Список не пуст");
    }

    @Test
    void shouldFindTask() {
        //Подготовка
        //Исполнение
        TaskManager.addTask(task);
        final int taskId = task.getId();
        //Проверка
        assertEquals(task, TaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindEmptyTask() {
        //Подготовка
        //Исполнение
        final int taskId = task.getId();
        //Проверка
        assertEquals(null, TaskManager.findTaskById(taskId));
    }

    @Test
    void shouldFindSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(firstSubTask, TaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEmptySubTask() {
        //Подготовка
        //Исполнение
        final int subTaskId = firstSubTask.getId();
        //Проверка
        assertEquals(null, TaskManager.findSubTaskById(subTaskId));
    }

    @Test
    void shouldFindEpic() {
        //Подготовка
        //Исполнение
        TaskManager.addEpic(epic);
        final int epicId = epic.getId();
        //Проверка
        assertEquals(epic, TaskManager.findEpicById(epicId));
    }

    @Test
    void shouldFindEmptyEpic() {
        //Подготовка
        //Исполнение
        final int epicId = epic.getId();
        //Проверка
        assertEquals(null, TaskManager.findEpicById(epicId));
    }

    @Test
    void shouldAddNewTask() {
        //Подготовка
        TaskManager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        final Task savedTask = TaskManager.findTaskById(taskId);
        final List<Task> tasks = TaskManager.returnAllTask();
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
        TaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.addTask(task);
                    }
                });

        assertEquals("Такая задача уже существует", ex.getMessage());
    }

    @Test
    void shouldAddTaskWithBusyTime() {
        //Подготовка
        TaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.addTask(task3);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldAddNewEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        //Исполнение
        final int epicId = epic.getId();
        final Task savedEpic = TaskManager.findEpicById(epicId);
        final List<Task> epics = TaskManager.returnAllEpic();
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
        TaskManager.addEpic(epic);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.addEpic(epic);
                    }
                });

        assertEquals("Такой эпик уже существует", ex.getMessage());
    }


    @Test
    void shouldAddNewSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        final int firstSubtaskId = firstSubTask.getId();
        final SubTask savedSubTaskFirst = TaskManager.findSubTaskById(firstSubtaskId);
        final List<SubTask> subTasks = TaskManager.returnAllSubTasksByEpic(epicId);
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
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.addSubTask(firstSubTask);
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
                        TaskManager.addSubTask(thirdSubTask);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldAddSubTaskWithBusyTime() {
        //Подготовка
        TaskManager.deleteAllTasks();
        TaskManager.deleteAllSubTasks();
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        SubTask fourthSubTask = new SubTask(10, SUBTASK, "SubTask4", NEW,
                "Description subtask4", firstSubTaskTime, firstSubTaskDuration,
                2);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.addSubTask(fourthSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateTask() {
        //Подготовка
        TaskManager.addTask(task);
        //Исполнение
        task.setName("updateTask");
        TaskManager.updateTask(task);
        final int taskId = task.getId();
        final Task savedTask = TaskManager.findTaskById(taskId);
        //Проверка
        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorTask() {
        //Подготовка
        TaskManager.addTask(task);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.updateTask(task2);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateTaskWithBusyTime() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        task.setName("updateTask");
        task.setStartTime(firstSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.updateTask(task);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldUpdateEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        //Исполнение
        epic.setName("updateEpic");
        TaskManager.updateEpic(epic);
        final int epicId = epic.getId();
        final Task savedEpic = TaskManager.findEpicById(epicId);
        //Проверка
        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        Epic epic2 = new Epic(8, EPIC, "Epic8", "Description epic8");
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.updateEpic(epic2);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldUpdateSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        firstSubTask.setName("updateSubTask");
        TaskManager.updateSubTask(firstSubTask);
        final SubTask savedSubTaskFirst = TaskManager.findSubTaskById(firstSubtaskId);
        //Проверка
        assertNotNull(savedSubTaskFirst, "Задача не найдена.");
        assertEquals(firstSubTask, savedSubTaskFirst, "Задачи не совпадают.");
    }

    @Test
    void shouldUpdateErrorSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.updateSubTask(secondSubTask);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
    }


    @Test
    void shouldUpdateSubTaskWithBusyTime() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        //Исполнение
        firstSubTask.setName("updateSubTask");
        firstSubTask.setStartTime(secondSubTaskTime);
        //Проверка
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        TaskManager.updateSubTask(firstSubTask);
                    }
                });

        assertEquals("Время занято другой задачей", ex.getMessage());
    }

    @Test
    void shouldDeleteTask() {
        //Подготовка
        TaskManager.addTask(task);
        //Исполнение
        final int taskId = task.getId();
        TaskManager.deleteTask(taskId);
        //Проверка
        assertNull(TaskManager.findTaskById(taskId), "Задача не удалена.");
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
                        TaskManager.deleteTask(taskId);
                    }
                });

        assertEquals("Такая задача не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        //Исполнение
        final int firstSubtaskId = firstSubTask.getId();
        TaskManager.deleteSubTask(firstSubtaskId);
        //Проверка
        assertNull(TaskManager.findSubTaskById(firstSubtaskId), "Задача не удалена.");

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
                        TaskManager.deleteSubTask(firstSubTaskId);
                    }
                });

        assertEquals("Такая подзадача не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        TaskManager.deleteEpic(epicId);
        final List<SubTask> allSubTasks = TaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertNull(TaskManager.findEpicById(epicId), "Задача не удалена.");
        assertNull(TaskManager.returnAllSubTasksByEpic(epicId));

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
                        TaskManager.deleteEpic(epicId);
                    }
                });

        assertEquals("Такой эпик не существует", ex.getMessage());
    }

    @Test
    void shouldDeleteAllTask() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addTask(task2);
        //Исполнение
        TaskManager.deleteAllTasks();
        final List<Task> allTask = TaskManager.returnAllTask();
        //Проверка
        assertTrue(allTask.size() == 0);

    }

    @Test
    void shouldDeleteAllSubTask() {
        //Подготовка
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        TaskManager.deleteAllSubTasks();
        final List<SubTask> subTasks = TaskManager.returnAllSubTasksByEpic(epicId);
        //Проверка
        assertTrue(subTasks.size() == 0);

    }

    @Test
    void shouldDeleteAllEpic() {
        //Подготовка
        TaskManager.addEpic(epic);
        Epic epic2 = new Epic(6, EPIC, "Epic3", "Description epic3");
        TaskManager.addEpic(epic2);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        //Исполнение
        final int epicId = epic.getId();
        TaskManager.deleteAllEpics();
        final List<Task> allEpics = TaskManager.returnAllEpic();
        //Проверка
        assertTrue(allEpics.size() == 0);
        assertNull(TaskManager.returnAllSubTasksByEpic(epicId), "Задачи не удалены.");

    }

    @Test
    void shouldEmptyHistoryManager() {
        //Подготовка
        //Исполнение
        List<Task> history = TaskManager.history();
        //Проверка
        assertTrue(history.size() == 0);
    }

    @Test
    void shouldAddNewTaskInHistoryManager() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        final int taskId = task.getId();
        final int epicId = epic.getId();
        final int subTaskId = firstSubTask.getId();
        //Исполнение
        TaskManager.findTaskById(taskId);
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(subTaskId);
        final List<Task> saveHistory = TaskManager.history();
        //Проверка
        assertNotNull(TaskManager.history(), "Задачи не добавляются.");
        assertEquals(task, TaskManager.history().get(0),
                "Задачи не совпадают.");
        assertEquals(epic, TaskManager.history().get(1),
                "Задачи не совпадают.");
        assertEquals(firstSubTask, TaskManager.history().get(2),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveDuplicationFirstTaskInHistoryManager() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        TaskManager.findTaskById(taskId);
        TaskManager.findTaskById(task2Id);
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(firstSubTaskId);
        TaskManager.findSubTaskById(secondSubTaskId);
        TaskManager.findTaskById(taskId);
        //Проверка
        assertTrue(TaskManager.history().size() == 5);
        assertEquals(task2, TaskManager.history().get(0),
                "Задачи не совпадают.");
        assertEquals(task, TaskManager.history().get(4),
                "Задачи не совпадают.");
    }

    @Test
    void shouldRemoveFirstTaskInHistoryManager() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        TaskManager.findTaskById(taskId);
        TaskManager.findTaskById(task2Id);
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(firstSubTaskId);
        TaskManager.findSubTaskById(secondSubTaskId);
        TaskManager.deleteTask(taskId);
        //Проверка
        assertTrue(TaskManager.history().size() == 4);
        assertNotEquals(task, TaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveSecondTaskInHistoryManager() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(firstSubTaskId);
        TaskManager.findSubTaskById(secondSubTaskId);
        TaskManager.findTaskById(taskId);
        TaskManager.findTaskById(task2Id);
        TaskManager.deleteEpic(epicId);
        //Проверка
        assertTrue(TaskManager.history().size() == 2);
        assertNotEquals(epic, TaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldRemoveLastTaskInHistoryManager() {
        //Подготовка
        TaskManager.addTask(task);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addTask(task2);
        final int taskId = task.getId();
        final int task2Id = task2.getId();
        final int epicId = epic.getId();
        final int firstSubTaskId = firstSubTask.getId();
        final int secondSubTaskId = secondSubTask.getId();
        //Исполнение
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(firstSubTaskId);
        TaskManager.findSubTaskById(secondSubTaskId);
        TaskManager.findTaskById(taskId);
        TaskManager.findTaskById(task2Id);
        TaskManager.deleteTask(task2Id);
        //Проверка
        assertTrue(TaskManager.history().size() == 4);
        assertNotEquals(task2, TaskManager.history().get(3),
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
        TaskManager.addTask(task);
        TaskManager.addTask(task2);
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addTask(task6);
        TaskManager.addTask(task7);
        TaskManager.addTask(task8);
        TaskManager.addTask(task9);
        TaskManager.addTask(task10);
        TaskManager.addTask(task11);
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
        TaskManager.findEpicById(epicId);
        TaskManager.findSubTaskById(firstSubTaskId);
        TaskManager.findSubTaskById(secondSubTaskId);
        TaskManager.findTaskById(taskId);
        TaskManager.findTaskById(task2Id);
        TaskManager.findTaskById(task6Id);
        TaskManager.findTaskById(task7Id);
        TaskManager.findTaskById(task8Id);
        TaskManager.findTaskById(task9Id);
        TaskManager.findTaskById(task10Id);
        TaskManager.findTaskById(task11Id);
        //Проверка
        assertTrue(TaskManager.history().size() == 10);
        assertNotEquals(epic, TaskManager.history().get(0),
                "Задачи совпадают.");
    }

    @Test
    void shouldAddTasksInTaskPrioritized() {
        //Подготовка
        //Исполнение
        TaskManager.addEpic(epic);
        TaskManager.addSubTask(secondSubTask);
        TaskManager.addSubTask(firstSubTask);
        TaskManager.addTask(task);
        //Проверка
        assertNotNull(TaskManager.getPrioritizedTasks(), "Задачи не добавляются.");
        assertEquals(task, TaskManager.getPrioritizedTasks().first(),
                "Задачи не совпадают.");
    }

    @Test
    void shouldReturnEmptyTaskPrioritized() {
        //Подготовка
        //Исполнение
        //Проверка
        assertEquals(0, TaskManager.getPrioritizedTasks().size());
    }
}
