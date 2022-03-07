import controller.*;
import model.Epic;
import model.SubTask;
import model.Task;
import model.TaskType;


import static model.Status.*;
import static model.TaskType.*;

public class Main {
    public static void main(String[] args) {
        //final TaskManager manager = Managers.getDefault();
        final FileBackedTasksManager manager1 = new FileBackedTasksManager();
        Task task1 = new Task(1,TASK, "rest", NEW,"");
        Epic epic1 = new Epic(2, EPIC, "clearing", "");
        SubTask subTask = new SubTask(3, SUBTASK, "wash dish", NEW, "", epic1);
        manager1.addTask(task1);
        manager1.addEpic(epic1);
        manager1.addSubTask(subTask);
        manager1.findSubTaskById(3);
        manager1.findEpicById(2);
        System.out.println(manager1.historyToString(manager1.history()));


        /*Epic Epic1 = new Epic(1, EPIC, "Переезд", "");
        manager.addEpic(Epic1);
        SubTask SubTask1 = new SubTask(2, SUBTASK, "Ремонт",  DONE,"ванная",  Epic1);
        manager.addSubTask(SubTask1);
        SubTask SubTask2 = new SubTask(3,SUBTASK,"Собрать вещи", NEW,"", Epic1);
        manager.addSubTask(SubTask2);
        SubTask SubTask3 = new SubTask(4,SUBTASK,"Разложить вещи", NEW,"",   Epic1);
        manager.addSubTask(SubTask3);
        Epic Epic2 = new Epic(5, EPIC,"Отпуск", "");
        manager.addEpic(Epic2);
        SubTask SubTask4 = new SubTask(6, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask4);
        SubTask SubTask5 = new SubTask(7, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask5);
        SubTask SubTask6 = new SubTask(8, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask6);
        SubTask SubTask7 = new SubTask(9, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask7);
        SubTask SubTask8 = new SubTask(10, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask8);
        SubTask SubTask9 = new SubTask(11, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask9);
        SubTask SubTask10 = new SubTask(12, SUBTASK,"4", DONE,"", Epic1);
        manager.addSubTask(SubTask10);


        System.out.println(manager.history());
        manager.findEpicById(5);
        System.out.println(manager.history());
        manager.findEpicById(1);
        System.out.println(manager.history());
        manager.findSubTaskById(2);
        System.out.println(manager.history());
        manager.findSubTaskById(3);
        System.out.println(manager.history());
        manager.findSubTaskById(4);
        System.out.println(manager.history());
        manager.findEpicById(5);
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
        manager.findEpicById(5);
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
*/
    }
}
