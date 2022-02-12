import controller.InMemoryTaskManager;
import controller.Managers;
import controller.TaskManager;
import model.Epic;
import model.SubTask;


import static model.Status.*;

public class Main {
    public static void main(String[] args) {
        final TaskManager manager = Managers.getDefault();

        Epic Epic1 = new Epic("Переезд", "", 1);
        manager.addEpic(Epic1);
        SubTask SubTask1 = new SubTask("Ремонт", "ванная", 2, DONE, Epic1);
        manager.addSubTask(SubTask1);
        SubTask SubTask2 = new SubTask("Собрать вещи", "", 3, NEW, Epic1);
        manager.addSubTask(SubTask2);
        SubTask SubTask3 = new SubTask("Разложить вещи", "", 4, NEW, Epic1);
        manager.addSubTask(SubTask3);
        Epic Epic2 = new Epic("Отпуск", "", 5);
        manager.addEpic(Epic2);
        SubTask SubTask4 = new SubTask("4", "", 6, DONE, Epic1);
        manager.addSubTask(SubTask4);
        SubTask SubTask5 = new SubTask("5", "", 7, DONE, Epic1);
        manager.addSubTask(SubTask5);
        SubTask SubTask6 = new SubTask("6", "", 8, DONE, Epic1);
        manager.addSubTask(SubTask6);
        SubTask SubTask7 = new SubTask("7", "", 9, DONE, Epic1);
        manager.addSubTask(SubTask7);
        SubTask SubTask8 = new SubTask("8", "", 10, DONE, Epic1);
        manager.addSubTask(SubTask8);
        SubTask SubTask9 = new SubTask("9", "", 11, DONE, Epic1);
        manager.addSubTask(SubTask9);
        SubTask SubTask10 = new SubTask("10", "", 12, DONE, Epic1);
        manager.addSubTask(SubTask10);


        System.out.println(manager.history());
        manager.findEpicsByID(5);
        System.out.println(manager.history());
        manager.findEpicsByID(1);
        System.out.println(manager.history());
        manager.findSubTaskById(2);
        System.out.println(manager.history());
        manager.findSubTaskById(3);
        System.out.println(manager.history());
        manager.findSubTaskById(4);
        System.out.println(manager.history());
        manager.findEpicsByID(5);
        System.out.println(manager.history());
        manager.findSubTaskById(6);
        System.out.println(manager.history());
        manager.findSubTaskById(7);
        System.out.println(manager.history());
        manager.findSubTaskById(8);
        System.out.println(manager.history());
        manager.findSubTaskById(9);
        System.out.println(manager.history());
        manager.findSubTaskById(10);
        System.out.println(manager.history());
        manager.findSubTaskById(11);
        System.out.println(manager.history());
        System.out.println(manager.history().size());
        manager.findSubTaskById(12);
        System.out.println(manager.history());
        System.out.println(manager.history().size());
        manager.findEpicsByID(5);
        System.out.println(manager.history());
        System.out.println(manager.history().size());
        manager.findSubTaskById(9);
        System.out.println(manager.history());
        System.out.println(manager.history().size());
        manager.findSubTaskById(8);
        System.out.println(manager.history());
        manager.findSubTaskById(7);
        System.out.println(manager.history());
        manager.findSubTaskById(6);
        System.out.println(manager.history());

        manager.deleteEpic(1);
        System.out.println(manager.history());

    }
}
