import controller.InMemoryTaskManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

import static model.Status.*;

public class Main {
    public static void main(String[] args) {
        final TaskManager manager = Managers.getDefault();

        System.out.println("Проверка задач");
        Task newTask41 = new Task("", "", 1, NEW);
        manager.addTask(newTask41);
        System.out.println("Новая задача: " + manager.findTaskById(1));
        Task newTask42 = new Task("Вынести мусор", "уже воняет", 1, IN_PROGRESS);
        manager.updateTask(newTask42);
        System.out.println("Обновленная задача: " + manager.findTaskById(1));
        System.out.println("Все задачи: " + manager.returnAllTask());
        manager.deleteAllTasks();
        System.out.println("Все задачи после удаления: " + manager.returnAllTask());

        System.out.println("Проверка эпиков и подзадач");
        Epic newEpic1 = new Epic("Переезд", "", 1);
        manager.addEpic(newEpic1);
        System.out.println("Новый эпик: " + manager.findEpicsByID(1));
        SubTask newSubTask1 = new SubTask("Ремонт", "ванная", 1,
                DONE, newEpic1);
        manager.addSubTask(newSubTask1);
        SubTask newSubTask2 = new SubTask("Собрать вещи", "", 2, NEW, newEpic1);
        manager.addSubTask(newSubTask2);
        System.out.println("Все подзадачи эпика: " + manager.returnAllSubTasksByEpic(1));
        System.out.println("Подзадача, id = 1: " + manager.findSubTaskById(1));
        System.out.println("Подзадача, id = 2: " + manager.findSubTaskById(2));
        SubTask newSubTask21 = new SubTask("Упаковать вещи", "не забыть кота",
                2, IN_PROGRESS, newEpic1);
        manager.updateSubTask(newSubTask21);
        System.out.println("Эпик после обновления подзадачи: " + manager.findEpicsByID(1));
        System.out.println("Все подзадачи эпика после обновления: "
                + manager.returnAllSubTasksByEpic(1));
        System.out.println("Подзадача после обновления, id = 2: " + manager.findSubTaskById(2));
        manager.deleteSubTask(2);
        System.out.println("Эпик после удаления подзадачи: " + manager.findEpicsByID(1));
        System.out.println("Все подзадачи эпика после удаления подзадачи: "
                + manager.returnAllSubTasksByEpic(1));
        System.out.println("Подзадача, id = 1: " + manager.findSubTaskById(1));
        System.out.println("Подзадача, id = 2: " + manager.findSubTaskById(2));
        System.out.println("Обновление эпика");
        Epic newEpic3 = new Epic("Отпуск", "", 1);
        manager.updateEpic(newEpic3);
        System.out.println("Эпик после обновления: " + manager.findEpicsByID(1));
        System.out.println("Все подзадачи эпика после обновления: "
                + manager.returnAllSubTasksByEpic(1));


        System.out.println("Проверка истории");
        if (manager.history().isEmpty()) {
            System.out.println("Не работает метод история");
        } else {
            for (Task item : manager.history()) {
                System.out.println("История : " + item);
            }
        }
        System.out.println("Проверка размера истории");
        for (int i = 0; i < 10; i++) {
            manager.findEpicsByID(1);
        }
        for (Task item : manager.history()) {
            System.out.println("История : " + item);
        }
        manager.deleteEpic(1);
        System.out.println("Все подзадачи эпика после его удаления: "
                + manager.returnAllSubTasksByEpic(1));
        manager.deleteAllEpics();
        System.out.println("Список эпиков после удаления всех эпиков: " + manager.returnAllEpic());


    }
}
