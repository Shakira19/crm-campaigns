/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.api.dto.ClientCampaignRQ;
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

    public List<Campaign> getCampaignByName(String name) {
        return this.campaignRepo.findByNameLikeIgnoreCaseOrderByNameAsc(name);
    }

    public List<Campaign> getCampaignByStartDateBetween(Date from, Date to) throws RegistryNotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByStartDateBetween(from, to);
        if (campaigns.isEmpty()) {
            log.info("No campaigns found with start date between {} and {}", from, to);
            throw new RegistryNotFoundException("No campaigns found with start date between " + from + " and " + to);
        } else {
            log.info("The campaigns with start date between {} and {} were retrieved", from, to);
            return campaigns;
        }
    }

    public List<Campaign> getCampaignByEndDateBetween(Date from, Date to) throws RegistryNotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByEndDateBetween(from, to);
        if (campaigns.isEmpty()) {
            log.info("No campaigns found with end date between {} and {}", from, to);
            throw new RegistryNotFoundException("No campaigns found with start date between " + from + " and " + to);
        } else {
            log.info("The campaigns with end date between {} and {} were retrieved", from, to);
            return campaigns;
        }
    }

    public void createCampaign(Campaign campaign) throws InsertException {
        try {
            campaign.setStatus(CampaignStatusEnum.ACTIVE.getStatus());
            campaign.setTotalNumberClients(0);
            campaign.setNumberAssignedClients(0);
            campaign.setNumberAccepetdClients(0);
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
        } catch (RegistryNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("There was an error creating contactability registry of client {} in campaign with id {}", client, id);
            throw new InsertException("contactablityRegistration", "There was an error creating contactability registry of client " + client + " in campaign with id " + id, e);
        }

    }

    public void actualizarContacto(Integer contactabilityId, ContactStatusEnum status) throws RegistryNotFoundException {
        Optional<ContactabilityRegistration> contactabilityToUpdate = this.contactabilityRegistrationRepo.findById(contactabilityId);
        if (contactabilityToUpdate.isPresent()) {
            ContactabilityRegistration updatedContactability = contactabilityToUpdate.get();
            updatedContactability.setStatus(status.getStatus());
            this.contactabilityRegistrationRepo.save(updatedContactability);
        } else {
            throw new RegistryNotFoundException("No se encontro un registro de contactabilidad con id: " + contactabilityId);
        }
    }
    
    public List<Campaign> getCampaignsByRegion(String region) {
        List<Campaign> campaigns = this.campaignRepo.findByRegion(region);
        if(!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con region: "+region);
        }
    }
    
    public List<Campaign> getCampaignsByNumberClientsInProgressEquals(Integer numberClientsInProgress) throws NotFoundException {
        List<Campaign> campaigns = this.campaignRepo.findByNumberClientsInProgressEquals(numberClientsInProgress);
        if(!campaigns.isEmpty()) {
            return campaigns;
        } else {
            throw new NotFoundException("No se encontro ninguna campaña con exactamente "+numberClientsInProgress+" clientes en progreso");
        }
    }

}
