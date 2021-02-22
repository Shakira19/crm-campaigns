/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.enums.CampaignStatusEnum;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.List;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
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

    public Campaign obtenerCampaignPorId(Integer id) throws RegistryNotFoundException {
        Optional<Campaign> campaign = this.campaignRepo.findById(id);
        if (campaign.isPresent()) {
            return campaign.get();
        } else {
            throw new RegistryNotFoundException("No existe una campaña con el id: " + id);
        }
    }

    public void crearCampaign(Campaign campaign) throws InsertException {
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

    public void editarCampaign(Campaign campaign, Integer id) throws RegistryNotFoundException {
        Optional<Campaign> campaignToEdit = this.campaignRepo.findById(id);
        if (campaignToEdit.isPresent()) {
            Campaign editedCampaign = campaignToEdit.get();
            editedCampaign.setName(campaign.getName());
            editedCampaign.setDescription(campaign.getDescription());
            editedCampaign.setStartDate(campaign.getStartDate());
            editedCampaign.setEndDate(campaign.getEndDate());
            editedCampaign.setUrlTermsConditions(campaign.getUrlTermsConditions());
            editedCampaign.setRegion(campaign.getRegion());
            this.campaignRepo.save(editedCampaign);
            log.info("The campaign with id: {}, was edited", id);
        } else {
            log.error("The campaign with id: {}, was not found to be edited", id);
            throw new RegistryNotFoundException("The campaign with id:" + id + ", was not found to be edited");
        }
    }

    public void asignarCliente(Integer id, String clientId) throws RegistryNotFoundException {
        Optional<Campaign> campaignToRegister = this.campaignRepo.findById(id);
        if (campaignToRegister.isPresent()) {
            ContactabilityRegistration contactabilityRegistration = ContactabilityRegistration.builder()
                    .campaign(campaignToRegister.get())
                    .idClient(clientId)
                    .status(ContactStatusEnum.PENDIENTE.getStatus()).build();
            this.contactabilityRegistrationRepo.save(contactabilityRegistration);
        } else {
            throw new RegistryNotFoundException("No se pudo encontrar una campaña con el id: " + id);
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

    public List<Campaign> getCampaignByName(String name) {
        return this.campaignRepo.findByNameLikeIgnoreCaseOrderByNameAsc(name);
    }
}
