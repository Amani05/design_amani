package com.fges.todoapp;

public class Todoitem {
    private String task;
    private boolean done;

    public Todoitem() {}

    public Todoitem(String task, boolean done) {
        this.task = task;
        this.done = done;
    }

    // Getters et Setters
    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}