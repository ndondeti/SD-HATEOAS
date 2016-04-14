/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.storage;

import edu.asu.cse564.appealsserver.models.Appeal;
import java.util.HashMap;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Vivek
 */
@ApplicationScoped
public class AppealsStorage {
    private HashMap<String, Appeal> appeals;
    
    public AppealsStorage(){
        appeals = new HashMap<>();
    }
    
    public void addAppeal(String studentName, Appeal appeal){
        appeals.put(studentName, appeal);
    }
    
    public Appeal getAppeal(String studentName){
        return appeals.get(studentName);
    }
    
    public void deleteAppeal(String studentName){
        appeals.remove(studentName);
    }
}
