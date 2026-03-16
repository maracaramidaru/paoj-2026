package com.pao.laboratory03.bonus;

class DuplicateTaskException extends RuntimeException {
    public DuplicateTaskException(String m) { super(m); }
}

class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String m) { super(m); }
}

class InvalidTransitionException extends RuntimeException {
    private final Status from, to;
    public InvalidTransitionException(Status from, Status to) {
        super("Nu se poate trece din " + from + " in " + to);
        this.from = from;
        this.to = to;
    }
}