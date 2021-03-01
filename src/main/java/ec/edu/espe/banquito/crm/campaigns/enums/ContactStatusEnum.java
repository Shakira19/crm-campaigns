/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.enums;

/**
 *
 * @author cofre
 */
public enum ContactStatusEnum {
    ASSIGNED("ASS", "ASSIGNDED"),
    INPROGRESS("INP", "INPROGRESS"),
    REJECTED("REJ", "REJECTED"),
    ACCEPTED("ACC", "ACCEPTED");
    
    private final String status;
    private final String description;
    
    private ContactStatusEnum(String status, String description) {
        this.status = status;
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }
    
    public String getDescription() {
        return description;
    }
}
