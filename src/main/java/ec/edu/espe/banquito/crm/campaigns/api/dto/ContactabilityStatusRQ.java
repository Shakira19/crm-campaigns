/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api.dto;

import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import java.util.ArrayList;
import java.util.List;
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

    public List<String> getStatusList() {
        List<String> statuses = new ArrayList<>();
        if (this.accepted) {
            statuses.add(ContactStatusEnum.ACEPTADO.getStatus());
        }
        if (this.rejected) {
            statuses.add(ContactStatusEnum.RECHAZADO.getStatus());
        }
        if (this.assigned) {
            statuses.add(ContactStatusEnum.PENDIENTE.getStatus()); // CAMBIAR ESTADO
        }
        if (this.inProgress) {
            statuses.add(ContactStatusEnum.CONTACTADO.getStatus()); // CAMBIAR ESTADO
        }
        return statuses;
    }
}
