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
    
    public void clearGradeStorage(){
        studentGradeMap = new HashMap<>();
    }
}