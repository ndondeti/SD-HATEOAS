/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.models;
/**
 *
 * @author Vivek
 */
public class Appeal {
    private String student;
    private String appeal;
    private AppealStatus status;
    
    public String getAppeals(){
        return appeal;
    }
    
    public void setAppeals(String appeal){
        this.appeal = appeal;
    }

    public AppealStatus getStatus() {
        return status;
    }

    public void setStatus(AppealStatus status) {
        this.status = status;
    }

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
    
    
}
