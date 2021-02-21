/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ContactabilityStatusRQ;
import ec.edu.espe.banquito.crm.campaigns.service.ContactabilityRegistrationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alan Quimbita
 */
@RestController
@RequestMapping(path="/api/contactabilityRegistry")
@Slf4j
public class ContactabilityRegistryController {
    private final ContactabilityRegistrationService service;

    public ContactabilityRegistryController(ContactabilityRegistrationService service) {
        this.service = service;
    }
    
    @GetMapping(path = "/byStatus/{status}")
    public ResponseEntity getContactabilityRegistryByStatus(@PathVariable String status){
        log.info("The contactability registries with status: {}, will be retrived", status);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatus(status));
    }
    
    @GetMapping(path = "/byStatusIn")
    public ResponseEntity getContactabilityRegistryByStatusIn(@RequestBody ContactabilityStatusRQ statusesRQ){
        List<String> statuses = statusesRQ.getStatusList();
        log.info("The contactability registries with one of this statuses: {}, will be retrived", statuses);
        return ResponseEntity.ok(this.service.getContactabilityRegistryByStatusIn(statuses));
    }
    
}
