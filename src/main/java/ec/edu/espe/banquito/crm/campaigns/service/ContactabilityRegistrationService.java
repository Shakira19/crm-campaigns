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

    public List<ContactabilityRegistration> getContactabilityRegistryByStatus(String status) {
        return this.contactabilityRegistrationRepo.findByStatusIs(status);
    }

    public List<ContactabilityRegistration> getContactabilityRegistryByStatusIn(List<String> statuses) {
        return this.contactabilityRegistrationRepo.findByStatusIn(statuses);
    }

    public List<ContactabilityRegistration> getContactabilityRegistryByClientIdentification(String clientIdentification) {
        return this.contactabilityRegistrationRepo.findByClientIdentificationOrderByClientSurnameDesc(clientIdentification);
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
}
