/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.enums;

/**
 *
 * @author esteban
 */
public enum CampaignKindProductEnum {
    LOAN("PRE", "PRESTAMO"),
    INSURANCE("SEG", "SEGURO");

    private final String type;
    private final String description;

    private CampaignKindProductEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
    
}
