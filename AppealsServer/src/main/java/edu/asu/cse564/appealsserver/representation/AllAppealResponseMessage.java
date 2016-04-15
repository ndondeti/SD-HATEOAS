/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.asu.cse564.appealsserver.representation;

import java.util.List;

/**
 *
 * @author Vivek
 */
public class AllAppealResponseMessage {
    private List<AppealRepresentation> appealRepresentations;
    
    public AllAppealResponseMessage(List<AppealRepresentation> appealRepresentations){
        this.appealRepresentations = appealRepresentations;
    }
}
