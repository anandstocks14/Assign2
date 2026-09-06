package com.example;

public class StudentGrade {

    public String calculateGrade(int marks) {

        if (marks < 0 || marks > 100) {
            return "Invalid Marks";
        }

        if (marks >= 80) {
            return "Grade A";
        } else if (marks >= 60) {
            return "Grade B";
        } else if (marks >= 40) {
            return "Grade C";
        } else {
            return "Fail";
        }
    }

    public boolean hasPassed(int marks) {
        return marks >= 40 && marks <= 100;
    }
}
