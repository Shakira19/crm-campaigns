/*
 * Creation date: 22 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.enums;

/**
 *
 * @author Alan Quimbita
 */
public enum CampaignStatusEnum {

    ACTIVE("ACT", "ACTIVE"),
    TERMINATED("TER", "TERMINATED"),
    SUSPENDED("SUS", "SUSPENDED");

    private final String status;
    private final String description;

    private CampaignStatusEnum(String status, String description) {
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
