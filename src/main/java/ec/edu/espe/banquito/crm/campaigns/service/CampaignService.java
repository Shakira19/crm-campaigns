/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.List;
import ec.edu.espe.banquito.crm.campaigns.exception.InsertException;
import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.enums.ContactStatusEnum;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author cofre
 */

@Service
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
        if(campaign.isPresent()) {
            return campaign.get();
        } else {
            throw new RegistryNotFoundException("No existe una campaña con el id: "+id);
        }
    }
    
    public void crearCampaign(Campaign campaign) throws InsertException {
        if(this.campaignRepo.save(campaign) == null) {
            throw new InsertException("campaign", "No se pudo insertar el registro, revise los datos.");
        } 
    }
    
    public void editarCampaign(Campaign campaign, Integer id) throws RegistryNotFoundException {
        Optional<Campaign> campaignToEdit = this.campaignRepo.findById(id);
        if(campaignToEdit.isPresent()) {
            Campaign editedCampaign = campaignToEdit.get();
            editedCampaign.setName(campaign.getName());
            editedCampaign.setDescription(campaign.getDescription());
            this.campaignRepo.save(editedCampaign);
        } else {
            throw new RegistryNotFoundException("No existe la campaña que desea editar con id: "+id);
        }
    }
    
    public void asignarCliente(Integer id, String clientId) throws RegistryNotFoundException {
        Optional<Campaign> campaignToRegister = this.campaignRepo.findById(id);
        if(campaignToRegister.isPresent()) {
            ContactabilityRegistration contactabilityRegistration = ContactabilityRegistration.builder()
                                                                                              .campaign(campaignToRegister.get())
                                                                                              .idClient(clientId)
                                                                                              .status(ContactStatusEnum.PENDIENTE.getStatus()).build();
            this.contactabilityRegistrationRepo.save(contactabilityRegistration);
        } else {
            throw new RegistryNotFoundException("No se pudo encontrar una campaña con el id: "+id);
        }
    }
    
    public void actualizarContacto(Integer contactabilityId, ContactStatusEnum status) throws RegistryNotFoundException{
        Optional<ContactabilityRegistration> contactabilityToUpdate = this.contactabilityRegistrationRepo.findById(contactabilityId);
        if(contactabilityToUpdate.isPresent()) {
            ContactabilityRegistration updatedContactability = contactabilityToUpdate.get();
            updatedContactability.setStatus(status.getStatus());
            this.contactabilityRegistrationRepo.save(updatedContactability);
        } else {
            throw new RegistryNotFoundException("No se encontro un registro de contactabilidad con id: "+contactabilityId);
        }
    }
}
