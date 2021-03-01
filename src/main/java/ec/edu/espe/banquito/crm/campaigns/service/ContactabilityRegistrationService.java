/*
 * Creation date: 21 feb. 2021
 * Company: ESPE
 * Project: Banco Banquito
 * Module: Banco Banquito - CRM
 */
package ec.edu.espe.banquito.crm.campaigns.service;

import ec.edu.espe.banquito.crm.campaigns.exception.RegistryNotFoundException;
import ec.edu.espe.banquito.crm.campaigns.model.Campaign;
import ec.edu.espe.banquito.crm.campaigns.model.ContactabilityRegistration;
import ec.edu.espe.banquito.crm.campaigns.repository.CampaignRepository;
import ec.edu.espe.banquito.crm.campaigns.repository.ContactabilityRegistrationRepository;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Alan Quimbita
 */
@Service
@Slf4j
public class ContactabilityRegistrationService {

    private final CampaignRepository campaignRepo;
    private final ContactabilityRegistrationRepository contactabilityRegistrationRepo;

    public ContactabilityRegistrationService(CampaignRepository campaignRepo, ContactabilityRegistrationRepository contactabilityRegistrationRepo) {
        this.campaignRepo = campaignRepo;
        this.contactabilityRegistrationRepo = contactabilityRegistrationRepo;
    }

    public List<ContactabilityRegistration> getContactabilityRegistryByStatus(String status) throws RegistryNotFoundException {
        List<ContactabilityRegistration> result = this.contactabilityRegistrationRepo.findByStatusIs(status);
        if (result.isEmpty()) {
            log.info("The campaigns with status {} were not found", status);
            throw new RegistryNotFoundException("The campaigns with status " + status + " were not found");
        } else {
            log.info("The campaigns with status {} were retrieved", status);
            return result;
        }
    }

    public List<ContactabilityRegistration> getContactabilityRegistrationByEmail(String email) throws RegistryNotFoundException {
        List<ContactabilityRegistration> contactabilities = this.contactabilityRegistrationRepo.findByClientEmail(email);
        if (!contactabilities.isEmpty()) {
            log.info("The registries that have email: {} were retrieved", email);
            return contactabilities;
        } else {
            log.info("Not found registries that have email: {}", email);
            throw new RegistryNotFoundException("Not found registries that have email: " + email);
        }
    }

    public List<ContactabilityRegistration> getContactabilityRegistryByClientIdentification(String clientIdentification) throws RegistryNotFoundException {
        List<ContactabilityRegistration> result = this.contactabilityRegistrationRepo.findByClientIdentificationOrderByClientSurnameDesc(clientIdentification);
        if (result.isEmpty()) {
            log.info("Not found registries that have identification: {}", clientIdentification);
            throw new RegistryNotFoundException("Not found registries that have identification: " + clientIdentification);
        } else {
            log.info("The registries that have identifications: {} were retrieved", clientIdentification);
            return result;
        }
    }

    public ContactabilityRegistration getContactabilityRegistryByClientIdentificationAndCampaign(String identification, Integer campaignId) throws RegistryNotFoundException {
        Optional<Campaign> campaign = this.campaignRepo.findById(campaignId);
        if (campaign.isPresent()) {
            return this.contactabilityRegistrationRepo.findByClientIdentificationAndCampaign(identification, campaign.get());
        } else {
            log.error("The campaign with id: {} does not exists", campaignId);
            throw new RegistryNotFoundException("The campaign with id" + campaignId + " does not exists");
        }
    }

    public List<ContactabilityRegistration> getContactabilityRegistrationByClientPhone(String clientPhone) throws RegistryNotFoundException {
        List<ContactabilityRegistration> contactabilities = this.contactabilityRegistrationRepo.findByClientPhone(clientPhone);
        if (!contactabilities.isEmpty()) {
            return contactabilities;
        } else {
            log.info("There are no contactabilities with {} as client phone number");
            throw new RegistryNotFoundException("No existe ningún contacto registrado con el telefono: " + clientPhone);
        }
    }

    public List<ContactabilityRegistration> getContactabilityRegistrationByClientNameAndSurname(String clientName, String clientSurname) throws RegistryNotFoundException {
        List<ContactabilityRegistration> contactabilities = this.contactabilityRegistrationRepo.findByClientNameIgnoringCaseLikeAndClientSurnameIgnoringCaseLike(clientName, clientSurname);
        if (!contactabilities.isEmpty()) {
            return contactabilities;
        } else {
            log.info("Couldn't find any contactabilities for {} {}", clientName, clientSurname);
            throw new RegistryNotFoundException("Couldn't find any contactabilities for " + clientName + " " + clientSurname);
        }
    }
}
