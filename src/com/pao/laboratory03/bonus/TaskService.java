package com.pao.laboratory03.bonus;

import java.util.*;

public class TaskService {
    private static TaskService instance;
    private final Map<String, Task> tasksById = new HashMap<>();
    private final Map<Priority, List<Task>> tasksByPriority = new HashMap<>();
    private final List<String> auditLog = new ArrayList<>();
    private int nextId = 1;

    private TaskService() {}

    public static TaskService getInstance() {
        if (instance == null) instance = new TaskService();
        return instance;
    }

    public Task addTask(String title, Priority priority) {
        String id = String.format("T%03d", nextId++);
        if (tasksById.containsKey(id)) throw new DuplicateTaskException("ID deja existent");

        Task task = new Task(id, title, priority);
        tasksById.put(id, task);
        tasksByPriority.computeIfAbsent(priority, k -> new ArrayList<>()).add(task);

        auditLog.add("[ADD] " + id + ": '" + title + "' (" + priority + ")");
        return task;
    }

    public void assignTask(String taskId, String assignee) {
        Task t = tasksById.get(taskId);
        if (t == null) throw new TaskNotFoundException("Task " + taskId + " inexistent");

        t.setAssignee(assignee);
        auditLog.add("[ASSIGN] " + taskId + " -> " + assignee);
    }

    public void changeStatus(String taskId, Status newStatus) {
        Task t = tasksById.get(taskId);
        if (t == null) throw new TaskNotFoundException("Task " + taskId + " inexistent");

        if (!t.getStatus().canTransitionTo(newStatus)) {
            throw new InvalidTransitionException(t.getStatus(), newStatus);
        }

        auditLog.add("[STATUS] " + taskId + ": " + t.getStatus() + " -> " + newStatus);
        t.setStatus(newStatus);
    }

    public List<Task> getTasksByPriority(Priority p) {
        return tasksByPriority.getOrDefault(p, Collections.emptyList());
    }

    public Map<Status, Long> getStatusSummary() {
        Map<Status, Long> summary = new HashMap<>();
        for (Status s : Status.values()) {
            long count = tasksById.values().stream().filter(t -> t.getStatus() == s).count();
            summary.put(s, count);
        }
        return summary;
    }

    public List<Task> getUnassignedTasks() {
        List<Task> unassigned = new ArrayList<>();
        for (Task t : tasksById.values()) if (t.getAssignee() == null) unassigned.add(t);
        return unassigned;
    }

    public double getTotalUrgencyScore(int baseDays) {
        return tasksById.values().stream()
                .filter(t -> t.getStatus() != Status.DONE && t.getStatus() != Status.CANCELLED)
                .mapToDouble(t -> t.getPriority().calculateScore(baseDays))
                .sum();
    }

    public void printAuditLog() {
        auditLog.forEach(System.out::println);
    }
}