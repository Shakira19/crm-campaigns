package ec.edu.espe.banquito.crm.campaigns.enums;

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
