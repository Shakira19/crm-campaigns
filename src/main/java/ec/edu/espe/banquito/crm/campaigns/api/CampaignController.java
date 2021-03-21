package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignRq;
import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignStatusRq;
import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRq;
import ec.edu.espe.banquito.crm.campaigns.enums.CampaignStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.service.CampaignService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/campaigns")
@Slf4j
public class CampaignController {

    private final CampaignService service;

    public CampaignController(CampaignService service) {
        this.service = service;
    }

    @GetMapping
    @ApiOperation(value = "List Campaigns")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> listarCampaigns() {
        ResponseEntity response;
        try {
            List<Campaign> items = new ArrayList<>();
            this.service.listarCampaigns().forEach(items::add);

            if (items.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            response = new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find campaign by id")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.service.getCampaignById(id));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byName/{name}")
    @ApiOperation(value = "Find campaign by name")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Campaign>> getCampaignByName(@PathVariable String name) {
        try {
            log.info("The campaign that match it's name with {}, will be retrieved", name);
            return ResponseEntity.ok(this.service.getCampaignByName(name));
        } catch (RegistryNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byStartDate/{startDate}")
    @ApiOperation(value = "Find campaign by stat date")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 400, message = "The date passed is not in the correct format: yyyyMMdd"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Campaign>> getCampaignByStartDateBetween(
            @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date startDate) {
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
    @ApiOperation(value = "Find campaign by end date")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 400, message = "The date passed is not in the correct format: yyyyMMdd"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Campaign>> getCampaignByEndDateBetween(
            @PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
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
    @ApiOperation(value = "Create a new campaign")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Campaign created successfully"),
        @ApiResponse(code = 400, message = "Some fields in the campaign passed are not correct"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<Campaign> createCampaign(@RequestBody CampaignRq campaign) {
        try {
            log.info("A new campaign will be created: {}", campaign);
            return ResponseEntity.ok(this.service.createCampaign(Campaign.builder()
                    .name(campaign.getName())
                    .description(campaign.getDescription())
                    .startDate(campaign.getStartDate())
                    .endDate(campaign.getEndDate())
                    .urlTermsConditions(campaign.getUrlTermsConditions())
                    .kindProduct(campaign.getKindProduct())
                    .region(campaign.getRegion())
                    .build()));
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update campaign")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Campaign updated successfully"),
        @ApiResponse(code = 400, message = "The date passed is not in the correct format: yyyyMMdd"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity editCampaign(@PathVariable Integer id, @RequestBody CampaignRq campaignRq) {
        try {
            log.info("The campaign with id: {}, will be edited", id);
            this.service.editCampaign(Campaign.builder()
                    .name(campaignRq.getName())
                    .description(campaignRq.getDescription())
                    .startDate(campaignRq.getStartDate())
                    .endDate(campaignRq.getEndDate())
                    .kindProduct(campaignRq.getKindProduct())
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
    @ApiOperation(value = "Update campaign status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Updated successfully"),
        @ApiResponse(code = 400, message = "No/Bad status defined in HTTP Request to update campaign statuts"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity updateCampaignStatus(@PathVariable Integer id, @RequestBody CampaignStatusRq statusRq) {
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
        ResponseEntity response;
        try {
            log.info("The status of product with id: {}, will be updated", id);
            this.service.updateCampaignStatus(id, status);
            response = ResponseEntity.ok().build();
        } catch (RegistryNotFoundException ex) {
            response = ResponseEntity.notFound().build();
        } catch (UpdateException ex) {
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }

    @PostMapping("/assignClient/{id}")
    @ApiOperation(value = "Assign client to campaign")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Client assigned successfully"),
        @ApiResponse(code = 400, message = "Some of the data provided is incorrect"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity asignarCliente(@PathVariable Integer id, @RequestBody ClientCampaignRq client) {
        try {
            this.service.assignClient(id, client);
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressEquals/{numberClientsInProgress}")
    @ApiOperation(value = "Get campaign by number of clients in progress")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberClientsInProgressEquals(
            @PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(this.service.getCampaignsByNumberClientsInProgressEquals(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressLessThan/{numberClientsInProgress}")
    @ApiOperation(value = "Get campaign by number of clients in progress less than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberClientsInProgressLessThan(
            @PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberClientsInProgressLessThan(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressLessThanEqual/{numberClientsInProgress}")
    @ApiOperation(value = "Get campaign by number of clients in progress less than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberClientsInProgressLessThanEqual(
            @PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberClientsInProgressLessThanEqual(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressGreaterThan/{numberClientsInProgress}")
    @ApiOperation(value = "Get campaign by number of clients in progress greater than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberClientsInProgressGreaterThan(
            @PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberClientsInProgressGreaterThan(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberClientsInProgressGreaterThanEqual/{numberClientsInProgress}")
    @ApiOperation(value = "Get campaign by number of clients in progress greater than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity getCampaignsByNumberClientsInProgressGreaterThanEqual(
            @PathVariable Integer numberClientsInProgress) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberClientsInProgressGreaterThanEqual(numberClientsInProgress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsEquals/{numberAcceptedClients}")
    @ApiOperation(value = "Get campaign by number of clients that accepted")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAcceptedClientsEquals(
            @PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAcceptedClientsEquals(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsLessThan/{numberAcceptedClients}")
    @ApiOperation(value = "Get campaign by number of clients that accepted less than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAcceptedClientsLessThan(
            @PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAcceptedClientsLessThan(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsLessThanEqual/{numberAcceptedClients}")
    @ApiOperation(value = "Get campaign by number of clients that accepted less than or equals to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAcceptedClientsLessThanEqual(
            @PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAcceptedClientsLessThanEqual(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsGreaterThan/{numberAcceptedClients}")
    @ApiOperation(value = "Get campaign by number of clients that accepted greater than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAcceptedClientsGreaterThan(
            @PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAcceptedClientsGreaterThan(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAcceptedClientsGreaterThanEqual/{numberAcceptedClients}")
    @ApiOperation(value = "Get campaign by number of clients that accepted greater than or equals to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAcceptedClientsGreaterThanEqual(
            @PathVariable Integer numberAcceptedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAcceptedClientsGreaterThanEqual(numberAcceptedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsEquals/{numberAssignedClients}")
    @ApiOperation(value = "Get campaign by number of assigned clients")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAssignedClientsEquals(
            @PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAssignedClientsEquals(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsLessThan/{numberAssignedClients}")
    @ApiOperation(value = "Get campaign by number of assigned clients less than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAssignedClientsLessThan(
            @PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAssignedClientsLessThan(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsLessThanEqual/{numberAssignedClients}")
    @ApiOperation(value = "Get campaign by number of assigned clients less than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAssignedClientsLessThanEqual(
            @PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAssignedClientsLessThanEqual(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsGreaterThan/{numberAssignedClients}")
    @ApiOperation(value = "Get campaign by number of assigned clients greater than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAssignedClientsGreaterThan(
            @PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAssignedClientsGreaterThan(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberAssignedClientsGreaterThanEqual/{numberAssignedClients}")
    @ApiOperation(value = "Get campaign by number of assigned clients greater than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberAssignedClientsGreaterThanEqual(
            @PathVariable Integer numberAssignedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberAssignedClientsGreaterThanEqual(numberAssignedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsEquals/{numberRejectedClients}")
    @ApiOperation(value = "Get campaign by number of rejected clients")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberRejectedClientsEquals(
            @PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberRejectedClientsEquals(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsLessThan/{numberRejectedClients}")
    @ApiOperation(value = "Get campaign by number of rejected clients less than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberRejectedClientsLessThan(
            @PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberRejectedClientsLessThan(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsLessThanEqual/{numberRejectedClients}")
    @ApiOperation(value = "Get campaign by number of rejected clients less than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberRejectedClientsLessThanEqual(
            @PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberRejectedClientsLessThanEqual(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsGreaterThan/{numberRejectedClients}")
    @ApiOperation(value = "Get campaign by number of rejected clients greater than given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberRejectedClientsGreaterThan(
            @PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberRejectedClientsGreaterThan(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byNumberRejectedClientsGreaterThanEqual/{numberRejectedClients}")
    @ApiOperation(value = "Get campaign by number of rejected clients greater than or equal to given value")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found"),
        @ApiResponse(code = 500, message = "Internal Server Error")})
    public ResponseEntity<List<Campaign>> getCampaignsByNumberRejectedClientsGreaterThanEqual(
            @PathVariable Integer numberRejectedClients) {
        try {
            return ResponseEntity.ok(
                    this.service.getCampaignsByNumberRejectedClientsGreaterThanEqual(numberRejectedClients));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/byKindProduct/{kindProduct}")
    @ApiOperation(value = "Get campaign by kind product")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Campaign>> getCampaignsByKindProduct(@PathVariable String kindProduct) {
        try {
            log.info("Retrieved the campaigns with {} as type of product", kindProduct);
            return ResponseEntity.ok(this.service.getCampaignsByKindProduct(kindProduct));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byStatus/{status}")
    @ApiOperation(value = "Get campaign by status")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Registries Found"),
        @ApiResponse(code = 404, message = "Not Found")})
    public ResponseEntity<List<Campaign>> getCampaignsByStatus(@PathVariable String status) {
        try {
            log.info("Retrieved the campaigns with {} as status", status);
            return ResponseEntity.ok(this.service.getCampaignsByStatus(status));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
