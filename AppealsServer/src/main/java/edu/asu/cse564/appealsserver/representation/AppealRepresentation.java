/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.representation;

import java.util.List;
import edu.asu.cse564.appealsserver.models.Appeal;

/**
 *
 * @author Vivek
 */
public class AppealRepresentation extends Representation {

    private Appeal appeal;

    public AppealRepresentation(Appeal appeals, List<Link> links) {
        this.appeal = appeals;
        this.links = links;
    }

    public Appeal getAppeals() {
        return appeal;
    }

    public void setAppeals(Appeal appeals) {
        this.appeal = appeals;
    }

}
