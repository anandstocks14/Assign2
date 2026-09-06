package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StudentGradeTest {

    private final StudentGrade studentGrade = new StudentGrade();

    @Test
    void testGradeA() {
        assertEquals("Grade A", studentGrade.calculateGrade(85));
    }

    @Test
    void testGradeB() {
        assertEquals("Grade B", studentGrade.calculateGrade(70));
    }

    @Test
    void testGradeC() {
        assertEquals("Grade C", studentGrade.calculateGrade(50));
    }

    @Test
    void testFail() {
        assertEquals("Fail", studentGrade.calculateGrade(30));
    }

    @Test
    void testInvalidMarks() {
        assertEquals("Invalid Marks", studentGrade.calculateGrade(110));
    }

    @Test
    void testPassStatus() {
        assertTrue(studentGrade.hasPassed(70));
        assertFalse(studentGrade.hasPassed(30));
    }
}
