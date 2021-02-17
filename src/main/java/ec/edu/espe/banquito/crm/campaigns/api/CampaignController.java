/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.api;

import ec.edu.espe.banquito.crm.campaigns.api.dto.CampaignRQ;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.service.CampaignService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
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

            if (items.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(items, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity obtenerCampaignPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.service.obtenerCampaignPorId(id));
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
            
    @PostMapping
    public ResponseEntity crearCampaign(@RequestBody CampaignRQ campaign) {
        try {
            this.service.crearCampaign(Campaign.builder()
                                        .name(campaign.getName())
                                        .description(campaign.getDescription()).build());
            return ResponseEntity.ok().build();
        } catch (InsertException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity editarCampaign(@PathVariable Integer id, @RequestBody CampaignRQ campaignRq) {
        try {
            Campaign campaign = this.service.obtenerCampaignPorId(id);
            campaign.setName(campaignRq.getName());
            campaign.setDescription(campaignRq.getDescription());
            this.service.editarCampaign(campaign, id);
            return ResponseEntity.ok().build();
        } catch (RegistryNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping("/asignar-cliente/{id}")
    public ResponseEntity asignarCliente(@PathVariable Integer id, @RequestParam String clientId) {
        try {
            this.service.asignarCliente(id, clientId);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
