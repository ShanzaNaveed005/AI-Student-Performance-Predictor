package com.example.aistudentperformancepredictor;

public class ScheduleItem {
    public String time;
    public String task;
    public String duration;
    public boolean isWeakSubject; // Is par hum color change karenge

    public ScheduleItem(String time, String task, String duration, boolean isWeakSubject) {
        this.time = time;
        this.task = task;
        this.duration = duration;
        this.isWeakSubject = isWeakSubject;
    }
}