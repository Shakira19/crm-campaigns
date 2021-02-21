/*
 * Creation date: 21 feb. 2021
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
public class ContactabilityStatusRQ {

    private boolean accepted;
    private boolean rejected;
    private boolean inProgress;
    private boolean assigned;

}
