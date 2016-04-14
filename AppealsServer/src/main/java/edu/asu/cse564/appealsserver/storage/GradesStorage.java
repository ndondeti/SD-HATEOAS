/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.storage;

import edu.asu.cse564.appealsserver.models.Grade;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Vivek
 */
@ApplicationScoped
public class GradesStorage {

    private HashMap<String, Grade> studentGradeMap;

    public GradesStorage() {
        studentGradeMap = new HashMap<>();
        setupGrades();
    }
    
    public Grade getGrade(String studentName){
        if(studentGradeMap.containsKey(studentName)){
            return studentGradeMap.get(studentName);
        }
        return null;
    }
    
    public void addGrade(String studentName, Grade grade){
        studentGradeMap.put(studentName, grade);
    }
    
    private void setupGrades(){
        Grade grade = new Grade();
        grade.setFeedback("Have Not attempted question number 5");
        grade.setGrade(80.0);
        studentGradeMap.put("TestStudent1", grade);
        Grade grade2 = new Grade();
        grade2.setFeedback("Wrong answer for question number 4");
        grade2.setGrade(75.0);
        studentGradeMap.put("TestStudent2", grade2);
        
    }
}