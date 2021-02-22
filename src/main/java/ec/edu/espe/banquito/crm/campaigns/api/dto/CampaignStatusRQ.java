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
public class CampaignStatusRQ {

    private boolean active;
    private boolean terminated;
    private boolean suspended;
}
