/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsclient.models;

/**
 *
 * @author Vivek
 */
public class Grade {

    private String feedback;
    private double grade;


    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedBack) {
        this.feedback = feedBack;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
    
    @Override
    public String toString(){
        return ("Grade - " + grade + ", Feedback - " + feedback);
    }
}
