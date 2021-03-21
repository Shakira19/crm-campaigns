package ec.edu.espe.banquito.crm.campaigns.enums;

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
