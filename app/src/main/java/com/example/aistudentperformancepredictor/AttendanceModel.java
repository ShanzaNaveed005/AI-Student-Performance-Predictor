package com.example.aistudentperformancepredictor;

public class AttendanceModel {

    public String userId;
    public int attendance;
    public String risk;
    public String suggestion;
    public long timestamp;

    // EMPTY constructor REQUIRED for Firebase
    public AttendanceModel() {
    }

    public AttendanceModel(String userId,
                           int attendance,
                           String risk,
                           String suggestion,
                           long timestamp) {
        this.userId = userId;
        this.attendance = attendance;
        this.risk = risk;
        this.suggestion = suggestion;
        this.timestamp = timestamp;
    }
}
