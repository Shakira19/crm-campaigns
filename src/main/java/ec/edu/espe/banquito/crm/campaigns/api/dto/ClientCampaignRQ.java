/*
 * Creation date: 22 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api.dto;

import lombok.Data;

/**
 *
 * @author Alan Quimbita
 */
@Data
public class ClientCampaignRQ {

    private String clientId;
    private String clientIdentification;
    private String clientName;
    private String clientSurname;
    private String clientPhone;
    private String clientEmail;
}
