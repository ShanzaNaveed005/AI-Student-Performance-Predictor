package com.example.aistudentperformancepredictor;

public class AttendanceModel {
    public String userId;
    public int attendancePercentage;
    public String riskLevel;
    public String suggestion;
    public long timestamp;

    public AttendanceModel() {} // Required for Firebase

    public AttendanceModel(String userId, int attendancePercentage, String riskLevel, String suggestion, long timestamp) {
        this.userId = userId;
        this.attendancePercentage = attendancePercentage;
        this.riskLevel = riskLevel;
        this.suggestion = suggestion;
        this.timestamp = timestamp;
    }
}