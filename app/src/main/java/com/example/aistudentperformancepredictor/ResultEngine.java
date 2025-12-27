package com.example.aistudentperformancepredictor;

import java.util.ArrayList;
import java.util.List;

public class ResultEngine {

    public static class Subject {
        public String name;
        public int obtained;
        public int total;

        public Subject() {} // Required for Firebase

        public Subject(String name, int obtained, int total) {
            this.name = name;
            this.obtained = obtained;
            this.total = total;
        }
    }

    public static class Result {
        public int totalMarks;
        public int obtainedMarks;
        public double percentage;
        public String grade;
        public List<String> weakSubjects;

        public Result() {}

        public Result(int totalMarks, int obtainedMarks, double percentage, String grade, List<String> weakSubjects) {
            this.totalMarks = totalMarks;
            this.obtainedMarks = obtainedMarks;
            this.percentage = percentage;
            this.grade = grade;
            this.weakSubjects = weakSubjects;
        }
    }

    // Core function to generate result
    public static Result calculateResult(List<Subject> subjects) {
        int totalMarks = 0;
        int obtainedMarks = 0;
        List<String> weakSubjects = new ArrayList<>();

        for (Subject s : subjects) {
            totalMarks += s.total;
            obtainedMarks += s.obtained;

            double percent = (s.obtained * 100.0) / s.total;
            if (percent < 50) { // Threshold for weak subject
                weakSubjects.add(s.name + " (" + s.obtained + "/" + s.total + ")");
            }
        }

        double percentage = (totalMarks == 0) ? 0 : (obtainedMarks * 100.0) / totalMarks;
        String grade;

        if (percentage >= 90) grade = "A+";
        else if (percentage >= 80) grade = "A";
        else if (percentage >= 70) grade = "B+";
        else if (percentage >= 60) grade = "B";
        else if (percentage >= 50) grade = "C";
        else grade = "F";

        return new Result(totalMarks, obtainedMarks, percentage, grade, weakSubjects);
    }
}
