package ec.edu.espe.banquito.crm.campaigns.api.dto;

import lombok.Data;

@Data
public class ClientCampaignRq {

    private String clientId;
    private String clientIdentification;
    private String clientName;
    private String clientSurname;
    private String clientPhone;
    private String clientEmail;
}