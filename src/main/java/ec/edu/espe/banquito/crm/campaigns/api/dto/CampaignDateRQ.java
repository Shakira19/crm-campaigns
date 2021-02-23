/*
 * Creation date: 23 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api.dto;

import java.util.Date;
import lombok.Data;

/**
 *
 * @author Alan Quimbita
 */
@Data
public class CampaignDateRQ {
    Date startDate;
    Date endDate;
}
