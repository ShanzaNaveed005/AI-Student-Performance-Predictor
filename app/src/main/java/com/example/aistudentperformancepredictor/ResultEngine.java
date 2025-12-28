package com.example.aistudentperformancepredictor;

import java.util.ArrayList;
import java.util.List;

public class ResultEngine {

    public static class Subject {
        public String name;
        public int obtained, total;

        public Subject(String name, int obtained, int total) {
            this.name = name;
            this.obtained = obtained;
            this.total = total;
        }
    }

    public static class Result {
        public double percentage;
        public String grade;
        public List<String> weakSubjects = new ArrayList<>();
    }

    public static Result calculateResult(List<Subject> subjects) {
        Result result = new Result();
        int totalObtained = 0;
        int totalMax = 0;

        for (Subject s : subjects) {
            totalObtained += s.obtained;
            totalMax += s.total;

            // AI Logic: Agar marks 50% se kam hain to weak subject hai
            double subPerc = (s.obtained * 100.0) / s.total;
            if (subPerc < 50) {
                result.weakSubjects.add(s.name);
            }
        }

        if (totalMax > 0) {
            result.percentage = (totalObtained * 100.0) / totalMax;
        }

        // Grading Logic
        if (result.percentage >= 80) result.grade = "A+ (Excellent)";
        else if (result.percentage >= 70) result.grade = "A (Very Good)";
        else if (result.percentage >= 60) result.grade = "B (Good)";
        else if (result.percentage >= 50) result.grade = "C (Needs Work)";
        else result.grade = "F (High Risk ❌)";

        return result;
    }
}