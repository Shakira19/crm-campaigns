package ec.edu.espe.banquito.crm.campaigns.api.dto;

import lombok.Data;

@Data
public class CampaignStatusRq {

    private boolean active;
    private boolean terminated;
    private boolean suspended;
}
