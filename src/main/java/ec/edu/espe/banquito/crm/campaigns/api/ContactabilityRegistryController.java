/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ContactabilityStatusRQ;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.service.ContactabilityRegistrationService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alan Quimbita
 */
@RestController
@RequestMapping(path = "/api/contactability-registry")
@Slf4j
public class ContactabilityRegistryController {

    private final ContactabilityRegistrationService service;

    public ContactabilityRegistryController(ContactabilityRegistrationService service) {
        this.service = service;
    }

    @GetMapping(path = "/by-status")
    public ResponseEntity getContactabilityRegistryByStatus(@RequestBody ContactabilityStatusRQ statusesRQ) {
        String status;
        if (statusesRQ.isAccepted()) {
            status = ContactStatusEnum.ACCEPTED.getStatus();
        } else if (statusesRQ.isRejected()) {
            status = ContactStatusEnum.REJECTED.getStatus();
        } else if (statusesRQ.isAssigned()) {
            status = ContactStatusEnum.ASSIGNED.getStatus();
        } else if (statusesRQ.isInProgress()) {
            status = ContactStatusEnum.INPROGRESS.getStatus();
        } else {
            log.error("No status defined in HTTP Request to retrive contactability registries");
            return ResponseEntity.badRequest().body("No status defined in HTTP Request to retrive contactability registries");
        }
        log.info("The contactability registries with status: {}, will be retrived", status);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatus(status));
    }

    @GetMapping(path = "/by-status-in")
    public ResponseEntity getContactabilityRegistryByStatusIn(@RequestBody ContactabilityStatusRQ statusesRQ) {
        List<String> statuses = new ArrayList<>();
        if (statusesRQ.isAccepted()) {
            statuses.add(ContactStatusEnum.ACCEPTED.getStatus());
        }
        if (statusesRQ.isRejected()) {
            statuses.add(ContactStatusEnum.REJECTED.getStatus());
        }
        if (statusesRQ.isAssigned()) {
            statuses.add(ContactStatusEnum.ASSIGNED.getStatus());
        }
        if (statusesRQ.isInProgress()) {
            statuses.add(ContactStatusEnum.INPROGRESS.getStatus());
        }
        if (statuses.size() < 2) {
            log.error("Not enough statuses defined in HTTP Request to retrive contactability registries");
            return ResponseEntity.badRequest().body("Not enough statuses defined in HTTP Request to retrive contactability registries");
        }
        log.info("The contactability registries with one of this statuses: {}, will be retrived", statuses);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatusIn(statuses));
    }
    
    @GetMapping("/byEmail")
    public ResponseEntity getContactabilityByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(this.service.getContactabilityRegistrationByEmail(email));
        } catch (NotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error interno al buscar los registros por email: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path = "/by-client-identification/{identification}")
    public ResponseEntity getContactabilityRegistryByClientIdentification(@PathVariable String identification) {
        log.info("The contactability registry of client with identification {} will be retrieved", identification);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByClientIdentification(identification));
    }

    @GetMapping(path = "/by-client-identification-campaign/{campaignId}/{identification}")
    public ResponseEntity getContactabilityRegistryByClientIdentificationAndCampaign(@PathVariable String identification, @PathVariable Integer campaignId) {
        try {
            log.info("The contactability registry of client with identification {} in campaign with id: {} will be retrieved", identification, campaignId);
            return ResponseEntity.ok(this.service.getContactabilityRegistryByClientIdentificationAndCampaign(identification, campaignId));
        } catch (RegistryNotFoundException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
