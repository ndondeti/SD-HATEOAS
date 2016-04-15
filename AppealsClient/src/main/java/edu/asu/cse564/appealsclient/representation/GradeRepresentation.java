/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsclient.representation;

import edu.asu.cse564.appealsclient.models.Grade;
import java.util.List;

/**
 *
 * @author Vivek
 */

public class GradeRepresentation extends Representation{

    private Grade grade;
    
    public GradeRepresentation(Grade grade, List<Link> links){
        this.grade = grade;
        this.links = links;
    }
    
    public Grade getGrade(){
        return grade;
    }
    
    public Link getPostAppealLink() {
        return getLinkByName("postAppeal");
    }
    
    public Link getSelfLink() {
        return getLinkByName("self");
    }
    
    public void print(){
        System.out.println();
    }
}
