/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRQ;
import ec.edu.espe.banquito.crm.campaigns.enums.CampaignKindProductEnum;
import ec.edu.espe.banquito.crm.campaigns.enums.CampaignStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.List;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.exception.NotFoundException;
import ec.edu.espe.banquito.crm.campaigns.exception.UpdateException;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author cofre
 */
@Service
@Slf4j
public class CampaignService {

    private final CampaignRepository campaignRepo;
    private final ContactabilityRegistrationRepository contactabilityRegistrationRepo;

    public CampaignService(CampaignRepository campaignRepo, ContactabilityRegistrationRepository contactabilityRegistrationRepo) {
        this.campaignRepo = campaignRepo;
        this.contactabilityRegistrationRepo = contactabilityRegistrationRepo;
    }

    public List<Campaign> listarCampaigns() {
        return this.campaignRepo.findAll();
    }

    public Campaign getCampaignById(Integer id) throws RegistryNotFoundException {
        Optional<Campaign> campaign = this.campaignRepo.findById(id);
        if (campaign.isPresent()) {
            return campaign.get();
        } else {
            log.info("The campaign with id: {} does not exists", id);
            throw new RegistryNotFoundException("The campaign with id" + id + " does not exists");
        }
    }

    public List<Campaign> getCampaignByName(String name) throws RegistryNotFoundException {
        List<Campaign> result = this.campaignRepo.findByNameLikeIgnoreCaseOrderByNameAsc(name);
        if (result.isEmpty()) {
            log.info("The campaign with name {} was not found", name);
            throw new RegistryNotFoundException("The campaign with name " + name + " was not found");
        } else {
            return result;
        }
    }

