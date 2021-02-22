/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ContactabilityStatusRQ;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.service.ContactabilityRegistrationService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alan Quimbita
 */
@RestController
@RequestMapping(path = "/api/contactabilityRegistry")
@Slf4j
public class ContactabilityRegistryController {

    private final ContactabilityRegistrationService service;

    public ContactabilityRegistryController(ContactabilityRegistrationService service) {
        this.service = service;
    }

    @GetMapping(path = "/byStatus")
    public ResponseEntity getContactabilityRegistryByStatus(@RequestBody ContactabilityStatusRQ statusesRQ) {
        String status;
        if (statusesRQ.isAccepted()) {
            status = ContactStatusEnum.ACEPTADO.getStatus();
        } else if (statusesRQ.isRejected()) {
            status = ContactStatusEnum.RECHAZADO.getStatus();
        } else if (statusesRQ.isAssigned()) {
            status = ContactStatusEnum.PENDIENTE.getStatus(); // CAMBIAR ESTADO
        } else if (statusesRQ.isInProgress()) {
            status = ContactStatusEnum.CONTACTADO.getStatus(); // CAMBIAR ESTADO
        } else {
            log.error("No status defined in HTTP Request to retrive contactability registries");
            return ResponseEntity.badRequest().body("No status defined in HTTP Request to retrive contactability registries");
        }
        log.info("The contactability registries with status: {}, will be retrived", status);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatus(status));
    }

    @GetMapping(path = "/byStatusIn")
    public ResponseEntity getContactabilityRegistryByStatusIn(@RequestBody ContactabilityStatusRQ statusesRQ) {
        List<String> statuses = new ArrayList<>();
        if (statusesRQ.isAccepted()) {
            statuses.add(ContactStatusEnum.ACEPTADO.getStatus());
        }
        if (statusesRQ.isRejected()) {
            statuses.add(ContactStatusEnum.RECHAZADO.getStatus());
        }
        if (statusesRQ.isAssigned()) {
            statuses.add(ContactStatusEnum.PENDIENTE.getStatus()); // CAMBIAR ESTADO
        }
        if (statusesRQ.isInProgress()) {
            statuses.add(ContactStatusEnum.CONTACTADO.getStatus()); // CAMBIAR ESTADO
        }
        if(statuses.size() < 2){
            log.error("Not enough statuses defined in HTTP Request to retrive contactability registries");
            return ResponseEntity.badRequest().body("Not enough statuses defined in HTTP Request to retrive contactability registries");
        }
        log.info("The contactability registries with one of this statuses: {}, will be retrived", statuses);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatusIn(statuses));
    }

}
