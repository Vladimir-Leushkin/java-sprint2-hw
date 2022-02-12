package controller;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    Node head = null;
    Node tail = null;
    HashMap<Integer, Node> map = new HashMap<>();
    private static final int RECENT_TASKS_COUNT = 10;

    public InMemoryHistoryManager() {
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        } else if (map.size() == RECENT_TASKS_COUNT) {
            LinkedList<Task> tasks = new LinkedList<>(getHistory());
            remove(tasks.getFirst().getId());
        }
        linkLast(task);
    }

    private void linkLast(Task task) {
        remove(task.getId());

        final Node newTask = new Node(task);
        if (head == null) {
            head = newTask;
        } else {
            tail.next = newTask;
            newTask.prev = tail;
        }
        tail = newTask;
        map.put(task.getId(), newTask);
    }

    @Override
    public void remove(Integer id) {
        final Node oldTask = map.remove(id);
        if (oldTask == null) {
        } else if (oldTask != null) {
            if (oldTask == head) {
                head = oldTask.next;
                oldTask.prev = null;
            } else if (oldTask == tail) {
                tail = oldTask.prev;
                tail.next = null;
            } else {
                oldTask.prev.next = oldTask.next;
                oldTask.next.prev = oldTask.prev;
            }
        }
    }

    @Override
    public List<Task> getHistory() {
        final List<Task> tasks = new LinkedList<>();
        Node current = head;
        while (current != null) {
            tasks.add(current.task);
            current = current.next;
        }
        return tasks;
    }


    static class Node {
        final Task task;
        Node prev;
        Node next;


        public Node(Task task) {
            this.task = task;
            this.prev = null;
            this.next = null;
        }
    }
}