    public List<Campaign> getCampaignByStartDate(Date startDate) throws RegistryNotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByStartDate(startDate);
        if (campaigns.isEmpty()) {
            log.info("No campaigns found with start date {} ", startDate);
            throw new RegistryNotFoundException("No campaigns found with start date " + startDate);
        } else {
            log.info("The campaigns with start date between {} were retrieved", startDate);
            return campaigns;
        }
    }

    public List<Campaign> getCampaignByEndDate(Date endDate) throws RegistryNotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByEndDate(endDate);
        if (campaigns.isEmpty()) {
            log.info("No campaigns found with end date between {}", endDate);
            throw new RegistryNotFoundException("No campaigns found with end date " + endDate);
        } else {
            log.info("The campaigns with end date between {} were retrieved", endDate);
            return campaigns;
        }
    }

    public void createCampaign(Campaign campaign) throws InsertException {
        try {
            campaign.setStatus(CampaignStatusEnum.ACTIVE.getStatus());
            campaign.setTotalNumberClients(0);
            campaign.setNumberAssignedClients(0);
            campaign.setNumberAcceptedClients(0);
            campaign.setNumberClientsInProgress(0);
            campaign.setNumberRejectedClients(0);
            this.campaignRepo.save(campaign);
            log.info("The campaign {}, was created", campaign);
        } catch (Exception e) {
            log.error("The campaign {} could not be created", campaign);
            throw new InsertException("campaign", "The campaign {} could not be created");
        }
    }

    public void editCampaign(Campaign campaign, Integer id) throws RegistryNotFoundException, UpdateException {
        try {
            Campaign editedCampaign = this.getCampaignById(id);
            editedCampaign.setName(campaign.getName());
            editedCampaign.setDescription(campaign.getDescription());
            editedCampaign.setStartDate(campaign.getStartDate());
            editedCampaign.setEndDate(campaign.getEndDate());
            editedCampaign.setUrlTermsConditions(campaign.getUrlTermsConditions());
            editedCampaign.setRegion(campaign.getRegion());
            this.campaignRepo.save(editedCampaign);
            log.info("The campaign with id: {}, was edited", id);
        } catch (RegistryNotFoundException e) {
            log.info("The campaign with id: {}, was not found to be edited", id);
            throw new RegistryNotFoundException("The campaign with id:" + id + ", was not found to be edited");
        } catch (Exception e) {
            log.error("An error occured when trying to update campaign with id: {}", id);
            throw new UpdateException("campaigns", "An error occured when trying to update campaign with id: " + id, e);
        }

    }

    public void updateCampaignStatus(Integer id, String newStatus) throws RegistryNotFoundException, UpdateException {
        try {
            Campaign editedCampaign = this.getCampaignById(id);
            String oldStatus = editedCampaign.getStatus();
            editedCampaign.setStatus(newStatus);
            this.campaignRepo.save(editedCampaign);
            log.info("The status campaign with id: {}, was updated from {} to {}", id, oldStatus, newStatus);
        } catch (RegistryNotFoundException e) {
            log.info("The campaign with id: {}, was not found to update its status", id);
            throw new RegistryNotFoundException("The campaign with id:" + id + ", was not found to update its status");
        } catch (Exception e) {
            log.error("An error occured when trying to update the status of campaign with id: {}", id);
            throw new UpdateException("campaigns", "An error occured when trying to update the status of campaign with id: " + id, e);
        }
    }

    public void assignClient(Integer id, ClientCampaignRQ client) throws RegistryNotFoundException, InsertException {
        
        try {
            Campaign campaignToRegister = this.getCampaignById(id);
            ContactabilityRegistration contactabilityValidation = this.contactabilityRegistrationRepo.findByClientIdentificationAndCampaign(client.getClientIdentification(), campaignToRegister);
            if(contactabilityValidation == null) {
                ContactabilityRegistration contactabilityRegistration = ContactabilityRegistration.builder()
                    .campaign(campaignToRegister)
                    .clientId(client.getClientId())
                    .clientIdentification(client.getClientIdentification())
                    .clientName(client.getClientName())
                    .clientSurname(client.getClientSurname())
                    .clientPhone(client.getClientPhone())
                    .clientEmail(client.getClientEmail())
                    .status(ContactStatusEnum.ASSIGNED.getStatus()).build();
                this.contactabilityRegistrationRepo.save(contactabilityRegistration);
                log.info("New contactability registry of client {} in campaign with id: {}, was created", client, id);
            } else {
                log.error("Client wirh id {} is already registered in campaign {}", client.getClientIdentification(), id);
                throw new InsertException("contactability_registration", "client already registered in the campaign");
            }
        } catch (RegistryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("There was an error creating contactability registry of client {} in campaign with id {}", client, id);
            throw new InsertException("contactablityRegistration", "There was an error creating contactability registry of client " + client + " in campaign with id " + id, e);
        }

    }

    public List<Campaign> getCampaignsByRegion(String region) {
        List<Campaign> campaigns = this.campaignRepo.findByRegion(region);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con region: " + region);
        }
    }

    public List<Campaign> getCampaignsByNumberClientsInProgressEquals(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressEquals(numberClientsInProgress);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con exactamente " + numberClientsInProgress + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberClientsInProgressLessThan(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressLessThan(numberClientsInProgress);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos de " + numberClientsInProgress + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberClientsInProgressLessThanEqual(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressLessThanEqual(numberClientsInProgress);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos o exactamente " + numberClientsInProgress + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberClientsInProgressGreaterThan(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressGreaterThan(numberClientsInProgress);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas de " + numberClientsInProgress + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberClientsInProgressGreaterThanEqual(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressGreaterThanEqual(numberClientsInProgress);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas o exactamente " + numberClientsInProgress + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberAcceptedClientsEquals(Integer numberAcceptedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAcceptedClientsEquals(numberAcceptedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con exactamente " + numberAcceptedClients + " clientes que aceptaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAcceptedClientsLessThan(Integer numberAcceptedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAcceptedClientsLessThan(numberAcceptedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos de " + numberAcceptedClients + " clientes que aceptaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAcceptedClientsLessThanEqual(Integer numberAcceptedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAcceptedClientsLessThanEqual(numberAcceptedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos o exactamente " + numberAcceptedClients + " clientes que aceptaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAcceptedClientsGreaterThan(Integer numberAcceptedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAcceptedClientsGreaterThan(numberAcceptedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas de " + numberAcceptedClients + " clientes que aceptaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAcceptedClientsGreaterThanEqual(Integer numberAcceptedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAcceptedClientsGreaterThanEqual(numberAcceptedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas o exactamente " + numberAcceptedClients + " clientes en progreso");
        }
    }

    public List<Campaign> getCampaignsByNumberAssignedClientsEquals(Integer numberAssignedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAssignedClientsEquals(numberAssignedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con exactamente " + numberAssignedClients + " clientes que se asignaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAssignedClientsLessThan(Integer numberAssignedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAssignedClientsLessThan(numberAssignedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos de " + numberAssignedClients + " clientes que se asignaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAssignedClientsLessThanEqual(Integer numberAssignedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAssignedClientsLessThanEqual(numberAssignedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos o exactamente " + numberAssignedClients + " clientes que se asignaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAssignedClientsGreaterThan(Integer numberAssignedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAssignedClientsGreaterThan(numberAssignedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas de " + numberAssignedClients + " clientes que se asignaron");
        }
    }

    public List<Campaign> getCampaignsByNumberAssignedClientsGreaterThanEqual(Integer numberAssignedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberAssignedClientsGreaterThanEqual(numberAssignedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas o exactamente " + numberAssignedClients + " clientes se asignaron");
        }
    }

    public List<Campaign> getCampaignsByNumberRejectedClientsEquals(Integer numberRejectedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberRejectedClientsEquals(numberRejectedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con exactamente " + numberRejectedClients + " clientes que se rechazaron");
        }
    }

    public List<Campaign> getCampaignsByNumberRejectedClientsLessThan(Integer numberRejectedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberRejectedClientsLessThan(numberRejectedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos de " + numberRejectedClients + " clientes que se rechazaron");
        }
    }

    public List<Campaign> getCampaignsByNumberRejectedClientsLessThanEqual(Integer numberRejectedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberRejectedClientsLessThanEqual(numberRejectedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con menos o exactamente " + numberRejectedClients + " clientes que se rechazaron");
        }
    }

    public List<Campaign> getCampaignsByNumberRejectedClientsGreaterThan(Integer numberRejectedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberRejectedClientsGreaterThan(numberRejectedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas de " + numberRejectedClients + " clientes que se rechazaron");
        }
    }

    public List<Campaign> getCampaignsByNumberRejectedClientsGreaterThanEqual(Integer numberRejectedClients) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberRejectedClientsGreaterThanEqual(numberRejectedClients);
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con mas o exactamente " + numberRejectedClients + " clientes se rechazaron");
        }
    }

    public List<Campaign> getCampaignsByKindProduct(String kindProduct) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByKindProduct(CampaignKindProductEnum.valueOf(kindProduct).getType());
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            log.info("Couldn't find any campaigns with {} as type of product", kindProduct);
            throw new NotFoundException("Couldn't find any campaigns with " + kindProduct + " as type of product");
        }
    }

    public List<Campaign> getCampaignsByStatus(String status) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByStatus(CampaignStatusEnum.valueOf(status).getStatus());
        if (!campaigns.isEmpty()) {
            return campaigns;
        } else {
            log.info("Couldn't find any campaigns with {} as status", status);
            throw new NotFoundException("Couldn't find any campaigns with " + status + " as status");
        }
    }

}
