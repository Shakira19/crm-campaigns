/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignRQ;
import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignStatusRQ;
import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRQ;
import ec.edu.espe.banquito.crm.campaigns.enums.CampaignStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.service.CampaignService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author cofre
 */
@RestController
@RequestMapping("/api/campaigns")
@Slf4j
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity listarCampaigns() {
        try {
            List<Campaign> items = new ArrayList<Campaign>();

            this.service.listarCampaigns().forEach(items::add);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getCampaignById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.service.getCampaignById(id));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byName/{name}")
    public ResponseEntity getCampaignByName(@PathVariable String name) {
        log.info("The campaign that match it's name with {}, will be retrieved", name);
        return ResponseEntity.ok(this.service.getCampaignByName(name));
    }

    @GetMapping("/byStartDate/{startDate}")
    public ResponseEntity getCampaignByStartDateBetween(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate) {
        ResponseEntity response;
        if (startDate != null) {
            try {
                log.info("The campaigns with start date between {} will be retrieved", startDate);
                response = ResponseEntity.ok(this.service.getCampaignByStartDate(startDate));
            } catch (RegistryNotFoundException e) {
                response = ResponseEntity.notFound().build();
            }
        } else {
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @GetMapping("/byEndDate/{endDate}")
    public ResponseEntity getCampaignByEndDateBetween(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        ResponseEntity response;
        if (endDate != null) {
            try {
                log.info("The campaigns with end date between {} will be retrieved", endDate);
                response = ResponseEntity.ok(this.service.getCampaignByEndDate(endDate));
            } catch (RegistryNotFoundException e) {
                response = ResponseEntity.notFound().build();
            }
        } else {
            response = ResponseEntity.badRequest().build();
        }
        return response;
    }

    @PostMapping
    public ResponseEntity createCampaign(@RequestBody CampaignRQ campaign) {
        try {
            log.info("A new campaign will be created: {}", campaign);
            this.service.createCampaign(Campaign.builder()
                    .name(campaign.getName())
                    .description(campaign.getDescription())
                    .startDate(campaign.getStartDate())
                    .endDate(campaign.getEndDate())
                    .urlTermsConditions(campaign.getUrlTermsConditions())
                    .region(campaign.getRegion())
                    .build());
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity editCampaign(@PathVariable Integer id, @RequestBody CampaignRQ campaignRq) {
        try {
            log.info("The campaign with id: {}, will be edited", id);
            this.service.editCampaign(Campaign.builder()
                    .name(campaignRq.getName())
                    .description(campaignRq.getDescription())
                    .startDate(campaignRq.getStartDate())
                    .endDate(campaignRq.getEndDate())
                    .urlTermsConditions(campaignRq.getUrlTermsConditions())
                    .region(campaignRq.getRegion()).build(), id);
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateStatus/{id}")
    public ResponseEntity updateCampaignStatus(@PathVariable Integer id, @RequestBody CampaignStatusRQ statusRq) {
        String status;
        if (statusRq.isActive()) {
            status = CampaignStatusEnum.ACTIVE.getStatus();
        } else if (statusRq.isSuspended()) {
            status = CampaignStatusEnum.SUSPENDED.getStatus();
        } else if (statusRq.isTerminated()) {
            status = CampaignStatusEnum.TERMINATED.getStatus();
        } else {
            log.error("No/Bad status defined in HTTP Request to update campaign statuts");
            return ResponseEntity.badRequest().body("No/Bad status defined in HTTP Request to update campaign statuts");
        }
        try {
            log.info("The status of product with id: {}, will be updated", id);
            this.service.updateCampaignStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (UpdateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/assignClient/{id}")
    public ResponseEntity asignarCliente(@PathVariable Integer id, @RequestParam ClientCampaignRQ client) {
        try {
            this.service.assignClient(id, client);
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/actualizar-contacto/{id}")
    public ResponseEntity actualizarContacto(@PathVariable Integer id, @RequestParam String status) {
        try {
            this.service.actualizarContacto(id, ContactStatusEnum.valueOf(status));
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byRegion/{region}")
    public ResponseEntity getCampaignsByRegion(@PathVariable String region) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByRegion(region));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressEquals/{numberClientsInProgress}")
    public ResponseEntity getCampaignsByNumberClientsInProgressEquals(@PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressEquals(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressLessThan/{numberClientsInProgress}")
    public ResponseEntity getCampaignsByNumberClientsInProgressLessThan(@PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressLessThan(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressLessThanEqual/{numberClientsInProgress}")
    public ResponseEntity getCampaignsByNumberClientsInProgressLessThanEqual(@PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressLessThanEqual(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressGreaterThan/{numberClientsInProgress}")
    public ResponseEntity getCampaignsByNumberClientsInProgressGreaterThan(@PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressGreaterThan(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressGreaterThanEqual/{numberClientsInProgress}")
    public ResponseEntity getCampaignsByNumberClientsInProgressGreaterThanEqual(@PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressGreaterThanEqual(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsEquals/{numberAcceptedClients}")
    public ResponseEntity getCampaignsByNumberAcceptedClientsEquals(@PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAcceptedClientsEquals(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsLessThan/{numberAcceptedClients}")
    public ResponseEntity getCampaignsByNumberAcceptedClientsLessThan(@PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAcceptedClientsLessThan(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsLessThanEqual/{numberAcceptedClients}")
    public ResponseEntity getCampaignsByNumberAcceptedClientsLessThanEqual(@PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAcceptedClientsLessThanEqual(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsGreaterThan/{numberAcceptedClients}")
    public ResponseEntity getCampaignsByNumberAcceptedClientsGreaterThan(@PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAcceptedClientsGreaterThan(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsGreaterThanEqual/{numberAcceptedClients}")
    public ResponseEntity getCampaignsByNumberAcceptedClientsGreaterThanEqual(@PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsEquals/{numberAssignedClients}")
    public ResponseEntity getCampaignsByNumberAssignedClientsEquals(@PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAssignedClientsEquals(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsLessThan/{numberAssignedClients}")
    public ResponseEntity getCampaignsByNumberAssignedClientsLessThan(@PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAssignedClientsLessThan(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsLessThanEqual/{numberAssignedClients}")
    public ResponseEntity getCampaignsByNumberAssignedClientsLessThanEqual(@PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAssignedClientsLessThanEqual(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsGreaterThan/{numberAssignedClients}")
    public ResponseEntity getCampaignsByNumberAssignedClientsGreaterThan(@PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAssignedClientsGreaterThan(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsGreaterThanEqual/{numberAssignedClients}")
    public ResponseEntity getCampaignsByNumberAssignedClientsGreaterThanEqual(@PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberAssignedClientsGreaterThanEqual(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsEquals/{numberRejectedClients}")
    public ResponseEntity getCampaignsByNumberRejectedClientsEquals(@PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberRejectedClientsEquals(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsLessThan/{numberRejectedClients}")
    public ResponseEntity getCampaignsByNumberRejectedClientsLessThan(@PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberRejectedClientsLessThan(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsLessThanEqual/{numberRejectedClients}")
    public ResponseEntity getCampaignsByNumberRejectedClientsLessThanEqual(@PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberRejectedClientsLessThanEqual(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsGreaterThan/{numberRejectedClients}")
    public ResponseEntity getCampaignsByNumberRejectedClientsGreaterThan(@PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberRejectedClientsGreaterThan(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsGreaterThanEqual/{numberRejectedClients}")
    public ResponseEntity getCampaignsByNumberRejectedClientsGreaterThanEqual(@PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberRejectedClientsGreaterThanEqual(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byKindProduct/{kindProduct}")
    public ResponseEntity getCampaignsByKindProduct(@PathVariable String kindProduct) {
        try {
            log.info("Retrieved the campaigns with {} as type of product", kindProduct);
            return ResponseEntity.ok(this.service.getCampaignsByKindProduct(kindProduct));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byStatus/{status}")
    public ResponseEntity getCampaignsByStatus(@PathVariable String status) {
        try {
            log.info("Retrieved the campaigns with {} as status", status);
            return ResponseEntity.ok(this.service.getCampaignsByStatus(status));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
