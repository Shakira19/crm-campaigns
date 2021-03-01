/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.service.ContactabilityRegistrationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alan Quimbita
 */
@CrossOrigin
@RestController
@RequestMapping(path = "/api/contactability-registry")
@Slf4j
public class ContactabilityRegistryController {

    private final ContactabilityRegistrationService service;

    public ContactabilityRegistryController(ContactabilityRegistrationService service) {
        this.service = service;
    }

    @GetMapping(path = "/byStatus/{status}")
    @ApiOperation(value = "Find contactability registries by a single status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 400, message = "No status defined in HTTP Request to retrive contactability registries"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<ContactabilityRegistration>> getContactabilityRegistryByStatus(@PathVariable String status) {
        if (ContactStatusEnum.ACCEPTED.getStatus().equals(status)
                || ContactStatusEnum.REJECTED.getStatus().equals(status)
                || ContactStatusEnum.ASSIGNED.getStatus().equals(status)
                || ContactStatusEnum.INPROGRESS.getStatus().equals(status)) {
            try {
                log.info("The contactability registries with status: {}, will be retrived", status);
                return ResponseEntity.ok(this.service.getContactabilityRegistryByStatus(status));
            } catch (RegistryNotFoundException ex) {
                return ResponseEntity.notFound().build();
            }
        } else {
            log.error("No status defined in HTTP Request to retrive contactability registries");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/byEmail")
    @ApiOperation(value = "Find contactability registries by email")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<ContactabilityRegistration>> getContactabilityByEmail(@RequestParam String email) {
        try {
            return ResponseEntity.ok(this.service.getContactabilityRegistrationByEmail(email));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/byClientIdentification/{identification}")
    @ApiOperation(value = "Find contactability registries by identification")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<ContactabilityRegistration>> getContactabilityRegistryByClientIdentification(@PathVariable String identification) {
        try {
            log.info("The contactability registry of client with identification {} will be retrieved", identification);
            return ResponseEntity.ok(this.service.getContactabilityRegistryByClientIdentification(identification));
        } catch (RegistryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/byClientIdentificationCampaign/{campaignId}/{identification}")
    @ApiOperation(value = "Find contactability registries by an identification in a campaign")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<ContactabilityRegistration> getContactabilityRegistryByClientIdentificationAndCampaign(@PathVariable String identification, @PathVariable Integer campaignId) {
        try {
            log.info("The contactability registry of client with identification {} in campaign with id: {} will be retrieved", identification, campaignId);
            return ResponseEntity.ok(this.service.getContactabilityRegistryByClientIdentificationAndCampaign(identification, campaignId));
        } catch (RegistryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byClientPhone/{clientPhone}")
    @ApiOperation(value = "Find contactability registries by phone")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<ContactabilityRegistration>> getContactabilityRegistrationByClientPhone(@PathVariable String clientPhone) {
        try {
            log.info("The contactastabilities with {} as phone number were retrieved", clientPhone);
            return ResponseEntity.ok(this.service.getContactabilityRegistrationByClientPhone(clientPhone));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byClientNameAndSurname/{clientName}-{clientSurname}")
    @ApiOperation(value = "Find contactability registries by name and surname")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<ContactabilityRegistration>> getContactabilityRegistrationByClientNameAndSurname(@PathVariable String clientName, @PathVariable String clientSurname) {
        try {
            log.info("Contactabilities for {} {} were retrieved", clientName, clientSurname);
            return ResponseEntity.ok(this.service.getContactabilityRegistrationByClientNameAndSurname(clientName, clientSurname));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
